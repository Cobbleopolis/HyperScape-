package entity

import org.lwjgl.util.vector.Vector3f

class Entity {

    var pos: Vector3f = new Vector3f

    var rot: Vector3f = new Vector3f

    def translate(x: Float, y: Float, z: Float): Unit ={
        pos.translate(x, y, z)
    }

    def translateInDirectionFacing(x: Float, y: Float, z: Float): Unit ={
        pos.translate(x * Math.cos(rot.getY).toFloat, y * Math.cos(rot.getX).toFloat, x * Math.sin(rot.getY).toFloat)
        pos.translate(z * -Math.sin(rot.getY).toFloat, 0, z * Math.cos(rot.getY).toFloat)
    }

    def rotate(roll: Float, pitch: Float, yaw: Float): Unit = {
        rot.translate(roll, pitch, yaw)
    }

}
