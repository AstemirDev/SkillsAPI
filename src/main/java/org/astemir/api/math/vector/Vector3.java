package org.astemir.api.math.vector;


import com.mojang.math.Vector3d;
import com.mojang.math.Vector3f;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.phys.Vec3;
import org.astemir.api.math.IMathOperand;
import org.astemir.api.math.MathUtils;

public class Vector3 implements IMathOperand<Vector3> {

    public float x;
    public float y;
    public float z;

    public Vector3(float x, float y, float z) {
        this.x = MathUtils.floatSafe(x);
        this.y = MathUtils.floatSafe(y);
        this.z = MathUtils.floatSafe(z);
    }

    public Vector3(double x, double y, double z) {
        this.x = MathUtils.floatSafe((float)x);
        this.y = MathUtils.floatSafe((float)y);
        this.z = MathUtils.floatSafe((float)z);
    }

    @Override
    public Vector3 copy(){
        return new Vector3(x,y,z);
    }

    @Override
    public Vector3 add(Vector3 vector){
        return new Vector3(x+vector.getX(),y+vector.getY(),z+vector.getZ());
    }

    @Override
    public Vector3 clamp(Vector3 min, Vector3 max) {
        return new Vector3(MathUtils.clamp(x,min.x,max.x),MathUtils.clamp(y,min.y,max.y),MathUtils.clamp(z,min.z,max.z));
    }

    @Override
    public Vector3 lerp(Vector3 vector, float t){
        return new Vector3(MathUtils.lerp(x,vector.getX(),t),MathUtils.lerp(y,vector.getY(),t),MathUtils.lerp(z,vector.getZ(),t));
    }

    public Vector3 rotLerp(Vector3 vector, float t){
        return new Vector3(MathUtils.lerpRot(x,vector.getX(),t),MathUtils.lerpRot(y,vector.getY(),t),MathUtils.lerpRot(z,vector.getZ(),t));
    }

    public static Vector3 from(float x, float y, float z){
        return new Vector3(x,y,z);
    }

    public static Vector3 from(BlockPos pos){
        return new Vector3(pos.getX(),pos.getY(),pos.getZ());
    }

    public static Vector3 from(Vector3d vector) {
        return Vector3.from((float)vector.x,(float)vector.y,(float)vector.z);
    }

    public static Vector3 from(Vec3 vector) {
        return Vector3.from((float)vector.x,(float)vector.y,(float)vector.z);
    }

    public static Vector3 from(Vector3f vector) {
        return Vector3.from(vector.x(),vector.y(),vector.z());
    }

    public static Vector3 rad(float x, float y, float z){
        return new Vector3(MathUtils.rad(x),MathUtils.rad(y),MathUtils.rad(z));
    }

    public Vector3 rotateAroundX(double angle) {
        double angleCos = Math.cos(angle);
        double angleSin = Math.sin(angle);
        double y = angleCos * getY() - angleSin * getZ();
        double z = angleSin * getY() + angleCos * getZ();
        return new Vector3(this.x,y,z);
    }

    public Vector3 rotateAroundY(double angle) {
        double angleCos = Math.cos(angle);
        double angleSin = Math.sin(angle);
        double x = angleCos * getX() + angleSin * getZ();
        double z = -angleSin * getX() + angleCos * getZ();
        return new Vector3(x,y,z);
    }

    public Vector3 rotateAroundZ(double angle) {
        double angleCos = Math.cos(angle);
        double angleSin = Math.sin(angle);
        double x = angleCos * getX() - angleSin * getY();
        double y = angleSin * getX() + angleCos * getY();
        return new Vector3(x,y,z);
    }

    public Vector3 add(Vector3 vector,float delta){
        Vector3 newVector = new Vector3(x+vector.getX(),y+vector.getY(),z+vector.getZ());
        return lerp(newVector,delta);
    }

    public double length() {
        return Math.sqrt(this.x * this.x + this.y * this.y + this.z * this.z);
    }


    public Vector3 sub(Vector3 vector){
        return new Vector3(x-vector.getX(),y-vector.getY(),z-vector.getZ());
    }

    public Vector3 mul(Vector3 vector){
        return new Vector3(x*vector.getX(),y*vector.getY(),z*vector.getZ());
    }

    public Vector3 div(Vector3 vector){
        return new Vector3(x/vector.getX(),y/vector.getY(),z/vector.getZ());
    }

    public Vector3 add(float x1,float y1,float z1){
        return new Vector3(x+x1,y+y1,z+z1);
    }

    public Vector3 sub(float x1,float y1,float z1){
        return new Vector3(x/x1,y/y1,z/z1);
    }

    public Vector3 mul(float x1,float y1,float z1){
        return new Vector3(x*x1,y*y1,z*z1);
    }

    public Vector3 div(float x1,float y1,float z1){
        return new Vector3(x/x1,y/y1,z/z1);
    }

    public Vector3 add(float t){
        return new Vector3(x+t,y+t,z+t);
    }

    public Vector3 sub(float t){
        return new Vector3(x-t,y-t,z-t);
    }

    public Vector3 mul(float t){
        return new Vector3(x*t,y*t,z*t);
    }

    public Vector3 div(float t){
        return new Vector3(x/t,y/t,z/t);
    }


    public float magnitude(){
        return (float) Math.sqrt(x*x+y*y+z*z);
    }

    public float lengthSquared(){
        return x*x+y*y+z*z;
    }

    public Vector3 normalize(){
        float magnitude = magnitude();
        if (magnitude > 0) {
            return div(magnitude);
        }else{
            return new Vector3(x,y,z);
        }
    }

    public Vector3 direction(Vector3 vector){
        return vector.sub(this).normalize();
    }

