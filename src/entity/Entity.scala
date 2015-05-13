package entity

import core.Debug
import org.lwjgl.util.vector.{Matrix4f, Vector3f}
import physics.AxisAlignedBB
import world.World

class Entity(worldObj: World) {

    /** A vector that is used to represent the entities location (x, y, z) */
    var position: Vector3f = new Vector3f

    /** Ax vector that is used to represent the entities rotation (roll, pitch, yaw) */
    var rotation: Vector3f = new Vector3f

    /** A vector representing the entities velocity */
    var velocity: Vector3f = new Vector3f

    /** Sets if the entity should check for collision */
    var hasCollision: Boolean = true

    /** Sets if the entity is effected by gravity */
    var isFlying: Boolean = false

    /** The entities bounding box */
    var boundingBox: AxisAlignedBB = new AxisAlignedBB

    /** true after entity is colliding on the ground */
    var onGround: Boolean = false

    var isSneaking: Boolean = false

    /**
     * Ticks the entity
     */
    def tick(): Unit = {
        //        println(position.getX + " " + position.getY + " " + position.getZ)
        if (!isFlying) velocity.setY(velocity.getY + worldObj.grav)
        Debug.printVec(position)
        Debug.debugPrint(" | ")
        Debug.printVec(velocity)
        Debug.debugPrint(" | ")
        Debug.debugPrintln(boundingBox.toString)
        moveEntity(velocity)
        Debug.printVec(position)
        Debug.debugPrint(" | ")
        Debug.printVec(velocity)
        Debug.debugPrint(" | ")
        Debug.debugPrintln(boundingBox.toString)
        //        applyCollision()
        //        position.translate(moveVec.getX, moveVec.getY, moveVec.getZ)
        //        moveVec.set(0, 0, 0)
        //        println("Final: " + position.toString + " | " + velocity.toString)
        //        println("---------------------------------------------------------------------------------")
    }

    //TODO Have Galen attempt to explain how collision is actually working and then cry because I either don't understand it or because I'm really stupid

    def moveEntity(vec: Vector3f): Unit = {
        val offsetVec = new Vector3f(vec)
        val blocks = worldObj.getCollidingBoundingBoxes(boundingBox.copy.addCoord(vec))

        for (bb <- blocks) {
            offsetVec.setY(bb.calcYOffset(boundingBox, offsetVec.getY))
        }
        boundingBox.translate(0, offsetVec.getY, 0)

        for (bb <- blocks) {
            offsetVec.setX(bb.calcXOffset(boundingBox, offsetVec.getX))
        }
        boundingBox.translate(offsetVec.getX, 0, 0)

        for (bb <- blocks) {
            offsetVec.setZ(bb.calcZOffset(boundingBox, offsetVec.getZ))
        }
        boundingBox.translate(0, 0, offsetVec.getZ)

        position.set(boundingBox.getOrigin)

        onGround = vec.getY != offsetVec.getY && vec.getY < 0

        if(vec.getX != offsetVec.getX) {
            println("X")
            velocity.setX(0)
        }

        if(vec.getY != offsetVec.getY) {
            println("Y")
            velocity.setY(0)
        }

        if(vec.getZ != offsetVec.getZ) {
            println("Z")
            velocity.setZ(0)
        }

//        val dir = new Vector3f(0, 0, -1)
//        val rotMat = new Matrix4f()
//        rotMat.rotate(rotation.getY, new Vector3f(0, 1, 0))
//        rotMat.rotate(rotation.getX, new Vector3f(1, 0, 0))
//        Matrix4f.translate(dir, rotMat, rotMat)
    }

    def moveEntity(x: Float, y: Float, z: Float): Unit = {
        moveEntity(new Vector3f(x, y, z))
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
        onGround = false

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
        for (x <- Math.floor(nextTranslatedBB.minX).toInt to Math.ceil(nextTranslatedBB.maxX).toInt) {
            for (z <- Math.floor(nextTranslatedBB.minZ).toInt to Math.ceil(nextTranslatedBB.maxZ).toInt) {

                val currBB = worldObj.getBlock(x, currPosY - 1, z).boundingBox.getTranslatedBoundingBox(x, currPosY - 1, z - 1)
                val nextBB = worldObj.getBlock(x, nextPosY, z).boundingBox.getTranslatedBoundingBox(x, nextPosY, z - 1)
                if (worldObj.getBlock(x, nextPosY, z).hasCollision) {

                    //                    println(bb == null)

                    if (nextTranslatedBB.isTouching(nextBB) && direction.getY < 0) {
                        newVector.setY(nextBB.maxY - currentTranslatedBB.minY)
                        //                        newVector.setY(0)
                    }

                }
                if (worldObj.getBlock(x, currPosY - 1, z).hasCollision) {
                    y = currBB.maxY
                    if (nextTranslatedBB.isTouching(currBB))
                        onGround = true
                }
            }
        }

        //North Collision (+x)
        for (y <- nextPosY + 1 to Math.ceil(nextTranslatedBB.maxY).toInt) {
            for (z <- nextPosZ to Math.ceil(nextTranslatedBB.maxZ).toInt) {
                if (worldObj.getBlock(nextPosX, y, nextPosZ + 1).hasCollision) {
                    val bb = worldObj.getBlock(nextPosX, y, nextPosZ + 1).boundingBox.getTranslatedBoundingBox(nextPosX, y, nextPosZ)
                    if (nextTranslatedBB.isTouching(bb) && direction.getX > 0) {
                        newVector.setX(currentTranslatedBB.maxX - bb.minX)
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
        boundingBox.setOrigin(position)
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
     * Moves the entity based on what direction it is facing
     * @param x Amount to translate in the x
     * @param y Amount to translate in the y
     * @param z Amount to translate in the z
     */
    def moveInDirectionFacing(x: Float, y: Float, z: Float): Unit = {
        //        println("Adding Speed " + x + " " + y + " " + z)
        val direction = new Vector3f()
        direction.translate(x * Math.sin(rotation.getY).toFloat, y, x * Math.cos(rotation.getY).toFloat)
        direction.translate(z * -Math.cos(rotation.getY).toFloat, 0, z * Math.sin(rotation.getY).toFloat)
        //        direction = applyCollision(direction, "Translate")
        moveEntity(direction)
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
