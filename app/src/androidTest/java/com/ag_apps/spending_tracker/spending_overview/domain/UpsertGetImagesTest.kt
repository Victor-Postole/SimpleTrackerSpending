
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.jupiter.api.Assertions.assertEquals

class ImagesRepositoryImpl {
    fun searchImages(imageName: String): List<String>? {
        // Simple implementation
        return when (imageName) {"testImage" -> listOf("image1.jpg", "image2.jpg")else -> null }
    }
}

class UpsertGetImages(private val imagesRepoImpl: ImagesRepositoryImpl) {
    suspend fun getImagesByName(imageName: String): List<String>? {
        return imagesRepoImpl.searchImages(imageName)
    }
}

class UpsertGetImagesTest {

    private val imagesRepoImpl = ImagesRepositoryImpl()
    private val upsertGetImages = UpsertGetImages(imagesRepoImpl)

    @Test
    fun testGetImagesByNameReturnsImagesWhenFound() = runBlocking {
        // Arrange
        val imageName = "testImage"
        val expectedImages = listOf("image1.jpg", "image2.jpg")

        // Act
        val result = upsertGetImages.getImagesByName(imageName)

        // Assert
        assertEquals(expectedImages, result)
    }

    @Test
    fun testGetImagesByNameReturnsNullWhenNoImagesFound() = runBlocking {
        // Arrange
        val imageName = "nonExistentImage"

        // Act
        val result = upsertGetImages.getImagesByName(imageName)

        // Assert
        assertEquals(null, result)
    }
}
