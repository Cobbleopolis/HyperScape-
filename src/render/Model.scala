package render

class Model(verts: Array[Float]) {
    var verticies = verts

    def translate(x: Float, y: Float, z: Float): Unit = {
        for(i <- 0 until verticies.length by 5) {
            verticies(i) += x
            verticies(i + 1) += y
            verticies(i + 2) += z
        }
    }

    def getVertices: Array[Float] = verts

    def copy: Model = {
        new Model(verticies)
    }
}
