package block

import registry.ModelRegistry
import render.{Model, RenderModel}

class Block {
    var texCoord: (Int, Int) = (0, 0) /** Texture Coordinates for the UV's*/
    var gameModel: Model = ModelRegistry.cube.copy /** Model to render with*/
    var lightLevel = 0 /** Light Level to render with */
}
