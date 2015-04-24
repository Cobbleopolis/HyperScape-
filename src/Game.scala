import java.io.{FileReader, IOException, BufferedReader, File}
import javax.imageio.ImageIO

import core.{Init, HyperScape}
import org.lwjgl.LWJGLException
import org.lwjgl.opengl._
import registry.{ShaderRegistry, TextureRegistry}

object Game {

    val WINDOW_TITLE = "Hyperspace"
    val WIDTH = 1080
    val HEIGHT = 720

    var vsId = 0
    var fsId = 0
    var pId = 0

    var hyperScape: HyperScape = null
    def main(args: Array[String]): Unit = {
        setNatives()
        initGL()
        Init.loadAssets()
        ShaderRegistry.bindShader("terrain")
        TextureRegistry.bindTexture("terrain")

        hyperScape = new HyperScape
        hyperScape.init()
        println("Done Loading")

        while (!Display.isCloseRequested) {
            hyperScape.tick()
            // Map the internal OpenGL coordinate system to the entire screen
            GL11.glViewport(0, 0, WIDTH, HEIGHT)
            hyperScape.render()
            val err = GL11.glGetError()
            if (err != 0)
                println(err)
            Display.sync(60)
            Display.update()
        }

        hyperScape.destroy()
        Display.destroy()

    }

    def setNatives(): Unit = {
        val os = System.getProperty("os.name").toLowerCase
        var suffix = ""
        if (os.contains("win")) {
            suffix = "windows"
        } else if (os.contains("mac")) {
            suffix = "macosx"
        } else {
            suffix = "linux"
        }
        val nativePath = System.getProperty("user.dir") + File.separator + "lib" + File.separator + "lwjgl" + File.separator + "native" + File.separator + suffix
        System.setProperty("org.lwjgl.librarypath", nativePath)
    }

    def initGL(): Unit = {
        // Setup an OpenGL context with API version 3.2
        try {
            val pixelFormat = new PixelFormat()
            val contextAtrributes = new ContextAttribs(3, 3)
                    .withForwardCompatible(true)
                    .withProfileCore(true)

            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT))
            Display.setTitle(WINDOW_TITLE)
            Display.create(pixelFormat, contextAtrributes)
        } catch {
            case e: LWJGLException => e.printStackTrace(); System.exit(-1)
        }
    }
}
