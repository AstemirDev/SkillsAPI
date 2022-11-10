package org.astemir.api.client.render;

import com.mojang.math.Vector3f;
import net.minecraft.core.Direction;

public class TexturedQuad {

    public final PositionTextureVertex[] vertexPositions;
    public final Vector3f normal;

    public TexturedQuad(PositionTextureVertex[] positionsIn, float u1, float v1, float u2, float v2, float texWidth, float texHeight, boolean mirrorIn, Direction directionIn) {
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
                PositionTextureVertex vertex = positionsIn[j];
                positionsIn[j] = positionsIn[i - 1 - j];
                positionsIn[i - 1 - j] = vertex;
            }
        }
        this.normal = directionIn.step();
        if (mirrorIn) {
            this.normal.mul(-1.0F, 1.0F, 1.0F);
        }
    }

}
