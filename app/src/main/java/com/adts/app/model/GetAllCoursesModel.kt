package com.adts.app.model

data class GetAllCoursesModel(
    val `data`: List<DataX>,
    val message: String,
    val status: String
)