    public Vector3 directionWithoutNormalize(Vector3 vector){
        return vector.sub(this);
    }

    public Vector3 rotation(){
        Vector3 dir = normalize();
        return new Vector3(MathUtils.cos(dir.x),MathUtils.sin(dir.y),MathUtils.sin(dir.z));
    }

    public Vector3 rotationDegrees(){
        Vector3 dir = normalize();
        return new Vector3(MathUtils.deg(MathUtils.cos(dir.x)),MathUtils.deg(MathUtils.sin(dir.y)),MathUtils.deg(MathUtils.sin(dir.z)));
    }

    public Vector2 yawPitch(){
        Vector3 dir = normalize();
        return new Vector2(MathUtils.atan2(dir.x,dir.z),MathUtils.asin(dir.y));
    }

    public Vector2 yawPitchDeg(){
        Vector3 dir = normalize();
        return new Vector2(MathUtils.deg(MathUtils.atan2(dir.x,dir.z)),MathUtils.deg(MathUtils.asin(dir.y)));
    }

    public static Vector3 fromYawPitch(float yaw,float pitch) {
        double pitchRadians = Math.toRadians(pitch);
        double yawRadians = Math.toRadians(yaw);
        double sinPitch = Math.sin(pitchRadians);
        double cosPitch = Math.cos(pitchRadians);
        double sinYaw = Math.sin(yawRadians);
        double cosYaw = Math.cos(yawRadians);
        return new Vector3(-cosPitch * sinYaw, sinPitch, -cosPitch * cosYaw);
    }


    public float distanceTo(Vector3 vector){
        return MathUtils.sqrt(distanceToSquared(vector));
    }

    public float distanceToSquared(Vector3 vector){
        float resX = (this.x-vector.x)*(this.x-vector.x);
        float resY = (this.y-vector.y)*(this.y-vector.y);
        float resZ = (this.z-vector.z)*(this.z-vector.z);
        return resX+resY+resZ;
    }

    public float distanceToX(Vector3 vector){
        float res = (this.x-vector.x)*(this.x-vector.x);
        return MathUtils.sqrt(res);
    }

    public float distanceToY(Vector3 vector){
        float res = (this.y-vector.y)*(this.y-vector.y);
        return MathUtils.sqrt(res);
    }

    public float distanceToZ(Vector3 vector){
        float res = (this.z-vector.z)*(this.z-vector.z);
        return MathUtils.sqrt(res);
    }


    public float distanceToXSquared(Vector3 vector){
        float res = (this.x-vector.x)*(this.x-vector.x);
        return res;
    }

    public float distanceToYSquared(Vector3 vector){
        float res = (this.y-vector.y)*(this.y-vector.y);
        return res;
    }

    public float distanceToZSquared(Vector3 vector){
        float res = (this.z-vector.z)*(this.z-vector.z);
        return res;
    }

    public Vector3 catmullrom(Vector3 previous,Vector3 next,Vector3 nextNext,float f){
        float t2 = f * f;
        float f1 = (float) (-0.5 * (f * f * f) + t2 - 0.5 * f);
        float f2 = (float) (1.5 * (f * f * f) - 2.5 * t2 + 1.0);
        float f3 = (float) (-1.5 * (f * f * f) + 2.0 * t2 + 0.5 * f);
        float f4 = (float) (0.5 * (f * f * f) - 0.5 * t2);
        float x = previous.x * f1 + this.x * f2 + next.x * f3 + nextNext.x * f4;
        float y = previous.y * f1 + this.y * f2 + next.y * f3 + nextNext.y * f4;
        float z = previous.z * f1 + this.z * f2 + next.z * f3 + nextNext.z * f4;
        return new Vector3(x,y,z);
    }


    public Vector3d toVector3d(){
        return new Vector3d(x,y,z);
    }

    public Vector3f toVector3f(){
        return new Vector3f(x,y,z);
    }

    public Vec3 toVec3(){
        return new Vec3(x,y,z);
    }

    public Vector2 xy(){
        return new Vector2(x,y);
    }

    public Vector2 zy(){
        return new Vector2(z,y);
    }

    public Vector2 xz(){
        return new Vector2(x,z);
    }

    public Vector3 wrapDegrees(){
        return new Vector3(MathUtils.wrapDegrees(x),MathUtils.wrapDegrees(y),MathUtils.wrapDegrees(z));
    }

    public Vector3 wrapRadians(){
        return new Vector3(MathUtils.wrapRadians(x),MathUtils.wrapRadians(y),MathUtils.wrapRadians(z));
    }

    public boolean equalsApprox(Vector3 vector){
        return MathUtils.equalsApprox(x,vector.x) && MathUtils.equalsApprox(y,vector.y) && MathUtils.equalsApprox(z,vector.z);
    }

    public CompoundTag toNbt(){
        CompoundTag tag = new CompoundTag();
        tag.putFloat("x",x);
        tag.putFloat("y",y);
        tag.putFloat("z",z);
        return tag;
    }

    public static Vector3 fromNbt(CompoundTag tag){
        return new Vector3(tag.getFloat("x"),tag.getFloat("y"),tag.getFloat("z"));
    }

    public float[] toFloatArray(Vector3 vector3){
        return new float[]{vector3.x,vector3.y,vector3.z};
    }

    public static Vector3 fromFloatArray(float[] array){
        return new Vector3(array[0],array[1],array[2]);
    }

    public static Vector3 zero(){
        return new Vector3(0,0,0);
    }

    public static Vector3 one(){
        return new Vector3(1,1,1);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setZ(float z) {
        this.z = z;
    }

    @Override
    public String toString() {
        return "[x="+x+";y="+y+";z="+z+"]";
    }
}
