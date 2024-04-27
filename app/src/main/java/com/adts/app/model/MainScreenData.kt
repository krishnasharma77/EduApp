package com.adts.app.model

import java.io.Serializable

    sealed class MediaType {
        data class Image(val resourceId: Int) : MediaType() // Public by default
        data class Video(val videoUrl: String) : MediaType() // Public by default
    }
    
    
    data class MainScreenData(
        val courseDescription: String,
        val courseDuration: String,
        val courseId: Int,
        val media: MediaType?, // The media type (image or video)
        val courseName: String,
        val coursePrice: String,
        val createdDate: String,
        val isBuy: Boolean
    ) : Serializable
