package block

import physics.AxisAlignedBB
import reference.{RenderTypes, BlockID}
import registry.ModelRegistry

class BlockSlab extends Block {
    blockID = BlockID.SLAB
    renderType = RenderTypes.NON_FULL_BLOCK
    gameModel = ModelRegistry.getModel("slab")
    boundingBox = new AxisAlignedBB(0, 1, 0, .5f, 0, 1)
    texCoord = (5, 0)
}
