package reference

object WorldRef {
    val CHUNK_DIM = 16
    val CHUNK_HEIGHT = 256
    val CHUNK_SIZE = CHUNK_DIM * CHUNK_DIM * CHUNK_HEIGHT
    val CHUNK_LIGHT_SIZE = (CHUNK_DIM + 2) * (CHUNK_DIM + 2) * CHUNK_HEIGHT
}
