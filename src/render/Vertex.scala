package render

object Vertex {

    /** The amount of elements in one vertex */
    val ELEMENT_COUNT = 8

    /** The amount of elements in the x, y, z location */
    val VERTEX_SIZE = 3

    /** The amount of elements in one uv element */
    val UV_SIZE = 2

    /** The amount of elements in one normal element */
    val NORMAL_SIZE = 3

    /** The offset for the x, y, z elements in bytes */
    val VERTEX_OFFSET = 0 * 4

    /** The offset for the UV elements in bytes */
    val UV_OFFSET_IN_BYTES = 3 * 4

    /** The offset for the normal in bytes */
    val NORMAL_OFFSET_IN_BYTES = 5 * 4

    /** The size of one vertex in bytes */
    val SIZE_IN_BYTES = ELEMENT_COUNT * 4
}