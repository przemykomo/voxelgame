package xyz.przemyk.voxelgame.window;

import org.lwjgl.system.MemoryStack;
import xyz.przemyk.voxelgame.ResourceUtils;
import xyz.przemyk.voxelgame.world.Blocks;

import java.io.IOException;
import java.nio.ByteBuffer;

import static org.lwjgl.opengl.GL11.*;

public class TextureAtlas {

    public void init(String fileName) throws IOException {
        int texture = glGenTextures();
        glBindTexture(GL_TEXTURE_2D, texture);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT);

        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);

        glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
        try (MemoryStack stack = MemoryStack.stackPush()) {
            ByteBuffer bytesBuffer = stack.bytes(ResourceUtils.loadImageResource(fileName));
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, 32, 32, 0, GL_RGBA, GL_UNSIGNED_BYTE, bytesBuffer);
        }
    }

    public float getLeftXTextureCoordinate(short block) {
        return switch (block) {
            case Blocks.BLOCK -> 0.0f;
            case Blocks.METEORITE -> 0.5f;
            default -> throw new IllegalStateException("Unexpected value: " + block);
        };
    }

    public float getRightXTextureCoordinate(short block) {
        return switch (block) {
            case Blocks.BLOCK -> 0.5f;
            case Blocks.METEORITE -> 1.0f;
            default -> throw new IllegalStateException("Unexpected value: " + block);
        };
    }

    public float getTopYTextureCoordinate(short block) {
        return 0.0f;
    }

    public float getBottomYTextureCoordinate(short block) {
        return 0.5f;
    }
}
