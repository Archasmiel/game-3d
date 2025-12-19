package geometry

import org.joml.Vector3f
import org.lwjgl.opengl.GL11.*

data class IndexedFace(val c: Color3f, val vIs: List<Int>)

data class IndexedModel(val vs: List<Vertex3f>, val faces: List<IndexedFace>) {

    fun draw(pos: Vector3f, rot: Vector3f) {
        glPushMatrix()

        glTranslatef(pos.x, pos.y, pos.z)
        glRotatef(rot.x, 1f, 0f, 0f)
        glRotatef(rot.y, 0f, 1f, 0f)
        glRotatef(rot.z, 0f, 0f, 1f)

        glBegin(GL_QUADS)

        faces.forEach { face ->
            glColor3f(face.c.r, face.c.g, face.c.b)
            face.vIs.forEach { i ->
                glVertex3f(vs[i].x, vs[i].y, vs[i].z)
            }
        }

        glEnd()
        glPopMatrix()
    }
}