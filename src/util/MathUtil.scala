package util

import org.lwjgl.util.vector.Vector3f

object MathUtil {
    val PI = Math.PI.toFloat
    val PI180 = PI / 180f
    val PI360 = PI / 360f
    val TAU = PI * 2

    def addVectors(vec1: Vector3f, vec2: Vector3f): Vector3f = {
        new Vector3f(vec1.getX + vec2.getX, vec1.getY + vec2.getY, vec1.getZ + vec2.getZ)
    }

    def subtractVectors(vec1: Vector3f, vec2: Vector3f): Vector3f = {
        new Vector3f(vec1.getX - vec2.getX, vec1.getY - vec2.getY, vec1.getZ - vec2.getZ)
    }

    /**
     * Returns the greatest integer less than or equal to the double argument
     */
    def floor_float(p_76128_0_ : Float): Int = {
        val i: Int = p_76128_0_.toInt
        if (p_76128_0_ < i.toFloat) i - 1 else i
    }

    /**
     * Copies the values from vecB to vecA
     * @param vecA The vector who's values will be changed the values in vecB
     * @param vecB The vector who's values will be assigned to vecA
     */
    def copyVec(vecA: Vector3f, vecB: Vector3f): Unit = {
        vecA.set(vecB)
    }
}
