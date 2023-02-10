package org.astemir.api.client.render.cube;

import org.astemir.api.math.Vector2;

public class CubeUV {

    private Vector2 uvPos = new Vector2(0,0);
    private Vector2 uvSize = new Vector2(0,0);

    public CubeUV(Vector2 uvPos, Vector2 uvSize) {
        this.uvPos = uvPos;
        this.uvSize = uvSize;
    }

    public Vector2 getUvPos() {
        return uvPos;
    }

    public Vector2 getUvSize() {
        return uvSize;
    }
}
