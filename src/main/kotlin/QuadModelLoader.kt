import geometry.Color3f
import geometry.Vertex3f
import java.io.File
import geometry.Model
import geometry.Face
import geometry.IndexedFace
import geometry.IndexedModel
import geometry.VBOModel


class QuadModelLoader {

    val colorRegex = Regex("""c3f\(([-0-9.]+)f?,\s*([-0-9.]+)f?,\s*([-0-9.]+)f?\)""")
    val vertexRegex = Regex("""v3f\(([-0-9.]+)f?,\s*([-0-9.]+)f?,\s*([-0-9.]+)f?\)""")

    fun readNormalModel(fileName: String): Model {
        val lines = File(fileName).readLines()

        var currentC: Color3f? = null
        val currentV = mutableListOf<Vertex3f>()
        val faces = mutableListOf<Face>()

        for (line in lines) {
            when {
                line.startsWith("c3f") -> {
                    val match = colorRegex.matchEntire(line) ?: continue
                    val (r, g, b) = match.destructured
                    currentC = Color3f(r.toFloat(), g.toFloat(), b.toFloat())
                    currentV.clear()
                }
                line.startsWith("v3f") -> {
                    val match = vertexRegex.matchEntire(line) ?: continue
                    val (x, y, z) = match.destructured
                    val v = Vertex3f(x.toFloat(), y.toFloat(), z.toFloat())
                    currentV.add(v)
                }
            }

            if (currentC != null && currentV.size == 4) {
                faces.add(Face(currentC, currentV.toList()))
                currentV.clear()
                currentC = null
            }
        }
        return Model(faces)
    }

    fun readIndexedModel(fileName: String): IndexedModel {
        val normalModel = readNormalModel(fileName)

        val uniqueVerts = mutableListOf<Vertex3f>()
        val vertMap = mutableMapOf<Vertex3f, Int>()  // maps vertex -> index
        val indexedFaces = mutableListOf<IndexedFace>()

        for (face in normalModel.faces) {
            val indices = face.vs.map { v ->
                vertMap.getOrPut(v) {
                    uniqueVerts.add(v)
                    uniqueVerts.size - 1
                }
            }
            indexedFaces.add(IndexedFace(face.c, indices))
        }

        return IndexedModel(uniqueVerts, indexedFaces)
    }

    fun readVboModel(fileName: String): VBOModel =
        VBOModel(readIndexedModel(fileName))
}

