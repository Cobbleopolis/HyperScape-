package entity

import core.HyperScape
import org.lwjgl.input.Keyboard
import org.lwjgl.opengl.GL20
import org.lwjgl.util.vector.{Matrix4f, Vector3f}
import physics.AxisAlignedBB
import registry.{ModelRegistry, ShaderRegistry}
import render.RenderModel
import world.World

class EntityPlayable(world: World) extends Entity(world) {
    var camHeight: Float = 1.3f
    boundingBox = new AxisAlignedBB(
        -0.3f, 0.3f,
        0.00f, 1.3f,
        -0.3f, 0.3f)

    var model = new RenderModel(ModelRegistry.getModel("player").getVertices)

    val shaders = Array("terrain", "debug", "Panic! at the Disco", "plaid")

    /**
     * Parses the input of the player
     */
    def parseInput(): Unit = {
        val speed: Float = 0.125f
        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState) {
                if (Keyboard.getEventKey == Keyboard.KEY_F3) {
                    HyperScape.shaderSelector += 1
                    if (HyperScape.shaderSelector >= shaders.length) HyperScape.shaderSelector = 0
                    println("Entering " + shaders(HyperScape.shaderSelector) + " mode...")
                }
                if (Keyboard.getEventKey == Keyboard.KEY_F4) {
                    isFlying = !isFlying
                    velocity.set(0, 0, 0)
                    println("Toggling Flying mode...")
                }
                if (Keyboard.getEventKey == Keyboard.KEY_F5) {
                    HyperScape.lines = !HyperScape.lines
                    println("Toggling Lines mode...")
                }
                if (Keyboard.getEventKey == Keyboard.KEY_R) {
                    position = new Vector3f(-0.5f, 17f, -0.5f)
                    boundingBox.setOrigin(position)
                    velocity = new Vector3f()
                    rotation = new Vector3f()
                }
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            moveInDirectionFacing(0, 0, -speed)
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            moveInDirectionFacing(-speed, 0, 0)
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            moveInDirectionFacing(0, 0, speed)
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            moveInDirectionFacing(speed, 0, 0)
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
//            println(isCollidingDown)
            if (onGround) {
//                println("Jump")
                addToSpeedInDirectionFacing(0, .35f, 0)
            } else if (isFlying) {
                moveInDirectionFacing(0, .35f, 0)
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && isFlying) {
            moveInDirectionFacing(0, -.25f, 0)
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
            rotate(0, Math.toRadians(2.5).toFloat, 0)
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
            rotate(0, -Math.toRadians(2.5).toFloat, 0)
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
            rotate(Math.toRadians(2.5).toFloat, 0, 0)
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
            rotate(-Math.toRadians(2.5).toFloat, 0, 0)
        }
    }

    def render(): Unit = {
        HyperScape.uploadBuffer.clear()
        val modelMat = new Matrix4f
        val loc = ShaderRegistry.getCurrentShader.getUniformLocation("modelMatrix")
        modelMat.translate(position)
        modelMat.store(HyperScape.uploadBuffer)
        HyperScape.uploadBuffer.flip()
        GL20.glUniformMatrix4(loc, false, HyperScape.uploadBuffer)
        HyperScape.uploadBuffer.clear()
        val colorLoc = ShaderRegistry.getCurrentShader.getUniformLocation("chunkColor")
        GL20.glUniform4f(colorLoc, 1, 1, 1, 1)
        model.render()
    }
}
