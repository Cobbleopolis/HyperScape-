package registry

import render.{Model, OBJLoader}

object ModelRegistry {
    private var models: Map[String, Model] = Map()

    /**
     * Loads a model into the game
     * @param pathToObj Path to the OBJ folder
     * @param modelName Name of the model to store it with
     */
    def loadModel(pathToObj: String, modelName: String): Unit = {
        models += (modelName -> OBJLoader.loadFromOBJFile(pathToObj))
    }

    /**
     * Retrieves a model from the model registry
     * @param modelName Name of the model to return
     * @return The model with the corresponding name
     */
    def getModel(modelName: String): Model = {
        models(modelName).copy
    }
//    val cube = OBJLoader.loadFromOBJFile("res/model/cube.obj")
//    val model = OBJLoader.loadFromOBJFile("res/model/model.obj")
}
