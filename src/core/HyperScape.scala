package core

import entity.EntityHuman
import org.lwjgl.BufferUtils
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.GL11
import org.lwjgl.util.vector.{Matrix4f, Vector3f}
import reference.Blocks
import render.Camera
import world.{World, WorldMainMenu}


class HyperScape {

    var world: World = null

    var player: EntityHuman = null

    val shaders = Array("terrain", "debug", "Panic! at the Disco", "plaid")
    var shaderSelector = 0

    /**
     * Initializes the game
     */
    def init(): Unit = {
        Blocks.registerBlocks()
        player = new EntityHuman
        player.translate(0, 17f, 0)
        player.rotate(0, Math.toRadians(180).toFloat, 0)
        world = new WorldMainMenu
        HyperScape.mainCamera.uploadPerspective()
        HyperScape.mainCamera.uploadView()
    }

    /**
     * Ticks the game
     */
    def tick(): Unit = {
        player.velocity = new Vector3f
        HyperScape.mainCamera.view = new Matrix4f
        val speed: Float = .5f
        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState) {
                if (Keyboard.getEventKey == Keyboard.KEY_F3) {
                    //                    if (Keyboard.getEventKeyState) {
                    HyperScape.shaderSelector += 1
                    if (HyperScape.shaderSelector >= shaders.length) HyperScape.shaderSelector = 0
                    println("Entering " + shaders(HyperScape.shaderSelector) + " mode...")
                    //                    }
                }
                if (Keyboard.getEventKey == Keyboard.KEY_E) {
                    println("Jump")
                    player.isJumping = true
                }
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            player.addToSpeedInDirectionFacing(0, 0, -speed)
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            player.addToSpeedInDirectionFacing(-speed, 0, 0)
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            player.addToSpeedInDirectionFacing(0, 0, speed)
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            player.addToSpeedInDirectionFacing(speed, 0, 0)
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_Q)) {
            player.addToSpeedInDirectionFacing(0, -speed, 0)
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            player.rotate(0, Math.toRadians(2.5).toFloat, 0)
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            player.rotate(0, -Math.toRadians(2.5).toFloat, 0)
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            player.rotate(Math.toRadians(2.5).toFloat, 0, 0)
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            player.rotate(-Math.toRadians(2.5).toFloat, 0, 0)
        }
        HyperScape.mainCamera.view.rotate(-player.rotation.getX, new Vector3f(1, 0, 0))
        HyperScape.mainCamera.view.rotate(-player.rotation.getY + Math.PI.toFloat / 2, new Vector3f(0, 1, 0))
        HyperScape.mainCamera.view.rotate(-player.rotation.getZ, new Vector3f(0, 0, 1))
        player.position.scale(-1.0f)
        HyperScape.mainCamera.view.translate(player.position)
        player.position.scale(-1.0f)
        HyperScape.mainCamera.view.translate(new Vector3f(0, -player.camHeight, 0))
        //        println(HyperScape.mainCamera.view.toString)
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
    /** The buffer used to upload to the GPU. Max is 64000000 floats */
    val uploadBuffer = BufferUtils.createFloatBuffer(64000000)

    var shaderSelector = 0

    /** The Camera that renders they game */
    val mainCamera = new Camera
}
