package physics

class BoundingBox(xMin: Float = 0f, xMax: Float = 1f, yMin: Float = 0f, yMax: Float = 1f, zMin: Float = 0f, zMax: Float = 1f) {

    def getMaxDistanceFromCenter: Float = {
        Math.max(xMin, Math.max(xMax, Math.max(yMin, Math.max(yMax, Math.max(zMin, zMax)))))
    }
}