package block

import registry.ModelRegistry

class BlockModel extends Block {
    gameModel = ModelRegistry.model.copy
}
