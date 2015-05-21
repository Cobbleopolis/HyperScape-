package physics

import org.lwjgl.util.vector.Vector3f

/**
 * Generates an axis aligned bounding box.
 * @param xMin The minimum x bound of the bounding box
 * @param xMax The maximum x bound of the bounding box
 * @param yMin The minimum y bound of the bounding box
 * @param yMax The maximum y bound of the bounding box
 * @param zMin The minimum z bound of the bounding box
 * @param zMax The maximum z bound of the bounding box
 */
class AxisAlignedBB(xMin: Float = 0f, xMax: Float = 1f, yMin: Float = 0f, yMax: Float = 1f, zMin: Float = 0f, zMax: Float = 1f) {

    /** The translated minimum x bound */
    var minX = xMin

    /** The translated maximum x bound */
    var maxX = xMax

    /** The translated minimum y bound */
    var minY = yMin

    /** The translated maximum y bound */
    var maxY = yMax

    /** The translated minimum z bound */
    var minZ = zMin

    /** The translated maximum z bound */
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

    /**
     * Gets the exact center of the bounding box
     * @return The exact center of the bounding box
     */
    def getCenter: (Float, Float, Float) = {
        ((minX + maxX) / 2, (minY + maxY) / 2, (minZ + maxZ) / 2)
    }

    /**
     * Gets the bounding box's origin
     * @return A vector containing the x, y, z of the bounding box's origin
     */
    def getOrigin: Vector3f = {
        new Vector3f(minX + Math.abs(xMin), minY + Math.abs(yMin), minZ + Math.abs(zMin))
    }

    /**
     * Sets the origin location of the bounding box
     * @param x X location of the origin
     * @param y Y location of the origin
     * @param z Z location of the origin
     */
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
        (bounds.maxX > this.minX || bounds.minX < this.maxX) &&
                (bounds.maxY > this.minY || bounds.minY < this.maxY) &&
                (bounds.maxZ > this.minZ || bounds.minZ < this.maxZ)
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

    /**
     * Detects if a vector is inside the bounding box
     * @param vec The vector to detect if it inside the bounding box
     * @return true if it inside the bounding box else otherwise
     */
    def isVecInBoundingBox(vec: Vector3f): Boolean = {
        vec.getX > minX && vec.getX < maxX &&
                vec.getY > minY && vec.getY < maxY &&
                vec.getZ > minZ && vec.getZ < maxZ
    }

    /**
     * Translates the bounding box
     * @param vec A vector combining the translations for the operation
     */
    def translate(vec: Vector3f): Unit = {
        translate(vec.getX, vec.getY, vec.getZ)
    }

    /**
     * Translates the bounding box
     * @param x How much to translate on the x axis
     * @param y How much to translate on the y axis
     * @param z How much to translate on the z axis
     */
    def translate(x: Float, y: Float, z: Float): Unit = {
        minX += x
        maxX += x
        minY += y
        maxY += y
        minZ += z
        maxZ += z
    }

    /**
     * Expands the bounding box by the given amount
     * @param vec The amount to expand in each direction
     * @return The expanded bounding box
     */
    def addCoord(vec: Vector3f): AxisAlignedBB = {
        if (vec.getX < 0)
            minX += vec.getX
        else
            maxX += vec.getX

        if (vec.getY < 0)
            minY += vec.getY
        else
            maxY += vec.getY

        if (vec.getZ < 0)
            minZ += vec.getZ
        else
            maxZ += vec.getZ

        this
    }

    //TODO http://z80.ukl.me/mc/sim.js

    /**
     * If bounds overlay on Y and Z plane, calculate distance between the 2 bounds, else return curOff.
     * If the distance is less than curOff, return it, else return curOff.
     * @param bounds The other bounding box
     * @param currentOffset The current distance between the 2 bounds
     * @return If the distance is less than curOff, return it, else return curOff.
     */
    def calcXOffset(bounds: AxisAlignedBB, currentOffset: Float): Float = {
        var curOff = currentOffset
        if (bounds.maxY > minY && bounds.minY < maxY && bounds.maxZ > minZ && bounds.minZ < maxZ) {
            var newOff = 0f
            if (curOff > 0 && bounds.maxX <= minX) {
                newOff = minX - bounds.maxX
                if (newOff < curOff) {
                    curOff = newOff
                }
            }
            if (curOff < 0 && bounds.minX >= maxX) {
                newOff = maxX - bounds.minX
                if (newOff > curOff) {
                    curOff = newOff
                }
            }
        }
        curOff
    }

    /**
     * If bounds overlay on X and Z plane, calculate distance between the 2 bounds, else return curOff.
     * If the distance is less than curOff, return it, else return curOff.
     * @param bounds The other bounding box
     * @param currentOffset The current distance between the 2 bounds
     * @return If the distance is less than curOff, return it, else return curOff.
     */
    def calcYOffset(bounds: AxisAlignedBB, currentOffset: Float): Float = {
        var curOff = currentOffset
        if (bounds.maxX > minX && bounds.minX < maxX && bounds.maxZ > minZ && bounds.minZ < maxZ) {
            var newOff = 0f
            if (curOff > 0 && bounds.maxY <= minY) {
                newOff = minY - bounds.maxY
                if (newOff < curOff)
                    curOff = newOff
            }
            if (curOff < 0 && bounds.minY >= maxY) {
                newOff = maxY - bounds.minY
                if (newOff > curOff)
                    curOff = newOff
            }
        }
        curOff
    }

    /**
     * If bounds overlay on X and Y plane, calculate distance between the 2 bounds, else return curOff.
     * If the distance is less than curOff, return it, else return curOff.
     * @param bounds The other bounding box
     * @param currentOffset The current distance between the 2 bounds
     * @return If the distance is less than curOff, return it, else return curOff.
     */
    def calcZOffset(bounds: AxisAlignedBB, currentOffset: Float): Float = {
        var curOff = currentOffset
        if (bounds.maxY > minY && bounds.minY < maxY && bounds.maxX > minX && bounds.minX < maxX) {
            var newOff = 0f
            if (curOff > 0 && bounds.maxZ <= minZ) {
                newOff = minZ - bounds.maxZ
                if (newOff < curOff) {
                    curOff = newOff
                }
            }
            if (curOff < 0 && bounds.minZ >= maxZ) {
                newOff = maxZ - bounds.minZ
                if (newOff > curOff) {
                    curOff = newOff
                }
            }
        }
        curOff
    }

    /**
     * Creates a copy of the current bounding box
     * @return a copy of the bounding box
     */
    def copy: AxisAlignedBB = {
        new AxisAlignedBB(minX, maxX, minY, maxY, minZ, maxZ)
    }

    /**
     * Generates a string based on the current state of the bounding box
     * @return a string based on the current state of the bounding box
     */
    override def toString: String = {
        "[" + minX + ", " + maxX + ", " + minY + ", " + maxY + ", " + minZ + ", " + maxZ + "]"
    }
}