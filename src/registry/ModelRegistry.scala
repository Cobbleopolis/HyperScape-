package registry

import render.OBJLoader

object ModelRegistry {
    val cube = OBJLoader.loadFromOBJFile("res/model/cube.obj")
    val model = OBJLoader.loadFromOBJFile("res/model/model.obj")
}
