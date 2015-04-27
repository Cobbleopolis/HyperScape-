package core

import entity.Entity
import org.lwjgl.BufferUtils
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.GL11
import org.lwjgl.util.vector.{Matrix4f, Vector3f}
import render.Camera
import world.WorldMainMenu


class HyperScape {

    val world = new WorldMainMenu

    var player: Entity = null

    /**
     * Initializes the game
     */
    def init(): Unit = {
        player = new Entity
        player.translate(0, 7.8f, 0)
        player.rotate(0, Math.toRadians(180).toFloat, 0)
        GL11.glClearColor(0.4f, 0.6f, 0.9f, 1f)
        GL11.glEnable(GL11.GL_DEPTH_TEST)
        HyperScape.mainCamera.uploadPerspective()
        HyperScape.mainCamera.uploadView()
    }

    /**
     * Ticks the game
     */
    def tick(): Unit = {
        HyperScape.mainCamera.view = new Matrix4f()
        val speed: Float = .25f
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            player.translateInDirectionFacing(0, 0, speed)
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            player.translateInDirectionFacing(speed, 0, 0)
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            player.translateInDirectionFacing(0, 0, -speed)
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            player.translateInDirectionFacing(-speed, 0, 0)
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
            player.translateInDirectionFacing(0, -speed, 0)
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
            player.translateInDirectionFacing(0, speed, 0)
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            player.rotate(0, -Math.toRadians(2.5).toFloat, 0)
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            player.rotate(0, Math.toRadians(2.5).toFloat, 0)
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            player.rotate(-Math.toRadians(2.5).toFloat, 0, 0)
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            player.rotate(Math.toRadians(2.5).toFloat, 0, 0)
        }
        HyperScape.mainCamera.view.rotate(player.rot.getX, new Vector3f(1, 0, 0))
        HyperScape.mainCamera.view.rotate(player.rot.getY, new Vector3f(0, 1, 0))
        HyperScape.mainCamera.view.rotate(player.rot.getZ, new Vector3f(0, 0, 1))
        HyperScape.mainCamera.view.translate(player.pos)
        HyperScape.mainCamera.uploadView()
        world.tick(player)
    }

    /**
     * Renders the game
     */
    def render(): Unit = {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT)
        world.render()

    }

    /**
     * Destroys the game
     */
    def destroy(): Unit = {
        world.destroy()
    }
}

object HyperScape {
    val mainCamera = new Camera /**The Camera that renders they game*/
    val uploadBuffer = BufferUtils.createFloatBuffer(64000000) /**The buffer used to upload to the GPU*/
}
