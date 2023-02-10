package org.astemir.api.client.render.cube;

import com.mojang.math.Vector3f;

public class TextureVertex {

    public final Vector3f position;
    public final float textureU;
    public final float textureV;

    public TextureVertex(float x, float y, float z, float texU, float texV) {
        this(new Vector3f(x, y, z), texU, texV);
    }

    public TextureVertex setTextureUV(float texU, float texV) {
        return new TextureVertex(this.position, texU, texV);
    }

    public TextureVertex(Vector3f posIn, float texU, float texV) {
        this.position = posIn;
        this.textureU = texU;
        this.textureV = texV;
    }


    public float getTextureU() {
        return textureU;
    }

    public float getTextureV() {
        return textureV;
    }
}
