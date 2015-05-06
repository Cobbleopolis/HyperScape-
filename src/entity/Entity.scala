package entity

import org.lwjgl.util.vector.Vector3f
import physics.AxisAlignedBoundingBox
import world.World

class Entity(world: World) {

    /** A vector that is used to represent the entities location (x, y, z) */
    var position: Vector3f = new Vector3f

    /** Ax vector that is used to represent the entities rotation (roll, pitch, yaw) */
    var rotation: Vector3f = new Vector3f

    /** A vector representing the entities velocity */
    var velocity: Vector3f = new Vector3f

    /** Sets if the entity has collision */
    var hasCollision: Boolean = true

    /** Sets if the entity is effected by gravity */
    var isFlying: Boolean = false

    /** The entities bounding box */
    var boundingBox: AxisAlignedBoundingBox = new AxisAlignedBoundingBox

    var isCollidingDown: Boolean = false

    /**
     * Ticks the entity
     */
    def tick(): Unit = {
        //        println(position.getX + " " + position.getY + " " + position.getZ)
        if (!isFlying) velocity.setY(velocity.getY + world.grav)
        velocity = applyCollision(velocity)
        position.translate(velocity.getX, velocity.getY, velocity.getZ)
    }

    /**
     * Checks if the entity is colliding with anything and changes the entities velocity and position accordingly.
     * @param direction The direction that the entity is moving.
     * @return The new vector after the collision has been applied
     */
    def applyCollision(direction: Vector3f): Vector3f = {
        val newVector = direction
        val posX = Math.floor(position.getX + direction.getX).toInt
        val posY = Math.floor(position.getY + direction.getY).toInt
        val posZ = Math.floor(position.getZ + direction.getZ).toInt
        val translatedBB = boundingBox.getTranslatedBoundingBox(position.getX + direction.getX, position.getY + direction.getY, position.getZ + direction.getZ)
        //        println(translatedBB.toString)
        isCollidingDown = false

        //Bottom Collision (-y)
        for (x <- Math.floor(translatedBB.getXMin).toInt to Math.ceil(translatedBB.getXMax).toInt) {
            for (z <- Math.floor(translatedBB.getZMin).toInt to Math.ceil(translatedBB.getZMax).toInt) {
                if (world.getBlock(x, posY, z).hasCollision) {
                    val bb = world.getBlock(x, posY, z).boundingBox.getTranslatedBoundingBox(x, posY, z - 1)
                    if (translatedBB.isCollidingWith(bb) && direction.getY < 0) {
                        newVector.setY(0)
                        isCollidingDown = true
                    }
                }
            }
        }

        //North Collision (+x)
        for (y <- posY + 1 to Math.ceil(translatedBB.getYMax).toInt) {
            for (z <- posZ to Math.ceil(translatedBB.getZMax).toInt) {
                if (world.getBlock(posX, y, posZ + 1).hasCollision)
                    if (translatedBB.isCollidingWith(world.getBlock(posX, y, posZ + 1).boundingBox.getTranslatedBoundingBox(posX, y, posZ)) && direction.getX > 0) {
                        newVector.setX(0)
                    }
            }
        }
        //        println(isCollidingDown)
        newVector
    }


    /**
     * Translates the entity
     * @param x Amount to translate in the x
     * @param y Amount to translate in the y
     * @param z Amount to translate in the z
     */
    def translate(x: Float, y: Float, z: Float): Unit = {
        position.translate(x, y, z)
    }

    /**
     * Adds to the speed of the entity
     * @param x Amount to translate in the x
     * @param y Amount to translate in the y
     * @param z Amount to translate in the z
     */
    def addToSpeed(x: Float, y: Float, z: Float): Unit = {
        //        position.translate(x, -y, z)
        velocity.set(velocity.getX + x, velocity.getY + y, velocity.getZ + z)
    }

    /**
     * Adds to the entity's velocity based on what direction it is facing
     * @param x Amount to translate in the x
     * @param y Amount to translate in the y
     * @param z Amount to translate in the z
     */
    def addToSpeedInDirectionFacing(x: Float, y: Float, z: Float): Unit = {
        //        println("Adding Speed " + x + " " + y + " " + z)
        velocity.translate(x * Math.sin(rotation.getY).toFloat, y, x * Math.cos(rotation.getY).toFloat)
        velocity.translate(z * -Math.cos(rotation.getY).toFloat, 0, z * Math.sin(rotation.getY).toFloat)
    }

    /**
     * Translates the entity based on what direction it is facing
     * @param x Amount to translate in the x
     * @param y Amount to translate in the y
     * @param z Amount to translate in the z
     */
    def translateInDirectionFacing(x: Float, y: Float, z: Float): Unit = {
        //        println("Adding Speed " + x + " " + y + " " + z)
        var direction = new Vector3f()
        direction.translate(x * Math.sin(rotation.getY).toFloat, y, x * Math.cos(rotation.getY).toFloat)
        direction.translate(z * -Math.cos(rotation.getY).toFloat, 0, z * Math.sin(rotation.getY).toFloat)
        direction = applyCollision(direction)
        position.translate(direction.getX, direction.getY, direction.getZ)
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
