package com.daxen.mydancekmpsharedui.features.teacher.classes.ui.composables

import com.daxen.mydancekmpsharedui.data.academy.classes.models.SpecificClassModel
import com.daxen.mydancekmpsharedui.data.academy.classes.models.WeeklyClassModel
import kotlinx.datetime.DayOfWeek

data class ClassesGroup(
    val dayOfWeek: DayOfWeek,
    val weeklyClasses: List<WeeklyClassModel>,
    val specificClasses: List<SpecificClassModel>
) 