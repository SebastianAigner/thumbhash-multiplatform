import kotlin.random.Random

fun main() {
    println(KotlinThumbHash.rgbaToThumbHash(100, 100, Random.Default.nextBytes(100 * 100 * 4)))
}