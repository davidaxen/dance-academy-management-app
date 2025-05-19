package com.daxen.mydancekmpsharedui.core.firebase.academy.classes.repository

import com.daxen.mydancekmpsharedui.core.firebase.academy.classes.model.ReservationAcademyModel
import com.daxen.mydancekmpsharedui.core.firebase.academy.classes.model.SpecificClassModel
import com.daxen.mydancekmpsharedui.core.firebase.academy.classes.model.StudentInfoReservation
import com.daxen.mydancekmpsharedui.core.firebase.academy.classes.model.StudentReservation
import com.daxen.mydancekmpsharedui.core.firebase.academy.classes.model.WeeklyClassModel
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.where
import kotlinx.serialization.DeserializationStrategy

class ClassRepositoryImpl(
    private val firestore: FirebaseFirestore
) : ClassRepository {

    private suspend inline fun <reified T, R> getClassesByAcademyId(
        academyId: String,
        collectionName: String,
        deserializer: DeserializationStrategy<T>,
        map: (T, String) -> R
    ): List<R> {
        val documents = firestore
            .collection("/academies/$academyId/$collectionName")
            .get()
            .documents

        return documents.mapNotNull { doc ->
            doc.data(deserializer)?.let { data ->
                map(data, doc.id)
            }
        }
    }

    override suspend fun getWeeklyClassesByAcademyId(academyId: String): List<WeeklyClassModel> {
        return getClassesByAcademyId(
            academyId = academyId,
            collectionName = "classes",
            deserializer = WeeklyClassModel.serializer()
        ) { response, uid ->
            WeeklyClassModel(
                dayOfWeek = response.dayOfWeek,
                id = uid,
                name = response.name,
                hour = response.hour,
                status = response.status,
                teachers = response.teachers
            )
        }
    }

    override suspend fun getSpecificClassesByAcademyId(academyId: String): List<SpecificClassModel> {
        return getClassesByAcademyId(
            academyId = academyId,
            collectionName = "specificClasses",
            deserializer = SpecificClassModel.serializer()
        ) { response, uid ->
            SpecificClassModel(
                date = response.date,
                id = uid,
                name = response.name,
                hour = response.hour,
                status = response.status,
                teachers = response.teachers
            )
        }
    }

    override suspend fun saveWeeklyClass(academyId: String, weeklyClass: WeeklyClassModel): Result<WeeklyClassModel> {
        return try {
            val docRef = if (weeklyClass.id.isEmpty()) {
                firestore.collection("/academies/$academyId/classes").add(weeklyClass)
            } else {
                firestore.collection("/academies/$academyId/classes")
                    .document(weeklyClass.id)
                    .set(weeklyClass)
                firestore.collection("/academies/$academyId/classes").document(weeklyClass.id)
            }
            
            val savedClass = weeklyClass.copy(id = docRef.id)
            if (weeklyClass.id.isEmpty()) {
                firestore.collection("/academies/$academyId/classes")
                    .document(savedClass.id)
                    .set(savedClass)
            }
            Result.success(savedClass)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun saveSpecificClass(academyId: String, specificClass: SpecificClassModel): Result<SpecificClassModel> {
        return try {
            val docRef = if (specificClass.id.isEmpty()) {
                firestore.collection("/academies/$academyId/specificClasses").add(specificClass)
            } else {
                firestore.collection("/academies/$academyId/specificClasses")
                    .document(specificClass.id)
                    .set(specificClass)
                firestore.collection("/academies/$academyId/specificClasses").document(specificClass.id)
            }
            
            val savedClass = specificClass.copy(id = docRef.id)
            if (specificClass.id.isEmpty()) {
                firestore.collection("/academies/$academyId/classes")
                    .document(savedClass.id)
                    .set(savedClass)
            }
            Result.success(savedClass)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deleteWeeklyClass(academyId: String, classId: String): Result<Unit> {
        return try {
            firestore.collection("/academies/$academyId/classes")
                .document(classId)
                .delete()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deleteSpecificClass(academyId: String, classId: String): Result<Unit> {
        return try {
            firestore.collection("/academies/$academyId/specificClasses")
                .document(classId)
                .delete()
            
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getWeeklyClassById(academyId: String, classId: String): Result<WeeklyClassModel> {
        return try {
            val document = firestore.collection("/academies/$academyId/classes")
                .document(classId)
                .get()

            Result.success(
                document.data(WeeklyClassModel.serializer()).let { data ->
                    WeeklyClassModel(
                        dayOfWeek = data.dayOfWeek,
                        id = classId,
                        name = data.name,
                        hour = data.hour,
                        status = data.status,
                        teachers = data.teachers
                    )
                }
            )
        } catch (e: Exception) {
            throw e
        }
    }

    override suspend fun getSpecificClassById(academyId: String, classId: String): Result<SpecificClassModel> {
        return try {
            val document = firestore.collection("/academies/$academyId/specificClasses")
                .document(classId)
                .get()

            Result.success(
                document.data(SpecificClassModel.serializer()).let { data ->
                    SpecificClassModel(
                        date = data.date,
                        id = classId,
                        name = data.name,
                        hour = data.hour,
                        status = data.status,
                        teachers = data.teachers
                    )
                }
            )
        } catch (e: Exception) {
            throw e
        }
    }
    
    override suspend fun getStudentReservationsByClassAndDate(
        academyId: String,
        classId: String,
        date: String
    ): Result<List<StudentReservation>> {
        return try {
            // Obtener todas las reservas para la clase y fecha específicas
            val reservationsQuery = firestore
                .collection("/academies/$academyId/reservations")
                .where("classId", "==", classId)
                .where("date", "==", date)
                .get()
                .documents
            
            // Mapear cada documento a un ReservationAcademyModel
            val reservations = reservationsQuery.map { doc ->
                doc.data(ReservationAcademyModel.serializer()).copy(documentId = doc.id)
            }
            
            // Para cada reserva, obtener los datos del estudiante
            val studentReservations = reservations.mapNotNull { reservation ->
                try {
                    // Obtener datos del estudiante de la colección "users"
                    val userDoc = firestore
                        .collection("users")
                        .document(reservation.userId)
                        .get()
                    
                    // Usar serialización para obtener el UserModel
                    val userModel = userDoc.data(com.daxen.mydancekmpsharedui.core.firebase.user.models.UserModel.serializer())
                    
                    // Crear un StudentInfoReservation con los datos del usuario
                    val studentInfo = StudentInfoReservation(
                        uid = userModel.uid,
                        name = userModel.name,
                        lastName = userModel.lastName,
                        danceRole = userModel.danceRole
                    )
                    
                    // Crear y retornar StudentReservation con los datos del estudiante y la reserva
                    StudentReservation(
                        studentInfo = studentInfo,
                        reservation = reservation
                    )
                } catch (e: Exception) {
                    null
                }
            }
            
            Result.success(studentReservations)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
} 