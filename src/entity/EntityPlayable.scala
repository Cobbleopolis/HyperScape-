package entity

import core.HyperScape
import org.lwjgl.input.Keyboard
import org.lwjgl.util.vector.Vector3f
import physics.BoundingBox

class EntityPlayable extends Entity {
    var camHeight: Float = 1.85f
    boundingBox = new BoundingBox(-0.25f, 0.25f,
        0.00f, 1.95f,
        -0.25f, 0.25f)

    val shaders = Array("terrain", "debug", "Panic! at the Disco", "plaid")

    def parseInput(): Unit = {
        val speed: Float = .25f
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
                if (Keyboard.getEventKey == Keyboard.KEY_R) {
                    position = new Vector3f(0, 16, 0)
                }
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
            translateInDirectionFacing(0, 0, -speed)
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
            translateInDirectionFacing(-speed, 0, 0)
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
            translateInDirectionFacing(0, 0, speed)
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
            translateInDirectionFacing(speed, 0, 0)
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
            if (isCollidingDown) {
                addToSpeedInDirectionFacing(0, .35f, 0)
            } else if (isFlying) {
                translateInDirectionFacing(0, .35f, 0)
            }
        }
        if (Keyboard.isKeyDown(Keyboard.KEY_LSHIFT) && isFlying) {
            translateInDirectionFacing(0, .25f, 0)
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
