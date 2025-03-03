package com.ag_apps.spending_tracker.core.data.mapper

import com.ag_apps.spending_tracker.core.data.remote.dto.ImageListDto
import com.ag_apps.spending_tracker.core.domain.models.Images


fun ImageListDto.toImages(): Images {
    return Images(images = hits?.map {
            imageDto -> imageDto.previewURL ?: ""
        } ?: emptyList()
    )
}