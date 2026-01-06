import loader.QbinLoader
import loader.QuadModelLoader
import org.joml.Math
import org.joml.Vector3f

class ModelObject {

    companion object {
        val fileNameTxt = "./models/octosphere_quads_1536.txt"
        val modelNormal = QuadModelLoader.normalModel(fileNameTxt)
        val modelIndexed = QuadModelLoader.indexedModel(fileNameTxt)
        val modelVBO = QuadModelLoader.vboModel(fileNameTxt)

        val fileNameQbin = "./models/octosphere_quads_1536.qbin"
        val binModelNormal = QbinLoader.normalModel(fileNameQbin)

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
        binModelNormal.draw(pos, rot)
    }

}