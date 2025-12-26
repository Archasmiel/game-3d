package loader

import geometry.Face
import geometry.IndexedFace
import geometry.IndexedModel
import geometry.Model
import geometry.VBOModel
import org.joml.Vector3f
import java.io.File

class QuadModelLoader {

    companion object {
        val v3fRegex = Regex("""[cv]3f\(([-0-9.]+)f?,\s*([-0-9.]+)f?,\s*([-0-9.]+)f?\)""")
    }

    fun normalModel(fileName: String): Model =
         Model(File(fileName).useLines { lines ->
             lines.filterNot(String::isBlank)
                 .chunked(5)
                 .mapNotNull { chunk ->
                     if (chunk.size != 5) return@mapNotNull null

                     val vectors = chunk.map { line ->
                         v3fRegex.matchEntire(line)?.destructured?.let { (x, y, z) ->
                             Vector3f(x.toFloat(), y.toFloat(), z.toFloat())
                         } ?: return@mapNotNull null
                     }

                     Face(vectors.first(), vectors.drop(1))
                 }
                 .toList()
         })

    fun indexedModel(fileName: String): IndexedModel {
        val normalModel = normalModel(fileName)

        val uniqueVerts = ArrayList<Vector3f>()
        val vertMap = HashMap<Vector3f, Int>()  // maps vertex -> index
        val indexedFaces = ArrayList<IndexedFace>(normalModel.faces.size)

        normalModel.faces.forEach { face ->
            val indices = IntArray(face.vs.size) { i ->
                vertMap.getOrPut(face.vs[i]) {
                    val idx = uniqueVerts.size
                    uniqueVerts.add(face.vs[i])
                    idx
                }
            }
            indexedFaces.add(IndexedFace(face.c, indices.toList()))
        }

        return IndexedModel(uniqueVerts, indexedFaces)
    }

    fun vboModel(fileName: String): VBOModel =
        VBOModel(indexedModel(fileName))
}