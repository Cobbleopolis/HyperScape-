package core

import org.lwjgl.opengl.GL11


class HyperScape {

    def init(): Unit = {

    }

    def tick(): Unit = {
        GL11.glClearColor(Math.random().toFloat, Math.random().toFloat, Math.random().toFloat, Math.random().toFloat)
    }

    def render(): Unit = {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT)
    }

    def destroy(): Unit = {

    }

}
