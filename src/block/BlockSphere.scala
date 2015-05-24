package block

import reference.{RenderTypes, BlockID}
import registry.ModelRegistry

class BlockSphere extends Block{
    blockID = BlockID.SPHERE
    gameModel = ModelRegistry.getModel("sphere")
    texCoord = (6, 0)
    renderType = RenderTypes.NON_FULL_BLOCK
}
