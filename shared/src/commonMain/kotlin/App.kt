import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.AbsoluteCutCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ButtonElevation
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import dev.icerock.moko.mvvm.compose.getViewModel
import dev.icerock.moko.mvvm.compose.viewModelFactory
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import model.BirdImage
import org.jetbrains.compose.resources.ExperimentalResourceApi


@Composable
fun BirdAppTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = MaterialTheme.colors.copy(primary = Color.Black),
        shapes = MaterialTheme.shapes.copy(
            AbsoluteCutCornerShape(0.dp),
            AbsoluteCutCornerShape(0.dp),
            AbsoluteCutCornerShape(0.dp)
        )
    ) {
        content()
    }
}


@Composable
fun App() {
    BirdAppTheme {
        Column {

            BirdsPage(getViewModel(Unit, viewModelFactory { BirdViewModel() }))
        }
    }
}

@Composable
fun BirdsPage(viewModel: BirdViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(5.dp),
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            uiState.category.forEach {
                Button(onClick = {
                    viewModel.selectCategory(it)
                },Modifier.aspectRatio(1f).fillMaxSize().weight(1f), elevation = ButtonDefaults.elevation(
                    defaultElevation = 0.dp,
                    focusedElevation = 0.dp
                )) {
                    Text(
                        it,
                        style = TextStyle(fontWeight = if (uiState.selectedCategory == it) FontWeight.Bold else null)
                    )
                }
            }
        }

        AnimatedVisibility(uiState.selectedImage.isNotEmpty() || uiState.images.isNotEmpty()) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(5.dp),
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier.fillMaxSize().padding(horizontal = 5.dp)
            ) {
                if (uiState.selectedImage.isNotEmpty()){
                    items(uiState.selectedImage) {
                        BirdImageCell(it)
                    }
                }else{
                    items(uiState.images){
                        BirdImageCell(it)
                    }
                }
            }
        }
    }
}

@Composable
fun BirdImageCell(it: BirdImage) {
    KamelImage(
        asyncPainterResource("https://sebi.io/demo-image-api/${it.path}"),
        "",
        contentScale = ContentScale.Crop,
        modifier = Modifier.fillMaxWidth().aspectRatio(1f)
    )
}


expect fun getPlatformName(): String