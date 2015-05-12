package core

import org.lwjgl.util.vector.Vector3f

object Debug {

    def printVec(vector: Vector3f): Unit = {
        printVec(vector.getX, vector.getY, vector.getZ)
    }

    def printVec(x: Float, y: Float, z: Float): Unit = {
        if(HyperScape.debug)
            print("(" + x + ", " + y + ", " + z + ")")
    }

    def printlnVec(vector: Vector3f): Unit = {
        printlnVec(vector.getX, vector.getY, vector.getZ)
    }

    def printlnVec(x: Float, y: Float, z: Float): Unit = {
        if(HyperScape.debug)
            println("(" + x + ", " + y + ", " + z + ")")
    }

    def debugPrint(o: Any): Unit = {
        if(HyperScape.debug)
            print(o)
    }

    def debugPrintln(o: Any): Unit = {
        if(HyperScape.debug)
            println(o)
    }
}
