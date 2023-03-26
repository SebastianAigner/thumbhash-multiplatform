import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.delay
import java.io.File
import javax.imageio.ImageIO

val hex = ImageIO.read(File("plant.png"))
val hash = hex.toHash()
val decodedImage = KotlinThumbHash.thumbHashToRGBA(hash.hash)
val renderedImage = renderImage(decodedImage)

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "Compose for Desktop",
        state = rememberWindowState(width = 300.dp, height = 300.dp)
    ) {
        MaterialTheme {
            Row(Modifier.fillMaxSize(), Arrangement.spacedBy(5.dp)) {
                Column {
                    Text("Skia Image (Multiplatform)")
                    ThumbhashImage(hash) {
                        delay(2000)
                        hex.toComposeImageBitmap()
                    }
                }
                Column {
                    Text("BufferedImage")
                    Image(
                        bitmap = renderedImage.toComposeImageBitmap(),
                        modifier = Modifier.size(200.dp), contentDescription = null
                    )
                }
            }
        }
    }
}