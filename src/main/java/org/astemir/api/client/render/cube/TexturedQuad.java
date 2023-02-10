package org.astemir.api.client.render.cube;

import com.mojang.math.Vector3f;
import net.minecraft.core.Direction;

public class TexturedQuad {

    public final TextureVertex[] vertexPositions;
    public final Vector3f normal;

    public TexturedQuad(TextureVertex[] positionsIn, float u1, float v1, float u2, float v2, float texWidth, float texHeight, boolean mirrorIn, Direction directionIn) {
        this.vertexPositions = positionsIn;
        float f = 0.0F / texWidth;
        float f1 = 0.0F / texHeight;
        positionsIn[0] = positionsIn[0].setTextureUV(u2 / texWidth - f, v1 / texHeight + f1);
        positionsIn[1] = positionsIn[1].setTextureUV(u1 / texWidth + f, v1 / texHeight + f1);
        positionsIn[2] = positionsIn[2].setTextureUV(u1 / texWidth + f, v2 / texHeight - f1);
        positionsIn[3] = positionsIn[3].setTextureUV(u2 / texWidth - f, v2 / texHeight - f1);
        if (mirrorIn) {
            int i = positionsIn.length;

            for(int j = 0; j < i / 2; ++j) {
                TextureVertex vertex = positionsIn[j];
                positionsIn[j] = positionsIn[i - 1 - j];
                positionsIn[i - 1 - j] = vertex;
            }
        }
        this.normal = directionIn.step();
        if (mirrorIn) {
            this.normal.mul(-1.0F, 1.0F, 1.0F);
        }
    }

    public TexturedQuad(TextureVertex[] positionsIn, float u1, float v1, float u2, float v2, float texWidth, float texHeight, boolean mirrorIn, Direction directionIn,float b) {
        this.vertexPositions = positionsIn;
        this.normal = directionIn.step();
        float uWidth = (u1 + u2) / texWidth;
        float vHeight = (v1 + v2) / texHeight;
        u1 /= texWidth;
        v1 /= texHeight;
        if (!mirrorIn) {
            float tempWidth = uWidth;
            uWidth = u1;
            u1 = tempWidth;

            normal.mul(-1, 1, 1);
        }
        positionsIn[0] = positionsIn[0].setTextureUV(u1, v1);
        positionsIn[1] = positionsIn[1].setTextureUV(uWidth, v1);
        positionsIn[2] = positionsIn[2].setTextureUV(uWidth, vHeight);
        positionsIn[3] = positionsIn[3].setTextureUV(u1, vHeight);
    }

}
