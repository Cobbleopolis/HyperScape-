package block

import registry.ModelRegistry
import render.{Model, RenderModel}

class Block {
    var gameModel: Model = ModelRegistry.cube
    var lightLevel = 0
}
