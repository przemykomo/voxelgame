package xyz.przemyk.voxelgame.world;

import java.time.Duration;
import java.time.Instant;
import java.util.concurrent.atomic.AtomicInteger;

public class World implements Runnable {
    public final PlayerEntity playerEntity;
    public final Chunk chunk;

    private boolean isStopped = false;

    public World() {
        playerEntity = new PlayerEntity(this);
        chunk = new Chunk();
    }

    public final AtomicInteger ticks = new AtomicInteger();

    public void tick() {
        playerEntity.tick();
        ticks.incrementAndGet();
    }

    public Duration deltaTime = Duration.ofMillis(1000 / 20);

    @SuppressWarnings("BusyWait")
    @Override
    public void run() {
        chunk.setBlock(Blocks.METEORITE, 0, 0, 0);
        chunk.setBlock(Blocks.METEORITE, 1, 0, 0);
        chunk.setBlock(Blocks.METEORITE, 0, 0, 1);
        chunk.setBlock(Blocks.METEORITE, 1, 0, 1);

        while (!isStopped()) {
            Instant beginTime = Instant.now();
            tick();

            try {
                Thread.sleep(1000 / 20);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            deltaTime = Duration.between(beginTime, Instant.now());
        }
    }

    private synchronized boolean isStopped() {
        return isStopped;
    }

    public synchronized void stop() {
        isStopped = true;
    }
}
