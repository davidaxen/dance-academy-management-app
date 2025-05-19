package com.daxen.mydancekmpsharedui.core.firebase.academy.classes.repository

import com.daxen.mydancekmpsharedui.core.firebase.academy.classes.model.SpecificClassModel
import com.daxen.mydancekmpsharedui.core.firebase.academy.classes.model.WeeklyClassModel
import dev.gitlive.firebase.firestore.FirebaseFirestore
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
} 