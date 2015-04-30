package util

import block.{Block, BlockAir}
import org.lwjgl.util.vector.Vector3f
import reference.{Blocks, BlockSides}
import registry.BlockRegistry
import world.{Chunk, World}

object WorldUtil {

    /**
     * Gets the BlockSides that are surrounding the x, y, z location in world
     * @param world The world the blocks are in
     * @param x The x value of the location
     * @param y The y value of the location
     * @param z The z value of the location
     * @return An array of BlockSides that surround a block
     */
    def getSurroundingSides(world: World, x: Int, y: Int, z: Int): Array[Int] = {
        val blocks = getSurroundingBlocks(world, x, y, z)
        var sides = Array[Int]()
        for ((block, i) <- blocks.view.zipWithIndex) {
            if (block != null && block != Blocks.air) {
                sides = sides :+ i
            }
        }
        sides
    }


    /**
     * Gets the sides that need to be rendered for a block
     * @param world The world the block is in
     * @param x The x value of the location
     * @param y The y value of the location
     * @param z The z value of the location
     * @return An array containing the BlockSides that need to be rendered
     */
    def getSidesForRender(world: World, x: Int, y: Int, z: Int): Array[Int] = {
        val blocks = getSurroundingBlocks(world, x, y, z)
        blocks.zipWithIndex
                .filter(x => {
            val (b, _) = x
            b == Blocks.air || b.renderType != 1
        })
                .map(x => x._2)
    }

    /**
     * Gets the blocks that surround a the x, y, z location
     * @param world World to check
     * @param x The x value of the location
     * @param y The y value of the location
     * @param z The x value of the location
     * @return An array of the blocks that surround the x, y, z location
     */
    def getSurroundingBlocks(world: World, x: Int, y: Int, z: Int): Array[Block] = {

        val blocks = new Array[Block](6)
        blocks(0) = getBlockFromSide(world, x, y, z, BlockSides.BOTTOM)
        blocks(1) = getBlockFromSide(world, x, y, z, BlockSides.TOP)
        blocks(2) = getBlockFromSide(world, x, y, z, BlockSides.NORTH)
        blocks(3) = getBlockFromSide(world, x, y, z, BlockSides.EAST)
        blocks(4) = getBlockFromSide(world, x, y, z, BlockSides.SOUTH)
        blocks(5) = getBlockFromSide(world, x, y, z, BlockSides.WEST)
        //        println(blocks.mkString(" "))
        blocks
    }

    /**
     * Gets the block on the side of the x, y z location in world
     * @param world World to check
     * @param x The x value of the location
     * @param y The y value of the location
     * @param z The z value of the location
     * @param side The BlockSides to get the block from
     * @return The block on the side of the x, y z location in world
     */
    def getBlockFromSide(world: World, x: Int, y: Int, z: Int, side: Int): Block = {
        var block: Block = null
        //        println("(" + x + ", " + y + ", " + z + ")")
        if (side == BlockSides.BOTTOM) {
            if (world.blockExists(x, y - 1, z))
                block = world.getBlock(x, y - 1, z)
        } else if (side == BlockSides.NORTH) {
            if (world.blockExists(x + 1, y, z))
                block = world.getBlock(x + 1, y, z)
        } else if (side == BlockSides.EAST) {
            if (world.blockExists(x, y, z - 1))
                block = world.getBlock(x, y, z - 1)
        } else if (side == BlockSides.SOUTH) {
            if (world.blockExists(x - 1, y, z))
                block = world.getBlock(x - 1, y, z)
        } else if (side == BlockSides.WEST) {
            if (world.blockExists(x, y, z + 1))
                block = world.getBlock(x, y, z + 1)
        } else if (side == BlockSides.TOP) {
            if (world.blockExists(x, y + 1, z))
                block = world.getBlock(x, y + 1, z)
        }
        if (block == null) {
            block = BlockRegistry.getBlock(Blocks.air.blockID)
        }
        block
    }

    /**
     * Gets the block on the side of the x, y z location in world
     * @param chunk World to check
     * @param x The x value of the location
     * @param y The y value of the location
     * @param z The z value of the location
     * @param side The BlockSides to get the block from
     * @return The block on the side of the x, y z location in world
     */
    def getBlockFromSide(chunk: Chunk, x: Int, y: Int, z: Int, side: Int): Block = {
        var block: Block = null
        //        println("(" + x + ", " + y + ", " + z + ")")
        if (side == BlockSides.BOTTOM) {
            block = chunk.getBlock(x, y - 1, z)
        } else if (side == BlockSides.NORTH) {
            block = chunk.getBlock(x + 1, y, z)
        } else if (side == BlockSides.EAST) {
            block = chunk.getBlock(x, y, z - 1)
        } else if (side == BlockSides.SOUTH) {
            block = chunk.getBlock(x - 1, y, z)
        } else if (side == BlockSides.WEST) {
            block = chunk.getBlock(x, y, z + 1)
        } else if (side == BlockSides.TOP) {
            block = chunk.getBlock(x, y + 1, z)
        }
        if (block == null) {
            block = Blocks.air
        }
        block
    }

    /**
     * Gets the chunks surrounding the position
     * @param position The position to get the surrounding chunks from
     * @param radius The radius to look around the position
     * @return An array with the indexes of the surrounding chunks
     */
    def getSurroundingChunkIndexes(position: Vector3f, radius: Int): Array[Int] = {
        var chunks = Array[Int]()
        for (x <- -radius to radius) {
            for (z <- -radius to radius) {
                val index = getChunkIndexFromXZ(position.x.toInt + (x * 16), position.z.toInt + (z * 16))
                chunks = chunks :+ index
            }
        }
        //        println("Center: " + position.getX + ", " + position.getY + ", " + position.getZ)
        //                println("Active Size: " + chunks.length + " | " + chunks.mkString(", "))
        chunks
    }

    /**
     * Converts a chunk's x, z coordinates into it's index
     * @param x X world coordinate
     * @param z Z world coordinate of the chunk
     * @return The index of the chunk at x, z
     */
    def getChunkIndexFromXZ(x: Int, z: Int): Int = {
        (x >> 4) << 16 | ((z >> 4) & 0xFFFF)
    }

    /**
     * Converts a chunk's index into it's x, z location
     * @param index Index of the chunk
     * @return The x, z location of the chunk
     */
    def getChunkXZFromIndex(index: Int): (Int, Int) = {
        (index >> 16, (index & 0xFFFF).toShort)
    }

}
