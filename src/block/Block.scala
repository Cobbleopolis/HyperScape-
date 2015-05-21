package block

import physics.AxisAlignedBB
import reference.RenderTypes
import registry.ModelRegistry
import render.Model

class Block {
    /** Texture Coordinates for the UVs */
    var texCoord: (Int, Int) = (0, 0)

    /** Model to render with */
    var gameModel: Model = ModelRegistry.getModel("cube")

    /** Light Level to render with */
    var lightLevel = 0

    /** Sets if the block has collision */
    var hasCollision: Boolean = true

    /** Block's bounding box */
    var boundingBox = new AxisAlignedBB

    /** Render method used by block */
    var renderType: Int = RenderTypes.FULL_BLOCK

    /** Tells the renderer if this block is opaque or not */
    var isOpaque: Boolean = true

    /** ID used by the block */
    var blockID: Int = 0

    /** Faces to add when the top side of the block is open */
    var topFaces: Array[Int] = Array[Int](2, 7)

    /** Faces to add when the north side of the block is open */
    var northFaces: Array[Int] = Array[Int](6, 11)

    /** Faces to add when the east side of the block is open */
    var eastFaces: Array[Int] = Array[Int](5, 10)

    /** Faces to add when the south side of the block is open */
    var southFaces: Array[Int] = Array[Int](4, 9)

    /** Faces to add when the west side of the block is open */
    var westFaces: Array[Int] = Array[Int](3, 8)

    /** Faces to add when the bottom side of the block is open */
    var bottomFaces: Array[Int] = Array[Int](0, 1)
}
