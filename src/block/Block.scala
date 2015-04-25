package block

import registry.ModelRegistry
import render.{Model, RenderModel}

class Block {
    var texCoord: (Int, Int) = (0, 0)
    var gameModel: Model = ModelRegistry.cube.copy
    var lightLevel = 0
}
