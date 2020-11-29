package xyz.przemyk.voxelgame.world;

import xyz.przemyk.voxelgame.window.Renderer;

public class World {
    public final PlayerCamera playerCamera;
    public final Chunk chunk;

    public World(Renderer renderer) {
        playerCamera = new PlayerCamera(renderer, this);
        chunk = new Chunk();
    }

    public void tick() {
        playerCamera.tick();
    }
}
