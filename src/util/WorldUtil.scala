package util

import block.Block
import org.lwjgl.util.vector.Vector3f
import reference.BlockSides
import world.World

object WorldUtil {

    def getSurroundingSides(world: World, x: Int, y: Int, z: Int): Array[Int] = {
        val blocks = getSurroundingBlocks(world, x, y, z)
        val sides = Array[Int]()
        for ((block, i) <- blocks.view.zipWithIndex) {
            if (block != null) {
                sides :+ i
            }
        }
        sides
    }

    def getSurroundingBlocks(world: World, x: Int, y: Int, z: Int): Array[Block] = {
        val blocks = new Array[Block](6)
        blocks(0) = getBlockFromSide(world, x, y - 1, z, BlockSides.BOTTOM)
        blocks(1) = getBlockFromSide(world, x + 1, y, z, BlockSides.NORTH)
        blocks(2) = getBlockFromSide(world, x, y, z + 1, BlockSides.EAST)
        blocks(3) = getBlockFromSide(world, x - 1, y, z, BlockSides.SOUTH)
        blocks(4) = getBlockFromSide(world, x, y, z - 1, BlockSides.WEST)
        blocks(5) = getBlockFromSide(world, x, y + 1, z, BlockSides.TOP)
        blocks
    }

    def getBlockFromSide(world: World, x: Int, y: Int, z: Int, side: Int): Block = {
        var block: Block = null
        //        println("(" + x + ", " + y + ", " + z + ")")
        if (side == BlockSides.BOTTOM) {
            block = world.getBlock(x, y - 1, z)
        } else if (side == BlockSides.NORTH) {
            block = world.getBlock(x + 1, y, z)
        } else if (side == BlockSides.EAST) {
            block = world.getBlock(x, y, z + 1)
        } else if (side == BlockSides.SOUTH) {
            block = world.getBlock(x - 1, y, z)
        } else if (side == BlockSides.WEST) {
            block = world.getBlock(x, y, z - 1)
        } else if (side == BlockSides.TOP) {
            block = world.getBlock(x, y + 1, z)
        }
        block
    }

    def getSurroundingChunkIndexes(position: Vector3f, radius: Int): Array[Int] = {
        var chunks = Array[Int]()
        for (x <- -radius until radius) {
            for (z <- -radius until radius) {
                val index = getChunkIndexFromXZ(position.x.toInt + (x * 16), position.z.toInt + (z * 16))
                chunks = chunks :+ index
            }
        }
        //        println("Active Size: " + chunks.length + " | " + chunks.mkString(" "))
        chunks
    }

    def getChunkIndexFromXZ(x: Int, z: Int): Int = {
        val index = (x >> 4) << 4 | z >> 4
        //        println("Index: " + index + " | " + x + " " + z)
        index
    }

    def getChunkXZFromIndex(index: Int): (Int, Int) = {
        (index >> 4, index & 15)
    }

}
