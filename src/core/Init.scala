package core

import registry.{TextureRegistry, ShaderRegistry}

object Init {

    /**
     * Loads all of the assets
     */
    def loadAssets(): Unit = {
        loadShaders()
        loadTextures()
        println("Done Loading")
    }

    /**
     * Loads all of the shaders
     */
    def loadShaders(): Unit = {
        println("Loading Shaders...")
        ShaderRegistry.loadShader("res/shader/terrain.vert", "res/shader/terrain.frag", "terrain")
        println("Finished Loading Shaders")
    }

    /**
     * Loads all of the textures
     */
    def loadTextures(): Unit = {
        println("Loading Textures...")
        TextureRegistry.loadTexture("res/blocks.png", "terrain")
        println("Finished Loading Textures")
    }

}
