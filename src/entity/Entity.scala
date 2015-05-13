package entity

import org.lwjgl.util.vector.Vector3f
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

    var moveSpeed: Float = .25f

    /**
     * Ticks the entity
     */
    def tick(): Unit = {
        //        println(position.getX + " " + position.getY + " " + position.getZ)
        //        if (!isFlying) velocity.setY(velocity.getY + worldObj.grav)
        moveEntity(velocity)
        //        applyCollision()
        //        position.translate(moveVec.getX, moveVec.getY, moveVec.getZ)
        //        moveVec.set(0, 0, 0)
        //        println("Final: " + position.toString + " | " + velocity.toString)
        //        println("---------------------------------------------------------------------------------")
    }

    /**
     * Checks if the entity is colliding with anything and changes the entities velocity and position accordingly.
     */
    def applyCollision(): Unit = {
        //        var nextPos: Vector3f = MathUtil.addVectors(position, MathUtil.addVectors(moveVec, velocity))
        //        var translatedBB: AxisAlignedBoundingBox = boundingBox.getTranslatedBoundingBox(nextPos)

        //        val startVec = new Vector3f(Math.floor(translatedBB.getXMin).toFloat, Math.floor(translatedBB.getYMin).toFloat, Math.floor(translatedBB.getZMin).toFloat)
        //        val endVec = new Vector3f(Math.floor(translatedBB.getXMax).toFloat + 1f, Math.floor(translatedBB.getYMax + 1f).toFloat, Math.floor(translatedBB.getZMax + 1).toFloat)

        onGround = false


    }

    def moveEntity(vec: Vector3f): Unit = {
        moveEntity(vec.getX, vec.getY, vec.getZ)
    }

    def moveEntity(x: Float, y: Float, z: Float): Unit = {
        var (movX, movY, movZ) = (x, y, z)
        position.translate(movX, movY, movZ)
        boundingBox.translate(movX, movY, movZ)
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
        for (x <- Math.floor(nextTranslatedBB.getXMin).toInt to Math.ceil(nextTranslatedBB.getXMax).toInt) {
            for (z <- Math.floor(nextTranslatedBB.getZMin).toInt to Math.ceil(nextTranslatedBB.getZMax).toInt) {

                val currBB = worldObj.getBlock(x, currPosY - 1, z).boundingBox.getTranslatedBoundingBox(x, currPosY - 1, z - 1)
                val nextBB = worldObj.getBlock(x, nextPosY, z).boundingBox.getTranslatedBoundingBox(x, nextPosY, z - 1)
                if (worldObj.getBlock(x, nextPosY, z).hasCollision) {

                    //                    println(bb == null)

                    if (nextTranslatedBB.isTouching(nextBB) && direction.getY < 0) {
                        newVector.setY(nextBB.getYMax - currentTranslatedBB.getYMin)
                        //                        newVector.setY(0)
                    }

                }
                if (worldObj.getBlock(x, currPosY - 1, z).hasCollision) {
                    y = currBB.getYMax
                    if (nextTranslatedBB.isTouching(currBB))
                        onGround = true
                }
            }
        }

        //North Collision (+x)
        for (y <- nextPosY + 1 to Math.ceil(nextTranslatedBB.getYMax).toInt) {
            for (z <- nextPosZ to Math.ceil(nextTranslatedBB.getZMax).toInt) {
                if (worldObj.getBlock(nextPosX, y, nextPosZ + 1).hasCollision) {
                    val bb = worldObj.getBlock(nextPosX, y, nextPosZ + 1).boundingBox.getTranslatedBoundingBox(nextPosX, y, nextPosZ)
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
        moveEntity(x, y, z)
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
