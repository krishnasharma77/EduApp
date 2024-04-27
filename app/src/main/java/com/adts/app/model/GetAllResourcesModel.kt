package com.adts.app.model

data class GetAllResourcesModel(
    val `data`: List<DataXX>,
    val message: String,
    val status: String
)