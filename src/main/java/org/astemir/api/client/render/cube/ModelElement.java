package org.astemir.api.client.render.cube;


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix3f;
import com.mojang.math.Matrix4f;
import com.mojang.math.Vector3f;
import com.mojang.math.Vector4f;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import net.minecraft.client.model.geom.ModelPart;
import org.astemir.api.client.render.RenderCall;
import org.astemir.api.client.model.AdvancedModel;
import org.astemir.api.math.Transform;
import org.astemir.api.math.Vector2;
import org.astemir.api.math.Vector3;


public class ModelElement {


    private final ObjectList<ModelCube> cubeList = new ObjectArrayList<>();
    private final ObjectList<ModelElement> childModels = new ObjectArrayList<>();

    private ModelElement parent;

    public float rotationX;
    public float rotationY;
    public float rotationZ;

    public float customRotationX;
    public float customRotationY;
    public float customRotationZ;


    public float positionX;
    public float positionY;
    public float positionZ;

    public float scaleX = 1;
    public float scaleY = 1;
    public float scaleZ = 1;

    public Vector3 defaultRotation = new Vector3(0,0,0);
    public Vector3 rotationPoint = new Vector3(0,0,0);
    public Vector2 textureSize = new Vector2(64,32);
    public Vector2 textureOffset = new Vector2(0,0);
    private String name;

    public boolean mirror = false;
    public boolean showModel = true;
    public boolean isRoot = false;


    public ModelElement(String name, int textureWidthIn, int textureHeightIn, int textureOffsetXIn, int textureOffsetYIn) {
        this.name = name;
        this.textureSize(textureWidthIn, textureHeightIn);
        this.textureOffset(textureOffsetXIn, textureOffsetYIn);
    }

    public void copyProperties(ModelPart part){
        this.rotationPoint.x = part.x;
        this.rotationPoint.y = part.y;
        this.rotationPoint.z = part.z;
        this.rotationX = part.xRot;
        this.rotationY = part.yRot;
        this.rotationZ = part.zRot;
        this.scaleX = part.xScale;
        this.scaleY = part.yScale;
        this.scaleZ = part.zScale;
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

    public void setCustomRotation(Vector3 rot){
        this.customRotationX = rot.x;
        this.customRotationY = rot.y;
        this.customRotationZ = rot.z;
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

    public ModelElement cube(Vector3 position, Vector3 size, float delta, boolean mirrorIn) {
        return this.cube(new Vector2(textureOffset.getX(), textureOffset.getY()), position,size, new Vector3(delta, delta, delta),mirrorIn);
    }

    private ModelElement cube(Vector2 textureOffset, Vector3 position, Vector3 size, Vector3 delta, boolean mirrorIn) {
        this.cubeList.add(new ModelCube((int)textureOffset.x, (int)textureOffset.y, position.x,position.y, position.z, size.x, size.y, size.z, delta.x, delta.y, delta.z, mirrorIn, this.textureSize.getX(), this.textureSize.getY()));
        return this;
    }

    public ModelElement rotationPoint(float rotationPointXIn, float rotationPointYIn, float rotationPointZIn) {
        this.rotationPoint = new Vector3(rotationPointXIn,rotationPointYIn,rotationPointZIn);
        return this;
    }

    public ModelElement textureSize(int textureWidthIn, int textureHeightIn) {
        this.textureSize = new Vector2(textureWidthIn,textureHeightIn);
        return this;
    }

    public ModelElement textureOffset(int x, int y) {
        this.textureOffset = new Vector2(x,y);
        return this;
    }


    public void render(AdvancedModel model, PoseStack matrixStackIn, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha, RenderCall renderCall, boolean resetBuffer) {
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
                for(ModelElement modelRenderer : this.childModels) {
                    modelRenderer.render(model,matrixStackIn, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha,renderCall,resetBuffer);
                }
                matrixStackIn.popPose();
            }
            matrixStackIn.popPose();
        }
    }

    public void translateRotate(PoseStack matrixStackIn) {
        matrixStackIn.translate((double)(rotationPoint.getX() / 16.0F)/scaleX, (double)(rotationPoint.getY() / 16.0F)/scaleY, (double)(rotationPoint.getZ() / 16.0F)/scaleZ);
        if (defaultRotation.z+rotationZ+customRotationZ != 0.0F) {
            matrixStackIn.mulPose(Vector3f.ZP.rotation(defaultRotation.z+rotationZ+customRotationZ));
        }
        if (defaultRotation.y+rotationY+customRotationY != 0.0F) {
            matrixStackIn.mulPose(Vector3f.YP.rotation(defaultRotation.y+rotationY+customRotationY));
        }
        if (defaultRotation.x+rotationX+customRotationX != 0.0F) {
            matrixStackIn.mulPose(Vector3f.XP.rotation(defaultRotation.x+rotationX+customRotationX));
        }
    }

    public void doRender(PoseStack.Pose entry,VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        for(ModelCube modelBox : this.cubeList) {
            Matrix4f matrix4f = entry.pose();
            Matrix3f matrix3f = entry.normal();
            for(TexturedQuad textureQuad : modelBox.getQuads()) {
                Vector3f vector3f = textureQuad.normal.copy();
                vector3f.transform(matrix3f);
                float f = vector3f.x();
                float f1 = vector3f.y();
                float f2 = vector3f.z();
                for(int i = 0; i < 4; ++i){
                    TextureVertex vertexPosition = textureQuad.vertexPositions[i];
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

    public ObjectList<ModelElement> getChildModels() {
        return childModels;
    }

    public boolean isChild(ModelElement child){
        for (ModelElement childModel : getChildModels()) {
            if (childModel == child){
                return true;
            }
        }
        return false;
    }

    public ModelElement addChild(ModelElement renderer) {
        this.childModels.add(renderer);
        return this;
    }

    public ModelElement addChildren(ModelElement... renderers) {
        for (ModelElement renderer : renderers) {
            addChild(renderer);
        }
        return this;
    }

    public ModelElement parent(ModelElement parent) {
        this.parent = parent;
        return this;
    }

    public ModelElement root() {
        this.isRoot = true;
        return this;
    }

    public ModelElement defaultRotation(float x, float y, float z){
        this.defaultRotation = new Vector3(x,y,z);
        return this;
    }

    public ModelElement getParent() {
        return parent;
    }

    public Vector3 getCustomRotation(){
        return new Vector3(customRotationX,customRotationY,customRotationZ);
    }

    public Vector3 getRotation(){
        return new Vector3(rotationX,rotationY,rotationZ);
    }

    public Vector3 getScale() { return new Vector3(scaleX,scaleY,scaleZ); }

    public Vector3 getPosition(){
        return new Vector3(positionX,positionY,positionZ);
    }

    public Vector3 getDefaultRotation() {
        return defaultRotation;
    }

    public String getName() {
        return name;
    }
}
