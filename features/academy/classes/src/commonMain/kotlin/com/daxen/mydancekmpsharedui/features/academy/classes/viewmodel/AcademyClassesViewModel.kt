package com.daxen.mydancekmpsharedui.features.academy.classes.viewmodel

import com.daxen.mydancekmpsharedui.data.academy.classes.repository.AcademyClassesRepository
import com.daxen.mydancekmpsharedui.data.user.model.UserAcademy
import com.daxen.mydancekmpsharedui.data.user.repository.AcademyUserRepository
import com.daxen.mydancekmpsharedui.features.academy.classes.models.AcademyClassesState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn

class AcademyClassesViewModel(
    private val academyClassesRepository: AcademyClassesRepository,
    private val coroutineScope: CoroutineScope = CoroutineScope(Dispatchers.Main),
    academyUserRepository: AcademyUserRepository
) {
    private val currentAcademy: StateFlow<UserAcademy> = academyUserRepository.currentAcademy

    private val _state = MutableStateFlow(AcademyClassesState())
    val state: StateFlow<AcademyClassesState> = _state.asStateFlow()

    init {
        initializeWeekDates()
    }

    private fun initializeWeekDates() {
        val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        
        // Calcular el inicio de la semana (lunes)
        val dayOfWeek = today.dayOfWeek.ordinal
        val mondayOffset = (dayOfWeek - 1)
        val startDate = today.minus(mondayOffset, DateTimeUnit.DAY)
        
        // Calcular el fin de la semana (domingo)
        val endDate = startDate.plus(6, DateTimeUnit.DAY)
        
        _state.update { 
            it.copy(
                selectedWeekStartDate = startDate,
                selectedWeekEndDate = endDate
            )
        }
        
        loadClasses()
    }

    fun moveWeekForward() {
        _state.value.selectedWeekStartDate?.let { startDate ->
            _state.value.selectedWeekEndDate?.let { endDate ->
                val newStartDate = startDate.plus(7, DateTimeUnit.DAY)
                val newEndDate = endDate.plus(7, DateTimeUnit.DAY)
                
                _state.update { 
                    it.copy(
                        selectedWeekStartDate = newStartDate,
                        selectedWeekEndDate = newEndDate
                    )
                }
                
                loadClasses()
            }
        }
    }

    fun moveWeekBackward() {
        _state.value.selectedWeekStartDate?.let { startDate ->
            _state.value.selectedWeekEndDate?.let { endDate ->
                val newStartDate = startDate.minus(7, DateTimeUnit.DAY)
                val newEndDate = endDate.minus(7, DateTimeUnit.DAY)
                
                _state.update { 
                    it.copy(
                        selectedWeekStartDate = newStartDate,
                        selectedWeekEndDate = newEndDate
                    )
                }
                
                loadClasses()
            }
        }
    }

    fun loadClasses() {
        coroutineScope.launch {
            try {
                _state.update { it.copy(isLoading = true, errorMessage = null) }
                
                val weeklyClasses = academyClassesRepository.getWeeklyClassesByAcademyId(currentAcademy.value.academyId)
                val specificClasses = academyClassesRepository.getSpecificClassesByAcademyId(currentAcademy.value.academyId)
                
                _state.update { 
                    it.copy(
                        isLoading = false,
                        weeklyClasses = weeklyClasses,
                        specificClasses = specificClasses
                    )
                }
            } catch (e: Exception) {
                _state.update { 
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error al cargar las clases"
                    )
                }
            }
        }
    }
} 