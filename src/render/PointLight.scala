package render

import org.lwjgl.util.vector.Vector3f

class PointLight(pointPosition: Vector3f, pointColor: Vector3f, pointIntensity: Float) {

    var position: Vector3f = pointPosition

    var color: Vector3f = pointColor

    var intensity: Float = pointIntensity

    def getFloatsForUpload: Array[Float] = {
        Array[Float](position.getX, position.getY, position.getZ, color.getX, color.getY, color.getZ, intensity)
    }
}
