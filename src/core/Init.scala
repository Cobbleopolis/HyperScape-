package core

import registry.{TextureRegistry, ShaderRegistry}

object Init {

    def loadAssets(): Unit = {
        loadShaders()
        loadTextures()
        println("Done Loading")
    }

    def loadShaders(): Unit = {
        println("Loading Shaders...")
        ShaderRegistry.loadShader("res/shader/terrain.vert", "res/shader/terrain.frag", "terrain")
        println("Finished Loading Shaders")
    }

    def loadTextures(): Unit = {
        println("Loading Textures...")
        TextureRegistry.loadTexture("res/blocks.png", "terrain")
        println("Finished Loading Textures")
    }

}
