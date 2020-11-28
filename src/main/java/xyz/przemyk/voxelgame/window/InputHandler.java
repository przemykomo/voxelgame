package xyz.przemyk.voxelgame.window;

import xyz.przemyk.voxelgame.VoxelGame;
import xyz.przemyk.voxelgame.world.Blocks;
import xyz.przemyk.voxelgame.world.Direction;
import xyz.przemyk.voxelgame.world.World;

import java.awt.*;

import static org.lwjgl.glfw.GLFW.*;

public class InputHandler {

    private final Renderer renderer;

    public InputHandler(Renderer renderer) {
        this.renderer = renderer;
    }

    public void setup(long window) {
        glfwSetKeyCallback(window, this::keyCallback);
        glfwSetCursorPosCallback(window, this::mouseMovedCallback);
        glfwSetMouseButtonCallback(window, this::mouseButtonCallback);

        Point point = renderer.window.getWindowSize();
        lastXPos = (double) point.x / 2;
        lastYPos = (double) point.y / 2;
    }

    protected void keyCallback(long window, int key, int scancode, int action, int mods) {
        World world = VoxelGame.getInstance().getWorld();
        if (action == GLFW_PRESS) {
            switch (key) {
                case GLFW_KEY_W -> world.playerCamera.startMoving(Direction.FORWARD);
                case GLFW_KEY_S -> world.playerCamera.startMoving(Direction.BACKWARD);
                case GLFW_KEY_A -> world.playerCamera.startMoving(Direction.LEFT);
                case GLFW_KEY_D -> world.playerCamera.startMoving(Direction.RIGHT);
                case GLFW_KEY_SPACE -> world.playerCamera.startMoving(Direction.UP);
                case GLFW_KEY_LEFT_CONTROL -> world.playerCamera.startMoving(Direction.DOWN);
            }
        } else if (action == GLFW_RELEASE) {
            switch (key) {
                case GLFW_KEY_ESCAPE -> glfwSetWindowShouldClose(window, true);
                case GLFW_KEY_E -> renderer.updateWireframe();
                case GLFW_KEY_C -> glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_DISABLED);
                case GLFW_KEY_V -> glfwSetInputMode(window, GLFW_CURSOR, GLFW_CURSOR_NORMAL);
                case GLFW_KEY_Q -> {
                    if (world.chunk.getBlock(0, 0, 0) == Blocks.AIR) {
                        world.chunk.setBlock(Blocks.METEORITE, 0, 0, 0);
                    } else {
                        world.chunk.setBlock(Blocks.AIR, 0, 0, 0);
                    }
                }

                case GLFW_KEY_W -> world.playerCamera.stopMoving(Direction.FORWARD);
                case GLFW_KEY_S -> world.playerCamera.stopMoving(Direction.BACKWARD);
                case GLFW_KEY_A -> world.playerCamera.stopMoving(Direction.LEFT);
                case GLFW_KEY_D -> world.playerCamera.stopMoving(Direction.RIGHT);
                case GLFW_KEY_SPACE -> world.playerCamera.stopMoving(Direction.UP);
                case GLFW_KEY_LEFT_CONTROL -> world.playerCamera.stopMoving(Direction.DOWN);
            }
        }
    }

    protected double lastXPos, lastYPos;
    protected void mouseMovedCallback(long window, double xPos, double yPos) {
        double xOffset = xPos - lastXPos;
        double yOffset = yPos - lastYPos;
        lastXPos = xPos;
        lastYPos = yPos;

        if (xOffset != 0 || yOffset != 0) {
            VoxelGame.getInstance().getWorld().playerCamera.rotate(xOffset, yOffset);
        }
    }

    protected void mouseButtonCallback(long window, int button, int action, int mods) {
        if (action == GLFW_PRESS) {
            switch (button) {
                case GLFW_MOUSE_BUTTON_LEFT -> VoxelGame.getInstance().getWorld().playerCamera.breakBlock();
                case GLFW_MOUSE_BUTTON_RIGHT -> VoxelGame.getInstance().getWorld().playerCamera.placeBlock(Blocks.BLOCK);
            }
        }
    }
}
