package com.adts.app.model

import java.io.Serializable

data class DataX(
    val CourseDescription: String,
    val CourseDuration: String,
    val CourseId: Int,
    val CourseImage: Int,
    val CourseName: String,
    val CoursePrice: String,
    val CreatedDate: String,
    val IsBuy: Boolean,
//    val cChapters: List<CChapter>
):Serializable