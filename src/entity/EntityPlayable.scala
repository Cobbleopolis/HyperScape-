package entity

import core.HyperScape
import org.lwjgl.input.Keyboard
import org.lwjgl.util.vector.Vector3f
import physics.AxisAlignedBB
import world.World

class EntityPlayable(world: World) extends Entity(world) {
    var camHeight: Float = 1.7f
    boundingBox = new AxisAlignedBB(
        -0.5f, 0.5f,
        0.00f, 1.95f,
        -0.5f, 0.5f)

    val shaders = Array("terrain", "debug", "Panic! at the Disco", "plaid")

    def parseInput(): Unit = {
        val speed: Float = 0.0625f
        while (Keyboard.next()) {
            if (Keyboard.getEventKeyState) {
                if (Keyboard.getEventKey == Keyboard.KEY_F3) {
                    HyperScape.shaderSelector += 1
                    if (HyperScape.shaderSelector >= shaders.length) HyperScape.shaderSelector = 0
                    println("Entering " + shaders(HyperScape.shaderSelector) + " mode...")
                }
                if (Keyboard.getEventKey == Keyboard.KEY_F4) {
                    isFlying = !isFlying
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
}
