package xyz.przemyk.voxelgame.world;

import org.joml.Vector3i;
import xyz.przemyk.voxelgame.VoxelGame;

public class Chunk {
    private final short[][][] blocks = new short[7][7][7];

    public void setBlock(short blockId, Vector3i blockPos) {
        setBlock(blockId, blockPos.x, blockPos.y, blockPos.z);
    }

    public void setBlock(short blockId, int x, int y, int z) {
        if (x < 0 || x >= blocks.length || y < 0 || y >= blocks[0].length || z < 0 || z >= blocks[0][0].length) {
            return;
        }
        blocks[x][y][z] = blockId;
        VoxelGame.getInstance().getWindow().getRenderer().updateChunk();
    }

    public short getBlock(Vector3i blockPos) {
        return getBlock(blockPos.x, blockPos.y, blockPos.z);
    }

    public short getBlock(int x, int y, int z) {
        if (x < 0 || x >= blocks.length || y < 0 || y >= blocks[0].length || z < 0 || z >= blocks[0][0].length) {
            return Blocks.AIR;
        }
        return blocks[x][y][z];
    }

    public int getLength(Coordinates coordinates) {
        switch (coordinates) {
            case X -> {
                return blocks.length;
            }
            case Y -> {
                return blocks[0].length;
            }
            case Z -> {
                return blocks[0][0].length;
            }
        }

        throw new IllegalArgumentException("Passed wrong Coordinate enum value");
    }
}
