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

    val shaders = Array("terrain", "debug", "plaid", "rave")
    var shaderSelector = 0

    /**
     * Initializes the game
     */
    def init(): Unit = {
        player = new Entity
        player.translate(0, 17.8f, 0)
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
        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState) {
                if (Keyboard.getEventKey == Keyboard.KEY_F3) {
                    if (Keyboard.getEventKeyState) {
                        HyperScape.shaderSelector += 1
                        if (HyperScape.shaderSelector >= shaders.length) HyperScape.shaderSelector = 0
                        println("Entering " + shaders(HyperScape.shaderSelector) + " mode...")
                    }
                }
            }
        }
        player.velocity = new Vector3f
        HyperScape.mainCamera.view = new Matrix4f
        val speed: Float = .25f
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            player.addToSpeedInDirectionFacing(0, 0, speed)
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            player.addToSpeedInDirectionFacing(speed, 0, 0)
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            player.addToSpeedInDirectionFacing(0, 0, -speed)
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            player.addToSpeedInDirectionFacing(-speed, 0, 0)
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_E)) {
            player.addToSpeedInDirectionFacing(0, -speed, 0)
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
            player.addToSpeedInDirectionFacing(0, speed, 0)
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
        HyperScape.mainCamera.view.rotate(player.rotation.getX, new Vector3f(1, 0, 0))
        HyperScape.mainCamera.view.rotate(player.rotation.getY, new Vector3f(0, 1, 0))
        HyperScape.mainCamera.view.rotate(player.rotation.getZ, new Vector3f(0, 0, 1))
        HyperScape.mainCamera.view.translate(player.position)
        HyperScape.mainCamera.view.translate(new Vector3f(1, 1, 1))
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
    val mainCamera = new Camera
    /**The Camera that renders they game*/

    val uploadBuffer = BufferUtils.createFloatBuffer(64000000)
    /**The buffer used to upload to the GPU*/

    var shaderSelector = 0
}
