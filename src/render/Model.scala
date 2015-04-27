package render

class Model(verts: Array[Float]) {
    var verticies = verts

    /**
     * Translates the model
     * @param x Amount to translate in the x
     * @param y Amount to translate in the y
     * @param z Amount to translate in the z
     */
    def translate(x: Float, y: Float, z: Float): Unit = {
        for(i <- 0 until verticies.length by 5) {
            verticies.update(i, verticies(i) + x)
            verticies.update(i + 1, verticies(i + 1) + y)
            verticies.update(i + 2, verticies(i + 2) + z)
        }
    }

    /**
     * Translates the model's UVs
     * @param x Amount of block textures to move on the x-axis
     * @param z Amount of block textures to move on the z-axis
     */
    def translateUV(x: Float, z: Float): Unit = {
        for(i <- 0 until verticies.length by 5) {
            verticies.update(i + 3, verticies(i + 3) + x)
            verticies.update(i + 4, verticies(i + 4) + z)
        }
    }

    /**
     * Removes a face from the model
     * @param faceIndex The index of the face to remove
     */
    def removeFace(faceIndex: Int): Unit = {
        verticies = verticies.dropRight(faceIndex * Vertex.ELEMENT_COUNT) ++ verticies.drop(faceIndex * (Vertex.ELEMENT_COUNT + 1))
    }

    /**
     * Gets the model's vertices
     * @return The model's vertices
     */
    def getVertices: Array[Float] = verticies

    /**
     * Gets a copy of the model
     * @return A copy of the model
     */
    def copy: Model = {
        new Model(verticies)
    }
}
