package geometry

import org.joml.Vector3f
import org.lwjgl.opengl.GL11.*
import org.lwjgl.opengl.GL15.*

class VBOModel(model: IndexedModel) {
    private var vboC = 0
    private var vboV = 0
    private var vNum = 0

    init {
        // [x y z r g b][x y z r g b][x y z r g b]...
        // real shit here...

        // Buffers
        val vBuf = FloatArray(model.faces.size * 4 * 3)
        val cBuf = FloatArray(model.faces.size * 4 * 3)

        var i = 0
        model.faces.forEach { face ->
            face.vIs.forEach { vi ->
                val v = model.vs[vi]

                vBuf[i]     = v.x
                vBuf[i + 1] = v.y
                vBuf[i + 2] = v.z

                cBuf[i]     = face.c.x
                cBuf[i + 1] = face.c.y
                cBuf[i + 2] = face.c.z

                i += 3
            }
        }

        vNum = model.faces.size * 4

        // VBO
        vboV = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vboV)
        glBufferData(GL_ARRAY_BUFFER, vBuf, GL_STATIC_DRAW)

        vboC = glGenBuffers()
        glBindBuffer(GL_ARRAY_BUFFER, vboC)
        glBufferData(GL_ARRAY_BUFFER, cBuf, GL_STATIC_DRAW)

        glBindBuffer(GL_ARRAY_BUFFER, 0)
    }

    fun destroy() {
        if (vboC != 0) glDeleteBuffers(vboC)
        if (vboV != 0) glDeleteBuffers(vboV)

        vboC = 0
        vboV = 0
    }

    fun draw(pos: Vector3f, rot: Vector3f) {
        if (vboC == 0 || vboV == 0 || vNum == 0)
            throw IllegalStateException("VBO model is missing!")

        glPushMatrix()

        glTranslatef(pos.x, pos.y, pos.z)
        glRotatef(rot.x, 1f, 0f, 0f)
        glRotatef(rot.y, 0f, 1f, 0f)
        glRotatef(rot.z, 0f, 0f, 1f)

        glEnableClientState(GL_VERTEX_ARRAY)
        glBindBuffer(GL_ARRAY_BUFFER, vboV)
        glVertexPointer(3, GL_FLOAT, 0, 0)

        glEnableClientState(GL_COLOR_ARRAY)
        glBindBuffer(GL_ARRAY_BUFFER, vboC)
        glColorPointer(3, GL_FLOAT, 0, 0)

        glDrawArrays(GL_QUADS, 0, vNum)

        glDisableClientState(GL_COLOR_ARRAY)
        glDisableClientState(GL_VERTEX_ARRAY)

        glPopMatrix()
    }
}