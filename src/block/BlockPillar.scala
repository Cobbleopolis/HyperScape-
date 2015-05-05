package block

import reference.{BlockID, RenderTypes}
import registry.ModelRegistry

class BlockPillar extends Block {
    blockID = BlockID.PILLAR
    renderType = RenderTypes.NON_FULL_BLOCK
    gameModel = ModelRegistry.getModel("pillar")
    texCoord = (4, 0)
}
