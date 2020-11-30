package xyz.przemyk.voxelgame.world;

import org.joml.RoundingMode;
import org.joml.Vector3f;
import org.joml.Vector3i;
import xyz.przemyk.voxelgame.window.Renderer;

import java.util.HashMap;
import java.util.Map;

public class PlayerEntity {

    public static final float gravityAcceleration = -0.01f;
    public static final float speed = 0.01f;
    public static final float strafeSpeed = 0.01f;

    public Vector3f position = new Vector3f(0.5f, 2.0f, 0.5f);

    public Vector3f front = new Vector3f(0.5f, -0.8f, -0.09f);
    public Vector3f up = new Vector3f(0.0f, 1.0f, 0.0f);
    public Vector3f right = new Vector3f(1.0f, 0.0f, 0.0f);
    public Vector3f velocity = new Vector3f();

    private final Map<Direction, Boolean> movement = new HashMap<>();

    public final World world;

    public PlayerEntity(World world) {
        this.world = world;

        for (Direction direction : Direction.values()) {
            movement.put(direction, false);
        }
    }

    public void startMoving(Direction direction) {
        synchronized (movement) {
            movement.put(direction, true);
        }
    }

    public void stopMoving(Direction direction) {
        synchronized (movement) {
            movement.put(direction, false);
        }
    }

    public boolean isOnGround() {
        return world.chunk.getBlock((int) position.x, (int) (position.y - 0.2f), (int) position.z) != Blocks.AIR;
    }

    public void tick() {
        float deltaTime = world.deltaTime.toMillis() / 20.0f;

        synchronized (movement) {
            if (movement.get(Direction.FORWARD) && !movement.get(Direction.BACKWARD)) {
                velocity.add(new Vector3f(front.x, 0, front.z).normalize().mul(speed).mul(deltaTime));
            }

            if (movement.get(Direction.BACKWARD) && !movement.get(Direction.FORWARD)) {
                velocity.sub(new Vector3f(front.x, 0, front.z).normalize().mul(speed).mul(deltaTime));
            }

            if (movement.get(Direction.LEFT) && !movement.get(Direction.RIGHT)) {
                velocity.sub(new Vector3f(right).mul(strafeSpeed).mul(deltaTime));
            }

            if (movement.get(Direction.RIGHT) && !movement.get(Direction.LEFT)) {
                velocity.add(new Vector3f(right).mul(strafeSpeed).mul(deltaTime));
            }

            if (movement.get(Direction.UP) && isOnGround()) {
                velocity.add(0, 0.08f, 0);
            }

            if (movement.get(Direction.DOWN) && !movement.get(Direction.UP)) {
                velocity.sub(new Vector3f(up).mul(speed).mul(deltaTime));
            }
        }

        velocity.add(0, gravityAcceleration, 0);
        velocity.mul(0.9f);
        move();
    }

    private void move() {
        if (velocity.lengthSquared() > 0) {
            Vector3f nextPosition = new Vector3f(position).add(velocity);
            if (world.chunk.getBlock(nextPosition.get(RoundingMode.FLOOR, new Vector3i())) == Blocks.AIR) {
                position = nextPosition;
            } else {
                velocity.set(0);
            }
        }
    }

    public synchronized void breakBlock() {
        Vector3f raycastPos = new Vector3f(position).add(0, 1.6f, 0);
        for (int i = 0; i < 5; ++i) {
            Vector3i blockpos = raycastPos.get(RoundingMode.FLOOR, new Vector3i());
            if (world.chunk.getBlock(blockpos) != Blocks.AIR) {
                world.chunk.setBlock(Blocks.AIR, blockpos);
                return;
            }
            raycastPos.add(front);
        }
    }

    public synchronized void placeBlock(short blockID) {
        Vector3f raycastPos = new Vector3f(position).add(0, 1.6f, 0);
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
