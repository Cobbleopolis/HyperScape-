package util

object RenderUtil {

    //    def mergeMeshes(models: Array[GameModel], transformations: Array[Vector3]): GameModel = {
    //        var combinedVerts = Array[Float]()
    //        for ((model, i) <- models.zipWithIndex) {
    //            model.translate(transformations(i))
    //            combinedVerts = combinedVerts ++ model.getVertices
    //        }
    //        new GameModel(combinedVerts)
    //    }

    //    def mergeMeshes(meshes: List[Mesh], transformations: List[Matrix4]): Mesh = {
    //        if (meshes.size == 0) return null
    //
    //        var vertexArrayTotalSize = 0
    //        var indexArrayTotalSize = 0
    //
    //        val va = meshes.head.getVertexAttributes
    //        val vaA = new Array[Int](va.size())
    //        for (i <- 0 until va.size()) {
    //            vaA(i) = va.get(i).usage
    //        }
    //
    //        for (i <- 0 until meshes.size) {
    //            val mesh = meshes(i)
    //            if (mesh.getVertexAttributes.size() != va.size()) {
    //                meshes.patch(i, Seq(copyMesh(mesh, isStatic = true, removeDuplicates = false, vaA)), 1)
    //            }
    //
    //            vertexArrayTotalSize = vertexArrayTotalSize + (mesh.getNumVertices * mesh.getVertexSize / 4)
    //            indexArrayTotalSize = indexArrayTotalSize + mesh.getNumIndices
    //        }
    //
    //        val vertices = new Array[Float](vertexArrayTotalSize)
    //        val indices = new Array[Short](indexArrayTotalSize)
    //
    //        var indexOffset = 0
    //        var vertexOffset = 0
    //        var vertexSizeOffset = 0
    //        var vertexSize = 0
    //
    //        for (i <- 0 until meshes.size) {
    //            val mesh: Mesh = meshes(i)
    //
    //            val numIndices = mesh.getNumIndices
    //            val numVertices = mesh.getNumVertices
    //            vertexSize = mesh.getVertexSize / 4
    //            val baseSize = numVertices * vertexSize
    //            val posAttr = mesh.getVertexAttribute(Usage.Position)
    //            val offset = posAttr.offset / 4
    //            val numComponents = posAttr.numComponents
    //
    //            {
    //                //uzupelnianie tablicy indeksow
    //                mesh.getIndices(indices, indexOffset)
    //                for (c <- indexOffset until (indexOffset + numIndices)) {
    //                    indices(c) = (indices(c) + vertexOffset).toShort
    //                }
    //                indexOffset += numIndices
    //            }
    //
    //            mesh.getVertices(0, baseSize, vertices, vertexSizeOffset)
    //            Mesh.transform(transformations(i), vertices, vertexSize, offset, numComponents, vertexOffset, numVertices)
    //            vertexOffset += numVertices
    //            vertexSizeOffset += baseSize
    //        }
    //
    //        val result = new Mesh(true, vertexOffset, indices.length, meshes.head.getVertexAttributes)
    //        result.setVertices(vertices)
    //        result.setIndices(indices)
    //        result
    //    }
    //
    //    def copyMesh(meshToCopy: Mesh, isStatic: Boolean, removeDuplicates: Boolean, usage: Array[Int]): Mesh = {
    //        // TODO move this to a copy constructor?
    //        // TODO duplicate the buffers without double copying the data if possible.
    //        // TODO perhaps move this code to JNI if it turns out being too slow.
    //        val vertexSize = meshToCopy.getVertexSize / 4
    //        var numVertices = meshToCopy.getNumVertices
    //        var vertices = Array[Float](numVertices * vertexSize)
    //        meshToCopy.getVertices(0, vertices.length, vertices)
    //        var checks: Array[Short] = null
    //        var attrs: Array[VertexAttribute] = null
    //        var newVertexSize = 0
    //        if (usage != null) {
    //            var size = 0
    //            var as = 0
    //            for (i <- 0 to usage.length)
    //                if (meshToCopy.getVertexAttribute(usage(i)) != null) {
    //                    size += meshToCopy.getVertexAttribute(usage(i)).numComponents
    //                    as = as + 1
    //                }
    //            if (size > 0) {
    //                attrs = new Array[VertexAttribute](as)
    //                checks = new Array[Short](size)
    //                var idx = -1
    //                var ai = -1
    //                for (i <- 0 to usage.length) {
    //                    val a = meshToCopy.getVertexAttribute(usage(i))
    //                    if (a == null) {
    //                        for (j <- 0 to a.numComponents) {
    //                            idx = idx + 1
    //                            checks(idx) = (a.offset / 4 + j).toShort
    //                        }
    //                        ai = ai + 1
    //                        attrs(ai) = new VertexAttribute(a.usage, a.numComponents, a.alias)
    //                        newVertexSize += a.numComponents
    //                    }
    //                }
    //            }
    //        }
    //        if (checks == null) {
    //            checks = new Array[Short](vertexSize)
    //            for (i <- 0 until vertexSize)
    //                checks(i) = i.toShort
    //            newVertexSize = vertexSize
    //        }
    //
    //        val numIndices = meshToCopy.getNumIndices
    //        var indices: Array[Short] = null
    //        if (numIndices > 0) {
    //            indices = new Array[Short](numIndices)
    //            meshToCopy.getIndices(indices)
    //            if (removeDuplicates || newVertexSize != vertexSize) {
    //                val tmp = new Array[Float](vertices.length)
    //                var size = 0
    //                for (i <- 0 to numIndices) {
    //                    val idx1 = indices(i) * vertexSize
    //                    var newIndex: Short = -1
    //                    if (removeDuplicates) {
    //                        for (j <- 0 to size) {
    //                            if (newIndex < 0) {
    //                                val idx2 = j * newVertexSize
    //                                var found = true
    //                                for (k <- 0 to checks.length) {
    //                                    if(found)
    //                                        if (tmp(idx2 + k) != vertices(idx1 + checks(k)))
    //                                            found = false
    //                                }
    //                                if (found) {
    //                                    newIndex = j.toShort
    //                                }
    //                            }
    //                        }
    //                    }
    //                    if (newIndex > 0){
    //                        indices(i) = newIndex
    //                    } else {
    //                        val idx = size * newVertexSize
    //                        for (j <- 0 to checks.length)
    //                        tmp(idx + j) = vertices(idx1 + checks(j))
    //                        indices(i) = size.toShort
    //                        size = size + 1
    //                    }
    //                }
    //                vertices = tmp
    //                numVertices = size
    //            }
    //        }
    //
    //        var result: Mesh = null
    ////        if (attrs == null)
    //            result = new Mesh(isStatic, numVertices, if (indices == null) 0 else indices.length, meshToCopy.getVertexAttributes)
    ////        else
    ////            result = new Mesh(isStatic, numVertices, if (indices == null) 0 else indices.length, attrs)
    //        result.setVertices(vertices, 0, numVertices * newVertexSize)
    //        result.setIndices(indices)
    //        result
    //    }
}
