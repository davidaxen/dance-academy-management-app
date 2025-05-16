package com.daxen.mydancekmpsharedui.features.student.reservation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.daxen.mydancekmpsharedui.data.classes.repository.ClassesRepository
import com.daxen.mydancekmpsharedui.data.reservation.repository.ReservationRepository
import com.daxen.mydancekmpsharedui.data.user.model.User
import com.daxen.mydancekmpsharedui.data.user.repository.UserRepository
import com.daxen.mydancekmpsharedui.features.student.reservation.utils.ClassOrigin
import com.daxen.mydancekmpsharedui.features.student.reservation.utils.DisplayClass
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn

internal class ReservationViewModel(
    userRepository: UserRepository,
    private val classesRepository: ClassesRepository,
    private val reservationRepository: ReservationRepository,
) : ViewModel() {
    val currentUser: StateFlow<User> = userRepository.currentUser

    val today = Clock.System.todayIn(TimeZone.currentSystemDefault())

    private val _classesListState = MutableStateFlow<ClassesListUiState>(ClassesListUiState.Loading)
    val classesListState: StateFlow<ClassesListUiState> get() = _classesListState

    private val _selectedDate = MutableStateFlow(today)
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    private val _currentWeek = MutableStateFlow(getCurrentWeek(today))
    val currentWeek: StateFlow<List<LocalDate>> = _currentWeek

    val canGoBack: StateFlow<Boolean> = _currentWeek.map { week ->
        week.last().minus(7, DateTimeUnit.DAY) >= today
    }.stateIn(viewModelScope, SharingStarted.Eagerly, true)

    // Almacén de profesores por ID
    private val teachersMap = MutableStateFlow<Map<String, String>>(emptyMap())
    
    // Mapa de reservas: Triple(fecha, classId) -> reservationId
    private val _userReservationsMap = MutableStateFlow<Map<Pair<String, String>, String>>(emptyMap())
    
    // Lista de reservas del usuario para la interfaz
    private val _userReservations = MutableStateFlow<List<Pair<String, String>>>(emptyList()) // Pair<classId, reservationId>
    val userReservations: StateFlow<List<Pair<String, String>>> = _userReservations

    init {
        loadClasses()
        loadUserReservations()
    }

    private fun loadClasses() {
        viewModelScope.launch {
            _classesListState.value = ClassesListUiState.Loading

            try {
                classesRepository.getClassesByAcademyId(currentUser.value.currentAcademyId)

                val weekly = classesRepository.weeklyClassesList.value
                val specific = classesRepository.specificClassesList.value

                // Aquí crearíamos un mapa de ID de profesor a nombre
                // Por ahora, asignamos nombres ficticios basados en los IDs
                val teacherIds = (weekly.map { it.data.teacherId } + specific.map { it.data.teacherId }).distinct()
                val teacherNames = teacherIds.associateWith { teacherId ->
                    "Profesor $teacherId" // En el futuro, obtendríamos el nombre real
                }
                teachersMap.value = teacherNames

                if (weekly.isEmpty() && specific.isEmpty()) {
                    _classesListState.value = ClassesListUiState.Empty
                } else {
                    _classesListState.value = ClassesListUiState.Success(
                        weekly = weekly,
                        specific = specific
                    )
                }
            } catch (e: Exception) {
                _classesListState.value = ClassesListUiState.Error
            }
        }
    }
    
    private fun loadUserReservations() {
        viewModelScope.launch {
            try {
                // Obtener reservas reales del usuario
                val userReservationList = reservationRepository.getUserReservations(
                    currentUser.value.uid,
                    currentUser.value.currentAcademyId
                )
                
                // Crear un mapa de (fecha, classId) a reservationId
                val reservationsMap = userReservationList.associate { 
                    (it.date to it.classId) to it.id 
                }
                _userReservationsMap.value = reservationsMap
                
                // Filtrar solo las reservas para la fecha actual seleccionada
                updateReservationsForSelectedDate()
            } catch (e: Exception) {
                println("Error al cargar las reservas del usuario: ${e.message}")
                // En caso de error, mantener mapas vacíos
                _userReservationsMap.value = emptyMap()
                _userReservations.value = emptyList()
            }
        }
    }
    
    private fun updateReservationsForSelectedDate() {
        val currentDate = _selectedDate.value.toString()
        // Para clases semanales, también debemos considerar el día de la semana
        val dayOfWeek = _selectedDate.value.dayOfWeek.name
        
        // Filtrar reservas por la fecha seleccionada
        val reservationsForDate = _userReservationsMap.value
            .filter { (dateClassIdPair, _) ->
                dateClassIdPair.first == currentDate
            }
            .map { (dateClassIdPair, reservationId) ->
                dateClassIdPair.second to reservationId
            }
            
        _userReservations.value = reservationsForDate
    }

    fun reserveClass(studentId: String, classId: String, name: String, hour: String) {
        viewModelScope.launch {
            try {
                reservationRepository.reserveClass(
                    currentUser.value.currentAcademyId, 
                    studentId, 
                    classId, 
                    name, 
                    hour, 
                    selectedDate.value.toString()
                )
                // Después de reservar, recargar las reservas del usuario
                loadUserReservations()
            } catch (e: Exception) {
                println("Error al reservar clase: ${e.message}")
            }
        }
    }
    
    fun cancelReservation(reservationId: String, classId: String) {
        viewModelScope.launch {
            try {
                // Obtener la fecha actual seleccionada para la cancelación
                val currentDate = selectedDate.value.toString()
                
                // Llamar al repositorio para cancelar la reserva
                reservationRepository.cancelReservation(
                    currentUser.value.uid, 
                    currentUser.value.currentAcademyId, 
                    classId,
                    currentDate
                )
                
                // Actualizar el mapa local de reservas
                val updatedReservationsMap = _userReservationsMap.value.filter { 
                    (dateClassIdPair, id) -> 
                    (dateClassIdPair.first != currentDate || dateClassIdPair.second != classId)
                }
                _userReservationsMap.value = updatedReservationsMap
                updateReservationsForSelectedDate()
            } catch (e: Exception) {
                println("Error al cancelar la reserva: ${e.message}")
            }
        }
    }
    
    fun isClassReserved(classId: String): Boolean {
        return _userReservations.value.any { it.first == classId }
    }
    
    fun getReservationId(classId: String): String {
        return _userReservations.value.firstOrNull { it.first == classId }?.second ?: ""
    }

    fun goToToday() {
        _selectedDate.value = today
        _currentWeek.value = getCurrentWeek(today)
        // Actualizar las reservas para la fecha seleccionada
        updateReservationsForSelectedDate()
    }

    fun selectDate(dateSelected: LocalDate) {
        _selectedDate.value = dateSelected
        // Actualizar las reservas para la nueva fecha seleccionada
        updateReservationsForSelectedDate()
    }

    fun goToPreviousWeek() {
        _currentWeek.value = getPreviousWeek(_currentWeek.value.first())
        if (_currentWeek.value.contains(today)) {
            _selectedDate.value = today
        } else {
            _selectedDate.value = _currentWeek.value.first()
        }
        // Actualizar las reservas para la nueva fecha seleccionada
        updateReservationsForSelectedDate()
    }

    fun goToNextWeek() {
        _currentWeek.value = getNextWeek(_currentWeek.value.first())
        _selectedDate.value = _currentWeek.value.first()
        // Actualizar las reservas para la nueva fecha seleccionada
        updateReservationsForSelectedDate()
    }

    private fun getCurrentWeek(date: LocalDate): List<LocalDate> {
        val todayWeekDay = date.dayOfWeek.isoDayNumber
        return (0..6).map { date.plus(it - todayWeekDay + 1, DateTimeUnit.DAY) }
    }

    private fun getPreviousWeek(date: LocalDate) = getCurrentWeek(date.minus(7, DateTimeUnit.DAY))
    private fun getNextWeek(date: LocalDate) = getCurrentWeek(date.plus(7, DateTimeUnit.DAY))

    fun reloadClasses() {
        _classesListState.value = ClassesListUiState.Loading
        loadClasses()
        loadUserReservations()
    }
    
    fun getTeacherName(teacherId: String): String {
        return teachersMap.value[teacherId] ?: "Profesor sin asignar"
    }
}