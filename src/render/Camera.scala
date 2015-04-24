package render

import core.HyperScape
import org.lwjgl.opengl.{GL20, Display}
import org.lwjgl.util.vector.Matrix4f
import registry.ShaderRegistry
import util.MathUtil

class Camera {
    var perspective: Matrix4f = perspective(75, Display.getWidth.toFloat / Display.getHeight.toFloat, .0001f, 300f)
    var view = new Matrix4f()

    def frustum(left: Float, right: Float, bottom: Float, top: Float, near: Float, far: Float): Matrix4f = {
        val width = right - left
        val height = top - bottom
        val length = far - near
        val dest = new Matrix4f()
        dest.m00 = (near * 2) / width
        dest.m02 = (left + right) / width
        dest.m11 = (near * 2) / height
        dest.m12 = (top + bottom) / height
        dest.m22 = -(far + near) / length
        dest.m23 = -(far * near * 2) / length
        dest.m32 = -1
        new Matrix4f(dest)
    }

    def perspective(fovInDegrees: Float, aspectRatio: Float, near: Float, far: Float): Matrix4f = {
        val top = (near * Math.tan(fovInDegrees * MathUtil.PI360)).toFloat
        val right = top * aspectRatio
        frustum(-right, right, -top, top, near, far)
    }

    def uploadPerspective(): Unit = {
        val loc = ShaderRegistry.getCurrentShader().getUniformLocation("projectionMatrix")
        perspective.store(HyperScape.uploadBuffer)
        HyperScape.uploadBuffer.flip()
        GL20.glUniformMatrix4(loc, false, HyperScape.uploadBuffer)
        HyperScape.uploadBuffer.clear()
    }

    def uploadView(): Unit = {
        val loc = ShaderRegistry.getCurrentShader().getUniformLocation("viewMatrix")
        view.store(HyperScape.uploadBuffer)
        HyperScape.uploadBuffer.flip()
        GL20.glUniformMatrix4(loc, false, HyperScape.uploadBuffer)
        HyperScape.uploadBuffer.clear()
    }

}
