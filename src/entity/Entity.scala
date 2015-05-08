package entity

import org.lwjgl.util.vector.Vector3f
import physics.AxisAlignedBoundingBox
import util.PhysicsUtil
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
        position.translate(velocity.getX, velocity.getY, velocity.getZ)
        applyCollision()
        println("Final: " + position.toString)
        println("---------------------------------------------------------------------------------")
    }

    /**
     * Checks if the entity is colliding with anything and changes the entities velocity and position accordingly.
     */
    def applyCollision(): Unit = {
        var translatedBB = boundingBox.getTranslatedBoundingBox(position)
        val startVec = new Vector3f(Math.floor(translatedBB.getXMin).toFloat, Math.floor(translatedBB.getYMin).toFloat, Math.floor(translatedBB.getZMin).toFloat)
        val endVec = new Vector3f(Math.floor(translatedBB.getXMax).toFloat + 1f, Math.floor(translatedBB.getYMax + 1f).toFloat, Math.floor(translatedBB.getZMax + 1).toFloat)

        isCollidingDown = false

        for (x <- startVec.getX.toInt to endVec.getX.toInt) {
            for (y <- startVec.getY.toInt to endVec.getY.toInt) {
                for (z <- startVec.getZ.toInt to endVec.getZ.toInt) {
                    if (world.getBlock(x, y, z).hasCollision) {
                        val bb = world.getBlock(x, y, z).boundingBox.getTranslatedBoundingBox(x, y, z)
                        val vec = PhysicsUtil.areBoundingBoxesColliding(translatedBB, bb)
                        if (vec != null) {
                            val dist = bb.getYMax - translatedBB.getYMin
                            print(position.toString + " | " + velocity.toString + " | " + translatedBB.toString + " | " + bb.toString + " | " + dist + " | (" + x + ", " + y + ", " + z + ") Collision | ")
                            if (vec.getY < 0) {
                                position.setY(position.getY + dist)
                                velocity.setY(0)
                                translatedBB = boundingBox.getTranslatedBoundingBox(position)
                                isCollidingDown = true
                            }

                        } else {
//                            println(position.toString + " | " + translatedBB.toString + " | " + bb.toString + " | (" + x + ", " + y + ", " + z + ") No Collision")
                        }
                        println(position.toString)
                    }
                }
            }
        }
    }

    def oldCollision(direction: Vector3f): Vector3f = {
        val newVector = new Vector3f(direction.getX, direction.getY, direction.getZ)
        val currPosX = Math.floor(position.getX).toInt
        val currPosY = Math.floor(position.getY).toInt
        val currPosZ = Math.floor(position.getZ).toInt

        val nextPosX = Math.floor(position.getX + direction.getX).toInt
        val nextPosY = Math.floor(position.getY + direction.getY).toInt
        val nextPosZ = Math.floor(position.getZ + direction.getZ).toInt
        val currentTranslatedBB = boundingBox.getTranslatedBoundingBox(position.getX, position.getY, position.getZ)
        val nextTranslatedBB = boundingBox.getTranslatedBoundingBox(position.getX + direction.getX, position.getY + direction.getY, position.getZ + direction.getZ)
        var y = -1f
        //        println(translatedBB.toString)
        isCollidingDown = false

        //        for (x <- Math.floor(nextTranslatedBB.getXMin).toInt to Math.ceil(nextTranslatedBB.getXMax).toInt) {
        //            for (z <- Math.floor(nextTranslatedBB.getZMin).toInt to Math.ceil(nextTranslatedBB.getZMax).toInt) {
        //                if (world.getBlock(x, currPosY, z).hasCollision) {
        //                    val bb = world.getBlock(x, currPosY - 1, z).boundingBox.getTranslatedBoundingBox(x, currPosY, z - 1)
        //                    if (nextTranslatedBB.isCollidingWith(bb)) {
        //                        newVector.setY(-Math.abs(bb.getYMax - currentTranslatedBB.getYMin))
        //                        isCollidingDown = true
        //                        y = bb.getYMax
        //                    }
        //                }
        //            }
        //        }

        //Bottom Collision (-y) I DO NOT KNOW WHY THIS WORKS BUT DO NOT TOUCH!
        for (x <- Math.floor(nextTranslatedBB.getXMin).toInt to Math.ceil(nextTranslatedBB.getXMax).toInt) {
            for (z <- Math.floor(nextTranslatedBB.getZMin).toInt to Math.ceil(nextTranslatedBB.getZMax).toInt) {

                val currBB = world.getBlock(x, currPosY - 1, z).boundingBox.getTranslatedBoundingBox(x, currPosY - 1, z - 1)
                val nextBB = world.getBlock(x, nextPosY, z).boundingBox.getTranslatedBoundingBox(x, nextPosY, z - 1)
                if (world.getBlock(x, nextPosY, z).hasCollision) {

                    //                    println(bb == null)

                    if (nextTranslatedBB.isTouching(nextBB) && direction.getY < 0) {
                        newVector.setY(nextBB.getYMax - currentTranslatedBB.getYMin)
                        //                        newVector.setY(0)
                    }

                }
                if (world.getBlock(x, currPosY - 1, z).hasCollision) {
                    y = currBB.getYMax
                    if (nextTranslatedBB.isTouching(currBB))
                        isCollidingDown = true
                }
            }
        }

        //North Collision (+x)
        for (y <- nextPosY + 1 to Math.ceil(nextTranslatedBB.getYMax).toInt) {
            for (z <- nextPosZ to Math.ceil(nextTranslatedBB.getZMax).toInt) {
                if (world.getBlock(nextPosX, y, nextPosZ + 1).hasCollision) {
                    val bb = world.getBlock(nextPosX, y, nextPosZ + 1).boundingBox.getTranslatedBoundingBox(nextPosX, y, nextPosZ)
                    if (nextTranslatedBB.isTouching(bb) && direction.getX > 0) {
                        newVector.setX(currentTranslatedBB.getXMax - bb.getXMin)
                    }
                }
            }
        }
        //        println(position.toString + " | " + direction.toString + " | " + newVector.toString + " | " + nextPosX + " | " + nextPosY + " | " + nextPosZ + " | " + currPosX + " | " + currPosY + " | " + currPosZ + " | " + y + " | " + isCollidingDown + " | " + place)
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
        val direction = new Vector3f()
        direction.translate(x * Math.sin(rotation.getY).toFloat, y, x * Math.cos(rotation.getY).toFloat)
        direction.translate(z * -Math.cos(rotation.getY).toFloat, 0, z * Math.sin(rotation.getY).toFloat)
        //        direction = applyCollision(direction, "Translate")
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
