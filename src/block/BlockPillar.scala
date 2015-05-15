package block

import physics.AxisAlignedBB
import reference.{BlockID, RenderTypes}
import registry.ModelRegistry

class BlockPillar extends Block {
    blockID = BlockID.PILLAR
    renderType = RenderTypes.NON_FULL_BLOCK
    gameModel = ModelRegistry.getModel("pillar")
    boundingBox = new AxisAlignedBB(0, .5f, 0, 1, 0, .5f)
    texCoord = (4, 0)
}
