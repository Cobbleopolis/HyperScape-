package core

import org.lwjgl.BufferUtils
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.{GL20, GL11}
import org.lwjgl.util.vector.{Matrix4f, Vector3f}
import registry.ShaderRegistry
import render.{OBJLoader, Camera, Model}


class HyperScape {

    val verts = Array[Float] (
        // Left bottom triangle
        -0.5f,  0.5f, 0f, 0f, 1f,
        -0.5f, -0.5f, 0f, 0f, 0f,
         0.5f, -0.5f, 0f, 1f, 0f,
        // Right top triangle
         0.5f, -0.5f, 0f, 1f, 0f,
         0.5f,  0.5f, 0f, 1f, 1f,
        -0.5f,  0.5f, 0f, 0f, 1f
    )

    val model = OBJLoader.loadFromOBJFile("res/model/cube.obj")

    val modelMatrix = new Matrix4f()
    def init(): Unit = {
//        GL11.glClearColor(Math.random().toFloat, Math.random().toFloat, Math.random().toFloat, Math.random().toFloat)
        GL11.glClearColor(0.4f, 0.6f, 0.9f, 1f)
        HyperScape.mainCamera.view.translate(new Vector3f(0, 0, -1))
        HyperScape.mainCamera.uploadPerspective()
        HyperScape.mainCamera.uploadView()
        val loc = ShaderRegistry.getCurrentShader().getUniformLocation("modelMatrix")
        modelMatrix.store(HyperScape.uploadBuffer)
        HyperScape.uploadBuffer.flip()
        GL20.glUniformMatrix4(loc, false, HyperScape.uploadBuffer)
        HyperScape.uploadBuffer.clear()
    }

    def tick(): Unit = {
        val speed = .025f
        if(Keyboard.isKeyDown(Keyboard.KEY_W)){
            HyperScape.mainCamera.view.translate(new Vector3f(0, 0, speed))
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_A)){
            HyperScape.mainCamera.view.translate(new Vector3f(speed, 0, 0))
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_S)){
            HyperScape.mainCamera.view.translate(new Vector3f(0, 0, -speed))
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_D)){
            HyperScape.mainCamera.view.translate(new Vector3f(-speed, 0, 0))
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_E)){
            HyperScape.mainCamera.view.translate(new Vector3f(0, -speed, 0))
        }
        if(Keyboard.isKeyDown(Keyboard.KEY_Q)){
            HyperScape.mainCamera.view.translate(new Vector3f(0, speed, 0))
        }
        HyperScape.mainCamera.uploadView()
//        println(HyperScape.mainCamera.view.toString)
    }

    def render(): Unit = {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT)
        model.render()
    }

    def destroy(): Unit = {
        model.destroy()
    }
}

object HyperScape {
    val mainCamera = new Camera
    val uploadBuffer = BufferUtils.createFloatBuffer(16000)
}
