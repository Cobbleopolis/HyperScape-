package core

import entity.EntityPlayable
import org.lwjgl.BufferUtils
import org.lwjgl.opengl.GL11
import org.lwjgl.util.vector.{Matrix4f, Vector3f}
import reference.Blocks
import render.Camera
import world.{World, WorldMainMenu}


class HyperScape {

    var world: World = null

    var player: EntityPlayable = null

    var shaderSelector = 0

    /**
     * Initializes the game
     */
    def init(): Unit = {
        Blocks.registerBlocks()
        player = new EntityPlayable
        player.translate(0, 15f, 0)
//        player.rotate(0, Math.toRadians(180).toFloat, 0)
        world = new WorldMainMenu
        HyperScape.mainCamera.uploadPerspective()
        HyperScape.mainCamera.uploadView()
    }

    /**
     * Ticks the game
     */
    def tick(): Unit = {
        HyperScape.mainCamera.view = new Matrix4f
        world.tick(player)
        player.parseInput()
        HyperScape.mainCamera.view.rotate(-player.rotation.getX, new Vector3f(1, 0, 0))
        HyperScape.mainCamera.view.rotate(-player.rotation.getY + (Math.PI.toFloat / 2), new Vector3f(0, 1, 0))
        HyperScape.mainCamera.view.rotate(-player.rotation.getZ, new Vector3f(0, 0, 1))
        player.position.scale(-1.0f)
        HyperScape.mainCamera.view.translate(player.position)
        player.position.scale(-1.0f)
        HyperScape.mainCamera.view.translate(new Vector3f(0, -player.camHeight, 0))
        //        println(HyperScape.mainCamera.view.toString)
        HyperScape.mainCamera.uploadView()
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

    var lines: Boolean = false

    /** The Camera that renders they game */
    val mainCamera = new Camera
}
