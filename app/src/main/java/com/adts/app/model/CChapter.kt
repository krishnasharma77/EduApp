package com.adts.app.model

import java.io.Serializable

data class CChapter(
    val ChapterDescription: String,
    val ChapterId: Int,
    val ChapterName: String,
    val ChapterPdf: String,
    val ChapterVideo: String,
    val CourseId: Int,
    val CreatedDate: String
):Serializable