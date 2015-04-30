package registry

import block.Block

object BlockRegistry {
    private var blocks: Map[Int, Block] = Map()

    def registerBlock(block: Block): Unit = {
//        block.blockID.finalize()
        blocks += (block.blockID -> block)
    }

    def getBlock(blockID: Int): Block = {
        blocks.getOrElse(blockID, null)
    }
}
