package xyz.przemyk.voxelgame.window;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.system.MemoryStack;
import xyz.przemyk.voxelgame.ResourceUtils;
import xyz.przemyk.voxelgame.VoxelGame;
import xyz.przemyk.voxelgame.world.PlayerCamera;

import java.awt.*;
import java.io.IOException;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL30C.glBindFragDataLocation;

public class Renderer {

    private boolean updateCamera;
    private boolean updateWireframe;
    private boolean updateChunk;

    private boolean wireframe;
    private int chunkVertexCount = 0;

    private int shaderProgram;

    private int VBO;

    public final Window window;
    private final ChunkVerticesGenerator chunkVerticesGenerator;
    private final TextureAtlas textureAtlas;

    public Renderer(Window window) {
        this.window = window;
        this.chunkVerticesGenerator = new ChunkVerticesGenerator();
        this.textureAtlas = new TextureAtlas();
    }

    public synchronized void updateCamera() {
        updateCamera = true;
    }

    public synchronized void updateWireframe() {
        updateWireframe = true;
    }

    public synchronized void updateChunk() {
        updateChunk = true;
    }

    void init() {
        updateChunk();

        glClearColor(0.3f, 0.3f, 0.3f, 1);
        glEnable(GL_DEPTH_TEST);

        glEnable(GL_CULL_FACE);
        glFrontFace(GL_CW);

        // init buffers
        try (MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer VAOBuffer = stack.mallocInt(1);
            glGenVertexArrays(VAOBuffer);
            int VAO = VAOBuffer.get(0);
            glBindVertexArray(VAO);

            IntBuffer VBOBuffer = stack.mallocInt(1);
            glGenBuffers(VBOBuffer);
            VBO = VBOBuffer.get(0);
            glBindBuffer(GL_ARRAY_BUFFER, VBO);
        }

        // init shaders
        try {
            // shader program for blocks
            String vertexSource = ResourceUtils.loadStringResource("./shaders/vertex.glsl");
            String fragmentSource = ResourceUtils.loadStringResource("./shaders/fragment.glsl");
            int vertexShader = createShader(GL_VERTEX_SHADER, vertexSource);
            int fragmentShader = createShader(GL_FRAGMENT_SHADER, fragmentSource);

            shaderProgram = glCreateProgram();
            glAttachShader(shaderProgram, vertexShader);
            glAttachShader(shaderProgram, fragmentShader);

            glBindFragDataLocation(shaderProgram, 0, "pixelColor");

            glLinkProgram(shaderProgram);
            glUseProgram(shaderProgram);

            int posAttrib = glGetAttribLocation(shaderProgram, "position");
            glVertexAttribPointer(posAttrib, 3, GL_FLOAT, false, 5 * Float.BYTES, 0);
            glEnableVertexAttribArray(posAttrib);

            int textureAttrib = glGetAttribLocation(shaderProgram, "texPosition");
            glVertexAttribPointer(textureAttrib, 2, GL_FLOAT, false, 5 * Float.BYTES, 3 * Float.BYTES);
            glEnableVertexAttribArray(textureAttrib);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            textureAtlas.init("./images/atlas.png");
        } catch (IOException e) {
            e.printStackTrace();
        }

        // init matrices
        {
            PlayerCamera playerCamera = VoxelGame.getInstance().getWorld().playerCamera;
            float[] modelMatrix = new Matrix4f().get(new float[16]);
            float[] viewMatrix = new Matrix4f().lookAt(playerCamera.position, new Vector3f(playerCamera.position).add(playerCamera.front), playerCamera.up).get(new float[16]);

            Point windowSize = window.getWindowSize();
            float[] projectionMatrix = new Matrix4f().perspective((float) Math.toRadians(45.0f), (float) windowSize.x / (float) windowSize.y, 0.1f, 100.0f).get(new float[16]);

            int modelLocation = glGetUniformLocation(shaderProgram, "model");
            int viewLocation = glGetUniformLocation(shaderProgram, "view");
            int projectionLocation = glGetUniformLocation(shaderProgram, "projection");

            glUniformMatrix4fv(modelLocation, false, modelMatrix);
            glUniformMatrix4fv(viewLocation, false, viewMatrix);
            glUniformMatrix4fv(projectionLocation, false, projectionMatrix);
        }
    }

    private int createShader(int type, String src) {
        int shader = glCreateShader(type);
        glShaderSource(shader, src);
        glCompileShader(shader);

        // Print debug info
        System.out.println(glGetShaderInfoLog(shader));

        return shader;
    }

    public void display() {
        synchronized (this) {
            if (updateWireframe) {
                if (wireframe) {
                    glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
                    wireframe = false;
                } else {
                    glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
                    wireframe = true;
                }

                updateWireframe = false;
            }

            if (updateCamera) {
                updateCamera = false;

                PlayerCamera playerCamera = VoxelGame.getInstance().getWorld().playerCamera;
                int viewLocation = glGetUniformLocation(shaderProgram, "view");
                Matrix4f viewMatrix = new Matrix4f().lookAt(playerCamera.position, new Vector3f(playerCamera.position).add(playerCamera.front), playerCamera.up);
                glUniformMatrix4fv(viewLocation, false, viewMatrix.get(new float[16]));
            }

            if (updateChunk) {
                updateChunk = false;

                //todo
                float[] chunkVertices = chunkVerticesGenerator.getVertices(VoxelGame.getInstance().getWorld().chunk, textureAtlas);

                glBufferData(GL_ARRAY_BUFFER, chunkVertices, GL_DYNAMIC_DRAW);

                chunkVertexCount = chunkVertices.length / 5;
            }
        }

        glDrawArrays(GL_TRIANGLES, 0, chunkVertexCount);
    }

    public void reshape(@SuppressWarnings("unused") long window, int width, int height) {
        float[] projectionMatrix = new Matrix4f().perspective((float) Math.toRadians(45.0f), (float) width / height, 0.1f, 100.0f).get(new float[16]);
        int projectionLocation = glGetUniformLocation(shaderProgram, "projection");
        glUniformMatrix4fv(projectionLocation, false, projectionMatrix);

        glViewport(0, 0, width, height);
    }
}