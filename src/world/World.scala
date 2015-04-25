package world

import block.{Block, BlockAir}
import core.HyperScape
import org.lwjgl.opengl.GL20
import org.lwjgl.util.vector.{Matrix4f, Vector3f}
import registry.ShaderRegistry
import render.{Model, RenderModel}
import util.WorldUtil

import scala.collection.mutable


abstract class World {
    var chunks = new mutable.HashMap[Int, Chunk]
    var time = 0
    var activeChunks: Array[Int] = null
    //    val environment = new Environment
    //    chunks.put(0, new Chunk(0, 0))

    /**
     * Sets the block at x, y, z to block
     * @param x X Coordinate of block
     * @param y Y Coordinate of block
     * @param z Z Coordinate of block
     * @param block Block to set the location to
     */
    def setBlock(x: Int, y: Int, z: Int, block: Block): Unit = {
        chunks(WorldUtil.getChunkIndexFromXZ(x, z)).setBlock(x & 15, y, z & 15, block)
    }

    /**
     * Returns the block at x, y, z
     * @param x X coordinate of block
     * @param y Y coordinate of block
     * @param z Z coordinate of block
     * @return Block at x, y, z
     */
    def getBlock(x: Int, y: Int, z: Int): Block = {
        if (x >= 0 && y >= 0 && z >= 0) {
            getChunk(x, z).getBlock(x & 15, y, z & 15)
        } else {
            null
        }

    }

    /**
     * Gets the chunk at the coordinate x, z
     * @param x X coordinate of chunk
     * @param z Z coordinate of chunk
     * @return Chunk at x, z
     */
    def getChunk(x: Int, z: Int): Chunk = {
        chunks(WorldUtil.getChunkIndexFromXZ(x, z))
    }

    /**
     * Gets the chunk at the index in the chunk array
     * @param index index of the chunk in the chunk array
     * @return Chunk at the index in the chunk array
     */
    def getChunkFromIndex(index: Int): Chunk = {
        chunks.getOrElse(index, null)
    }

    /**
     * Called every tick and updates the world
     */
    def tick(): Unit = {
        //                println("Tick")
        activeChunks = WorldUtil.getSurroundingChunkIndexes(new Vector3f(-HyperScape.mainCamera.pos.x, HyperScape.mainCamera.pos.y, -HyperScape.mainCamera.pos.z), 4)
        activeChunks.foreach(chunkIndex => {
            if (chunks.getOrElse(chunkIndex, null) == null) {
                println("New Chunk " + chunkIndex)
                val (x, z) = WorldUtil.getChunkXZFromIndex(chunkIndex)
                chunks.put(chunkIndex, new Chunk(x, z))
            }
            if (chunks.getOrElse(chunkIndex, null).isEmpty) {
                chunks(chunkIndex).generate()
            }
            chunks.getOrElse(chunkIndex, null).tick()
        })
//        println(activeChunks.length)
    }

    /**
     * Renders the world
     */
    def render(): Unit = {
        checkDirtyChunks()
        HyperScape.mainCamera.uploadView()
        var i = 0
        activeChunks.foreach(chunkIndex => {
            val chunk = chunks(chunkIndex)

            val modelMatrix = new Matrix4f()
//            println("X, Z | " + chunk.getXCoord + " " + chunk.getZCoord)
            modelMatrix.translate(new Vector3f(chunk.getXCoord * 16, 0, chunk.getZCoord * 16))
//            println(modelMatrix.toString)
            val loc = ShaderRegistry.getCurrentShader().getUniformLocation("modelMatrix")
            modelMatrix.store(HyperScape.uploadBuffer)
            HyperScape.uploadBuffer.flip()
            GL20.glUniformMatrix4(loc, false, HyperScape.uploadBuffer)
            HyperScape.uploadBuffer.clear()

            chunk.chunkModel.render()
            i =  i + 1
        })
//        println(HyperScape.mainCamera.view)
    }

    def checkDirtyChunks(): Unit = {
        //Those chunks are so dirty
        activeChunks.foreach(chunkIndex => {
            if (chunks(chunkIndex).isDirty) {
                println("Dirty Chunk: " + chunkIndex)
                updateChunkModel(chunkIndex)
            }
        })
    }

    /**
     * Updates the chunk's model
     * @param index the index of the chunk to update
     */
    def updateChunkModel(index: Int): Unit = {
        println("Updating Chunk " + index)
        val chunk = chunks(index)
        var verts = Array[Float]()
        var num = 0
        for ((block, i) <- chunk.blocks.zipWithIndex) {
            if (block != null && !block.isInstanceOf[BlockAir]) {
                val (x,y,z) = chunk.getBlockXYZFromIndex(i)
                val newModel: Model = new Model(block.gameModel.getVertices.clone())
                newModel.translate(x, y, z)
                verts = verts ++ newModel.getVertices
                num = num + 1
            }
        }
        println("Number of Blocks: " + num)
        //        val newChunkMesh = RenderUtil.mergeMeshes(models, transforms)
        val newChunkModel = new RenderModel(verts)
        //        val newChunkMaterial = new Material()
        //        newChunkModel.meshes.add(newChunkMesh)
        //        newChunkMaterial.set(new TextureAttribute(TextureAttribute.Diffuse, TextureRegistry.getTexture("terrainMap")))
        //        newChunkModel.materials.add(newChunkMaterial)
        println("Number of Verts: " + verts.length)
        newChunkModel.translate(chunk.getXCoord * 16, 0, chunk.getZCoord * 16)
        chunks(index).chunkModel = newChunkModel
        chunks(index).isDirty = false
    }

    def destroy(): Unit = {
        for(chunk <- chunks)
            chunk._2.chunkModel.destroy()
    }

    //    /**
    //     * Generates the chunk at chunk coordinate x, z
    //     * @param x X coordinate of chunk
    //     * @param z Z coordinate of chunk
    //     */
    //    def generateChunk(x: Int, z: Int): Unit = {
    //        val chunk = new Chunk
    //        for (x <- 0 to 15) {
    //            for (y <- 0 to 64) {
    //                for (z <- 0 to 15) {
    //                    chunk.setBlock(x, y, z, new BlockBlank)
    //                }
    //            }
    //        }
    //        chunks.put(WorldUtil.getChunkIndexFromXY(x, z), chunk)
    //    }
}