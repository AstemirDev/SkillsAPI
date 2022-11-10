package org.astemir.api.client.render;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import org.astemir.api.client.model.AdvancedModel;
import org.astemir.api.math.Transform;
import org.astemir.api.math.Vector2;
import org.astemir.api.math.Vector3;



public class AdvancedCubeRenderer {


    private final ObjectList<ModelBox> cubeList = new ObjectArrayList<>();
    private final ObjectList<AdvancedCubeRenderer> childModels = new ObjectArrayList<>();

    private float rotationX;
    private float rotationY;
    private float rotationZ;

    private float positionX;
    private float positionY;
    private float positionZ;

    private float scaleX = 1;
    private float scaleY = 1;
    private float scaleZ = 1;

    public Vector3 defaultRotation = new Vector3(0,0,0);
    public Vector3 rotationPoint = new Vector3(0,0,0);
    public Vector2 textureSize = new Vector2(64,32);
    public Vector2 textureOffset = new Vector2(0,0);
    private String name;

    public boolean mirror = false;
    public boolean showModel = true;


    public AdvancedCubeRenderer(String name, int textureWidthIn, int textureHeightIn, int textureOffsetXIn, int textureOffsetYIn) {
        this.name = name;
        this.textureSize(textureWidthIn, textureHeightIn);
        this.textureOffset(textureOffsetXIn, textureOffsetYIn);
    }

    public void apply(Transform transform){
        setRotation(transform.getRotation());
        setPosition(transform.getPosition());
        setScale(transform.getScale());
    }

    public void setPosition(Vector3 position){
        this.positionX = position.x;
        this.positionY = position.y;
        this.positionZ = position.z;
    }

    public void setScale(Vector3 scale){
        this.scaleX = scale.x;
        this.scaleY = scale.y;
        this.scaleZ = scale.z;
    }

    public void setRotation(Vector3 rot){
        this.rotationX = rot.x;
        this.rotationY = rot.y;
        this.rotationZ = rot.z;
    }


    public Vector3 getRotation(){
        return new Vector3(rotationX,rotationY,rotationZ);
    }

    public Vector3 getScale(){
        return new Vector3(scaleX,scaleY,scaleZ);
    }

    public Vector3 getPosition(){
        return new Vector3(positionX,positionY,positionZ);
    }


    public void reset(){
        scaleX = 1;
        scaleY = 1;
        scaleZ = 1;
        rotationX = 0;
        rotationY = 0;
        rotationZ = 0;
        positionX = 0;
        positionY = 0;
        positionZ = 0;
    }


    public AdvancedCubeRenderer cube(String name, float x, float y, float z, int width, int height, int depth, float delta, int texX, int texY) {
        this.name = name;
        this.textureOffset(texX, texY);
        return cube((int)textureOffset.getX(), (int)textureOffset.getY(), x, y, z, (float)width, (float)height, (float)depth, delta, delta, delta, this.mirror);
    }

    public AdvancedCubeRenderer cube(float x, float y, float z, float width, float height, float depth) {
        return cube((int)textureOffset.getX(), (int)textureOffset.getY(), x, y, z, width, height, depth, 0.0F, 0.0F, 0.0F, this.mirror);
    }

    public AdvancedCubeRenderer cube(float x, float y, float z, float width, float height, float depth, boolean mirrorIn) {
        return cube((int)textureOffset.getX(), (int)textureOffset.getY(), x, y, z, width, height, depth, 0.0F, 0.0F, 0.0F, mirrorIn);
    }

    public AdvancedCubeRenderer cube(float x, float y, float z, float width, float height, float depth, float delta) {
        return this.cube((int)textureOffset.getX(), (int)textureOffset.getY(), x, y, z, width, height, depth, delta, delta, delta, this.mirror);
    }

    public AdvancedCubeRenderer cube(float x, float y, float z, float width, float height, float depth, float deltaX, float deltaY, float deltaZ) {
        return this.cube((int)textureOffset.getX(), (int)textureOffset.getY(), x, y, z, width, height, depth, deltaX, deltaY, deltaZ, this.mirror);
    }

    public AdvancedCubeRenderer cube(float x, float y, float z, float width, float height, float depth, float delta, boolean mirrorIn) {
        return this.cube((int)textureOffset.getX(),(int) textureOffset.getY(), x, y, z, width, height, depth, delta, delta, delta, mirrorIn);
    }

    private AdvancedCubeRenderer cube(int texOffX, int texOffY, float x, float y, float z, float width, float height, float depth, float deltaX, float deltaY, float deltaZ, boolean mirorIn) {
        this.cubeList.add(new ModelBox(texOffX, texOffY, x, y, z, width, height, depth, deltaX, deltaY, deltaZ, mirorIn, this.textureSize.getX(), this.textureSize.getY()));
        return this;
    }

