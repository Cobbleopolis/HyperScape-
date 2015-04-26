package render

import org.lwjgl.opengl.GL20

class Shader(programId: Int, vertexId: Int, fragmentId: Int) {

    var uniformLocations: Map[String, Int] = Map() /** Map of the uniform locations for the shader*/

    /**
     * Returns the location for the uniform location
     * @param name Name of the uniform variable
     * @return Location of the uniform variable
     */
    def getUniformLocation(name: String): Int = {
        var location: Int = 0
        if (uniformLocations.contains(name)) {
            location = uniformLocations(name)
        } else {
            location = GL20.glGetUniformLocation(programId, name)
            uniformLocations += (name -> location)
        }
        location
    }


    /**
     * Binds the shader
     */
    def bind(): Unit = {
        GL20.glUseProgram(programId)
    }

    /**
     * Destroies the shader
     */
    def destroy(): Unit = {
        GL20.glUseProgram(0)
        GL20.glDetachShader(programId, vertexId)
        GL20.glDetachShader(programId, fragmentId)

        GL20.glDeleteShader(fragmentId)
        GL20.glDeleteShader(vertexId)
        GL20.glDeleteProgram(programId)
    }
}
