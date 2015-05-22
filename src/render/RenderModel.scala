package render

import core.HyperScape
import org.lwjgl.opengl.{GL11, GL15, GL20, GL30}
import org.lwjgl.util.vector.Matrix4f

/**
 * Creates a renderable model
 * @param verts An array of floats used to define a model object (must be ordered x, y, z, u, v)
 */
class RenderModel(verts: Array[Float]) extends Model(verts) {
    val modelMatrix = new Matrix4f()

    HyperScape.uploadBuffer.clear()
    HyperScape.uploadBuffer.put(verts)
    HyperScape.uploadBuffer.flip()

    val vao = GL30.glGenVertexArrays()
    GL30.glBindVertexArray(vao)

    val vbo = GL15.glGenBuffers()
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vbo)
    GL15.glBufferData(GL15.GL_ARRAY_BUFFER, HyperScape.uploadBuffer, GL15.GL_STATIC_DRAW)

    GL20.glVertexAttribPointer(0, Vertex.VERTEX_SIZE, GL11.GL_FLOAT, false, Vertex.SIZE_IN_BYTES, Vertex.VERTEX_OFFSET)
    GL20.glVertexAttribPointer(1, Vertex.UV_SIZE, GL11.GL_FLOAT, false, Vertex.SIZE_IN_BYTES, Vertex.UV_OFFSET_IN_BYTES)
    GL20.glVertexAttribPointer(2, Vertex.NORMAL_SIZE, GL11.GL_FLOAT, false, Vertex.SIZE_IN_BYTES, Vertex.NORMAL_OFFSET_IN_BYTES)
    GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0)

    GL30.glBindVertexArray(0)

    /**
     * Renders the model
     */
    def render(drawLines: Boolean = false): Unit = {
        // Bind to the VAO that has all the information about the quad vertices
        GL30.glBindVertexArray(vao)
        GL20.glEnableVertexAttribArray(0)
        GL20.glEnableVertexAttribArray(1)
        GL20.glEnableVertexAttribArray(2)

        // Draw the vertices
        if (drawLines)
            GL11.glDrawArrays(GL11.GL_LINES, 0, verticies.length / Vertex.ELEMENT_COUNT)
        else
            GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, verticies.length / Vertex.ELEMENT_COUNT)

        // Put everything back to default (deselect)
        GL20.glDisableVertexAttribArray(0)
        GL20.glDisableVertexAttribArray(1)
        GL30.glBindVertexArray(0)
    }

    /**
     * Destroies the model
     */
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

    override def copy: RenderModel = {
        new RenderModel(verticies)
    }
}
