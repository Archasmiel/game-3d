import org.lwjgl.glfw.GLFW.*

object InputCallbacks {

    val keysPressed = mutableSetOf<Int>()
    var lastMouseX = 400.0
        private set
    var lastMouseY = 300.0
        private set
    var yaw = 0f
        private set
    var pitch = 0f
        private set
    var mouseSensitivity = 0.1f

    fun init() {

    }

    fun keyCallback(window: Long, key: Int, scancode: Int, action: Int, mods: Int) {
        if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
            glfwSetWindowShouldClose(window, true)
            return
        }

        when (action) {
            GLFW_PRESS -> keysPressed.add(key)
            GLFW_RELEASE -> keysPressed.remove(key)
        }
    }

    fun mouseCallback(window: Long, xPos: Double, yPos: Double) {
        val xOffset = (xPos - lastMouseX).toFloat() * mouseSensitivity
        val yOffset = (yPos - lastMouseY).toFloat() * mouseSensitivity
        lastMouseX = xPos
        lastMouseY = yPos

        yaw += xOffset
        pitch += yOffset
        pitch = pitch.coerceIn(-90f, 90f)
    }

    fun isKeyHolding(key: Int): Boolean {
        return key in keysPressed
    }

}