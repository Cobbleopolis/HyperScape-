package block

import reference.BlockID
import registry.ModelRegistry

class BlockModel extends Block {
    blockID = BlockID.MODEL
    gameModel = ModelRegistry.getModel("model")
    texCoord = (2, 0)
    renderType = 2
}
