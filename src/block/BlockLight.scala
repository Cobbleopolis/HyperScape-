package block

import org.lwjgl.util.vector.Vector3f
import reference.BlockID

class BlockLight extends Block {
    blockID = BlockID.LIGHT
    lightLevel = 10
    lightPos = new Vector3f(0.5f, 0.5f, 0.5f)
    lightColor = new Vector3f(1, 1, 1)
    texCoord = (1, 0)
}
