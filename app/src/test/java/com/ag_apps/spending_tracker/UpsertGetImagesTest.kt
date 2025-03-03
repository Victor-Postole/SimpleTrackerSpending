package com.ag_apps.spending_tracker

import com.ag_apps.spending_tracker.core.data.remote.API.ImagesRepositoryImpl
import com.ag_apps.spending_tracker.core.domain.models.Images // Make sure to import the correct Images class
import com.ag_apps.spending_tracker.spending_overview.domain.UpsertGetImages
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

class UpsertGetImagesTest {

    @Mock
    private lateinit var imagesRepoImpl: ImagesRepositoryImpl

    private lateinit var upsertGetImages: UpsertGetImages

    // Initialize mocks before each test
    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        upsertGetImages = UpsertGetImages(imagesRepoImpl)
    }

    @Test
    fun `should return list of image names when images are found`() = runBlocking {
        // Given
        val imageName = "sunset"
        val expectedImages = listOf("image1.jpg", "image2.jpg")
        val mockImages = mockImages(expectedImages) // Return Images object
        Mockito.`when`(imagesRepoImpl.searchImages(imageName)).thenReturn(mockImages)

        // When
        val result = upsertGetImages.getImagesByName(imageName)

        // Then
        assertEquals(expectedImages, result)
    }

    @Test
    fun `should return null when no images are found`() = runBlocking {
        // Given
        val imageName = "moonlight"
        Mockito.`when`(imagesRepoImpl.searchImages(imageName)).thenReturn(null)

        // When
        val result = upsertGetImages.getImagesByName(imageName)

        // Then
        assertNull(result)
    }

    // Mock the Images object, assuming Images is a class that wraps a List<String>
    private fun mockImages(images: List<String>): Images? {
        return Images(images)
    }
}
