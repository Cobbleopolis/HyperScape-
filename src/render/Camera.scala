package render

import org.lwjgl.opengl.Display
import org.lwjgl.util.vector.Matrix4f
import util.MathUtil

class Camera {
    var perspective = perspective(30, Display.getWidth.toFloat / Display.getHeight.toFloat, .001f, 300)


    def frustrum(left: Float, right: Float, bottom: Float, top: Float, near: Float, far: Float): Matrix4f = {
        val width = right - left
        val height = top - bottom
        val length = far - near
        val dest = new Matrix4f()
        dest.m00 = (near * 2) / width
        dest.m11 = (near * 2) / height
        dest.m02 = (left + right) / width
        dest.m12 = (top + bottom) / height
        dest.m22 = -(far + near) / length
        dest.m32 = -1
        dest.m23 = -(far * near * 2) / length
        new Matrix4f(dest)
    }

    def perspective(fovInDegrees: Float, aspectRatio: Float, near: Float, far: Float): Matrix4f = {
        val top = (near * Math.tan(fovInDegrees * MathUtil.PI360)).toFloat
        val right = top * aspectRatio
        frustrum(-right, right, -top, top, near, far)
    }

}
