package xyz.przemyk.voxelgame.window;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import xyz.przemyk.voxelgame.ResourceUtils;
import xyz.przemyk.voxelgame.VoxelGame;
import xyz.przemyk.voxelgame.world.Chunk;
import xyz.przemyk.voxelgame.world.PlayerEntity;
import xyz.przemyk.voxelgame.world.World;

import java.awt.*;
import java.io.IOException;

import static org.lwjgl.opengl.ARBVertexArrayObject.glBindVertexArray;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;
import static org.lwjgl.opengl.GL30C.glBindFragDataLocation;

public class Renderer {

    private boolean updateWireframe;
    private boolean updateChunk;

    private boolean wireframe;
    private int chunkVertexCount = 0;

    private int VAO;
    private int VBO;
    private int shaderProgram;

    private int GuiVAO;
    private int GuiVBO;
    private int guiShaderProgram;

    public final Window window;
    private final ChunkVerticesGenerator chunkVerticesGenerator;
    private final TextureAtlas textureAtlas;
    private PlayerCamera playerCamera;

    public Renderer(Window window) {
        this.window = window;
        this.chunkVerticesGenerator = new ChunkVerticesGenerator();
        this.textureAtlas = new TextureAtlas();
    }

    public synchronized void updateWireframe() {
        updateWireframe = true;
    }

    public synchronized void updateChunk() {
        updateChunk = true;
    }

    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    void init() {
        playerCamera = new PlayerCamera(VoxelGame.getInstance().getWorld().playerEntity, this);
        updateChunk();

        glClearColor(0.3f, 0.3f, 0.3f, 1);
        glEnable(GL_DEPTH_TEST);

        glEnable(GL_CULL_FACE);
        glFrontFace(GL_CW);

        // init buffers
        {
            // First (working) program:
            VAO = glGenVertexArrays();
            glBindVertexArray(VAO);

            VBO = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, VBO);

            // Gui program:
            GuiVAO = glGenVertexArrays();
            glBindVertexArray(GuiVAO);

            GuiVBO = glGenBuffers();
            glBindBuffer(GL_ARRAY_BUFFER, GuiVBO);
            glBufferData(GL_ARRAY_BUFFER, new float[] {
                    -0.05f, +0.05f, 0.0f, 0.5f,
                    +0.05f, +0.05f, 0.15625f, 0.5f,
                    -0.05f, -0.05f, 0.0f, 0.65625f,
                    -0.05f, -0.05f, 0.0f, 0.65625f,
                    +0.05f, +0.05f, 0.15625f, 0.5f,
                    +0.05f, -0.05f, 0.15625f, 0.65625f
            }, GL_STATIC_DRAW);
        }

