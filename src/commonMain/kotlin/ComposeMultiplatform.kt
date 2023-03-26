import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.unit.dp
import org.jetbrains.skia.ColorAlphaType
import org.jetbrains.skia.ColorType
import org.jetbrains.skia.Image
import org.jetbrains.skia.ImageInfo

fun turnIntoSkiaImage(width: Int, height: Int, ba: ByteArray): Image {
    val imgInfo = ImageInfo(width, height, ColorType.RGBA_8888, ColorAlphaType.UNPREMUL)
    println(ba.size)
    val x = Image.makeRaster(
        imgInfo,
        ba,
        imgInfo.bytesPerPixel * width,
    )

    return x
}

@Composable
fun ThumbhashImage(thumbhash: Thumbhash, loadImage: suspend () -> ImageBitmap?) {
    var image by remember {
        mutableStateOf(
            turnIntoSkiaImage(
                thumbhash.width,
                thumbhash.height,
                thumbhash.image().rgba
            ).toComposeImageBitmap()
        )
    }
    LaunchedEffect(thumbhash) {
        val newImg = loadImage()
        if (newImg != null) {
            image = newImg
        }
    }
    Crossfade(targetState = image) {
        Image(
            bitmap = it,
            modifier = Modifier.size(200.dp), contentDescription = null
        )
    }
}