package world

import java.util.Random

import block._
import reference.{Blocks, WorldRef}
import registry.BlockRegistry
import render.RenderModel

/**
 * Creates a chunk object
 * @param xCoord The X chunk coordinate of the chunk
 * @param zCoord The Z chunk coordinate of the chunk
 */
class Chunk(xCoord: Int, zCoord: Int) {
    val rand = new Random
    var blocks = new Array[Int](WorldRef.CHUNK_SIZE)
    var lightLevels = new Array[Byte](WorldRef.CHUNK_SIZE)
    for ((block, i) <- blocks.zipWithIndex) {
        blocks(i) = Blocks.air.blockID
    }
    var chunkModel: RenderModel = null
    var isDirty: Boolean = false
    var isEmpty: Boolean = true
    //    generate()

    /**
     * Ticks the chunk
     */
    def tick(): Unit = {
//        for ((blockID, i) <- blocks.zipWithIndex) {
//            val block = BlockRegistry.getBlock(blockID)

//        }
    }

    //    /**
    //     * Returns if the chunk has not been generated
    //     * @return If the chunk has not been generated
    //     */
    //    def isEmpty: Boolean = {
    //        blocks.foreach(blockID => {
    //            if (BlockRegistry.getBlock(blockID) != Blocks.air) false
    //        })
    //        true
    //    }

    /**
     * Gets the x, y, z of the block at index
     * @param index Index of the block
     * @return The x, y, z of the block
     */
    def getBlockXYZFromIndex(index: Int): (Int, Int, Int) = {
        ((index >> 4) & 15, index >> 8, index & 15)
    }

    /**
     * Gets the index of a block
     * @param x X location of the block
     * @param y Y location of the block
     * @param z Z location of the block
     * @return
     */
    def getBlockIndexFromXYZ(x: Int, y: Int, z: Int): Int = {
        y << 8 | x << 4 | z
    }

    /**
     * Checks if the block exists (isn't null and isn't air)
     * @param x X location of the block
     * @param y Y location of the block
     * @param z Z location of the block
     * @return If the block at x, y, z exists
     */
    def blockExists(x: Int, y: Int, z: Int): Boolean = getBlock(x, y, z) != Blocks.air

    /**
     * Returns the block at x, y, x (chunk coordinates)
     * @param x X location of the block
     * @param y Y location of the block
     * @param z Z location of the block
     * @return The blockID at x, y, x (chunk coordinates)
     */
    def getBlock(x: Int, y: Int, z: Int): Block = {
        //        println("(" + x + ", " + y + ", " + z + ")")
        val index = getBlockIndexFromXYZ(x, y, z)
        var block: Block = null
        if (index >= 0 && index < blocks.length) {
            block = BlockRegistry.getBlock(blocks(y << 8 | x << 4 | z))
            if (block == null) block = Blocks.air
        } else {
            block = Blocks.air
        }
        block
    }

    /**
     * Generates the chunk
     */
    def generate(): Unit = {
        println("Generate Chunk | " + xCoord + " " + zCoord)
        val size = 8
        val opts = Array(Blocks.blank, Blocks.light, Blocks.model, Blocks.glass, Blocks.pillar, Blocks.slab, Blocks.sphere)
        for (x <- 0 to 15) {
            for (z <- 0 to 15) {
                for (y <- 0 to 16) {
                    if ((x < size && z < size && y < 5) || y < 1) {
                        val r = rand.nextInt(opts.length)
                        setBlock(x, y, z, opts(r))
                    }
                }
            }
        }
        isEmpty = false
    }

    /**
     * Sets the block at x, y, z (chunk coordinates) to block
     * @param x X location of the block
     * @param y Y location of the block
     * @param z Z location of the block
     * @param block Sets the block at x, y, z (internal chunk coordinates) to block
     */
    def setBlock(x: Int, y: Int, z: Int, block: Block): Unit = {
        blocks(getBlockIndexFromXYZ(x, y, z)) = block.blockID
        isDirty = true
    }

    /**
     * Sets the lightlevel at the provided x, y, z (chunk coordinates)
     * @param x X location of the block
     * @param y Y location of the block
     * @param z Z location of the block
     * @param lightLevel Level of the light to set
     */
    def setLightLevel(x: Int, y: Int, z: Int, lightLevel: Byte): Unit = {
        //        val cappedLightLevel = Math.max(0, Math.min(16, lightLevel)).toByte
        lightLevels(getBlockIndexFromXYZ(x, y, z)) = lightLevel
    }

    /**
     * Gets the light level at x, y, z (chunk coordinates)
     * @param x X location of the block
     * @param y Y location of the block
     * @param z Z location of the block
     * @return The light level at x, y, z (chunk coordinates)
     */
    def getLightLevel(x: Int, y: Int, z: Int): Byte = {
        lightLevels(getBlockIndexFromXYZ(x, y, z))
    }

    /**
     * Gets the x coordinate of the chunk
     * @return The x coordinate of the chunk
     */
    def getXCoord: Int = xCoord

    /**
     * Gets the z coordinate of the chunk
     * @return The z coordinate of the chunk
     */
    def getZCoord: Int = zCoord

    //    def getModelInstances: (Array[GameModel], Environment) = {
    //        val blocksToUse = blocks.filter(b => b != null)
    //        var instances = Array[GameModel]()
    //        val environment = new Environment
    //        for((block: Block, i) <- blocksToUse.view.zipWithIndex){
    //            if(block != null) {
    //                val instance = new GameModel(block.model)
    //                val xyz = getBlockXYZFromIndex(i)
    //                instance.transform.translate(new Vector3(xyz._1 + (xCoord * 16), xyz._2, xyz._3 + (zCoord * 16)))
    //                instances = instances :+ instance
    //                if(block.lightLevel != 0){
    //                    environment.add(new PointLight().set(Color.WHITE, xyz._1, xyz._2, xyz._3, block.lightLevel))
    //                }
    //            }
    //        }
    //        (instances, environment)
    //    }
}
