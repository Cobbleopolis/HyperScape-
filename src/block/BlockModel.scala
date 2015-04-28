package block

import registry.ModelRegistry

class BlockModel extends Block {
    gameModel = ModelRegistry.model.copy
    texCoord = (2, 0)
    renderType = 2
}
