package org.astemir.api.client.render.cube;

import net.minecraft.core.Direction;

public class ModelCube {

    private final TexturedQuad[] quads;
    public final float posX1;
    public final float posY1;
    public final float posZ1;
    public final float posX2;
    public final float posY2;
    public final float posZ2;

    public ModelCube(int texOffX, int texOffY, float x, float y, float z, float width, float height, float depth, float deltaX, float deltaY, float deltaZ, boolean mirorIn, float texWidth, float texHeight) {
        this.posX1 = x;
        this.posY1 = y;
        this.posZ1 = z;
        this.posX2 = x + width;
        this.posY2 = y + height;
        this.posZ2 = z + depth;
        this.quads = new TexturedQuad[6];
        float f = x + width;
        float f1 = y + height;
        float f2 = z + depth;
        x = x - deltaX;
        y = y - deltaY;
        z = z - deltaZ;
        f = f + deltaX;
        f1 = f1 + deltaY;
        f2 = f2 + deltaZ;
        if (mirorIn) {
            float f3 = f;
            f = x;
            x = f3;
        }

        TextureVertex vertexA = new TextureVertex(f, y, z, 0.0F, 8.0F);
        TextureVertex vertexB = new TextureVertex(f, f1, z, 8.0F, 8.0F);
        TextureVertex vertexC = new TextureVertex(x, f1, z, 8.0F, 0.0F);
        TextureVertex vertexD = new TextureVertex(x, y, f2, 0.0F, 0.0F);
        TextureVertex vertexE = new TextureVertex(f, y, f2, 0.0F, 8.0F);
        TextureVertex vertexF = new TextureVertex(f, f1, f2, 8.0F, 8.0F);
        TextureVertex vertexG = new TextureVertex(x, f1, f2, 8.0F, 0.0F);
        TextureVertex vertexH = new TextureVertex(x, y, z, 0.0F, 0.0F);

        float f4 = (float)texOffX;
        float f5 = (float)texOffX + depth;
        float f6 = (float)texOffX + depth + width;
        float f7 = (float)texOffX + depth + width + width;
        float f8 = (float)texOffX + depth + width + depth;
        float f9 = (float)texOffX + depth + width + depth + width;
        float f10 = (float)texOffY;
        float f11 = (float)texOffY + depth;
        float f12 = (float)texOffY + depth + height;

        this.quads[2] = new TexturedQuad(new TextureVertex[]{vertexE, vertexD, vertexH, vertexA}, f5, f10, f6, f11, texWidth, texHeight, mirorIn, Direction.DOWN);
        this.quads[3] = new TexturedQuad(new TextureVertex[]{vertexB, vertexC, vertexG, vertexF}, f6, f11, f7, f10, texWidth, texHeight, mirorIn, Direction.UP);
        this.quads[1] = new TexturedQuad(new TextureVertex[]{vertexH, vertexD, vertexG, vertexC}, f4, f11, f5, f12, texWidth, texHeight, mirorIn, Direction.WEST);
        this.quads[4] = new TexturedQuad(new TextureVertex[]{vertexA, vertexH, vertexC, vertexB}, f5, f11, f6, f12, texWidth, texHeight, mirorIn, Direction.NORTH);
        this.quads[0] = new TexturedQuad(new TextureVertex[]{vertexE, vertexA, vertexB, vertexF}, f6, f11, f8, f12, texWidth, texHeight, mirorIn, Direction.EAST);
        this.quads[5] = new TexturedQuad(new TextureVertex[]{vertexD, vertexE, vertexF, vertexG}, f8, f11, f9, f12, texWidth, texHeight, mirorIn, Direction.SOUTH);
    }

    public TexturedQuad[] getQuads() {
        return quads;
    }
}
