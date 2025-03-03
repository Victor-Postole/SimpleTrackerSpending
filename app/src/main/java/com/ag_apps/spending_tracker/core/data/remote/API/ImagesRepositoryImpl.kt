package com.ag_apps.spending_tracker.core.data.remote.API

import com.ag_apps.spending_tracker.core.data.mapper.toImages
import com.ag_apps.spending_tracker.core.domain.repositories.ImageRepository
import com.ag_apps.spending_tracker.core.domain.models.Images

class ImagesRepositoryImpl(private val imagesApi: ImagesApi): ImageRepository {

    override suspend fun searchImages(query: String): Images? {
        return imagesApi.searchImages(query)?.toImages()
    }
}