package entity

import org.lwjgl.util.vector.Vector3f
import physics.BoundingBox
import world.World

class Entity {

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
    var boundingBox: BoundingBox = new BoundingBox

    var isCollidingDown: Boolean = false

    /**
     * Ticks the entity
     */
    def tick(world: World): Unit = {
//        println(position.getX + " " + position.getY + " " + position.getZ)
        if (!isFlying) velocity.setY(velocity.getY + world.grav)
        checkCollision(world)
        position.translate(velocity.getX, velocity.getY, velocity.getZ)
    }

    /**
     * Checks if the entity is colliding with anything and changes the entities velocity and position accordingly.
     * @param world The world the entity is in.
     */
    def checkCollision(world: World): Unit = {
        val y = Math.floor(position.getY).toInt
        val translatedBB = boundingBox.getTranslatedBoundingBox(position.getX, position.getY, position.getZ)
        //        println(translatedBB.toString)
        isCollidingDown = false

        for (x <- Math.floor(translatedBB.getXMin).toInt to Math.ceil(translatedBB.getXMax).toInt) {
            for (z <- Math.floor(translatedBB.getZMin).toInt to Math.ceil(translatedBB.getZMax).toInt) {
                if (world.getBlock(x, y, z).hasCollision) {
//                    println(x + " " + y + " " + z)
                    if (translatedBB.isCollidingWith(world.getBlock(x, y, z).boundingBox.getTranslatedBoundingBox(x, y, z - 1)) && velocity.getY < 0) {
                        velocity.setY(0)
                        isCollidingDown = true
                    }
                }
            }
        }
        //        println(isCollidingDown)
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
        velocity.set(velocity.getX + (x * Math.sin(rotation.getY).toFloat), velocity.getY + y, velocity.getZ + (x * Math.cos(rotation.getY).toFloat))
        velocity.set(velocity.getX + (z * -Math.cos(rotation.getY).toFloat), velocity.getY, velocity.getZ + (z * Math.sin(rotation.getY).toFloat))
    }

    /**
     * Translates the entity based on what direction it is facing
     * @param x Amount to translate in the x
     * @param y Amount to translate in the y
     * @param z Amount to translate in the z
     */
    def translateInDirectionFacing(x: Float, y: Float, z: Float): Unit = {
        //        println("Adding Speed " + x + " " + y + " " + z)
        position.set(position.getX + (x * Math.sin(rotation.getY).toFloat), position.getY + y, position.getZ + (x * Math.cos(rotation.getY).toFloat))
        position.set(position.getX + (z * -Math.cos(rotation.getY).toFloat), position.getY, position.getZ + (z * Math.sin(rotation.getY).toFloat))
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
