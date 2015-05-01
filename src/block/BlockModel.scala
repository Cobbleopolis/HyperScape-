package block

import reference.{BlockID, RenderTypes}
import registry.ModelRegistry

class BlockModel extends Block {
    blockID = BlockID.MODEL
    gameModel = ModelRegistry.getModel("model")
    texCoord = (2, 0)
    renderType = RenderTypes.NON_FULL_BLOCK
}
