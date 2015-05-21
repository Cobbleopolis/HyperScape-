package entity

import org.lwjgl.util.vector.Vector3f
import physics.AxisAlignedBB
import util.MathUtil
import world.World

/**
 * Creates an entity object
 * @param worldObj The world the entity is in
 */
class Entity(worldObj: World) {

    /** A vector that is used to represent the entities location (x, y, z) */
    var position: Vector3f = new Vector3f

    /** Ax vector that is used to represent the entities rotation (roll, pitch, yaw) */
    var rotation: Vector3f = new Vector3f

    /** Defines the maximum angle (in degrees) at which the entity can look up. */
    var maxLookUp = 90

    /** Defines the minimum angle (in degrees) at which the entity can look down. */
    var maxLookDown = -90

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

    /** true if the entity is sneaking, false otherwise */
    var isSneaking: Boolean = false

    /**
     * Ticks the entity
     */
    def tick(): Unit = {
        //        println(position.getX + " " + position.getY + " " + position.getZ)
        if (!isFlying) velocity.setY(velocity.getY + worldObj.grav)
        val yVal = velocity.getY
        //        Debug.printVec(position)
        moveEntity(velocity)
        onGround = velocity.getY != yVal && yVal < 0
        //        applyCollision()
        //        position.translate(moveVec.getX, moveVec.getY, moveVec.getZ)
        //        moveVec.set(0, 0, 0)
        //        println("Final: " + position.toString + " | " + velocity.toString)
        //        println("---------------------------------------------------------------------------------")
    }

    //TODO Have Galen attempt to explain how collision is actually working and then cry because I either don't understand it or because I'm really stupid

    /**
     * Attempts to move the entity with collision
     * @param vec The vector containing the x, y, z values for the transformations
     */
    def moveEntity(vec: Vector3f): Unit = {
        val offsetVec = new Vector3f(vec)
        val blocks = worldObj.getCollidingBoundingBoxes(boundingBox.copy.addCoord(MathUtil.addVectors(position, offsetVec)))

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

        //        onGround = vec.getY != offsetVec.getY

        if (vec.getX != offsetVec.getX) {
            velocity.setX(0)
        }

        if (vec.getY != offsetVec.getY) {
            velocity.setY(0)
        }

        if (vec.getZ != offsetVec.getZ) {
            velocity.setZ(0)
        }

        //        val dir = new Vector3f(0, 0, -1)
        //        val rotMat = new Matrix4f()
        //        rotMat.rotate(rotation.getY, new Vector3f(0, 1, 0))
        //        rotMat.rotate(rotation.getX, new Vector3f(1, 0, 0))
        //        Matrix4f.translate(dir, rotMat, rotMat)
    }

    /**
     * Attempts to move the entity with collision
     * @param x x value of the transformation
     * @param y y value of the transformation
     * @param z z value of the transformation
     */
    def moveEntity(x: Float, y: Float, z: Float): Unit = {
        moveEntity(new Vector3f(x, y, z))
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
        if (rotation.getY > MathUtil.TAU) rotation.setY(rotation.getY - MathUtil.TAU)
        if (rotation.getY < 0) rotation.setY(MathUtil.TAU + rotation.getY)
        if (rotation.getX > Math.toRadians(maxLookUp).toFloat) rotation.setX(Math.toRadians(maxLookUp).toFloat)
        if (rotation.getX < Math.toRadians(maxLookDown).toFloat) rotation.setX(Math.toRadians(maxLookDown).toFloat)

    }

    /**
     * Rotates the entity
     * @param roll Amount to rotate the entity on the x-axis in degrees
     * @param pitch Amount to rotate the entity on the y-axis in degrees
     * @param yaw Amount to rotate the entity on the z-axis in degrees
     */
    def rotateDeg(roll: Float, pitch: Float, yaw: Float): Unit = {
        rotate(Math.toRadians(roll).toFloat, Math.toRadians(pitch).toFloat, Math.toRadians(yaw).toFloat)
    }

}
