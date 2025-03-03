package com.ag_apps.spending_tracker.core.data.remote.API

import com.ag_apps.spending_tracker.core.data.remote.dto.ImageListDto
import retrofit2.http.GET
import retrofit2.http.Query

interface ImagesApi {

    @GET("/api/")
    suspend fun searchImages(
        @Query("q") query: String,
        @Query("key") apiKey: String = APY_KEY
    ): ImageListDto?

    companion object {
        const val  BASE_URL = "https://pixabay.com"
        const val  APY_KEY = "48132329-d4fcce57faf555b2617b45354"
    }
}