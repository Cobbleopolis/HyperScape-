package physics

import org.lwjgl.util.vector.Vector3f

class AxisAlignedBoundingBox(xMin: Float = 0f, xMax: Float = 1f, yMin: Float = 0f, yMax: Float = 1f, zMin: Float = 0f, zMax: Float = 1f) {

    /** Returns the xMin value of the bounding box. */
    def getXMin: Float = xMin

    /** Returns the xMax value of the bounding box. */
    def getXMax: Float = xMax

    /** Returns the yMin value of the bounding box. */
    def getYMin: Float = yMin

    /** Returns the yMax value of the bounding box. */
    def getYMax: Float = yMax

    /** Returns the zMin value of the bounding box. */
    def getZMin: Float = zMin

    /** Returns the zMax value of the bounding box. */
    def getZMax: Float = zMax

    /**
     * Returns a copy of the bounding box translated by the coordinates
     * @param x The x value of the translation
     * @param y The y value of the translation
     * @param z The z value of the translation
     * @return The translated bounding box
     */
    def getTranslatedBoundingBox(x: Float, y: Float, z: Float): AxisAlignedBoundingBox = {
        new AxisAlignedBoundingBox(xMin + x, xMax + x, yMin + y, yMax + y, zMin + z, zMax + z)
    }

    /**
     * Returns a copy of the bounding box translated by the coordinates
     * @param xyz A vector with the x, y, z values of the translation
     * @return The translated bounding box
     */
    def getTranslatedBoundingBox(xyz: Vector3f): AxisAlignedBoundingBox = {
        new AxisAlignedBoundingBox(xMin + xyz.getX, xMax + xyz.getX, yMin + xyz.getY, yMax + xyz.getY, zMin + xyz.getZ, zMax + xyz.getZ)
    }

    def getCenter: (Float, Float, Float) = {
        ((xMin + xMax) / 2, (yMin + yMax) / 2, (zMin + zMax) / 2)
    }

    /**
     * Checks if a the bounding box is colliding with the passed bounding box. Both bounding boxes should be translated for accurate detection.
     * @param otherBoundingBox The other bounding box to check collision with
     * @return If the bounding box is colliding with the passed bounding box
     */
    def isCollidingWith(otherBoundingBox: AxisAlignedBoundingBox): Boolean = {
        getXMax > otherBoundingBox.getXMin &&
                otherBoundingBox.getXMax > getXMin &&
                getYMax > otherBoundingBox.getYMin &&
                otherBoundingBox.getYMax > getYMin &&
                getZMax > otherBoundingBox.getZMin &&
                otherBoundingBox.getZMax > getZMin
    }

    /**
     * Checks if a the bounding box is touching or colliding with the passed bounding box. Both bounding boxes should be translated for accurate detection.
     * @param otherBoundingBox The other bounding box to check collision with
     * @return If the bounding box is touching or colliding with the passed bounding box
     */
    def isTouching(otherBoundingBox: AxisAlignedBoundingBox): Boolean = {
        getXMax >= otherBoundingBox.getXMin &&
                otherBoundingBox.getXMax >= getXMin &&
                getYMax >= otherBoundingBox.getYMin &&
                otherBoundingBox.getYMax >= getYMin &&
                getZMax >= otherBoundingBox.getZMin &&
                otherBoundingBox.getZMax >= getZMin
    }

    override def toString: String = {
        "[" + xMin + ", " + xMax + ", " + yMin + ", " + yMax + ", " + zMin + ", " + zMax + "]"
    }
}