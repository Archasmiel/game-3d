package geometry

import org.joml.Vector3f
import org.lwjgl.opengl.GL11.*

data class Face(val c: Vector3f, val vs: List<Vector3f>)

data class Model(val faces: List<Face>) {

    fun draw(pos: Vector3f, rot: Vector3f) {
        glPushMatrix()

        glTranslatef(pos.x, pos.y, pos.z)
        glRotatef(rot.x, 1f, 0f, 0f)
        glRotatef(rot.y, 0f, 1f, 0f)
        glRotatef(rot.z, 0f, 0f, 1f)

        glBegin(GL_QUADS)

        faces.forEach { face ->
            glColor3f(face.c.x, face.c.y, face.c.z)
            face.vs.forEach { i ->
                glVertex3f(i.x, i.y, i.z)
            }
        }

        glEnd()
        glPopMatrix()
    }
}