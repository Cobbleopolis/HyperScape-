package block

import reference.{RenderTypes, BlockID}

class BlockAir extends Block {
    blockID = BlockID.AIR
    renderType = RenderTypes.DOES_NOT_RENDER
    hasCollision = false
}
