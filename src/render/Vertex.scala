package render

object Vertex {
    val ELEMENT_COUNT = 5 /** The amount of elements in one vertex*/
    val UV_OFFSET_IN_BYTES = 3 * 4 /** The offset for the UV elements in bytes*/
    val SIZE_IN_BYTES = ELEMENT_COUNT * 4 /** The size of one vertex in bytes*/
}