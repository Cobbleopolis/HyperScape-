package core

import org.lwjgl.BufferUtils
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.GL11
import org.lwjgl.util.vector.{Matrix4f, Vector3f}
import render.Camera
import world.WorldMainMenu


class HyperScape {

    //    val model = ModelRegistry.cube.copy
    //    val model2 = ModelRegistry.cube.copy

    val world = new WorldMainMenu

    def init(): Unit = {
        GL11.glClearColor(0.4f, 0.6f, 0.9f, 1f)
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        HyperScape.mainCamera.uploadPerspective()
        HyperScape.mainCamera.uploadView()
    }

    def tick(): Unit = {
        HyperScape.mainCamera.view = new Matrix4f()
        val speed = 1
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            HyperScape.mainCamera.pos.translate(0, 0, speed)
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            HyperScape.mainCamera.pos.translate(speed, 0, 0)
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            HyperScape.mainCamera.pos.translate(0, 0, -speed)
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            HyperScape.mainCamera.pos.translate(-speed, 0, 0)
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
            HyperScape.mainCamera.pos.translate(0, -speed, 0)
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
            HyperScape.mainCamera.pos.translate(0, speed, 0)
        }
        HyperScape.mainCamera.view.translate(HyperScape.mainCamera.pos)
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
