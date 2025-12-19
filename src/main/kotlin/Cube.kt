import org.joml.Math
import org.joml.Vector3f

class Cube {

    companion object {
        val model = Game.quadLoader.readNormalModel("cube.quad")
        val modelI = Game.quadLoader.readIndexedModel("cube.quad")
        val modelVBO = Game.quadLoader.readVboModel("cube.quad")
    }

    var pos = Vector3f()
        private set
    var rot = Vector3f()
        private set

    constructor(pos: Vector3f) {
        this.pos = pos
        this.rot = Vector3f(
            Math.random().toFloat() * 360f,
            0f,
            0f
        )
    }

    fun draw() {
        modelVBO.draw(pos, rot)
    }

}