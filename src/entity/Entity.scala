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

    var isJumping: Boolean = false

    var jumpTime: Int = 5

    var jumpSpeed = .5f

    /**
     * Ticks the entity
     */
    def tick(world: World): Unit = {
//        println(velocity.getX + " " + velocity.getY + " " + velocity.getZ)
        velocity.setY(velocity.getY + world.grav)
        if(isJumping){
            velocity.setY(velocity.getY + jumpSpeed)
            jumpTime -= 1
            if(jumpTime <= 0){
                jumpTime = 5
                isJumping = false
            }
        }
        checkCollision(world)
//        position.translate(velocity.getX, velocity.getY, velocity.getZ)
    }

    /**
     * Checks if the entity is colliding with anything and changes the entities velocity accordingly.
     * @param world The world the entity is in.
     */
    def checkCollision(world: World): Unit = {
        val y = Math.floor(position.getY).toInt
        val translatedBB = boundingBox.getTranslatedBoundingBox(position.getX, position.getY, position.getZ)
        var bottomBounds: Array[BoundingBox] = Array[BoundingBox]()
        for (x <- Math.floor(position.getX - boundingBox.getXMin).toInt to Math.floor(position.getX + boundingBox.getXMax).toInt) {
            for (z <- Math.floor(position.getZ - boundingBox.getZMin).toInt to Math.floor(position.getZ + boundingBox.getZMax).toInt) {
                if (world.getBlock(x, y, z).hasCollision)
                    bottomBounds = bottomBounds :+ world.getBlock(x, y, z).boundingBox.getTranslatedBoundingBox(x, y, z)
            }
        }
        //        print(velocity.getY + " ")
        for (bb <- bottomBounds) {
            //            print(translatedBB.isCollidingWith(bb) + " ")
            if (translatedBB.isCollidingWith(bb) && velocity.getY < 0) {
//                print("Setting Y = 0... | " + position.getX + ", " + position.getY + ", " + position.getZ + " | " + bb.getYMax + ", " + translatedBB.getYMin + ", " + Math.abs(bb.getYMax - translatedBB.getYMin))
                velocity.setY(Math.abs(bb.getYMax - translatedBB.getYMin))
                velocity.setY(0)
//                position.translate(0, , 0)
//                println(" | " + position.getX + ", " + position.getY + ", " + position.getZ)
            }
        }
        //        println()
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
     * Sets the speed of the entity
     * @param x Amount to translate in the x
     * @param y Amount to translate in the y
     * @param z Amount to translate in the z
     */
    def setSpeed(x: Float, y: Float, z: Float): Unit = {
        //        position.translate(x, -y, z)
        velocity.set(velocity.getX + x, velocity.getY + y, velocity.getZ + z)
    }

    /**
     * Translates the entity based on what direction it is facing
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
     * Rotates the entity
     * @param roll Amount to rotate the entity on the x-axis in radians
     * @param pitch Amount to rotate the entity on the y-axis in radians
     * @param yaw Amount to rotate the entity on the z-axis in radians
     */
    def rotate(roll: Float, pitch: Float, yaw: Float): Unit = {
        rotation.translate(roll, pitch, yaw)
    }

}
