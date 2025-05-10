package com.daxen.mydancekmpsharedui.features.academy.students.listing.ui

data class StudentModel(
    val id: String,
    val name: String,
    val surnames: String,
    val email: String,
    val profileImageUrl: String? = null
) 