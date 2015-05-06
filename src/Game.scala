import java.io._
import javax.imageio.ImageIO

import core.{Init, HyperScape}
import org.lwjgl.LWJGLException
import org.lwjgl.opengl._
import registry.{ShaderRegistry, TextureRegistry}

object Game {

    val WINDOW_TITLE = "Hyperscape"
    val WIDTH = 1080
    val HEIGHT = 720

    var hyperScape: HyperScape = null

    var isFullscreen = false

    /**
     * Main method of the game
     * @param args Arguments passed to the game
     */
    def main(args: Array[String]): Unit = {
        setNatives()
        initGL()
        Init.loadAssets()
        ShaderRegistry.bindShader("terrain")
        TextureRegistry.bindTexture("terrain")

        isFullscreen = args.contains("--fullscreen")

        hyperScape = new HyperScape
        hyperScape.init()

        while (!Display.isCloseRequested) {
            hyperScape.tick()
            // Map the internal OpenGL coordinate system to the entire screen
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

    /**
     * Sets the natives based on the operating system
     */
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
//        val nativePath = System.getProperty("user.dir") + File.separator + "lib" + File.separator + "lwjgl" + File.separator + "native" + File.separator + suffix
        val nativePath = System.getProperty("user.dir") + File.separator + "native" + File.separator + suffix
        System.setProperty("org.lwjgl.librarypath", nativePath)
    }

    /**
     * Initializes OpenGL
     */
    def initGL(): Unit = {
        // Setup an OpenGL context with API version 3.2
        try {
            val pixelFormat = new PixelFormat()
            val contextAtrributes = new ContextAttribs(3, 3)
                    .withForwardCompatible(true)
                    .withProfileCore(true)

            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT))
            Display.setTitle(WINDOW_TITLE)
            //            Display.setResizable(true)
            Display.create(pixelFormat, contextAtrributes)
        } catch {
            case e: LWJGLException => e.printStackTrace(); System.exit(-1)
        }
        GL11.glViewport(0, 0, WIDTH, HEIGHT)
        GL11.glEnable(GL11.GL_CULL_FACE)
        GL11.glClearColor(0.4f, 0.6f, 0.9f, 1f)
        GL11.glEnable(GL11.GL_DEPTH_TEST)
//        GL11.glEnable(GL11.GL_BLEND)
//        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA)
    }
}
