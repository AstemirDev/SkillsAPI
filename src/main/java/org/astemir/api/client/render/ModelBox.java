package org.astemir.api.client.render;

import net.minecraft.core.Direction;

public class ModelBox {

    private final TexturedQuad[] quads;
    public final float posX1;
    public final float posY1;
    public final float posZ1;
    public final float posX2;
    public final float posY2;
    public final float posZ2;

    public ModelBox(int texOffX, int texOffY, float x, float y, float z, float width, float height, float depth, float deltaX, float deltaY, float deltaZ, boolean mirorIn, float texWidth, float texHeight) {
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

        PositionTextureVertex vertexA = new PositionTextureVertex(f, y, z, 0.0F, 8.0F);
        PositionTextureVertex vertexB = new PositionTextureVertex(f, f1, z, 8.0F, 8.0F);
        PositionTextureVertex vertexC = new PositionTextureVertex(x, f1, z, 8.0F, 0.0F);
        PositionTextureVertex vertexD = new PositionTextureVertex(x, y, f2, 0.0F, 0.0F);
        PositionTextureVertex vertexE = new PositionTextureVertex(f, y, f2, 0.0F, 8.0F);
        PositionTextureVertex vertexF = new PositionTextureVertex(f, f1, f2, 8.0F, 8.0F);
        PositionTextureVertex vertexG = new PositionTextureVertex(x, f1, f2, 8.0F, 0.0F);
        PositionTextureVertex vertexH = new PositionTextureVertex(x, y, z, 0.0F, 0.0F);

        float f4 = (float)texOffX;
        float f5 = (float)texOffX + depth;
        float f6 = (float)texOffX + depth + width;
        float f7 = (float)texOffX + depth + width + width;
        float f8 = (float)texOffX + depth + width + depth;
        float f9 = (float)texOffX + depth + width + depth + width;
        float f10 = (float)texOffY;
        float f11 = (float)texOffY + depth;
        float f12 = (float)texOffY + depth + height;

        this.quads[2] = new TexturedQuad(new PositionTextureVertex[]{vertexE, vertexD, vertexH, vertexA}, f5, f10, f6, f11, texWidth, texHeight, mirorIn, Direction.DOWN);
        this.quads[3] = new TexturedQuad(new PositionTextureVertex[]{vertexB, vertexC, vertexG, vertexF}, f6, f11, f7, f10, texWidth, texHeight, mirorIn, Direction.UP);
        this.quads[1] = new TexturedQuad(new PositionTextureVertex[]{vertexH, vertexD, vertexG, vertexC}, f4, f11, f5, f12, texWidth, texHeight, mirorIn, Direction.WEST);
        this.quads[4] = new TexturedQuad(new PositionTextureVertex[]{vertexA, vertexH, vertexC, vertexB}, f5, f11, f6, f12, texWidth, texHeight, mirorIn, Direction.NORTH);
        this.quads[0] = new TexturedQuad(new PositionTextureVertex[]{vertexE, vertexA, vertexB, vertexF}, f6, f11, f8, f12, texWidth, texHeight, mirorIn, Direction.EAST);
        this.quads[5] = new TexturedQuad(new PositionTextureVertex[]{vertexD, vertexE, vertexF, vertexG}, f8, f11, f9, f12, texWidth, texHeight, mirorIn, Direction.SOUTH);
    }

    public TexturedQuad[] getQuads() {
        return quads;
    }
}
