package render

import core.HyperScape
import org.lwjgl.opengl.{GL20, Display}
import org.lwjgl.util.vector.{Vector3f, Matrix4f}
import registry.ShaderRegistry
import util.MathUtil

class Camera {
    var perspective: Matrix4f = perspective(70, Display.getWidth.toFloat / Display.getHeight.toFloat, .1f, 30000)
    var view = new Matrix4f()
    var pos = new Vector3f()

    private def frustum(left: Float, right: Float, bottom: Float, top: Float, near: Float, far: Float): Matrix4f = {
        val width = right - left
        val height = top - bottom
        val length = far - near
        val dest = new Matrix4f()
        dest.m00 = (near * 2) / width
        dest.m02 = (left + right) / width
        dest.m11 = (near * 2) / height
        dest.m12 = (top + bottom) / height
        dest.m22 = -(far + near) / length
        dest.m32 = -(far * near * 2) / length
        dest.m23 = -1
        new Matrix4f(dest)
    }

    private def perspective(fovInDegrees: Float, aspectRatio: Float, near: Float, far: Float): Matrix4f = {
        val top = (near * Math.tan(fovInDegrees * MathUtil.PI360)).toFloat
        val right = top * aspectRatio
        frustum(-right, right, -top, top, near, far)
    }

    /**
     * Uploads the perspective matrix to the GPU
     */
    def uploadPerspective(): Unit = {
        val loc = ShaderRegistry.getCurrentShader.getUniformLocation("projectionMatrix")
        perspective.store(HyperScape.uploadBuffer)
        HyperScape.uploadBuffer.flip()
        GL20.glUniformMatrix4(loc, false, HyperScape.uploadBuffer)
        HyperScape.uploadBuffer.clear()
    }

    /**
     * Uploads the view matrix to the GPU
     */
    def uploadView(): Unit = {
        val loc = ShaderRegistry.getCurrentShader.getUniformLocation("viewMatrix")
        view.store(HyperScape.uploadBuffer)
        HyperScape.uploadBuffer.flip()
        GL20.glUniformMatrix4(loc, false, HyperScape.uploadBuffer)
        HyperScape.uploadBuffer.clear()
    }

}
