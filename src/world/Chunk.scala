package world

import java.util.Random

import block.{BlockLight, Block, BlockAir, BlockBlank}
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

    def tick(): Unit = {

    }

    def isEmpty: Boolean = {
        blocks.isEmpty
    }

    def getBlockXYZFromIndex(index: Int): (Int, Int, Int) = {
        ((index >> 4) & 15, index >> 8, index & 15)
    }

    def blockExists(x: Int, y: Int, z: Int): Boolean = {
        getBlock(x, y, z) != null
    }

    def getBlock(x: Int, y: Int, z: Int): Block = {
        //        println("(" + x + ", " + y + ", " + z + ")")
        blocks(y << 8 | x << 4 | z)
    }

    def generate(): Unit = {
        println("Generate Chunk | " + xCoord + " " + zCoord)
        for (x <- 0 to 15) {
            for (y <- 0 to 5) {
                for (z <- 0 to 15) {
                    if (rand.nextBoolean())
                        setBlock(x, y, z, new BlockBlank)
                    else
                        setBlock(x, y, z, new BlockLight)
                }
            }
        }
    }

    def setBlock(x: Int, y: Int, z: Int, block: Block): Unit = {
        blocks(y << 8 | x << 4 | z) = block
        isDirty = true
    }

    def getXCoord: Int = xCoord

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
