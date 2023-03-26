@JvmInline
value class Thumbhash(val hash: ByteArray) {
    override fun toString(): String {
        return hash.joinToString(" ") {
            it.toUByte().toString(16).padStart(2, '0')
        }
    }

    fun image(): KotlinThumbHash.Image {
        return KotlinThumbHash.thumbHashToRGBA(hash)
    }

    val width
        get() = image().width

    val height
        get() = image().height
}