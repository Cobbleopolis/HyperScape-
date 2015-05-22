package render

import java.util

import org.lwjgl.util.vector.Vector3f

/**
 * Creates a model object. This can not be rendered.
 * @param verts An array of floats used to define a model object (must be ordered x, y, z, u, v, normalX, normalY, normalX)
 */
class Model(verts: Array[Float]) {
    var verticies = util.Arrays.copyOf(verts, verts.length)

    def translate(vector: Vector3f): Unit = {
        translate(vector.getX, vector.getY, vector.getX)
    }
    /**
     * Translates the model
     * @param x Amount to translate in the x
     * @param y Amount to translate in the y
     * @param z Amount to translate in the z
     */
    def translate(x: Float, y: Float, z: Float): Unit = {
        for (i <- 0 until verticies.length by Vertex.ELEMENT_COUNT) {
            verticies.update(i, verticies(i) + x)
            verticies.update(i + 1, verticies(i + 1) + y)
            verticies.update(i + 2, verticies(i + 2) + z)
        }
    }

    def rotate(roll: Float, pitch: Float, yaw: Float): Unit = {
        val avg = new Vector3f()
        for (i <- 0 until verticies.length by Vertex.ELEMENT_COUNT) {
            avg.translate(verticies(i), verticies(i + 1), verticies(i + 2))
        }
        val vertCount = verticies.length / Vertex.ELEMENT_COUNT
        avg.set(avg.getX / vertCount, avg.getY / vertCount, avg.getZ / vertCount)
        val moveVec = avg.negate().asInstanceOf[Vector3f]
        translate(moveVec)
        for (i <- 0 until verticies.length by Vertex.ELEMENT_COUNT) {
            verticies.update(i, verticies(i) + Math.sin(Math.toRadians(roll)).toFloat)
            verticies.update(i + 1, verticies(i + 1) + Math.sin(Math.toRadians(pitch)).toFloat)
            verticies.update(i + 2, verticies(i + 2) + Math.sin(Math.toRadians(yaw)).toFloat)
        }
        translate(avg)
    }

    def rotate(vec: Vector3f): Unit = {
        rotate(vec.getX, vec.getY, vec.getZ)
    }

    /**
     * Translates the model's UVs
     * @param x Amount of block textures to move on the x-axis
     * @param z Amount of block textures to move on the z-axis
     */
    def translateUV(x: Float, z: Float): Unit = {
        for (i <- 0 until verticies.length by Vertex.ELEMENT_COUNT) {
            verticies.update(i + 3, verticies(i + 3) + x)
            verticies.update(i + 4, verticies(i + 4) + z)
        }
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
