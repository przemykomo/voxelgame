package xyz.przemyk.voxelgame;

import xyz.przemyk.voxelgame.window.Renderer;
import xyz.przemyk.voxelgame.window.Window;
import xyz.przemyk.voxelgame.world.World;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.opengl.GL11.*;

public class VoxelGame {

    private static VoxelGame voxelGame;

    public static VoxelGame getInstance() {
        return voxelGame;
    }

    private final Window window;
    private final World world;

    private VoxelGame() {
        window = new Window();
        world = new World();
    }

    public World getWorld() {
        return world;
    }

    public Window getWindow() {
        return window;
    }

    private void run() {
        window.setup(640, 480, "DevWindow");

        long windowPointer = window.getWindowPointer();
        Renderer renderer = window.getRenderer();

        Thread worldThread = new Thread(world);
        worldThread.start();

        while (!glfwWindowShouldClose(windowPointer)) {
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);

            renderer.display();

            glfwSwapBuffers(windowPointer);
            glfwPollEvents();
        }

        world.stop();
        try {
            worldThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        glfwTerminate();
    }

    public static void main(String[] args) {
        voxelGame = new VoxelGame();
        voxelGame.run();
    }
}
