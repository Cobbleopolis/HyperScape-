package core

import org.lwjgl.BufferUtils
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.{GL11, GL20}
import org.lwjgl.util.vector.Vector3f
import registry.{ModelRegistry, ShaderRegistry}
import render.{Model, Camera, RenderModel}
import world.WorldMainMenu


class HyperScape {

    //    val model = ModelRegistry.cube.copy
    //    val model2 = ModelRegistry.cube.copy

    val world = new WorldMainMenu

    val models = Array[Model](
        ModelRegistry.cube.copy, ModelRegistry.cube.copy, ModelRegistry.cube.copy,
        ModelRegistry.cube.copy, ModelRegistry.cube.copy, ModelRegistry.cube.copy,
        ModelRegistry.cube.copy, ModelRegistry.cube.copy, ModelRegistry.cube.copy
    )

    def init(): Unit = {
        GL11.glClearColor(0.4f, 0.6f, 0.9f, 1f)
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        HyperScape.mainCamera.uploadPerspective()
        HyperScape.mainCamera.uploadView()
        for(i <- 0 until models.length) {
            models(i).translate(0, 0, -i)
        }
    }

    def tick(): Unit = {
        val speed = 1
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            HyperScape.mainCamera.view.translate(new Vector3f(0, 0, speed))
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            HyperScape.mainCamera.view.translate(new Vector3f(speed, 0, 0))
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            HyperScape.mainCamera.view.translate(new Vector3f(0, 0, -speed))
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            HyperScape.mainCamera.view.translate(new Vector3f(-speed, 0, 0))
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
            HyperScape.mainCamera.view.translate(new Vector3f(0, -speed, 0))
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
            HyperScape.mainCamera.view.translate(new Vector3f(0, speed, 0))
        }
        HyperScape.mainCamera.uploadView()
        world.tick()
        //        println(HyperScape.mainCamera.view.toString)
//        for(model <- models){
//            model.translate(0, 0, .005f)
//        }
    }

    def render(): Unit = {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT)
        world.render()

    }

    def destroy(): Unit = {
        world.destroy()
    }
}

object HyperScape {
    val mainCamera = new Camera
    val uploadBuffer = BufferUtils.createFloatBuffer(64000000)
}
