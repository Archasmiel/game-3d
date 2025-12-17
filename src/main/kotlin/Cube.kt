import org.joml.Vector3f
import org.lwjgl.opengl.GL11.*

class Cube {

    var pos = Vector3f()
        private set

    constructor(pos: Vector3f) {
        this.pos = pos
    }

    fun draw() {
        glPushMatrix()
        glTranslatef(pos.x, pos.y, pos.z)

        glBegin(GL_QUADS)

        // Front face (red)
        glColor3f(1f, 0f, 0f)
        glVertex3f(-0.5f, -0.5f, 0.5f)
        glVertex3f(0.5f, -0.5f, 0.5f)
        glVertex3f(0.5f, 0.5f, 0.5f)
        glVertex3f(-0.5f, 0.5f, 0.5f)

        // Back face (green)
        glColor3f(0f, 1f, 0f)
        glVertex3f(-0.5f, -0.5f, -0.5f)
        glVertex3f(-0.5f, 0.5f, -0.5f)
        glVertex3f(0.5f, 0.5f, -0.5f)
        glVertex3f(0.5f, -0.5f, -0.5f)

        // Left face (blue)
        glColor3f(0f, 0f, 1f)
        glVertex3f(-0.5f, -0.5f, -0.5f)
        glVertex3f(-0.5f, -0.5f, 0.5f)
        glVertex3f(-0.5f, 0.5f, 0.5f)
        glVertex3f(-0.5f, 0.5f, -0.5f)

        // Right face (blue)
        glColor3f(0f, 0f, 1f)
        glVertex3f(0.5f, -0.5f, -0.5f)
        glVertex3f(0.5f, 0.5f, -0.5f)
        glVertex3f(0.5f, 0.5f, 0.5f)
        glVertex3f(0.5f, -0.5f, 0.5f)

        glColor3f(1f, 1f, 0f)
        glVertex3f(-0.5f, 0.5f, 0.5f)
        glVertex3f(0.5f, 0.5f, 0.5f)
        glVertex3f(0.5f, 0.5f, -0.5f)
        glVertex3f(-0.5f, 0.5f, -0.5f)

        glColor3f(0f, 1f, 1f)
        glVertex3f(-0.5f, -0.5f, 0.5f)
        glVertex3f(-0.5f, -0.5f, -0.5f)
        glVertex3f(0.5f, -0.5f, -0.5f)
        glVertex3f(0.5f, -0.5f, 0.5f)

        glEnd()
        glPopMatrix()
    }

}