import org.joml.Math.*
import org.joml.Matrix4f
import org.joml.Vector3f
import org.lwjgl.*
import org.lwjgl.glfw.*

import org.lwjgl.glfw.Callbacks.*
import org.lwjgl.glfw.GLFW.*
import org.lwjgl.opengl.GL
import org.lwjgl.opengl.GL11.*
import org.lwjgl.system.MemoryStack.*
import org.lwjgl.system.MemoryUtil.*

import InputCallbacks.keyCallback
import InputCallbacks.mouseCallback
import InputCallbacks.isKeyHolding
import InputCallbacks.pitch
import InputCallbacks.yaw


class Game {

    private var window: Long = 0L

    private val viewPos = Vector3f(0f, 0f, 15f)
    private val moveSpeed = 5f
    private val moveVectors = arrayOf(
        arrayOf(Vector3f(-1f, 0f, 0f), Vector3f(1f, 0f, 0f)), // x
        arrayOf(Vector3f(0f, -1f, 0f), Vector3f(0f, 1f, 0f)), // y
        arrayOf(Vector3f(0f, 0f, -1f), Vector3f(0f, 0f, 1f))  // z
    )

    fun run() {
        println("LWJGL version ${Version.getVersion()}")

        init()
        loop()

        // After finishing loop
        // Remove all windows & cb
        glfwFreeCallbacks(window)
        glfwDestroyWindow(window)

        // Terminating GLFW & cleaning error cb
        glfwTerminate()
        glfwSetErrorCallback(null)?.free()
    }

    fun updateMoveVectors() {
        val yawRad = toRadians(yaw)
        val pitchRad = toRadians(pitch)

        val forward = Vector3f(
            sin(yawRad),
            0f,
            -cos(yawRad)
        ).normalize()

        val right = Vector3f(
            cos(yawRad),
            0f,
            sin(yawRad)
        ).normalize()

        val up = Vector3f(0f, 1f, 0f)

        moveVectors[0][0] = right
        moveVectors[0][1] = Vector3f(right).negate()
        moveVectors[1][0] = up
        moveVectors[1][1] = Vector3f(up).negate()
        moveVectors[2][0] = forward
        moveVectors[2][1] = Vector3f(forward).negate()
    }

    private fun init() {
        // Set error callback to stream System.err
        GLFWErrorCallback.createPrint(System.err).set()

        // GLFW initialization
        if (!glfwInit())
            throw IllegalStateException("Unable to init GLFW")

        // Default window, invisible and resizable
        glfwDefaultWindowHints()
        glfwWindowHint(GLFW_VISIBLE, GLFW_FALSE)
        glfwWindowHint(GLFW_RESIZABLE, GLFW_TRUE)

        // Window creation
        window = glfwCreateWindow(800, 600, "Game", NULL, NULL)
        if (window == NULL)
            throw RuntimeException("Failed to create GLFW window")

        // Set cb for keys
        glfwSetKeyCallback(window, ::keyCallback)
        glfwSetCursorPosCallback(window) { window, xPos, yPos ->
            mouseCallback(window, xPos, yPos)
            updateMoveVectors()
        }

        // Set window position
        // also stackPush() pushes new frame
        stackPush().use {
            val pWidth = it.mallocInt(1)
            val pHeight = it.mallocInt(1)

            glfwGetWindowSize(window, pWidth, pHeight)

            val vidMode = glfwGetVideoMode(glfwGetPrimaryMonitor())
                ?: throw RuntimeException("Failed to get video mode of monitor")
            glfwSetWindowPos(
                window,
                (vidMode.width()-pWidth.get(0))/2,
                (vidMode.height()-pHeight.get(0))/2
            )
        }

        // Make OpenGL as window context
        glfwMakeContextCurrent(window)
        // vsync
        glfwSwapInterval(1)
        // Show window, because hint was "invisible"
        glfwShowWindow(window)

        glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED)
        updateMoveVectors()
    }

    private fun updateMovement(deltaTime: Float) {
        val speed = moveSpeed * deltaTime

        if (isKeyHolding(GLFW_KEY_W)) viewPos.fma(speed, moveVectors[2][0])
        if (isKeyHolding(GLFW_KEY_S)) viewPos.fma(speed, moveVectors[2][1])
        if (isKeyHolding(GLFW_KEY_D)) viewPos.fma(speed, moveVectors[0][0])
        if (isKeyHolding(GLFW_KEY_A)) viewPos.fma(speed, moveVectors[0][1])
        if (isKeyHolding(GLFW_KEY_E)) viewPos.fma(speed, moveVectors[1][0])
        if (isKeyHolding(GLFW_KEY_Q)) viewPos.fma(speed, moveVectors[1][1])
    }

    private fun loop() {
        // Super important for bindings of OpenGL
        GL.createCapabilities()

        glEnable(GL_DEPTH_TEST)
        glEnable(GL_CULL_FACE)
        glCullFace(GL_BACK)

        val projection = Matrix4f().perspective(
            toRadians(60f),
            800f/600f,
            0.1f, 100f
        )
        val fb = BufferUtils.createFloatBuffer(16)
        projection.get(fb)

        // Clear color is black
        glClearColor(0.0f, 0.0f, 0.0f, 0.0f)

        var lastTime = glfwGetTime()

        val cubes = mutableListOf<Cube>()
        for (i in 0..5) {
            for (j in 0..5) {
                cubes.add(Cube(Vector3f(i.toFloat()*2, 0f, j.toFloat()*2)))
            }
        }

        while (!glfwWindowShouldClose(window)) {
            val currentTime = glfwGetTime()
            val deltaTime = (currentTime - lastTime).toFloat()
            lastTime = currentTime

            updateMovement(deltaTime)

            // Clearing buffer
            glClear(GL_COLOR_BUFFER_BIT or GL_DEPTH_BUFFER_BIT)

            glMatrixMode(GL_PROJECTION)
            glLoadMatrixf(fb)
            glMatrixMode(GL_MODELVIEW)
            glLoadIdentity()
            glRotatef(pitch, 1f, 0f, 0f)
            glRotatef(yaw, 0f, 1f, 0f)
            glTranslatef(-viewPos.x, -viewPos.y, -viewPos.z)

            cubes.forEach {
                it.draw()
            }

            // Swap color buffers
            glfwSwapBuffers(window)

            glfwPollEvents()
        }
    }
}

fun main(args: Array<String>) {
    Game().run()
}
