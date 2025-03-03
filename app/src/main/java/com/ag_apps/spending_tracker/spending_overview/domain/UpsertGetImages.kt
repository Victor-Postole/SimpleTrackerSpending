package com.ag_apps.spending_tracker.spending_overview.domain

import com.ag_apps.spending_tracker.core.data.remote.API.ImagesRepositoryImpl

class UpsertGetImages(private val imagesRepoImpl: ImagesRepositoryImpl) {

    suspend  fun getImagesByName(imageName : String) : List<String>? {
        return imagesRepoImpl.searchImages(imageName)?.images
    }
}


