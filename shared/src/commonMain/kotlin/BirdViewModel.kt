import dev.icerock.moko.mvvm.viewmodel.ViewModel
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import model.BirdImage

/* Created by Hanif on 16/04/24 */


data class BirdUiState(
    val images: List<BirdImage> = emptyList(),
    val selectedCategory: String? = null
) {
    val category = images.map { it.category }.toSet()
    val selectedImage = images.filter { it.category == selectedCategory }
}

class BirdViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(BirdUiState())
    val uiState = _uiState.asStateFlow()


    init {
        updateImages()
    }

    override fun onCleared() {
        httpClient.close()
    }

    private val httpClient = HttpClient {
        install(ContentNegotiation) {
            json()
        }
    }

    fun selectCategory(category: String) = run {
        _uiState.update {
            it.copy(selectedCategory = category)
        }
    }

    private fun updateImages() {
        viewModelScope.launch {
            val images = getImages()
            _uiState.update {
                it.copy(images = images)
            }
        }
    }

    private suspend fun getImages(): List<BirdImage> {
        return httpClient.get("https://sebi.io/demo-image-api/pictures.json")
            .body<List<BirdImage>>()
    }

}