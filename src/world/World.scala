package world


import block.Block
import core.HyperScape
import entity.Entity
import org.lwjgl.opengl.GL20
import org.lwjgl.util.vector.{Matrix4f, Vector3f}
import physics.AxisAlignedBB
import reference.{BlockSides, Blocks, RenderTypes}
import registry.{BlockRegistry, ShaderRegistry}
import render.{Model, RenderModel, Vertex}
import util.WorldUtil

import scala.collection.mutable


abstract class World {
    var chunks = new mutable.HashMap[Int, Chunk]
    var time = 0
    var activeChunks: Array[Int] = null
    val grav = -0.025f
    //    val environment = new Environment
    //    chunks.put(0, new Chunk(0, 0))

    /**
     * Sets the block at x, y, z to block
     * @param x X Coordinate of block
     * @param y Y Coordinate of block
     * @param z Z Coordinate of block
     * @param block Block to set the location to
     */
    def setBlock(x: Int, y: Int, z: Int, block: Int): Unit = {
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
        chunks(WorldUtil.getChunkIndexFromXZ(x, z)).getBlock(x & 15, y, z & 15)
    }

    def blockExists(x: Int, y: Int, z: Int): Boolean = {
        if (chunks.contains(WorldUtil.getChunkIndexFromXZ(x, z))) {
            getBlock(x, y, z) != Blocks.air
        } else {
            false
        }
    }

