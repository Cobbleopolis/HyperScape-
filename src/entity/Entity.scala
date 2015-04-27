package entity

import org.lwjgl.util.vector.Vector3f

class Entity {

    var pos: Vector3f = new Vector3f /** A vector that is used to represent the entities location (x, y, z)*/

    var rot: Vector3f = new Vector3f /** A vector that is used to represent the entities rotation (roll, pitch, yaw)*/

    /**
     * Translates the entity
     * @param x Amount to translate in the x
     * @param y Amount to translate in the y
     * @param z Amount to translate in the z
     */
    def translate(x: Float, y: Float, z: Float): Unit ={
        pos.translate(x, -y, z)
    }

    /**
     * Translates the entity based on what direction it is facing
     * @param x Amount to translate in the x
     * @param y Amount to translate in the y
     * @param z Amount to translate in the z
     */
    def translateInDirectionFacing(x: Float, y: Float, z: Float): Unit ={
        pos.translate(x * Math.cos(rot.getY).toFloat, y, x * Math.sin(rot.getY).toFloat)
        pos.translate(z * -Math.sin(rot.getY).toFloat, 0, z * Math.cos(rot.getY).toFloat)
    }

    /**
     * Rotates the entity
     * @param roll Amount to rotate the entity on the x-axis
     * @param pitch Amount to rotate the entity on the y-axis
     * @param yaw Amount to rotate the entity on the z-axis
     */
    def rotate(roll: Float, pitch: Float, yaw: Float): Unit = {
        rot.translate(Math.toRadians(roll).toFloat, Math.toRadians(pitch).toFloat, Math.toRadians(yaw).toFloat)
    }

}
