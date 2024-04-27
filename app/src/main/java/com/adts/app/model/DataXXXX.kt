package com.adts.app.model

import java.io.Serializable

data class DataXXXX(
    val CourseDescription: String,
    val CourseDuration: String,
    val CourseId: Int,
    val CourseImage: String,
    val CourseName: String,
    val CoursePrice: Int,
    val CreatedDate: String,
    val cChapters: List<CChapter>
): Serializable