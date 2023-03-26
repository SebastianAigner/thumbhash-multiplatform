import java.awt.Color
import java.awt.image.BufferedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

fun BufferedImage.toHash(): Thumbhash {
    val newImage = BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB)
    val graphics = newImage.createGraphics()
    graphics.drawImage(this, 0, 0, width, height, null)
    return thumbhash(newImage)
}

@OptIn(ExperimentalStdlibApi::class)
private fun thumbhash(bufferedImage: BufferedImage): Thumbhash {
    val myImage = if (bufferedImage.width > 100 || bufferedImage.height > 100) {
        bufferedImage.resize(100, 100)
    } else bufferedImage

    require(bufferedImage.type == BufferedImage.TYPE_INT_ARGB) {
        "Expected image type ${findImageTypeName(BufferedImage.TYPE_INT_ARGB)}, got: ${findImageTypeName(bufferedImage.type)}"
    }

    with(myImage) {
        val ba = ByteArray(4 * width * height)
        for (row in 0..<height) {
            for (column in 0..<width) {
                val color = Color(getRGB(column, row), true)
                val offset = (row * width + column) * 4
                ba[offset + 0] = (color.red and 0xFF).toByte()
                ba[offset + 1] = (color.green and 0xFF).toByte()
                ba[offset + 2] = (color.blue and 0xFF).toByte()
                ba[offset + 3] = (color.alpha and 0xFF).toByte()
            }
        }
        val hash = KotlinThumbHash.rgbaToThumbHash(width, height, ba)
        return Thumbhash(hash)
    }
}

fun findImageTypeName(imageType: Int): String? {
    return BufferedImage::class.java.fields.find {
        it.type == Int::class.javaPrimitiveType &&
                it.name.startsWith("TYPE_") &&
                it.getInt(null) == imageType
    }?.name
}

fun BufferedImage.resize(w: Int, h: Int): BufferedImage {
    val newImage = BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB)
    val graphics = newImage.createGraphics()
    graphics.drawImage(this, 0, 0, w, h, null)
    return newImage
}

@OptIn(ExperimentalTime::class)
fun main() {
    println(measureTime {
        val hex = ImageIO.read(File("plant.png"))
        val hash = hex.toHash()
        println(hash)
        val decodedImage = KotlinThumbHash.thumbHashToRGBA(hash.hash)
        val renderedImage = renderImage(decodedImage)
        ImageIO.write(renderedImage, "png", File("blurred.png"))
    })
}

fun renderImage(rgbaImage: KotlinThumbHash.Image): BufferedImage {
    val w = rgbaImage.width
    val h = rgbaImage.height
    val render = BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB)
    val byteArr = rgbaImage.rgba
    val argbArr = IntArray(byteArr.size / 4) { pixelIdx ->
        val hashIx = pixelIdx * 4
        val r = byteArr[hashIx + 0].toUByte()
        val g = byteArr[hashIx + 1].toUByte()
        val b = byteArr[hashIx + 2].toUByte()
        val a = byteArr[hashIx + 3].toUByte()
        val argb: Int =
            (a.toInt() shl (3 * 8)) or
                    (r.toInt() shl (2 * 8)) or
                    (g.toInt() shl (1 * 8)) or
                    (b.toInt() shl (0 * 8))
        argb
    }

    render.setRGB(0, 0, w, h, argbArr, 0, w)

    return render
}