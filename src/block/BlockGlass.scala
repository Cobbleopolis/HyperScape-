package block

import reference.{RenderTypes, BlockID}

class BlockGlass extends Block {
    blockID = BlockID.GLASS
    texCoord = (3, 0)
    renderType = RenderTypes.GLASS
}
