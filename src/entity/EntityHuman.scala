package entity

import physics.BoundingBox

class EntityHuman extends Entity{
    var camHeight: Float = 1.85f
    boundingBox = new BoundingBox(.25f, .75f, .25f, .75f, .25f, .75f)
}
