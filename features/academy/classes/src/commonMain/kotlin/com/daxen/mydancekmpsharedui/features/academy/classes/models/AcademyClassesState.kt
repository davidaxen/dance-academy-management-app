package com.daxen.mydancekmpsharedui.features.academy.classes.models

import com.daxen.mydancekmpsharedui.data.academy.classes.models.SpecificClassModel
import com.daxen.mydancekmpsharedui.data.academy.classes.models.WeeklyClassModel
import kotlinx.datetime.LocalDate

data class AcademyClassesState(
    val isLoading: Boolean = false,
    val weeklyClasses: List<WeeklyClassModel> = emptyList(),
    val specificClasses: List<SpecificClassModel> = emptyList(),
    val selectedWeekStartDate: LocalDate? = null,
    val selectedWeekEndDate: LocalDate? = null,
    val errorMessage: String? = null
) 