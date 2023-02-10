package org.astemir.api.client.render.cube;

import net.minecraft.core.Direction;
import org.astemir.api.math.Vector3;

public class ModelCube {

    private final TexturedQuad[] quads;
    public float posX1;
    public float posY1;
    public float posZ1;
    public final float posX2;
    public final float posY2;
    public final float posZ2;
    public float rotX = 0;
    public float rotY = 0;
    public float rotZ = 0;
    public float pivotX = 0;
    public float pivotY = 0;
    public float pivotZ = 0;



    public ModelCube(float texOffX, float texOffY, float x, float y, float z, float width, float height, float depth, float deltaX, float deltaY, float deltaZ, boolean mirrorIn, float texWidth, float texHeight) {
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
        if (mirrorIn) {
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
        float f4 = texOffX;
        float f5 = texOffX + depth;
        float f6 = texOffX + depth + width;
        float f7 = texOffX + depth + width + width;
        float f8 = texOffX + depth + width + depth;
        float f9 = texOffX + depth + width + depth + width;
        float f10 = texOffY;
        float f11 = texOffY + depth;
        float f12 = texOffY + depth + height;
        this.quads[0] = new TexturedQuad(new TextureVertex[]{vertexE, vertexA, vertexB, vertexF}, f6, f11, f8, f12, texWidth, texHeight, mirrorIn, Direction.EAST);
        this.quads[1] = new TexturedQuad(new TextureVertex[]{vertexH, vertexD, vertexG, vertexC}, f4, f11, f5, f12, texWidth, texHeight, mirrorIn, Direction.WEST);
        this.quads[2] = new TexturedQuad(new TextureVertex[]{vertexE, vertexD, vertexH, vertexA}, f5, f10, f6, f11, texWidth, texHeight, mirrorIn, Direction.DOWN);
        this.quads[3] = new TexturedQuad(new TextureVertex[]{vertexB, vertexC, vertexG, vertexF}, f6, f11, f7, f10, texWidth, texHeight, mirrorIn, Direction.UP);
        this.quads[4] = new TexturedQuad(new TextureVertex[]{vertexA, vertexH, vertexC, vertexB}, f5, f11, f6, f12, texWidth, texHeight, mirrorIn, Direction.NORTH);
        this.quads[5] = new TexturedQuad(new TextureVertex[]{vertexD, vertexE, vertexF, vertexG}, f8, f11, f9, f12, texWidth, texHeight, mirrorIn, Direction.SOUTH);
    }

    public ModelCube(float x, float y, float z, float width, float height, float depth, float deltaX, float deltaY, float deltaZ, boolean mirrorIn, float texWidth, float texHeight,UVMap uvMap) {
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
        if (mirrorIn) {
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

        if (uvMap.has(Direction.EAST)) {
            CubeUV uv = uvMap.get(Direction.EAST);
            this.quads[0] = new TexturedQuad(new TextureVertex[]{vertexE, vertexA, vertexB, vertexF}, uv.getUvPos().x, uv.getUvPos().y, uv.getUvSize().x, uv.getUvSize().y, texWidth, texHeight, mirrorIn, Direction.EAST,0);
        }
        if (uvMap.has(Direction.WEST)) {
            CubeUV uv = uvMap.get(Direction.WEST);
            this.quads[1] = new TexturedQuad(new TextureVertex[]{vertexH, vertexD, vertexG, vertexC},uv.getUvPos().x,uv.getUvPos().y, uv.getUvSize().x, uv.getUvSize().y, texWidth, texHeight, mirrorIn, Direction.WEST,0);
        }
        if (uvMap.has(Direction.DOWN)){
            CubeUV uv = uvMap.get(Direction.DOWN);
            this.quads[3] = new TexturedQuad(new TextureVertex[]{vertexB, vertexC, vertexG, vertexF}, uv.getUvPos().x, uv.getUvPos().y, uv.getUvSize().x, uv.getUvSize().y, texWidth, texHeight, mirrorIn, Direction.UP,0);
        }
        if (uvMap.has(Direction.UP)) {
            CubeUV uv = uvMap.get(Direction.UP);
            this.quads[2] = new TexturedQuad(new TextureVertex[]{vertexE, vertexD, vertexH, vertexA}, uv.getUvPos().x,uv.getUvPos().y, uv.getUvSize().x, uv.getUvSize().y, texWidth, texHeight, mirrorIn, Direction.DOWN,0);
        }
        if (uvMap.has(Direction.NORTH)) {
            CubeUV uv = uvMap.get(Direction.NORTH);
            this.quads[4] = new TexturedQuad(new TextureVertex[]{vertexA, vertexH, vertexC, vertexB},uv.getUvPos().x, uv.getUvPos().y, uv.getUvSize().x, uv.getUvSize().y, texWidth, texHeight, mirrorIn, Direction.NORTH,0);
        }
        if (uvMap.has(Direction.SOUTH)) {
            CubeUV uv = uvMap.get(Direction.SOUTH);
            this.quads[5] = new TexturedQuad(new TextureVertex[]{vertexD, vertexE, vertexF, vertexG},uv.getUvPos().x, uv.getUvPos().y, uv.getUvSize().x, uv.getUvSize().y, texWidth, texHeight, mirrorIn, Direction.SOUTH,0);
        }
    }

    public ModelCube setRotation(Vector3 rot){
        this.rotX = rot.x;
        this.rotY = rot.y;
        this.rotZ = rot.z;
        return this;
    }

    public ModelCube setPivot(Vector3 pivot){
        this.pivotX = pivot.x;
        this.pivotY = pivot.y;
        this.pivotZ = pivot.z;
        return this;
    }


    public TexturedQuad[] getQuads() {
        return quads;
    }
}
