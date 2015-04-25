package core

import org.lwjgl.BufferUtils
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.{GL20, GL11}
import org.lwjgl.util.vector.{Matrix4f, Vector3f}
import registry.{ModelRegistry, ShaderRegistry}
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

//    val model = ModelRegistry.cube.copy
//    val model2 = ModelRegistry.cube.copy

    val models = Array[Model](ModelRegistry.cube.copy, ModelRegistry.cube.copy, ModelRegistry.cube.copy)

    def init(): Unit = {
//        GL11.glClearColor(Math.random().toFloat, Math.random().toFloat, Math.random().toFloat, Math.random().toFloat)
        GL11.glClearColor(0.4f, 0.6f, 0.9f, 1f)
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        HyperScape.mainCamera.uploadPerspective()
        HyperScape.mainCamera.uploadView()
        models(1).translate(1, 0, 0)
        models(2).translate(-1, 0, 0)
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
        val loc = ShaderRegistry.getCurrentShader().getUniformLocation("modelMatrix")
        for(model <- models){
            model.modelMatrix.store(HyperScape.uploadBuffer)
            HyperScape.uploadBuffer.flip()
            GL20.glUniformMatrix4(loc, false, HyperScape.uploadBuffer)
            HyperScape.uploadBuffer.clear()
            model.render()
        }

    }

    def destroy(): Unit = {
        for(model <- models){
            model.destroy()
        }
    }
}

object HyperScape {
    val mainCamera = new Camera
    val uploadBuffer = BufferUtils.createFloatBuffer(16000)
}
