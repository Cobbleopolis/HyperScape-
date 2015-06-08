package block

import reference.{BlockID, RenderTypes}

class BlockAir extends Block {
    blockID = BlockID.AIR
    renderType = RenderTypes.DOES_NOT_RENDER
    hasCollision = false
    isOpaque = false
}