    /**
     * Gets the chunk at the coordinate x, z
     * @param x X coordinate of chunk
     * @param z Z coordinate of chunk
     * @return Chunk at x, z
     */
    def getChunk(x: Int, z: Int): Chunk = {
        chunks.getOrElse(WorldUtil.getChunkIndexFromXZ(x, z), null)
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
    def tick(player: Entity): Unit = {
        //        println(player.position.toString)
        activeChunks = WorldUtil.getSurroundingChunkIndexes(new Vector3f(player.position.x, player.position.y, player.position.z), 4)
        //        activeChunks = WorldUtil.getSurroundingChunkIndexes(new Vector3f(0, 0, 0), 2)
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
        player.tick()
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
            val loc = ShaderRegistry.getCurrentShader.getUniformLocation("modelMatrix")
            modelMatrix.store(HyperScape.uploadBuffer)
            HyperScape.uploadBuffer.flip()
            GL20.glUniformMatrix4(loc, false, HyperScape.uploadBuffer)
            HyperScape.uploadBuffer.clear()

            val colorLoc = ShaderRegistry.getCurrentShader.getUniformLocation("chunkColor")
            if (HyperScape.shaderSelector == 0) {
                GL20.glUniform4f(colorLoc, 1, 1, 1, 1)
            } else if (HyperScape.shaderSelector == 1) {
                val (r, g) = Math.abs(chunk.getXCoord + chunk.getZCoord) % 2 match {
                    case 0 => (1, 0)
                    case 1 => (0, 1)
                }
                GL20.glUniform4f(colorLoc, r, g, 0.3125f, 1)
            } else if (HyperScape.shaderSelector == 2) {
                GL20.glUniform4f(colorLoc, Math.random().toFloat, Math.random().toFloat, Math.random().toFloat, Math.random().toFloat)
            } else if (HyperScape.shaderSelector == 3) {
                GL20.glUniform4f(colorLoc, Math.sin(chunk.getXCoord).toFloat, Math.sin(chunk.getZCoord).toFloat, 0.3125f, 1)
            }

            chunk.chunkModel.render()
            i = i + 1
        })
        //        println(HyperScape.mainCamera.view)
    }

    /**
     * Checks for dirty chunks and regenerates it's model if it is dirty
     */
    def checkDirtyChunks(): Unit = {
        //Those chunks are so dirty
        activeChunks.foreach(chunkIndex => {
            if (chunks(chunkIndex).isDirty) {
                println("Dirty Chunk: " + chunkIndex)
                if (chunks(chunkIndex).chunkModel != null) chunks(chunkIndex).chunkModel.destroy()
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
        for ((blockID, i) <- chunk.blocks.zipWithIndex) {
            val block = BlockRegistry.getBlock(blockID)
            if (block.renderType != RenderTypes.DOES_NOT_RENDER) {
                val (x, y, z) = chunk.getBlockXYZFromIndex(i)
                val modelVerts = block.gameModel.getVertices.clone()
                var newVerts = Array[Float]()
                //                x += (chunk.getXCoord * 16)
                //                z += (chunk.getZCoord * 16)
                if (block.renderType != RenderTypes.DOES_NOT_RENDER && block.renderType != RenderTypes.NON_FULL_BLOCK) {
                    val surroundingBlocks = WorldUtil.getSidesForRender(this, x + (chunk.getXCoord * 16), y, z + (chunk.getZCoord * 16))
                    if (surroundingBlocks.contains(BlockSides.TOP)) {
                        for (i <- block.topFaces) {
                            newVerts = newVerts ++ modelVerts.clone().slice(i * (Vertex.ELEMENT_COUNT * 3),
                                (i * (Vertex.ELEMENT_COUNT * 3)) + Vertex.ELEMENT_COUNT * 3)
                        }
                    }
                    if (surroundingBlocks.contains(BlockSides.NORTH)) {
                        for (i <- block.northFaces) {
                            newVerts = newVerts ++ modelVerts.clone().slice(i * (Vertex.ELEMENT_COUNT * 3),
                                (i * (Vertex.ELEMENT_COUNT * 3)) + Vertex.ELEMENT_COUNT * 3)
                        }
                    }
                    if (surroundingBlocks.contains(BlockSides.EAST)) {
                        for (i <- block.eastFaces) {
                            newVerts = newVerts ++ modelVerts.clone().slice(i * (Vertex.ELEMENT_COUNT * 3),
                                (i * (Vertex.ELEMENT_COUNT * 3)) + Vertex.ELEMENT_COUNT * 3)
                        }
                    }
                    if (surroundingBlocks.contains(BlockSides.SOUTH)) {
                        for (i <- block.southFaces) {
                            newVerts = newVerts ++ modelVerts.clone().slice(i * (Vertex.ELEMENT_COUNT * 3),
                                (i * (Vertex.ELEMENT_COUNT * 3)) + Vertex.ELEMENT_COUNT * 3)
                        }
                    }
                    if (surroundingBlocks.contains(BlockSides.WEST)) {
                        for (i <- block.westFaces) {
                            newVerts = newVerts ++ modelVerts.clone().slice(i * (Vertex.ELEMENT_COUNT * 3),
                                (i * (Vertex.ELEMENT_COUNT * 3)) + Vertex.ELEMENT_COUNT * 3)
                        }
                    }
                    if (surroundingBlocks.contains(BlockSides.BOTTOM)) {
                        for (i <- block.bottomFaces) {
                            newVerts = newVerts ++ modelVerts.clone().slice(i * (Vertex.ELEMENT_COUNT * 3),
                                (i * (Vertex.ELEMENT_COUNT * 3)) + Vertex.ELEMENT_COUNT * 3)
                        }
                    }
                } else {
                    newVerts = modelVerts.clone()
                }

                val newModel: Model = new Model(newVerts)

                newModel.translate(x, y, z)
                newModel.translateUV(block.texCoord._1.toFloat / 16f, block.texCoord._2.toFloat / 16f)
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

    def getCollidingBoundingBoxes(boundingBox: AxisAlignedBB): Array[AxisAlignedBB] = {
        var boundingBoxes = Array[AxisAlignedBB]()
        val xMin: Int = Math.floor(boundingBox.minX - 1.0f).toInt
        val xMax: Int = Math.floor(boundingBox.maxX + 1.0f).toInt
        val yMin: Int = Math.floor(boundingBox.minY - 1.0f).toInt
        val yMax: Int = Math.floor(boundingBox.maxY + 1.0f).toInt
        val zMin: Int = Math.floor(boundingBox.minZ - 1.0f).toInt
        val zMax: Int = Math.floor(boundingBox.maxZ + 1.0f).toInt

        for (x <- xMin to xMax) {
            for (y <- yMin to yMax) {
                for (z <- zMin to zMax) {
                    if (getBlock(x, y, z).hasCollision) {
                        val bb = getBlock(x, y, z).boundingBox.getTranslatedBoundingBox(x, y, z - 1)
//                        print(boundingBox.isTouching(bb) + " | ")
                        //                        if(boundingBox.intersects(bb))
                            boundingBoxes = boundingBoxes :+ bb
                    }
                }
            }
        }

        //TODO Add detection of entities inside of AABB
//        Debug.printVec(xMin, yMin, zMin)
//        Debug.printVec(xMax, yMax, zMax)
        boundingBoxes
    }


    /**
     * Destroies the world
     */
    def destroy(): Unit = {
        for (chunk <- chunks)
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