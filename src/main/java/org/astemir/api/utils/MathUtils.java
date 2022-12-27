package org.astemir.api.utils;



public class MathUtils {

    public static float PI = 3.14159265358979323846f;

    public static float abs(float value){ return Math.abs(value);}

    public static float rad(float angle) {
        return angle * PI / 180;
    }

    public static float deg(float angle) {
        return angle * 180/PI;
    }

    public static float sqrt(float value){
        return (float)Math.sqrt(value);
    }

    public static float cos(float value){
        return (float)Math.cos(value);
    }

    public static float sin(float value){
        return (float)Math.sin(value);
    }

    public static float atan2(float y,float x){
        return (float)Math.atan2(y,x);
    }

    public static float asin(float a){
        return (float)Math.asin(a);
    }

    public static float lerp(float a, float b, float t){
        return a + t * (b - a);
    }

    public static float lerpRot(float a, float b, float t) {
       float wrap = MathUtils.rad(wrapDegrees(MathUtils.deg(b)-MathUtils.deg(a)));
       return a + t * wrap;
    }

    public static float shortestAngle(float a,float b){
        return ((b-a) + 180) % 360 - 180;
    }

    public static float wrapDegrees(float angle) {
        float f = angle % 360.0F;
        if (f >= 180.0F) {
            f -= 360.0F;
        }
        if (f < -180.0F) {
            f += 360.0F;
        }
        return f;
    }

    public static float clamp(float val, float min, float max) {
        return Math.max(min, Math.min(max, val));
    }

    public static boolean equalsApprox(float a, float b){
        float diff = Math.abs(b - a);
        float tolerance = 0.1f/100 * b;
        return diff < tolerance;
    }

    public static float progressOfTime(float ticks,float value){
        return ((float) (ticks % value)) / value;
    }

    public static float arrange(float oldMin, float oldMax, float newMin, float newMax, float value){
        float oldRange = (oldMax - oldMin);
        float newValue;
        if (oldRange == 0) {
            newValue = newMin;
        }
        else {
            float newRange = (newMax - newMin);
            newValue = (((value - oldMin) * newRange) / oldRange) + newMin;
        }
        return newValue;
    }

}
