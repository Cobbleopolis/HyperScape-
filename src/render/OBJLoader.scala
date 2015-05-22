package render

import scala.io.Source

object OBJLoader {

    /**
     * Loads an OBJ file
     * @param filePath Path to the OBJ model to load
     * @return A model generated from the OBJ file
     */
    def loadFromOBJFile(filePath: String): Model = {
        val objFileContentsArray = Source.fromFile(filePath).getLines()
        //        val vertArray = BufferUtils.newFloatBuffer(objFileContentsArray.count(line => line.startsWith("v ")))
        var vertArray = Array[Float]()
        var uvArray = Array[Float]()
        var normArray = Array[Float]()
        var indArray = Array[(Int, Int, Int)]()
        objFileContentsArray.foreach(line => {
            if (line.startsWith("v ")) {
                val vertLine = line.split(" ")
                vertArray = vertArray :+ vertLine(1).toFloat :+ vertLine(2).toFloat :+ vertLine(3).toFloat
                //                vertArray.put(vertLine(0).toFloat).put(vertLine(1).toFloat).put(vertLine(2).toFloat)
            } else if (line.startsWith("vt ")) {
                val uvLine = line.split(" ")
                uvArray = uvArray :+ uvLine(1).toFloat :+ uvLine(2).toFloat
            } else if (line.startsWith("vn ")) {
                val normLine = line.split(" ")
                normArray = normArray :+ normLine(1).toFloat :+ normLine(2).toFloat :+ normLine(3).toFloat
            } else if (line.startsWith("f ")) {
                val indLine = line.split(" ")

                var splitInd = indLine(1).split("/")
                indArray = indArray :+(splitInd(0).toInt - 1, splitInd(1).toInt - 1, splitInd(2).toInt - 1)

                splitInd = indLine(2).split("/")
                indArray = indArray :+(splitInd(0).toInt - 1, splitInd(1).toInt - 1, splitInd(2).toInt - 1)

                splitInd = indLine(3).split("/")
                indArray = indArray :+(splitInd(0).toInt - 1, splitInd(1).toInt - 1, splitInd(2).toInt - 1)
            }
        })
        var completeArray = Array[Float]()
        indArray.foreach(indexes => {
            val vertLoc = indexes._1 * 3
            val uvLoc = indexes._2 * 2
            val normLoc = indexes._3 * 3
            completeArray = completeArray :+ vertArray(vertLoc) :+ vertArray(vertLoc + 1) :+ vertArray(vertLoc + 2) :+ uvArray(uvLoc) :+ uvArray(uvLoc + 1) :+ normArray(normLoc) :+ normArray(normLoc + 1) :+ normArray(normLoc + 2)
        })
//        println(vertArray.mkString(" "))
//        println(uvArray.mkString(" "))
//        println(completeArray.mkString(" "))
        new Model(completeArray)
    }
}
