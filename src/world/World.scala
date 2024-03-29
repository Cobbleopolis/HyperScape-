package world


import block.Block
import core.HyperScape
import entity.Entity
import org.lwjgl.opengl.GL20
import org.lwjgl.util.vector.{Matrix4f, Vector3f}
import physics.AxisAlignedBB
import reference.{BlockSides, Blocks, RenderTypes, WorldRef}
import registry.{BlockRegistry, ShaderRegistry, TextureRegistry}
import render._
import util.WorldUtil

import scala.collection.mutable


abstract class World {
    var chunks = new mutable.HashMap[Int, Chunk]
    var time = 0
    var activeChunks: Array[Int] = null
    val grav = -0.025f
    //    val sun: SunLamp = new SunLamp(new Vector3f(1f, -1f, 1f), new Vector3f(1f, 1f, 1f), 0.25f)

    //    chunks.put(0, new Chunk(0, 0))

    /**
     * Sets the block at x, y, z to block
     * @param x X Coordinate of block
     * @param y Y Coordinate of block
     * @param z Z Coordinate of block
     * @param block Block to set the location to
     */
    def setBlock(x: Int, y: Int, z: Int, block: Block): Unit = {
        val oldBlock = chunks(WorldUtil.getChunkIndexFromXZ(x, z)).getBlock(x, y, z)
        chunks(WorldUtil.getChunkIndexFromXZ(x, z)).setBlock(x & 15, y, z & 15, block)
        if (block.lightLevel != oldBlock.lightLevel) {
            checkLight(x, y, z)
        }
    }

    /**
     * Returns the block at x, y, z
     * @param x X coordinate of block
     * @param y Y coordinate of block
     * @param z Z coordinate of block
     * @return Returns air block if the block at x, y, z does not exist otherwise returns the block at x, y, z
     */
    def getBlock(x: Int, y: Int, z: Int): Block = {
        val chunk = getChunk(x, z)
        if (chunk != null)
            chunks(WorldUtil.getChunkIndexFromXZ(x, z)).getBlock(x & 15, y, z & 15)
        else
            Blocks.air

    }

    /**
     * Sets the lightlevel at the provided x, y, z
     * @param x X location of the block
     * @param y Y location of the block
     * @param z Z location of the block
     * @param lightLevel Level of the light to set
     */
    def setLightLevel(x: Int, y: Int, z: Int, lightLevel: Byte): Unit = {
        //        val cappedLightLevel = Math.max(0, Math.min(16, lightLevel)).toByte
        chunks(WorldUtil.getChunkIndexFromXZ(x, z)).setLightLevel(x & 15, y, z & 15, lightLevel)
    }

    /**
     * Gets the light level at x, y, z
     * @param x X location of the block
     * @param y Y location of the block
     * @param z Z location of the block
     * @return The light level at x, y, z
     */
    def getLightLevel(x: Int, y: Int, z: Int): Byte = {
        chunks(WorldUtil.getChunkIndexFromXZ(x, z)).getLightLevel(x & 15, y, z & 15)
    }

    /**
     *
     * @param x
     * @param y
     * @param z
     */
    def checkLight(x: Int, y: Int, z: Int): Unit = {
        var lightLevel = getLightLevel(x, y, z)
        var blockList = Array[Vector3f]()
        if (lightLevel > 0) {
            if (!getBlock(x - 1, y, z).isOpaque) blockList = blockList :+ new Vector3f(x - 1, y, z)
            if (!getBlock(x + 1, y, z).isOpaque) blockList = blockList :+ new Vector3f(x + 1, y, z)
            if (!getBlock(x, y - 1, z).isOpaque) blockList = blockList :+ new Vector3f(x, y - 1, z)
            if (!getBlock(x, y + 1, z).isOpaque) blockList = blockList :+ new Vector3f(x, y + 1, z)
            if (!getBlock(x, y, z - 1).isOpaque) blockList = blockList :+ new Vector3f(x, y, z - 1)
            if (!getBlock(x, y, z + 1).isOpaque) blockList = blockList :+ new Vector3f(x, y, z + 1)
        }
        calcLight(blockList)
    }

    def calcLight(blockList: Array[Vector3f]): Unit = {
        if (blockList.length > 0) {
            var addedBlockList = blockList.tail
            val head = blockList.head
            val (x, y, z) = (head.getX.toInt, head.getY.toInt, head.getZ.toInt)
            var lightLevel: Byte = 0
            if (getLightLevel(x - 1, y, z) > lightLevel) {
                lightLevel = getLightLevel(x - 1, y, z)
            }
            if (getLightLevel(x + 1, y, z) > lightLevel) {
                lightLevel = getLightLevel(x + 1, y, z)
            }
            if (getLightLevel(x, y - 1, z) > lightLevel) {
                lightLevel = getLightLevel(x, y - 1, z)
            }
            if (getLightLevel(x, y + 1, z) > lightLevel) {
                lightLevel = getLightLevel(x, y + 1, z)
            }
            if (getLightLevel(x, y, z - 1) > lightLevel) {
                lightLevel = getLightLevel(x, y, z - 1)
            }
            if (getLightLevel(x, y, z + 1) > lightLevel) {
                lightLevel = getLightLevel(x, y, z + 1)
            }
            lightLevel = (lightLevel - 1).toByte
            setLightLevel(x, y, z, lightLevel)
            if (lightLevel > 0) {
                if (!getBlock(x - 1, y, z).isOpaque) addedBlockList = addedBlockList :+ new Vector3f(x - 1, y, z)
                if (!getBlock(x + 1, y, z).isOpaque) addedBlockList = addedBlockList :+ new Vector3f(x + 1, y, z)
                if (!getBlock(x, y - 1, z).isOpaque) addedBlockList = addedBlockList :+ new Vector3f(x, y - 1, z)
                if (!getBlock(x, y + 1, z).isOpaque) addedBlockList = addedBlockList :+ new Vector3f(x, y + 1, z)
                if (!getBlock(x, y, z - 1).isOpaque) addedBlockList = addedBlockList :+ new Vector3f(x, y, z - 1)
                if (!getBlock(x, y, z + 1).isOpaque) addedBlockList = addedBlockList :+ new Vector3f(x, y, z + 1)
            }
            if (addedBlockList.length > 0) {
                calcLight(addedBlockList)
            }
        }
    }

