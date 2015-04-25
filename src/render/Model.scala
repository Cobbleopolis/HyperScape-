package render

import core.HyperScape
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.{GL11, GL20, GL15, GL30}
import org.lwjgl.util.vector.{Vector3f, Matrix4f}


class Model(verts: Array[Float]) {
    val modelMatrix = new Matrix4f()

    HyperScape.uploadBuffer.clear()
    HyperScape.uploadBuffer.put(verts)
    HyperScape.uploadBuffer.flip()

    val vao = GL30.glGenVertexArrays()
    GL30.glBindVertexArray(vao)

    val vbo = GL15.glGenBuffers()
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, HyperScape.uploadBuffer, GL15.GL_STATIC_DRAW)

    GL20.glVertexAttribPointer(0, 3, GL11.GL_FLOAT, false, Vertex.SIZE_IN_BYTES, 0)
    GL20.glVertexAttribPointer(1, 2, GL11.GL_FLOAT, false, Vertex.SIZE_IN_BYTES, Vertex.UV_OFFSET_IN_BYTES)
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)

    GL30.glBindVertexArray(0)

    def translate(x: Float, y: Float, z: Float): Unit = {
        modelMatrix.translate(new Vector3f(x, y, z))
    }

    def render(): Unit = {
        // Bind to the VAO that has all the information about the quad vertices
        GL30.glBindVertexArray(vao)
        GL20.glEnableVertexAttribArray(0)
        GL20.glEnableVertexAttribArray(1)

        // Draw the vertices
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, verts.length / 5)

        // Put everything back to default (deselect)
        GL20.glDisableVertexAttribArray(0)
        GL20.glDisableVertexAttribArray(1)
        GL30.glBindVertexArray(0)
    }

    def destroy(): Unit = {
        // Disable the VBO index from the VAO attributes list
        GL20.glDisableVertexAttribArray(0)

        // Delete the VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)
        GL15.glDeleteBuffers(vbo)

        // Delete the VAO
        GL30.glBindVertexArray(0)
        GL30.glDeleteVertexArrays(vao)
    }

    def copy: Model = {
        new Model(verts)
    }
}
