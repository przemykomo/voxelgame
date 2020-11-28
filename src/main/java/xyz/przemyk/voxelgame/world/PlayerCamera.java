package xyz.przemyk.voxelgame.world;

import org.joml.RoundingMode;
import org.joml.Vector3f;
import org.joml.Vector3i;
import xyz.przemyk.voxelgame.window.Renderer;

import java.util.HashMap;
import java.util.Map;

public class PlayerCamera {
    public static float speed = 0.03f;
    public static float strafeSpeed = 0.03f;

    public Vector3f position = new Vector3f(0.0f, 0.0f, 3.0f);
    public Vector3f front = new Vector3f(0.0f, 0.0f, -1.0f);
    public Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
    public Vector3f right = new Vector3f(1.0f, 0.0f, 0.0f);

    private final Map<Direction, Boolean> movement = new HashMap<>();

    private final Renderer renderer;
    private final World world;

    public PlayerCamera(Renderer renderer, World world) {
        this.renderer = renderer;
        this.world = world;

        for (Direction direction : Direction.values()) {
            movement.put(direction, false);
        }
    }

    public void startMoving(Direction direction) {
        movement.put(direction, true);
    }

    public void stopMoving(Direction direction) {
        movement.put(direction, false);
    }

    public void rotate(double x, double y) {
        front.rotateY((float) (-x / 1000));
        front.rotateAxis((float) (-y / 1000), right.x, right.y, right.z);
        up.cross(front, right).normalize().negate();
        renderer.updateCamera();
    }

    public void tick() {
        int deltaTime = 1;
        boolean movementOccurred = false;

        if (movement.get(Direction.FORWARD) && !movement.get(Direction.BACKWARD)) {
            position.add(new Vector3f(front.x, 0, front.z).normalize().mul(speed).mul(deltaTime));
            movementOccurred = true;
        }

        if (movement.get(Direction.BACKWARD) && !movement.get(Direction.FORWARD)) {
            position.sub(new Vector3f(front.x, 0, front.z).normalize().mul(speed).mul(deltaTime));
            movementOccurred = true;
        }

        if (movement.get(Direction.LEFT) && !movement.get(Direction.RIGHT)) {
            position.sub(new Vector3f(right).mul(strafeSpeed).mul(deltaTime));
            movementOccurred = true;
        }

        if (movement.get(Direction.RIGHT) && !movement.get(Direction.LEFT)) {
            position.add(new Vector3f(right).mul(strafeSpeed).mul(deltaTime));
            movementOccurred = true;
        }

        if (movement.get(Direction.UP) && !movement.get(Direction.DOWN)) {
            position.add(new Vector3f(up).mul(speed).mul(deltaTime));
            movementOccurred = true;
        }

        if (movement.get(Direction.DOWN) && !movement.get(Direction.UP)) {
            position.sub(new Vector3f(up).mul(speed).mul(deltaTime));
            movementOccurred = true;
        }

        if (movementOccurred) {
            renderer.updateCamera();
        }
    }

    public void breakBlock() {
        Vector3f raycastPos = new Vector3f(position);
        for (int i = 0; i < 5; ++i) {
            Vector3i blockpos = raycastPos.get(RoundingMode.FLOOR, new Vector3i());
            if (world.chunk.getBlock(blockpos) != Blocks.AIR) {
                world.chunk.setBlock(Blocks.AIR, blockpos);
                return;
            }
            raycastPos.add(front);
        }
    }

    public void placeBlock(short blockID) {
        Vector3f raycastPos = new Vector3f(position);
        for (int i = 0; i < 5; ++i) {
            Vector3i blockpos = raycastPos.get(RoundingMode.FLOOR, new Vector3i());
            if (world.chunk.getBlock(blockpos) != Blocks.AIR) {
                world.chunk.setBlock(blockID, raycastPos.sub(front).get(RoundingMode.FLOOR, new Vector3i()));
                return;
            }
            raycastPos.add(front);
        }
    }
}
