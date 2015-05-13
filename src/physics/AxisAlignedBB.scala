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

    def getOrigin: Vector3f = {
        new Vector3f(minX + Math.abs(xMin), minY + Math.abs(yMin), minZ + Math.abs(zMin))
    }

    def setOrigin(x: Float, y: Float, z: Float): Unit = {
        minX = x + xMin
        maxX = x + xMax
        minY = y + yMin
        maxY = y + yMax
        minZ = z + zMin
        maxZ = z + zMax
    }

    def setOrigin(vec: Vector3f): Unit = {
        setOrigin(vec.getX, vec.getY, vec.getY)
    }

    /**
     * Checks if a the bounding box is colliding with the passed bounding box. Both bounding boxes should be translated for accurate detection.
     * @param bounds The other bounding box to check collision with
     * @return If the bounding box is colliding with the passed bounding box
     */
    def intersects(bounds: AxisAlignedBB): Boolean = {
        bounds.maxX > this.minX && bounds.minX < this.maxX &&
                bounds.maxY > this.minY && bounds.minY < this.maxY &&
                bounds.maxZ > this.minZ && bounds.minZ < this.maxZ
    }

    /**
     * Checks if a the bounding box is touching or colliding with the passed bounding box. Both bounding boxes should be translated for accurate detection.
     * @param otherBoundingBox The other bounding box to check collision with
     * @return If the bounding box is touching or colliding with the passed bounding box
     */
    def isTouching(otherBoundingBox: AxisAlignedBB): Boolean = {
        maxX >= otherBoundingBox.minX &&
                otherBoundingBox.maxX >= minX &&
                maxY >= otherBoundingBox.minY &&
                otherBoundingBox.maxY >= minY &&
                maxZ >= otherBoundingBox.minZ &&
                otherBoundingBox.maxZ >= minZ
    }

    def isVecInBoundingBox(vec: Vector3f): Boolean = {
        vec.getX > minX && vec.getX < maxX &&
                vec.getY > minY && vec.getY < maxY &&
                vec.getZ > minZ && vec.getZ < maxZ
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
        if (intersects(bb)) {
            //            val (cX, cY, cZ) = getCenter
            //            val (oX, oY, oZ) = bb.getCenter
            val outX = Math.max(Math.abs(maxX - bb.minX), Math.abs(bb.maxX - minX))
            val outY = Math.max(Math.abs(maxY - bb.minZ), Math.abs(bb.maxY - minX))
            val outZ = Math.max(Math.abs(maxZ - bb.minY), Math.abs(bb.maxZ - minX))
            Debug.printlnVec(outX, outY, outZ)
            (outX, outY, outZ)
        } else {
            (0, 0, 0)
        }
    }

    /**
     * Expands the bounding box by the given amount
     * @param vec The amount to expand in each direction
     * @return The expanded bounding box
     */
    def addCoord(vec: Vector3f): AxisAlignedBB = {
        if(vec.getX < 0)
            minX += vec.getX
        else
            maxX += vec.getX

        if(vec.getY < 0)
            minY += vec.getY
        else
            maxY += vec.getY

        if(vec.getZ < 0)
            minZ += vec.getZ
        else
            maxZ += vec.getZ

        this
    }

    //TODO http://z80.ukl.me/mc/sim.js

    /**
     * if instance and the argument bounding boxes overlap in the Y and Z dimensions, calculate the offset between them
     * in the X dimension.
     * @return var2 if the bounding boxes do not overlap or if var2 is closer to 0 then the calculated offset. Otherwise return the calculated offset.
     */
    def calcXOffset(bounds: AxisAlignedBB, currentOffset: Float): Float = {
        var curOff = currentOffset
        if (bounds.maxY > minY && bounds.minY < maxY && bounds.maxZ > minZ && bounds.minZ > maxZ) {
            var newOff = 0f
            if (curOff > 0 && bounds.maxX <= minX) {
                newOff = minX - bounds.maxX
                if (newOff < curOff)
                    curOff = newOff
            }
            if (curOff < 0 && bounds.minX >= bounds.maxX) {
                newOff = maxX - bounds.minX
                if (newOff > curOff) {
                    curOff = newOff
                }
            }
        }
        curOff
    }

    /**
     * if instance and the argument bounding boxes overlap in the X and Z dimensions, calculate the offset between them
     * in the Y dimension.  return var2 if the bounding boxes do not overlap or if var2 is closer to 0 then the
     * calculated offset.  Otherwise return the calculated offset.
     */
    def calcYOffset(bounds: AxisAlignedBB, currentOffset: Float): Float = {
        var curOff = currentOffset
        if(bounds.maxX > minX && bounds.minX < maxX && bounds.maxZ > minZ && bounds.minZ < maxZ) {
            var newOff = 0f
            if(curOff > 0 && bounds.maxY <= minY) {
                newOff = minY - bounds.maxY
                if(newOff < curOff)
                    curOff = newOff
            }
            if(curOff < 0 && bounds.minY >= maxY){
                newOff = maxY - bounds.minY
                if(newOff > curOff)
                    curOff = newOff
            }
        }
        curOff
    }

    /**
     * if instance and the argument bounding boxes overlap in the Y and X dimensions, calculate the offset between them
     * in the Z dimension.  return var2 if the bounding boxes do not overlap or if var2 is closer to 0 then the
     * calculated offset.  Otherwise return the calculated offset.
     */
    def calcZOffset(bounds: AxisAlignedBB, currentOffset: Float): Float = {
        var curOff = currentOffset
        if(bounds.maxY > minY && bounds.minY < maxY && bounds.maxX > minX && bounds.minX < maxX) {
            var newOff = 0f
            if(curOff > 0  && bounds.maxZ <= minZ){
                newOff = minZ - bounds.maxZ
                if(newOff < curOff)
                    curOff = newOff
            }
            if(curOff < 0 && bounds.minZ >= maxZ) {
                newOff = maxZ - bounds.minZ
                if(newOff > curOff)
                    curOff = newOff
            }
        }
        curOff
    }

    def copy: AxisAlignedBB = {
        new AxisAlignedBB(minX, maxX, minY, maxY, minZ, maxZ)
    }

    override def toString: String = {
        "[" + minX + ", " + maxX + ", " + minY + ", " + maxY + ", " + minZ + ", " + maxZ + "]"
    }
}