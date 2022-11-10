package org.astemir.api.client.render;

import com.mojang.math.Vector3f;

public class PositionTextureVertex {
    public final Vector3f position;
    public final float textureU;
    public final float textureV;

    public PositionTextureVertex(float x, float y, float z, float texU, float texV) {
        this(new Vector3f(x, y, z), texU, texV);
    }

    public PositionTextureVertex setTextureUV(float texU, float texV) {
        return new PositionTextureVertex(this.position, texU, texV);
    }

    public PositionTextureVertex(Vector3f posIn, float texU, float texV) {
        this.position = posIn;
        this.textureU = texU;
        this.textureV = texV;
    }
}
