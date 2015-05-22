package registry

import java.io.File
import javax.imageio.ImageIO

import org.lwjgl.BufferUtils
import org.lwjgl.opengl._


object TextureRegistry {
    private var textures: Map[String, Int] = Map()
    private var currTexture = ""

    /**
     * Loads a texture and uploads it it the GPU
     * @param pathToTexture The path to the texture that needs to be uploaded
     * @param textureName The name used to identify the texture with the registry
     */
    def loadTexture(pathToTexture: String, textureName: String): Unit = {
        val img = ImageIO.read(new File(pathToTexture))
        val rgb = new Array[Int](img.getWidth * img.getHeight * 4)
        img.getRGB(0, 0, img.getWidth, img.getHeight, rgb, 0, img.getWidth)
        val pixels = BufferUtils.createIntBuffer(rgb.length)
        pixels.put(rgb).flip()
        val texID = GL11.glGenTextures()
        GL13.glActiveTexture(GL13.GL_TEXTURE0)
        GL11.glBindTexture(GL11.GL_TEXTURE_2D, texID)
        GL11.glTexImage2D(GL11.GL_TEXTURE_2D, 0, GL11.GL_RGBA, img.getWidth, img.getHeight, 0, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, pixels)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST)
        GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR)
//        GL30.glGenerateMipmap(GL11.GL_TEXTURE_2D)
        textures += (textureName -> texID)
        println("\tTexture Id | " + textureName + " | " + texID)
    }

    /**
     * Binds the texture to GL_TEXTURE0
     * @param textureName Name of the texture to bind
     */
    def bindTexture(textureName: String): Unit = {
        if (!textureName.equals(currTexture)) {
            //            println("Binding Texture | " + textureName)
            val texture: Int = textures(textureName)
            GL13.glActiveTexture(GL13.GL_TEXTURE0)
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture)
            currTexture = textureName
        }
    }

    /**
     * Destroys a texture
     * @param textureName Name of the texture to destroy
     */
    def destroyTexture(textureName: String): Unit ={
        textures(textureName)
    }
}