        // init shaders
        try {
            glBindVertexArray(VAO);
            glBindBuffer(GL_ARRAY_BUFFER, VBO);
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


            glBindVertexArray(GuiVAO);
            glBindBuffer(GL_ARRAY_BUFFER, GuiVBO);
            String guiVertexSource = ResourceUtils.loadStringResource("./shaders/guiVertex.glsl");
            int guiVertexShader = createShader(GL_VERTEX_SHADER, guiVertexSource);

            guiShaderProgram = glCreateProgram();
            glAttachShader(guiShaderProgram, guiVertexShader);
            glAttachShader(guiShaderProgram, fragmentShader);

            glBindFragDataLocation(guiShaderProgram, 0, "pixelColor");

            glLinkProgram(guiShaderProgram);
            glUseProgram(guiShaderProgram);

            int guiPosAttrib = glGetAttribLocation(guiShaderProgram, "position");
            glVertexAttribPointer(guiPosAttrib, 2, GL_FLOAT, false, 4 * Float.BYTES, 0);
            glEnableVertexAttribArray(guiPosAttrib);

            int guiTextureAttrib = glGetAttribLocation(guiShaderProgram, "texPosition");
            glVertexAttribPointer(guiTextureAttrib, 2, GL_FLOAT, false, 4 * Float.BYTES, 2 * Float.BYTES);
            glEnableVertexAttribArray(guiTextureAttrib);

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
            PlayerEntity playerEntityCamera = VoxelGame.getInstance().getWorld().playerEntity;
            float[] modelMatrix = new Matrix4f().get(new float[16]);


            int modelLocation = glGetUniformLocation(shaderProgram, "model");
            int viewLocation = glGetUniformLocation(shaderProgram, "view");

            glUseProgram(shaderProgram);
            glUniformMatrix4fv(modelLocation, false, modelMatrix);

            synchronized (playerEntityCamera) {
                float[] viewMatrix = new Matrix4f().lookAt(playerEntityCamera.position, new Vector3f(playerEntityCamera.position).add(playerEntityCamera.front), playerEntityCamera.up).get(new float[16]);
                glUniformMatrix4fv(viewLocation, false, viewMatrix);
            }

            Point windowSize = window.getWindowSize();
            reshape(window.getWindowPointer(), windowSize.x, windowSize.y);
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

    private int renderTicksSinceLastWorldTick = 0;
    private int renderTicksPerWordTick = 3;
    private int lastWorldTick = 0;
    private float partialTicks = 0.0f;

    @SuppressWarnings("SynchronizationOnLocalVariableOrMethodParameter")
    public void display() {
        World world = VoxelGame.getInstance().getWorld();
        int worldTicks = world.ticks.get();
        if (lastWorldTick < worldTicks) {
            lastWorldTick = worldTicks;
            partialTicks = 0.0f;
            renderTicksPerWordTick = renderTicksSinceLastWorldTick;
            renderTicksSinceLastWorldTick = 0;
        }
        partialTicks += (float) renderTicksSinceLastWorldTick / renderTicksPerWordTick;
        playerCamera.update(partialTicks);

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

            if (updateChunk) {
                updateChunk = false;

                //todo
                Chunk chunk = world.chunk;

                glBindVertexArray(VAO);
                glBindBuffer(GL_ARRAY_BUFFER, VBO);
                glUseProgram(shaderProgram);
                synchronized (chunk) {
                    float[] chunkVertices = chunkVerticesGenerator.getVertices(chunk, textureAtlas);
                    glBufferData(GL_ARRAY_BUFFER, chunkVertices, GL_DYNAMIC_DRAW);
                    chunkVertexCount = chunkVertices.length / 5;
                }
            }
        }

        glBindVertexArray(VAO);
        glBindBuffer(GL_ARRAY_BUFFER, VBO);
        glUseProgram(shaderProgram);
        glDrawArrays(GL_TRIANGLES, 0, chunkVertexCount);

        glBindVertexArray(GuiVAO);
        glBindBuffer(GL_ARRAY_BUFFER, GuiVBO);
        glUseProgram(guiShaderProgram);
        glDrawArrays(GL_TRIANGLES, 0, 6);

        ++renderTicksSinceLastWorldTick;
    }

    public void reshape(@SuppressWarnings("unused") long window, int width, int height) {
        float aspectRatio = (float) width / height;
        float[] projectionMatrix = new Matrix4f().perspective((float) Math.toRadians(45.0f), aspectRatio, 0.1f, 100.0f).get(new float[16]);
        int projectionLocation = glGetUniformLocation(shaderProgram, "projection");
        glUseProgram(shaderProgram);
        glUniformMatrix4fv(projectionLocation, false, projectionMatrix);

        int aspectRatioLocation = glGetUniformLocation(guiShaderProgram, "aspectRatio");
        glUseProgram(guiShaderProgram);
        glUniform1f(aspectRatioLocation, aspectRatio);

        glViewport(0, 0, width, height);
    }

    public int getShaderProgram() {
        return shaderProgram;
    }

    public PlayerCamera getCamera() {
        return playerCamera;
    }
}
