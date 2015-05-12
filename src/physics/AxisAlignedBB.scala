package physics

import core.Debug
import org.lwjgl.util.vector.Vector3f

class AxisAlignedBB(xMin: Float = 0f, xMax: Float = 1f, yMin: Float = 0f, yMax: Float = 1f, zMin: Float = 0f, zMax: Float = 1f) {
    var minX = xMin
    var maxX = xMax
    var minY = yMin
    var maxY = yMax
    var minZ = zMin
    var maxZ = zMax
    /** Returns the xMin value of the bounding box. */
    def getXMin: Float = minX

    /** Returns the xMax value of the bounding box. */
    def getXMax: Float = maxX

    /** Returns the yMin value of the bounding box. */
    def getYMin: Float = minY

    /** Returns the yMax value of the bounding box. */
    def getYMax: Float = maxY

    /** Returns the zMin value of the bounding box. */
    def getZMin: Float = minZ

    /** Returns the zMax value of the bounding box. */
    def getZMax: Float = maxZ

    /**
     * Returns a copy of the bounding box translated by the coordinates
     * @param x The x value of the translation
     * @param y The y value of the translation
     * @param z The z value of the translation
     * @return The translated bounding box
     */
    def getTranslatedBoundingBox(x: Float, y: Float, z: Float): AxisAlignedBB = {
        new AxisAlignedBB(minX + x, maxX + x, minY + y, maxY + y, minZ + z, maxZ + z)
    }

    /**
     * Returns a copy of the bounding box translated by the coordinates
     * @param xyz A vector with the x, y, z values of the translation
     * @return The translated bounding box
     */
    def getTranslatedBoundingBox(xyz: Vector3f): AxisAlignedBB = {
        getTranslatedBoundingBox(xyz.getX, xyz.getY, xyz.getZ)
    }

    def getCenter: (Float, Float, Float) = {
        ((minX + maxX) / 2, (minY + maxY) / 2, (minZ + maxZ) / 2)
    }

    /**
     * Checks if a the bounding box is colliding with the passed bounding box. Both bounding boxes should be translated for accurate detection.
     * @param otherBoundingBox The other bounding box to check collision with
     * @return If the bounding box is colliding with the passed bounding box
     */
    def isCollidingWith(otherBoundingBox: AxisAlignedBB): Boolean = {
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
    def isTouching(otherBoundingBox: AxisAlignedBB): Boolean = {
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
        minX += x
        maxX += x
        minY += y
        maxY += y
        minZ += z
        maxZ += z
    }

    def getOverlap(bb: AxisAlignedBB): (Float, Float, Float) = {
        if (isCollidingWith(bb)) {
//            val (cX, cY, cZ) = getCenter
//            val (oX, oY, oZ) = bb.getCenter
            val outX = Math.max(Math.abs(getXMax - bb.getXMin), Math.abs(bb.getXMax - getXMin))
            val outY = Math.max(Math.abs(getYMax - bb.getZMin), Math.abs(bb.getYMax - getXMin))
            val outZ = Math.max(Math.abs(getZMax - bb.getYMin), Math.abs(bb.getZMax - getXMin))
            Debug.printlnVec(outX, outY, outZ)
            (outX, outY, outZ)
        } else {
            (0, 0, 0)
        }
    }


    def getOffsets(boundingBox: AxisAlignedBB, offset: Float = 0.0f): (Float, Float, Float) = {
        (getXOffset(boundingBox, offset), getYOffset(boundingBox, offset), getZOffset(boundingBox, offset))
    }

    /**
     * if instance and the argument bounding boxes overlap in the Y and Z dimensions, calculate the offset between them
     * in the X dimension.
     * @return var2 if the bounding boxes do not overlap or if var2 is closer to 0 then the calculated offset. Otherwise return the calculated offset.
     */
    def getXOffset(boundingBox: AxisAlignedBB, offset: Float = 0.0f): Float = {
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
            } else {
                out
            }
        } else {
            out
        }
    }

    /**
     * if instance and the argument bounding boxes overlap in the X and Z dimensions, calculate the offset between them
     * in the Y dimension.  return var2 if the bounding boxes do not overlap or if var2 is closer to 0 then the
     * calculated offset.  Otherwise return the calculated offset.
     */
    def getYOffset(boundingBox: AxisAlignedBB, offset: Float = 0.0f): Float = {
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
            } else {
                out
            }
        } else {
            out
        }
    }

    /**
     * if instance and the argument bounding boxes overlap in the Y and X dimensions, calculate the offset between them
     * in the Z dimension.  return var2 if the bounding boxes do not overlap or if var2 is closer to 0 then the
     * calculated offset.  Otherwise return the calculated offset.
     */
    def getZOffset(boundingBox: AxisAlignedBB, offset: Float = 0.0f): Float = {
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
            } else {
                out
            }
        } else {
            out
        }
    }

    def copy: AxisAlignedBB = {
        new AxisAlignedBB(minX, maxX, minY, maxY, minZ, maxZ)
    }

    override def toString: String = {
        "[" + minX + ", " + maxX + ", " + minY + ", " + maxY + ", " + minZ + ", " + maxZ + "]"
    }
}