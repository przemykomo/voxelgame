package xyz.przemyk.voxelgame.window;

import xyz.przemyk.voxelgame.world.Blocks;
import xyz.przemyk.voxelgame.world.Chunk;
import xyz.przemyk.voxelgame.world.Coordinates;

import java.util.ArrayList;
import java.util.List;

public class ChunkVerticesGenerator {

    public float[] getVertices(Chunk chunk, TextureAtlas textureAtlas) {
        List<Float> vertices = new ArrayList<>();

        for (int x = 0; x < chunk.getLength(Coordinates.X); x++) {
            for (int y = 0; y < chunk.getLength(Coordinates.Y); y++) {
                for (int z = 0; z < chunk.getLength(Coordinates.Z); z++) {

                    short currentBlock = chunk.getBlock(x, y, z);

                    if (currentBlock != Blocks.AIR) {
                        //cube
                        //30 floats per face
                        //180 floats per full cube

                        //left face
                        if (chunk.getBlock(x - 1, y, z) == Blocks.AIR) {
                            //top-right
                            vertices.add((float) x);
                            vertices.add(1f + y);
                            vertices.add(1f + z);
                            vertices.add(textureAtlas.getRightXTextureCoordinate(currentBlock));
                            vertices.add(textureAtlas.getTopYTextureCoordinate(currentBlock));

                            //bottom-left
                            vertices.add((float) x);
                            vertices.add((float) y);
                            vertices.add((float) z);
                            vertices.add(textureAtlas.getLeftXTextureCoordinate(currentBlock));
                            vertices.add(textureAtlas.getBottomYTextureCoordinate(currentBlock));

                            //top-left
                            vertices.add((float) x);
                            vertices.add(1f + y);
                            vertices.add((float) z);
                            vertices.add(textureAtlas.getLeftXTextureCoordinate(currentBlock));
                            vertices.add(textureAtlas.getTopYTextureCoordinate(currentBlock));

                            //bottom-left
                            vertices.add((float) x);
                            vertices.add((float) y);
                            vertices.add((float) z);
                            vertices.add(textureAtlas.getLeftXTextureCoordinate(currentBlock));
                            vertices.add(textureAtlas.getBottomYTextureCoordinate(currentBlock));

                            //top-right
                            vertices.add((float) x);
                            vertices.add(1f + y);
                            vertices.add(1f + z);
                            vertices.add(textureAtlas.getRightXTextureCoordinate(currentBlock));
                            vertices.add(textureAtlas.getTopYTextureCoordinate(currentBlock));

                            //bottom-right
                            vertices.add((float) x);
                            vertices.add((float) y);
                            vertices.add(1f + z);
                            vertices.add(textureAtlas.getRightXTextureCoordinate(currentBlock));
                            vertices.add(textureAtlas.getBottomYTextureCoordinate(currentBlock));
                        }

                        //right face
                        if (chunk.getBlock(x + 1, y, z)== Blocks.AIR) {
                            //top-left
                            vertices.add(1f + x);
                            vertices.add(1f + y);
                            vertices.add(1f + z);
                            vertices.add(textureAtlas.getLeftXTextureCoordinate(currentBlock));
                            vertices.add(textureAtlas.getTopYTextureCoordinate(currentBlock));

                            //top-right
                            vertices.add(1f + x);
                            vertices.add(1f + y);
                            vertices.add((float) z);
                            vertices.add(textureAtlas.getRightXTextureCoordinate(currentBlock));
                            vertices.add(textureAtlas.getTopYTextureCoordinate(currentBlock));

                            //bottom-right
                            vertices.add(1f + x);
                            vertices.add((float) y);
                            vertices.add((float) z);
                            vertices.add(textureAtlas.getRightXTextureCoordinate(currentBlock));
                            vertices.add(textureAtlas.getBottomYTextureCoordinate(currentBlock));

                            //bottom-right
                            vertices.add(1f + x);
                            vertices.add((float) y);
                            vertices.add((float) z);
                            vertices.add(textureAtlas.getRightXTextureCoordinate(currentBlock));
                            vertices.add(textureAtlas.getBottomYTextureCoordinate(currentBlock));

                            //bottom-left
                            vertices.add(1f + x);
                            vertices.add((float) y);
                            vertices.add(1f + z);
                            vertices.add(textureAtlas.getLeftXTextureCoordinate(currentBlock));
                            vertices.add(textureAtlas.getBottomYTextureCoordinate(currentBlock));

                            //top-left
                            vertices.add(1f + x);
                            vertices.add(1f + y);
                            vertices.add(1f + z);
                            vertices.add(textureAtlas.getLeftXTextureCoordinate(currentBlock));
                            vertices.add(textureAtlas.getTopYTextureCoordinate(currentBlock));
                        }

                        //bottom face
                        if (chunk.getBlock(x, y - 1, z) == Blocks.AIR) {
                            //top-right
                            vertices.add((float) x);
                            vertices.add((float) y);
                            vertices.add((float) z);
                            vertices.add(textureAtlas.getRightXTextureCoordinate(currentBlock));
                            vertices.add(textureAtlas.getTopYTextureCoordinate(currentBlock));

                            //bottom-left
                            vertices.add(1f + x);
                            vertices.add((float) y);
                            vertices.add(1f + z);
                            vertices.add(textureAtlas.getLeftXTextureCoordinate(currentBlock));
                            vertices.add(textureAtlas.getBottomYTextureCoordinate(currentBlock));

                            //top-left
                            vertices.add(1f + x);
                            vertices.add((float) y);
                            vertices.add((float) z);
                            vertices.add(textureAtlas.getLeftXTextureCoordinate(currentBlock));
                            vertices.add(textureAtlas.getTopYTextureCoordinate(currentBlock));

                            //bottom-left
                            vertices.add(1f + x);
                            vertices.add((float) y);
                            vertices.add(1f + z);
                            vertices.add(textureAtlas.getLeftXTextureCoordinate(currentBlock));
                            vertices.add(textureAtlas.getBottomYTextureCoordinate(currentBlock));

                            //top-right
                            vertices.add((float) x);
                            vertices.add((float) y);
                            vertices.add((float) z);
                            vertices.add(textureAtlas.getRightXTextureCoordinate(currentBlock));
                            vertices.add(textureAtlas.getTopYTextureCoordinate(currentBlock));

                            //bottom-right
                            vertices.add((float) x);
                            vertices.add((float) y);
                            vertices.add(1f + z);
                            vertices.add(textureAtlas.getRightXTextureCoordinate(currentBlock));
                            vertices.add(textureAtlas.getBottomYTextureCoordinate(currentBlock));
                        }

                        //top face
                        if (chunk.getBlock(x, y + 1, z) == Blocks.AIR) {
                            //top-left
                            vertices.add((float) x);
                            vertices.add(1f + y);
                            vertices.add((float) z);
                            vertices.add(textureAtlas.getLeftXTextureCoordinate(currentBlock));
                            vertices.add(textureAtlas.getTopYTextureCoordinate(currentBlock));

                            //top-right
                            vertices.add(1f + x);
                            vertices.add(1f + y);
                            vertices.add((float) z);
                            vertices.add(textureAtlas.getRightXTextureCoordinate(currentBlock));
                            vertices.add(textureAtlas.getTopYTextureCoordinate(currentBlock));

                            //bottom-right
                            vertices.add(1f + x);
                            vertices.add(1f + y);
                            vertices.add(1f + z);
                            vertices.add(textureAtlas.getRightXTextureCoordinate(currentBlock));
                            vertices.add(textureAtlas.getBottomYTextureCoordinate(currentBlock));

                            //bottom-right
                            vertices.add(1f + x);
                            vertices.add(1f + y);
                            vertices.add(1f + z);
                            vertices.add(textureAtlas.getRightXTextureCoordinate(currentBlock));
                            vertices.add(textureAtlas.getBottomYTextureCoordinate(currentBlock));

                            //bottom-left
                            vertices.add((float) x);
                            vertices.add(1f + y);
                            vertices.add(1f + z);
                            vertices.add(textureAtlas.getLeftXTextureCoordinate(currentBlock));
                            vertices.add(textureAtlas.getBottomYTextureCoordinate(currentBlock));

                            //top-left
                            vertices.add((float) x);
                            vertices.add(1f + y);
                            vertices.add((float) z);
                            vertices.add(textureAtlas.getLeftXTextureCoordinate(currentBlock));
                            vertices.add(textureAtlas.getTopYTextureCoordinate(currentBlock));
                        }

                        //back face
                        if (chunk.getBlock(x, y, z - 1) == Blocks.AIR) {
                            //bottom-right
                            vertices.add((float) x);
                            vertices.add((float) y);
                            vertices.add((float) z);
                            vertices.add(textureAtlas.getRightXTextureCoordinate(currentBlock));
                            vertices.add(textureAtlas.getBottomYTextureCoordinate(currentBlock));

                            //bottom-left
                            vertices.add(1f + x);
                            vertices.add((float) y);
                            vertices.add((float) z);
                            vertices.add(textureAtlas.getLeftXTextureCoordinate(currentBlock));
                            vertices.add(textureAtlas.getBottomYTextureCoordinate(currentBlock));

                            //top-left
                            vertices.add(1f + x);
                            vertices.add(1f + y);
                            vertices.add((float) z);
                            vertices.add(textureAtlas.getLeftXTextureCoordinate(currentBlock));
                            vertices.add(textureAtlas.getTopYTextureCoordinate(currentBlock));

                            //top-left
                            vertices.add(1f + x);
                            vertices.add(1f + y);
                            vertices.add((float) z);
                            vertices.add(textureAtlas.getLeftXTextureCoordinate(currentBlock));
                            vertices.add(textureAtlas.getTopYTextureCoordinate(currentBlock));

                            //top-right
                            vertices.add((float) x);
                            vertices.add(1f + y);
                            vertices.add((float) z);
                            vertices.add(textureAtlas.getRightXTextureCoordinate(currentBlock));
                            vertices.add(textureAtlas.getTopYTextureCoordinate(currentBlock));

                            //bottom-right
                            vertices.add((float) x);
                            vertices.add((float) y);
                            vertices.add((float) z);
                            vertices.add(textureAtlas.getRightXTextureCoordinate(currentBlock));
                            vertices.add(textureAtlas.getBottomYTextureCoordinate(currentBlock));
                        }

                        //front face
                        if (chunk.getBlock(x, y, z + 1) == Blocks.AIR) {
                            //bottom-left
                            vertices.add((float) x);
                            vertices.add((float) y);
                            vertices.add(1f + z);
                            vertices.add(textureAtlas.getLeftXTextureCoordinate(currentBlock));
                            vertices.add(textureAtlas.getBottomYTextureCoordinate(currentBlock));

                            //top-right
                            vertices.add(1f + x);
                            vertices.add(1f + y);
                            vertices.add(1f + z);
                            vertices.add(textureAtlas.getRightXTextureCoordinate(currentBlock));
                            vertices.add(textureAtlas.getTopYTextureCoordinate(currentBlock));

                            //bottom-right
                            vertices.add(1f + x);
                            vertices.add((float) y);
                            vertices.add(1f + z);
                            vertices.add(textureAtlas.getRightXTextureCoordinate(currentBlock));
                            vertices.add(textureAtlas.getBottomYTextureCoordinate(currentBlock));

                            //top-right
                            vertices.add(1f + x);
                            vertices.add(1f + y);
                            vertices.add(1f + z);
                            vertices.add(textureAtlas.getRightXTextureCoordinate(currentBlock));
                            vertices.add(textureAtlas.getTopYTextureCoordinate(currentBlock));

                            //bottom-left
                            vertices.add((float) x);
                            vertices.add((float) y);
                            vertices.add(1f + z);
                            vertices.add(textureAtlas.getLeftXTextureCoordinate(currentBlock));
                            vertices.add(textureAtlas.getBottomYTextureCoordinate(currentBlock));

                            //top-left
                            vertices.add((float) x);
                            vertices.add(1f + y);
                            vertices.add(1f + z);
                            vertices.add(textureAtlas.getLeftXTextureCoordinate(currentBlock));
                            vertices.add(textureAtlas.getTopYTextureCoordinate(currentBlock));
                        }
                    }
                }
            }
        }

        float[] verticesArray = new float[vertices.size()];
        int i = 0;

        for (Float f : vertices) {
            verticesArray[i++] = (f != null ? f : Float.NaN);
        }

        return verticesArray;
    }
}
