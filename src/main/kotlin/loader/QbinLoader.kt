package loader

import geometry.Face
import geometry.Model
import org.joml.Vector3f
import java.io.ByteArrayInputStream
import java.io.File
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.zip.InflaterInputStream

object QbinLoader {

    const val FLOATS_PER_RECORD = 15
    const val RECORD_SIZE = FLOATS_PER_RECORD * Float.SIZE_BYTES

    fun zlibDecompress(input: ByteArray): ByteArray {
        InflaterInputStream(ByteArrayInputStream(input)).use { zis ->
            return zis.readBytes()
        }
    }

    fun normalModel(fileName: String): Model {
        val compressed = File(fileName).readBytes()
        val bytes = zlibDecompress(compressed)

        if ((bytes.size - Int.SIZE_BYTES) % RECORD_SIZE != 0) {
            error("File size is not aligned to record size [4 bytes + ($RECORD_SIZE bytes) * faces]")
        }

        val buffer = ByteBuffer.wrap(bytes).order(ByteOrder.LITTLE_ENDIAN)

        val facesCount = buffer.int
        val faces = ArrayList<Face>(facesCount)

        repeat(facesCount) {
            if (buffer.remaining() < RECORD_SIZE) {
                error("File $fileName corrupted: face count is smaller than expected")
            }

            val values = FloatArray(FLOATS_PER_RECORD) { buffer.float }

            val color = Vector3f(values[0], values[1], values[2])
            val vertices = values.drop(3).chunked(3)
                .map { Vector3f(it[0], it[1], it[2]) }

            faces.add(Face(color, vertices))
        }

        return Model(faces = faces)
    }
}