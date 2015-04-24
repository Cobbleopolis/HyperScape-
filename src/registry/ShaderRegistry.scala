package registry

import org.lwjgl.opengl.{GL11, GL20}
import render.Shader

object ShaderRegistry {
    var programs: Map[String, Shader] = Map()
    var currShader = ""

    def loadShader(vertPath: String, fragPath: String, shaderName: String): Unit = {
        // Load the vertex shader
        val vsId = this.loadShader("res/shader/terrain.vert", GL20.GL_VERTEX_SHADER)
        // Load the fragment shader
        val fsId = this.loadShader("res/shader/terrain.frag", GL20.GL_FRAGMENT_SHADER)

        // Create a new shader program that links both shaders
        val pId = GL20.glCreateProgram()
        GL20.glAttachShader(pId, vsId)
        GL20.glAttachShader(pId, fsId)

        // Position information will be attribute 0
        GL20.glBindAttribLocation(pId, 0, "in_Position")
        // Color information will be attribute 1
        GL20.glBindAttribLocation(pId, 1, "in_TextureCoord")

        GL20.glLinkProgram(pId)
        GL20.glValidateProgram(pId)

        val errorCheckValue = GL11.glGetError()
        if (errorCheckValue != GL11.GL_NO_ERROR) {
            System.out.println("ERROR - Could not create the shaders: " + Integer.toHexString(errorCheckValue))
            System.exit(-1)
        }
        programs += (shaderName -> new Shader(pId, vsId, fsId))
        println("\tShader Id | " + shaderName + " | " + pId + ", " + vsId + ", " + fsId)
    }

    def loadShader(filename: String, shaderType: Int): Int = {
        val shaderSource = scala.io.Source.fromFile(filename).mkString

        val shaderID = GL20.glCreateShader(shaderType)
        GL20.glShaderSource(shaderID, shaderSource)
        GL20.glCompileShader(shaderID)
        val log = GL20.glGetShaderInfoLog(shaderID, 32767)
        if (!log.isEmpty) {
            println("Shader Log:")
            println(log)
        }
        shaderID
    }

    def bindShader(shaderName: String): Unit = {
        if(!shaderName.equals(currShader)){
            programs(shaderName).bind()
            currShader = shaderName
        }
    }

    def getCurrentShader(): Shader = {
        programs(currShader)
    }

    def destroyShader(shaderName: String): Unit = {
        programs(shaderName).destroy()
    }

    def destroyAllShaders(): Unit = {
        for(shader <- programs) {
            shader._2.destroy()
        }
    }

}
