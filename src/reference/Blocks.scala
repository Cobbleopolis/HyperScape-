package reference

import block.{BlockAir, BlockBlank, BlockLight, BlockModel}
import registry.BlockRegistry

object Blocks {
    val air = new BlockAir
    val blank = new BlockBlank
    val light = new BlockLight
    val model = new BlockModel

    def registerBlocks(): Unit = {
        BlockRegistry.registerBlock(air)
        BlockRegistry.registerBlock(blank)
        BlockRegistry.registerBlock(light)
        BlockRegistry.registerBlock(model)
    }
}
