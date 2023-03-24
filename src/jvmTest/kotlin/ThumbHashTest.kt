import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import kotlin.random.Random
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class ThumbHashTest {

    @Test
    fun shouldBeAbleToInstantiateJavaClass() {
        JavaThumbHash()
    }

    @Test
    fun shouldThrowForTooLargeImages() {
        assertThrows<IllegalArgumentException> { JavaThumbHash.rgbaToThumbHash(101, 101, ByteArray(101 * 101 * 4)) }
        assertThrows<IllegalArgumentException> { KotlinThumbHash.rgbaToThumbHash(101, 101, ByteArray(101 * 101 * 4)) }
    }

    @Test
    fun shouldBehaveSame() {
        repeat(100_000) {
            val seed = Random.nextInt()
            try {
                executeTest(seed)
            } catch (e: Exception) {
                println("seed=$seed")
            }
        }
    }

    fun executeTest(seed: Int) {
        val random = Random(seed)
        val w = random.nextInt(2, 100)
        val h = random.nextInt(2, 100)
        val image = random.generateRandomImage(w, h)

        // rgbaToThumbHash
        val javaHash = JavaThumbHash.rgbaToThumbHash(w, h, image)
        val kotlinHash = KotlinThumbHash.rgbaToThumbHash(w, h, image)
        assertContentEquals(javaHash, kotlinHash, "Implementations of algorithm deviate.")
        val hash = javaHash

        // thumbHashToRBGA
        val javaRgba = JavaThumbHash.thumbHashToRGBA(hash)
        val kotlinRgba = KotlinThumbHash.thumbHashToRGBA(hash)
        assertEquals(javaRgba.height, kotlinRgba.height)
        assertEquals(javaRgba.width, kotlinRgba.width)
        assertContentEquals(javaRgba.rgba, kotlinRgba.rgba)

        // thumbHashToAverageRGBA
        val javaAvgRgba = JavaThumbHash.thumbHashToAverageRGBA(hash)
        val kotlinAvgRgba = KotlinThumbHash.thumbHashToAverageRGBA(hash)
        assertEquals(javaAvgRgba.r, kotlinAvgRgba.r)
        assertEquals(javaAvgRgba.g, kotlinAvgRgba.g)
        assertEquals(javaAvgRgba.b, kotlinAvgRgba.b)
        assertEquals(javaAvgRgba.a, kotlinAvgRgba.a)

        // thumbHashToApproximateAspectRatio
        val javaAspectRatio = JavaThumbHash.thumbHashToApproximateAspectRatio(hash)
        val kotlinAspectRatio = KotlinThumbHash.thumbHashToApproximateAspectRatio(hash)
        assertEquals(javaAspectRatio, kotlinAspectRatio)
    }
}

fun Random.generateRandomImage(w: Int, h: Int) = nextBytes(w * h * 4)