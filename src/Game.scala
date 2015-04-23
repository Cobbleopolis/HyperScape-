import java.io.File

import org.lwjgl.LWJGLException
import org.lwjgl.opengl._

object Game {

    val WINDOW_TITLE = "Hyperspace"
    val WIDTH = 1080
    val HEIGHT = 720

    def main(args: Array[String]): Unit = {
        setNatives()
        initGL()
        println("Done Loading")

        while (!Display.isCloseRequested) {

            Display.sync(60)
            Display.update()
        }

//        Game.destroy()
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
            val contextAtrributes = new ContextAttribs(3, 2)
                    .withForwardCompatible(true)
                    .withProfileCore(true)

            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT))
            Display.setTitle(WINDOW_TITLE)
            Display.create(pixelFormat, contextAtrributes)

            GL11.glViewport(0, 0, WIDTH, HEIGHT)
        } catch {
            case e: LWJGLException => e.printStackTrace(); System.exit(-1)
        }

        // Setup an XNA like background color
        GL11.glClearColor(0.4f, 0.6f, 0.9f, 0f)

        // Map the internal OpenGL coordinate system to the entire screen
        GL11.glViewport(0, 0, WIDTH, HEIGHT)
    }
}
