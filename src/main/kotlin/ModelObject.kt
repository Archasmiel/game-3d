import org.joml.Math
import org.joml.Vector3f

class ModelObject {

    companion object {
        val fileName = "octosphere_quads_1536.txt"
        val modelNormal = Game.quadLoader.normalModel(fileName)
        val modelIndexed = Game.quadLoader.indexedModel(fileName)
        val modelVBO = Game.quadLoader.vboModel(fileName)

        init {
            println("${modelNormal.faces.size * 4} ${modelIndexed.vs.size}")
        }
    }

    var pos = Vector3f()
        private set
    var rot = Vector3f()
        private set

    constructor(pos: Vector3f) {
        this.pos = pos
        val rVal = Math.random().toFloat() * 360f
        this.rot = Vector3f(
            rVal - rVal % 90f,
            0f,
            0f
        )
    }

    fun draw() {
        modelVBO.draw(pos, rot)
    }

}