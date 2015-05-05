package physics

class BoundingBox(xMin: Float = 0f, xMax: Float = 1f, yMin: Float = 0f, yMax: Float = 1f, zMin: Float = 0f, zMax: Float = 1f) {

    /** Returns the xMin value of the bounding box. */
    def getXMin: Float = xMin

    /** Returns the xMax value of the bounding box. */
    def getXMax: Float = xMax

    /** Returns the yMin value of the bounding boy. */
    def getYMin: Float = yMin

    /** Returns the yMax value of the bounding boy. */
    def getYMax: Float = yMax

    /** Returns the zMin value of the bounding boz. */
    def getZMin: Float = zMin

    /** Returns the zMax value of the bounding boz. */
    def getZMax: Float = zMax

    /**
     * Returns a copy of the bounding box translated by the coordinates
     * @param x The x value of the translation
     * @param y The y value of the translation
     * @param z The z value of the translation
     * @return The translated bounding box
     */
    def getTranslatedBoundingBox(x: Float, y: Float, z:Float): BoundingBox = {
        new BoundingBox(xMin + x, xMax + x, yMin + y, yMax + y, zMin + z, zMax + z)
    }

    /**
     * Checks if a the bounding box is colliding with the passed bounding box. Both bounding boxes should be translated for accurate detection.
     * @param otherBoundingBox The other bounding box to check collision with
     * @return If the bounding box is colliding with the passed bounding box
     */
    def isCollidingWith(otherBoundingBox: BoundingBox): Boolean = {
        getXMax > otherBoundingBox.getXMin &&
        otherBoundingBox.getXMax > getXMin &&
        getYMax > otherBoundingBox.getYMin &&
        otherBoundingBox.getYMax > getYMin &&
        getZMax > otherBoundingBox.getZMin &&
        otherBoundingBox.getZMax > getZMin
    }

    override def toString: String = {
        xMin + " " + xMax + " " + yMin + " " + yMax + " " + zMin + " " + zMax
    }
}