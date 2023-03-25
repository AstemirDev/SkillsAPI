package org.astemir.api.math.vector;

import net.minecraft.nbt.CompoundTag;
import org.astemir.api.math.IMathOperand;
import org.astemir.api.math.MathUtils;

public class Vector2 implements IMathOperand<Vector2> {

    public float x;
    public float y;

    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public Vector2(double x, double y) {
        this.x = (float)x;
        this.y = (float)y;
    }


    public Vector2 add(Vector2 vector){
        return new Vector2(x+vector.getX(),y+vector.getY());
    }

    public Vector2 add(Vector2 vector, float delta){
        Vector2 newVector = new Vector2(x+vector.getX(),y+vector.getY());
        return interpolate(newVector,delta);
    }

    public Vector2 sub(Vector2 vector){
        return new Vector2(x-vector.getX(),y-vector.getY());
    }

    public Vector2 mul(Vector2 vector){
        return new Vector2(x*vector.getX(),y*vector.getY());
    }

    public Vector2 div(Vector2 vector){
        return new Vector2(x/vector.getX(),y/vector.getY());
    }

    public Vector2 add(float x1, float y1){
        return new Vector2(x*x1,y*y1);
    }

    public Vector2 sub(float x1, float y1){
        return new Vector2(x/x1,y/y1);
    }

    public Vector2 mul(float x1, float y1){
        return new Vector2(x*x1,y*y1);
    }

    public Vector2 div(float x1, float y1){
        return new Vector2(x/x1,y/y1);
    }

    public Vector2 add(float t){
        return new Vector2(x+t,y+t);
    }

    public Vector2 sub(float t){
        return new Vector2(x-t,y-t);
    }

    public Vector2 mul(float t){
        return new Vector2(x*t,y*t);
    }

    public Vector2 div(float t){
        return new Vector2(x/t,y/t);
    }

    public float magnitude(){
        return MathUtils.sqrt(x*x+y*y);
    }

    public Vector2 normalize(){
        float magnitude = magnitude();
        if (magnitude > 0) {
            return div(magnitude);
        }else{
            return new Vector2(x,y);
        }
    }

    public Vector2 direction(Vector2 vector){
        return vector.sub(this).normalize();
    }

    public Vector2 directionWithoutNormalize(Vector2 vector){
        return vector.sub(this);
    }


    public Vector2 rotation(){
        Vector2 dir = normalize();
        return new Vector2(MathUtils.cos(dir.x),MathUtils.sin(dir.y));
    }


    public float distanceTo(Vector2 vector){
        return MathUtils.sqrt(distanceToSquared(vector));
    }

    public float distanceToSquared(Vector2 vector){
        float resX = (this.x-vector.x)*(this.x-vector.x);
        float resY = (this.y-vector.y)*(this.y-vector.y);
        return resX+resY;
    }

    public Vector2 rotationDegrees(){
        Vector2 dir = normalize();
        return new Vector2(MathUtils.deg(MathUtils.cos(dir.x)),MathUtils.deg(MathUtils.sin(dir.y)));
    }

    public Vector2 interpolate(Vector2 vector, float t){
        return new Vector2(MathUtils.lerp(x,vector.getX(),t),MathUtils.lerp(y,vector.getY(),t));
    }


    public Vector2 clamp(Vector2 min, Vector2 max) {
        return new Vector2(MathUtils.clamp(x,min.x,max.x),MathUtils.clamp(y,min.y,max.y));
    }

    public static Vector2 create(float x, float y){
        return new Vector2(x,y);
    }

    public static Vector2 rad(float x, float y){
        return new Vector2(MathUtils.rad(x),MathUtils.rad(y));
    }

    public Vector2 lerp(Vector2 vector, float t){
        return new Vector2(MathUtils.lerp(x,vector.getX(),t),MathUtils.lerp(y,vector.getY(),t));
    }

    @Override
    public Vector2 copy() {
        return new Vector2(x,y);
    }

    public Vector2 catmullrom(Vector2 previous,Vector2 next,Vector2 nextNext,float f){
        float t2 = f * f;
        float f1 = (float) (-0.5 * (f * f * f) + t2 - 0.5 * f);
        float f2 = (float) (1.5 * (f * f * f) - 2.5 * t2 + 1.0);
        float f3 = (float) (-1.5 * (f * f * f) + 2.0 * t2 + 0.5 * f);
        float f4 = (float) (0.5 * (f * f * f) - 0.5 * t2);
        float x = previous.x * f1 + this.x * f2 + next.x * f3 + nextNext.x * f4;
        float y = previous.y * f1 + this.y * f2 + next.y * f3 + nextNext.y * f4;
        return new Vector2(x,y);
    }


    public Vector2 rotLerp(Vector2 vector, float t){
        return new Vector2(MathUtils.lerpRot(x,vector.getX(),t),MathUtils.lerpRot(y,vector.getY(),t));
    }

    public Vector2 wrapDegrees(){
        return new Vector2(MathUtils.wrapDegrees(x),MathUtils.wrapDegrees(y));
    }

    public Vector2 wrapRadians(){
        return new Vector2(MathUtils.wrapRadians(x),MathUtils.wrapRadians(y));
    }

    public boolean equalsApprox(Vector2 vector){
        return MathUtils.equalsApprox(x,vector.x) && MathUtils.equalsApprox(y,vector.y);
    }

    public CompoundTag toNbt(){
        CompoundTag tag = new CompoundTag();
        tag.putFloat("x",x);
        tag.putFloat("y",y);
        return tag;
    }

    public static Vector2 fromNbt(CompoundTag tag){
        return new Vector2(tag.getFloat("x"),tag.getFloat("y"));
    }

    public float[] toFloatArray(Vector2 vector2){
        return new float[]{vector2.x,vector2.y};
    }

    public static Vector2 fromFloatArray(float[] array){
        return new Vector2(array[0],array[1]);
    }

    public static Vector2 zero(){
        return new Vector2(0,0);
    }

    public static Vector2 one(){
        return new Vector2(1,1);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "[x="+x+";y="+y+"]";
    }
}
