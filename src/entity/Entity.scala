package entity

import org.lwjgl.util.vector.Vector3f
import physics.BoundingBox
import world.World

class Entity {

    var position: Vector3f = new Vector3f
    /** A vector that is used to represent the entities location (x, y, z) */

    var rotation: Vector3f = new Vector3f
    /** A vector that is used to represent the entities rotation (roll, pitch, yaw) */

    var velocity: Vector3f = new Vector3f
    /** A vector representing the entities velocity */

    var hasCollision: Boolean = true
    /** Sets if the entity has collision */

    var boundingBox: BoundingBox = new BoundingBox
    /** The entities bounding box */

    /**
     * Ticks the entity
     */
    def tick(world: World): Unit = {
        checkCollision(world)
        position.translate(velocity.getX, velocity.getY, velocity.getZ)
    }

    def checkCollision(world: World): Unit = {

    }


    /**
     * Translates the entity
     * @param x Amount to translate in the x
     * @param y Amount to translate in the y
     * @param z Amount to translate in the z
     */
    def translate(x: Float, y: Float, z: Float): Unit = {
        position.translate(x, -y, z)
    }

    /**
     * Sets the speed of the entity
     * @param x Amount to translate in the x
     * @param y Amount to translate in the y
     * @param z Amount to translate in the z
     */
    def setSpeed(x: Float, y: Float, z: Float): Unit = {
//        position.translate(x, -y, z)
        velocity.set(velocity.getX + x, velocity.getY - y, velocity.getZ + z)
    }

    /**
     * Translates the entity based on what direction it is facing
     * @param x Amount to translate in the x
     * @param y Amount to translate in the y
     * @param z Amount to translate in the z
     */
    def addToSpeedInDirectionFacing(x: Float, y: Float, z: Float): Unit = {
        velocity.set(velocity.getX + (x * Math.cos(rotation.getY).toFloat), velocity.getY + y, velocity.getZ + (x * Math.sin(rotation.getY).toFloat))
        velocity.set(velocity.getX + (z * -Math.sin(rotation.getY).toFloat), velocity.getY, velocity.getZ + (z * Math.cos(rotation.getY).toFloat))
//        position.translate(x * Math.cos(rotation.getY).toFloat, y, x * Math.sin(rotation.getY).toFloat)
//        position.translate(z * -Math.sin(rotation.getY).toFloat, 0, z * Math.cos(rotation.getY).toFloat)
    }

    /**
     * Rotates the entity
     * @param roll Amount to rotate the entity on the x-axis in radians
     * @param pitch Amount to rotate the entity on the y-axis in radians
     * @param yaw Amount to rotate the entity on the z-axis in radians
     */
    def rotate(roll: Float, pitch: Float, yaw: Float): Unit = {
        rotation.translate(roll, pitch, yaw)
    }

}
