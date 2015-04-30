package registry

import render.{Model, OBJLoader}

object ModelRegistry {
    private var models: Map[String, Model] = Map()

    def loadModel(pathToObj: String, modelName: String): Unit = {
        models += (modelName -> OBJLoader.loadFromOBJFile(pathToObj))
    }

    def getModel(modelName: String): Model = {
        models(modelName).copy
    }
//    val cube = OBJLoader.loadFromOBJFile("res/model/cube.obj")
//    val model = OBJLoader.loadFromOBJFile("res/model/model.obj")
}