    public AdvancedCubeRenderer rotationPoint(float rotationPointXIn, float rotationPointYIn, float rotationPointZIn) {
        this.rotationPoint = new Vector3(rotationPointXIn,rotationPointYIn,rotationPointZIn);
        return this;
    }

    public AdvancedCubeRenderer textureSize(int textureWidthIn, int textureHeightIn) {
        this.textureSize = new Vector2(textureWidthIn,textureHeightIn);
        return this;
    }

    public AdvancedCubeRenderer textureOffset(int x, int y) {
        this.textureOffset = new Vector2(x,y);
        return this;
    }


    public void render(AdvancedModel model, PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha,RenderCall renderCall,boolean resetBuffer) {
        if (this.showModel) {
            matrixStackIn.pushPose();
            matrixStackIn.scale(scaleX,scaleY,scaleZ);
            matrixStackIn.translate(positionX/16.0f,-positionY/16.0f,positionZ/16.0f);
            if (!this.cubeList.isEmpty() || !this.childModels.isEmpty()) {
                matrixStackIn.pushPose();
                this.translateRotate(matrixStackIn);
                model.onRenderModelCube(this,matrixStackIn,bufferIn,renderCall,packedLightIn,packedOverlayIn,red,green,blue,alpha);
                if (resetBuffer) {
                    bufferIn = model.returnDefaultBuffer();
                }
                this.doRender(matrixStackIn.last(), bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
                for(AdvancedCubeRenderer modelRenderer : this.childModels) {
                    modelRenderer.render(model,matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha,renderCall,resetBuffer);
                }
                matrixStackIn.popPose();
            }
            matrixStackIn.popPose();
        }
    }

    public void translateRotate(PoseStack matrixStackIn) {
        matrixStackIn.translate((double)(rotationPoint.getX() / 16.0F)/scaleX, (double)(rotationPoint.getY() / 16.0F)/scaleY, (double)(rotationPoint.getZ() / 16.0F)/scaleZ);
        if (defaultRotation.z+rotationZ != 0.0F) {
            matrixStackIn.mulPose(Vector3f.ZP.rotation(defaultRotation.z+rotationZ));
        }
        if (defaultRotation.y+rotationY != 0.0F) {
            matrixStackIn.mulPose(Vector3f.YP.rotation(defaultRotation.y+rotationY));
        }
        if (defaultRotation.x+rotationX != 0.0F) {
            matrixStackIn.mulPose(Vector3f.XP.rotation(defaultRotation.x+rotationX));
        }
    }

    public void doRender(PoseStack.Pose matrixEntryIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        Matrix4f matrix4f = matrixEntryIn.pose();
        Matrix3f matrix3f = matrixEntryIn.normal();
        for(ModelBox modelBox : this.cubeList) {
            for(TexturedQuad textureQuad : modelBox.getQuads()) {
                Vector3f vector3f = textureQuad.normal.copy();
                vector3f.transform(matrix3f);
                float f = vector3f.x();
                float f1 = vector3f.y();
                float f2 = vector3f.z();
                for(int i = 0; i < 4; ++i){
                    PositionTextureVertex vertexPosition = textureQuad.vertexPositions[i];
                    float f3 = vertexPosition.position.x() / 16.0F;
                    float f4 = vertexPosition.position.y() / 16.0F;
                    float f5 = vertexPosition.position.z() / 16.0F;
                    Vector4f vector4f = new Vector4f(f3, f4, f5, 1.0F);
                    vector4f.transform(matrix4f);
                    float textureU = vertexPosition.textureU;
                    float textureV = vertexPosition.textureV;
                    bufferIn.vertex(vector4f.x(), vector4f.y(), vector4f.z(), red, green, blue, alpha, textureU,textureV , packedOverlayIn, packedLightIn, f, f1, f2);
                }
            }
        }

    }

    public ObjectList<AdvancedCubeRenderer> getChildModels() {
        return childModels;
    }

    public boolean isChild(AdvancedCubeRenderer child){
        for (AdvancedCubeRenderer childModel : getChildModels()) {
            if (childModel == child){
                return true;
            }
        }
        return false;
    }

    public void addChild(AdvancedCubeRenderer renderer) {
        this.childModels.add(renderer);
    }

    public void addChild(AdvancedCubeRenderer... renderers) {
        for (AdvancedCubeRenderer renderer : renderers) {
            addChild(renderer);
        }
    }

    public AdvancedCubeRenderer defaultRotation(float x,float y,float z){
        this.defaultRotation = new Vector3(x,y,z);
        return this;
    }

    public Vector3 getDefaultRotation() {
        return defaultRotation;
    }

    public String getName() {
        return name;
    }
}