    /**
     * Detects if a block is not air
     * @param x X location of the block
     * @param y Y location of the block
     * @param z Z location of the block
     * @return false if the block is not air, true otherwise
     */
    def isNonAirBlock(x: Int, y: Int, z: Int): Boolean = {
        if (chunks.contains(WorldUtil.getChunkIndexFromXZ(x, z))) {
            getBlock(x, y, z) != Blocks.air
        } else {
            false
        }
    }

    def canBlockSeeSky(x: Int, y: Int, z: Int): Boolean = {
        for (i <- y + 1 to WorldRef.CHUNK_HEIGHT) {
            if (getBlock(x, i, z) isOpaque) false
        }
        true
    }

    /**
     * Gets the chunk at the coordinate x, z
     * @param x X coordinate of chunk
     * @param z Z coordinate of chunk
     * @return Chunk at x, z null if the chunk at x, z does not exist
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
        activeChunks = WorldUtil.getSurroundingChunkIndexes(new Vector3f(player.position.x, player.position.y, player.position.z), 5)
        //        activeChunks = WorldUtil.getSurroundingChunkIndexes(new Vector3f(0, 0, 0), 2)
        activeChunks.foreach(chunkIndex => {
            if (chunks.getOrElse(chunkIndex, null) == null) {
                println("New Chunk " + chunkIndex)
                val (x, z) = WorldUtil.getChunkXZFromIndex(chunkIndex)
                chunks.put(chunkIndex, new Chunk(x, z))
            }
            if (chunks.getOrElse(chunkIndex, null).isEmpty) {
                println("Generating Chunk " + chunkIndex)
                chunks(chunkIndex).generate()
            }
            chunks.getOrElse(chunkIndex, null).tick()
        })
        player.tick()
        time = time + 1
        if (time == 300) {
            setBlock(10, 10, 10, Blocks.light);
            println("Set Block")
        }
        if (time > 24000) time = 0
        println("Tick " + time)
    }

    /**
     * Renders the world
     */
    def render(): Unit = {
        checkDirtyChunks()
        TextureRegistry.bindTexture("terrain")
        HyperScape.mainCamera.uploadView()
        //        var lights: Array[PointLight] = Array[PointLight]()
        //        var lightFloats: Array[Float] = Array[Float]()
        //        activeChunks.foreach(i => {
        //            val chunk = chunks(i)
        //            lights = lights ++ chunk.lights
        //        })
        //        lights.foreach(light => {
        ////            println((lightFloats == null) + " | " + (light.getFloatsForUpload == null))
        //            if(light != null)
        //                lightFloats = lightFloats ++ light.getFloatsForUpload
        //        })
        //        val pointFloatsLoc = ShaderRegistry.getCurrentShader.getUniformLocation("pointLampFloats")
        //        HyperScape.uploadBuffer.clear()
        //        HyperScape.uploadBuffer.put(lightFloats)
        //        HyperScape.uploadBuffer.flip()
        ////        GL20.glUniform1(pointFloatsLoc, HyperScape.uploadBuffer)
        //        HyperScape.uploadBuffer.clear()
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

            //            val sunDirectionLoc = ShaderRegistry.getCurrentShader.getUniformLocation("sunDirection")
            //            GL20.glUniform3f(sunDirectionLoc, sun.direction.getX, sun.direction.getY, sun.direction.getZ)
            //            val sunColorLoc = ShaderRegistry.getCurrentShader.getUniformLocation("sunColor")
            //            GL20.glUniform4f(sunColorLoc, sun.color.getX, sun.color.getY, sun.color.getZ, 1.0f)
            //            val sunIntensityLoc = ShaderRegistry.getCurrentShader.getUniformLocation("sunAmbient")
            //            GL20.glUniform1f(sunIntensityLoc, sun.ambient)
            chunk.chunkModel.render(HyperScape.lines)
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
                    if (isNonAirBlock(x, y, z))
                        if (getBlock(x, y, z).hasCollision) {
                            val bb = getBlock(x, y, z).boundingBox.getTranslatedBoundingBox(x, y, z)
                            //                        print(boundingBox.isTouching(bb) + " | ")
                            if (boundingBox.intersects(bb))
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