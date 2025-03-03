package com.ag_apps.spending_tracker.core.domain.repositories

import com.ag_apps.spending_tracker.core.domain.models.Images

interface ImageRepository {
    suspend fun searchImages(query: String): Images?
}