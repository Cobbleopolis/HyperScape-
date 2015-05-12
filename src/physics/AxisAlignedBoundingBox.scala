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

    def isVecInBoundingBox(vec: Vector3f): Boolean = {
        vec.getX > getXMin && vec.getX < getXMax &&
                vec.getY > getYMin && vec.getY < getYMax &&
                vec.getZ > getZMin && vec.getZ < getZMax
    }

    def translate(vec: Vector3f): Unit = {
        translate(vec.getX, vec.getY, vec.getZ)
    }

    def translate(x: Float, y: Float, z: Float): Unit = {
        xMin += x
        xMax += x
        yMin += y
        yMax += y
        zMin += z
        zMax += z
    }

    /**
     * if instance and the argument bounding boxes overlap in the Y and Z dimensions, calculate the offset between them
     * in the X dimension.  return var2 if the bounding boxes do not overlap or if var2 is closer to 0 then the
     * calculated offset.  Otherwise return the calculated offset.
     */
    def getXOffset(boundingBox: AxisAlignedBoundingBox, offset: Float): Float = {
        var out = offset
        if (boundingBox.getYMax > getYMin && boundingBox.getYMin < getYMax) {
            if (boundingBox.getZMax > getZMin && boundingBox.getZMin < getZMax) {
                var d1: Float = .0f
                if (offset > 0.0f && boundingBox.getXMax <= getXMin) {
                    d1 = getXMin - boundingBox.getXMax
                    if (d1 < offset) {
                        out = d1
                    }
                }
                if (offset < 0.0f && boundingBox.getXMin >= getXMax) {
                    d1 = getXMax - boundingBox.getXMin
                    if (d1 > offset) {
                        out = d1
                    }
                }
                out
            }
            else {
                out
            }
        }
        else {
            out
        }
    }

    /**
     * if instance and the argument bounding boxes overlap in the X and Z dimensions, calculate the offset between them
     * in the Y dimension.  return var2 if the bounding boxes do not overlap or if var2 is closer to 0 then the
     * calculated offset.  Otherwise return the calculated offset.
     */
    def getYOffset(boundingBox: AxisAlignedBoundingBox, offset: Float): Float = {
        var out = offset
        if (boundingBox.getXMax > getXMin && boundingBox.getXMin < getXMax) {
            if (boundingBox.getZMax > getZMin && boundingBox.getZMin < getZMax) {
                var d1: Float = .0f
                if (offset > 0.0D && boundingBox.getYMax <= getYMin) {
                    d1 = getYMin - boundingBox.getYMax
                    if (d1 < offset) {
                        out = d1
                    }
                }
                if (offset < 0.0D && boundingBox.getYMin >= getYMax) {
                    d1 = getYMax - boundingBox.getYMin
                    if (d1 > offset) {
                        out = d1
                    }
                }
                out
            }
            else {
                out
            }
        }
        else {
            out
        }
    }

    /**
     * if instance and the argument bounding boxes overlap in the Y and X dimensions, calculate the offset between them
     * in the Z dimension.  return var2 if the bounding boxes do not overlap or if var2 is closer to 0 then the
     * calculated offset.  Otherwise return the calculated offset.
     */
    def getZOffset(boundingBox: AxisAlignedBoundingBox, offset: Float): Float = {
        var out = offset
        if (boundingBox.getXMax > getXMin && boundingBox.getXMin < getXMax) {
            if (boundingBox.getYMax > getYMin && boundingBox.getYMin < getYMax) {
                var d1: Float = .0f
                if (offset > 0.0f && boundingBox.getZMax <= getZMin) {
                    d1 = getZMin - boundingBox.getZMax
                    if (d1 < offset) {
                        out = d1
                    }
                }
                if (offset < 0.0f && boundingBox.getZMin >= getZMax) {
                    d1 = getZMax - boundingBox.getZMin
                    if (d1 > offset) {
                        out = d1
                    }
                }
                out
            }
            else {
                out
            }
        }
        else {
            out
        }
    }

    def copy: AxisAlignedBoundingBox = {
        new AxisAlignedBoundingBox(xMin, xMax, yMin, yMax, zMin, zMax)
    }

    override def toString: String = {
        "[" + xMin + ", " + xMax + ", " + yMin + ", " + yMax + ", " + zMin + ", " + zMax + "]"
    }
}