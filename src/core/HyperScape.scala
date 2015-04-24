package core

import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.util.vector.Vector3f
import render.{Camera, Model}


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

    val model = new Model(verts)

    def init(): Unit = {
//        GL11.glClearColor(Math.random().toFloat, Math.random().toFloat, Math.random().toFloat, Math.random().toFloat)
        GL11.glClearColor(0.4f, 0.6f, 0.9f, 1f)
        HyperScape.mainCamera.view.translate(new Vector3f(.5f, 0, 0))
        HyperScape.mainCamera.uploadPerspective()
        HyperScape.mainCamera.uploadView()
    }

    def tick(): Unit = {

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
