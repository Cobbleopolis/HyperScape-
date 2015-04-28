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

    var topVerts: Array[Int] = Array[Int](2, 7)
    var northVerts: Array[Int] = Array[Int](3, 8)
//    var eastVerts: Array[Int] = Array[Int](4, 9)
    var eastVerts: Array[Int] = Array[Int](6, 11)
    var southVerts: Array[Int] = Array[Int](5, 10)
//    var westVerts: Array[Int] = Array[Int](6, 11)
    var westVerts: Array[Int] = Array[Int](4, 9)
    var bottomVerts: Array[Int] = Array[Int](0, 1)
}
