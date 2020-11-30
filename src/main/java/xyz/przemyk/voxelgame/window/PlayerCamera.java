package xyz.przemyk.voxelgame.window;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import xyz.przemyk.voxelgame.world.PlayerEntity;

import static org.lwjgl.opengl.GL20.*;

public class PlayerCamera {
    private final PlayerEntity playerEntity;
    private final Renderer renderer;

    public PlayerCamera(PlayerEntity playerEntity, Renderer renderer) {
        this.playerEntity = playerEntity;
        this.renderer = renderer;
    }

    public void update(float partialTicks) {
        if (playerEntity.velocity.lengthSquared() > 0) {
            Vector3f nextPosition = new Vector3f(playerEntity.position).add(playerEntity.velocity.x * partialTicks, playerEntity.velocity.y * partialTicks + 1.6f, playerEntity.velocity.z * partialTicks);

            updateViewMatrix(renderer.getShaderProgram(), nextPosition);
        }
    }

    public void rotate(double x, double y) {
        playerEntity.front.rotateY((float) (-x / 1000));
        playerEntity.front.rotateAxis((float) (-y / 1000), playerEntity.right.x, playerEntity.right.y, playerEntity.right.z);
        playerEntity.up.cross(playerEntity.front, playerEntity.right).normalize().negate();

        updateViewMatrix(renderer.getShaderProgram(), new Vector3f(playerEntity.position).add(0, 1.6f, 0));
    }

    private void updateViewMatrix(int shaderProgram, Vector3f position) {
        int viewLocation = glGetUniformLocation(shaderProgram, "view");
        glUseProgram(shaderProgram);
        Matrix4f viewMatrix = new Matrix4f().lookAt(position, new Vector3f(position).add(playerEntity.front), playerEntity.up);
        glUniformMatrix4fv(viewLocation, false, viewMatrix.get(new float[16]));
    }
}
