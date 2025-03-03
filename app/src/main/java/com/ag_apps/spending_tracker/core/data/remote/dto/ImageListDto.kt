package com.ag_apps.spending_tracker.core.data.remote.dto

data class ImageListDto(
    val hits: List<ImageDto>?,
    val total: Int?,
    val totalHits : Int?
)