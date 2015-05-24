package render

import org.lwjgl.util.vector.Vector3f

class SunLamp(lampDirection: Vector3f, lampColor: Vector3f, lampAmbient: Float) {

    var direction: Vector3f = lampDirection

    var color: Vector3f = lampColor

    var ambient: Float = lampAmbient

    def getFloatsForUpload: Array[Float] = {
        Array[Float](direction.getX, direction.getY, direction.getZ, color.getX, color.getY, color.getZ, ambient)
    }
}
