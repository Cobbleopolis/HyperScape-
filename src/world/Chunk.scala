package world

import java.util.Random

import block._
import render.{RenderModel, Model}

class Chunk(xCoord: Int, zCoord: Int) {
    val rand = new Random
    var blocks = new Array[Block](16384)
    for ((block, i) <- blocks.zipWithIndex) {
        blocks(i) = new BlockAir
    }
    var chunkModel: RenderModel = null
    var isDirty: Boolean = false
    generate()

    /**
     * Ticks the chunk
     */
    def tick(): Unit = {

    }

    /**
     * Returns if the chunk is empty
     * @return If the chunk is empty or not
     */
    def isEmpty: Boolean = {
        blocks.isEmpty
    }

    /**
     * Gets the x, y, z of the block at index
     * @param index Index of the block
     * @return The x, y, z of the block
     */
    def getBlockXYZFromIndex(index: Int): (Int, Int, Int) = {
        ((index >> 4) & 15, index >> 8, index & 15)
    }

    /**
     * Checks if the block exists (isn't null and isn't air
     * @param x X location of the block
     * @param y Y location of the block
     * @param z Z location of the block
     * @return If the block at x, y, z exists
     */
    def blockExists(x: Int, y: Int, z: Int): Boolean = {
        getBlock(x, y, z) != null && !getBlock(x, y, z).isInstanceOf[BlockAir]
    }

    /**
     * Returns the block at x, y, x (chunk coordinates)
     * @param x X location of the block
     * @param y Y location of the block
     * @param z Z location of the block
     * @return The block at x, y, x (chunk coordinates)
     */
    def getBlock(x: Int, y: Int, z: Int): Block = {
        //        println("(" + x + ", " + y + ", " + z + ")")
        blocks(y << 8 | x << 4 | z)
    }

    /**
     * Generates the chunk
     */
    def generate(): Unit = {
        println("Generate Chunk | " + xCoord + " " + zCoord)
        for (x <- 0 to 15) {
            for (y <- 0 to 5) {
                for (z <- 0 to 15) {
                    val r = rand.nextInt(3)
                    if (r == 0)
                        setBlock(x, y, z, new BlockBlank)
                    else if (r == 1)
                        setBlock(x, y, z, new BlockLight)
                    else
                        setBlock(x, y, z, new BlockModel)
                }
            }
        }
    }

    /**
     * Sets the block at x, y, z (chunk coordinates) to block
     * @param x X location of the block
     * @param y Y location of the block
     * @param z Z location of the block
     * @param block Sets the block at x, y, z (chunk coordinates) to block
     */
    def setBlock(x: Int, y: Int, z: Int, block: Block): Unit = {
        blocks(y << 8 | x << 4 | z) = block
        isDirty = true
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
