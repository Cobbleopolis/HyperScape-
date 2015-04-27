package block

import physics.BoundingBox
import registry.ModelRegistry
import render.Model

class Block {
    var texCoord: (Int, Int) = (0, 0)
    /** Texture Coordinates for the UVs */

    var gameModel: Model = ModelRegistry.cube.copy
    /** Model to render with */

    var lightLevel = 0
    /** Light Level to render with */

    var hasCollision: Boolean = true
    /** Sets if the block has collision */

    var boundingBox = new BoundingBox
    /** Block's bounding box */

    var renderType: Int = 1
    /** Render method used by block */

    var topVerts: Array[Int] = Array[Int]()
    var northVerts: Array[Int] = Array[Int]()
    var eastVerts: Array[Int] = Array[Int]()
    var southVerts: Array[Int] = Array[Int]()
    var bottomVerts: Array[Int] = Array[Int]()
}
