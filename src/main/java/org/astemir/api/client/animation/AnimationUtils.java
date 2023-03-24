package org.astemir.api.client.animation;
import net.minecraft.util.Mth;
import org.astemir.api.math.vector.Vector3;
import org.astemir.api.math.MathUtils;

import java.util.List;

public class AnimationUtils {

    public static float length(KeyFrame[] points){
        if (points != null && points.length > 0) {
            return points[points.length - 1].getPosition();
        }else{
            return 0f;
        }
    }

    public static float length(List<KeyFrame> points){
        if (points != null && points.size() > 0) {
            return points.get(points.size() - 1).getPosition();
        }else{
            return 0f;
        }
    }

    public static float flip(float x){
        return 1-x;
    }

    public static float easingIn(float f){
        return f*f;
    }

    public static float easingOut(float f){
        float square = flip(f)*flip(f);
        return flip(square);
    }

    public static float easingInOut(float f){
        return MathUtils.lerp(easingIn(f),easingOut(f),f);
    }

    public static float spike(float f){
        if (f < 0.5){
            return easingIn(f/0.5f);
        }
        return easingIn(flip(f)/0.5f);
    }


    public static int getCurrentIndex(KeyFrame[] frames, float time){
        for (int i = 0; i < frames.length; i++) {
            KeyFrame frame = frames[i];
            if (time <= frame.getPosition()){
                return i;
            }
        }
        return frames.length-1;
    }

    public static int getNextIndex(KeyFrame[] frames, float time){
        int cur = getCurrentIndex(frames,time);
        if (cur+1 > frames.length-1){
            return 0;
        }
        return cur+1;
    }

    public static Vector3 interpolatePointsCatmullRom(KeyFrame[] frames, float position){
        if (frames.length == 1 && position == 0){
            return interpolatePoints(frames,position);
        }else{
            float value = 1.0f;
            int i = Math.max(0, Mth.binarySearch(0, frames.length, (p_232315_) -> {
                return position <= frames[p_232315_].getPosition();
            }) - 1);
            int j = Math.min(frames.length - 1, i + 1);
            KeyFrame keyframe = frames[i];
            KeyFrame keyframe1 = frames[j];
            float f1 = position - keyframe.getPosition();
            float f2 = Mth.clamp(f1 / (keyframe1.getPosition() - keyframe.getPosition()), 0.0F, 1.0F);
            Vector3 previousValue = frames[Math.max(0, i - 1)].getValue();
            Vector3 currentValue = frames[i].getValue();
            Vector3 nextValue = frames[j].getValue();
            Vector3 nextNextValue = frames[Math.min(frames.length - 1, j + 1)].getValue();
            return new Vector3(MathUtils.catmullrom(previousValue.x, currentValue.x, nextValue.x, nextNextValue.x, f2) * value, MathUtils.catmullrom(previousValue.y, currentValue.y, nextValue.y, nextNextValue.y, f2) * value, MathUtils.catmullrom(previousValue.z, currentValue.z, nextValue.z, nextNextValue.z, f2) * value);
        }
    }

    public static Vector3 interpolatePoints(KeyFrame[] points, float position){
        float delta = 0;
        if (points.length == 0) {
            return Vector3.zero();
        }
        if (points.length == 1) {
            return points[0].getValue();
        }
        double firstPos = points[0].getPosition();
        double secondPos = points[points.length - 1].getPosition();
        Vector3 firstValue = points[0].getValue();
        Vector3 secondValue = points[points.length - 1].getValue();
        if (position < firstPos) {
            float c = (float) (delta * (firstPos - position));
            return firstValue.add(new Vector3(c,c,c).mul(-1));
        }
        else if (position > secondPos)
        {
            float c = (float) (delta * (position - secondPos));
            return secondValue.add(new Vector3(c,c,c));
        }
        for (int i = 1;i<points.length;i++) {
            KeyFrame point = points[i];
            if (point.getPosition() >= position) {
                float num1 = (float)((position - firstPos) / (point.getPosition() - firstPos));
                float num2 = num1 * num1;
                float num3 = num2 * num1;
                Vector3 num4 = firstValue.mul(2.0f * num3 - 3.0f * num2 + 1.0f);
                float num5 = (float)(num3 - 2.0 * num2 + num1) * delta;
                Vector3 num6 = point.getValue().mul(3.0f * num2 - 2.0f * num3);
                Vector3 num7 = num4.add(new Vector3(num5,num5,num5));
                Vector3 num8 = num7.add(num6);
                float num9 = (num3 - num2) * delta;
                return num8.add(new Vector3(num9,num9,num9));
            }
            firstPos = point.getPosition();
            firstValue = point.getValue();
        }
        return Vector3.zero();
    }



    public static float animationFraction(float time,float duration,float partialRenderTicks) {
        return (time + partialRenderTicks) / duration;
    }


    public static float progressSmooth(float time,float duration,float partialRenderTicks) {
        if (time > 0) {
            if (time < duration) {
                return (float) (1.0D / (1.0D + Math.exp(4.0D - 8.0D * animationFraction(time,duration,partialRenderTicks))));
            } else {
                return 1.0F;
            }
        }
        return 0.0F;
    }


    public static float progressStep(float time,float duration,float partialRenderTicks) {
        return (float) (1.0D / (1.0D + Math.exp(6.0D - 12.0D * animationFraction(time,duration,partialRenderTicks))));
    }


    public static float progressSin(float time,float duration,float partialRenderTicks) {
        return MathUtils.sin(1.57079632679F * animationFraction(time,duration,partialRenderTicks));
    }


    public static float progressSinSqrt(float time,float duration,float partialRenderTicks) {
        float result = MathUtils.sin(1.57079632679F * animationFraction(time,duration,partialRenderTicks));
        return result * result;
    }


    public static float progressSinToTen(float time,float duration,float partialRenderTicks) {
        return (float) Math.pow(MathUtils.sin(1.57079632679F * animationFraction(time,duration,partialRenderTicks)), 10);
    }

    public static float progressSinPowerOf(float time,float duration,float partialRenderTicks, int i) {
        return (float) Math.pow(MathUtils.sin(1.57079632679F * animationFraction(time,duration,partialRenderTicks)), i);
    }

    public static float progressPoly2(float time,float duration,float partialRenderTicks) {
        float x = animationFraction(time,duration,partialRenderTicks);
        float x2 = x * x;
        return x2 / (x2 + (1 - x) * (1 - x));
    }


    public static float progressPoly3(float time,float duration,float partialRenderTicks) {
        float x = animationFraction(time,duration,partialRenderTicks);
        float x3 = x * x * x;
        return x3 / (x3 + (1 - x) * (1 - x) * (1 - x));
    }

    public static float progressPolyN(float time,float duration,float partialRenderTicks, int n) {
        double x = animationFraction(time,duration,partialRenderTicks);
        double xi = Math.pow(x, n);
        return (float) (xi / (xi + Math.pow(1.0D - x, n)));
    }


    public static float progressArcTan(float time,float duration,float partialRenderTicks) {
        return (float) (0.5F + 0.49806510671F * Math.atan(3.14159265359D * (animationFraction(time,duration,partialRenderTicks) - 0.5D)));
    }


    public static float progressTemporary(float time,float duration,float partialRenderTicks) {
        float x = 6.28318530718F * animationFraction(time,duration,partialRenderTicks);
        return 0.5F - 0.5F * MathUtils.cos(x + MathUtils.sin(x));
    }

    public static float progressTemporaryFS(float time,float duration,float partialRenderTicks) {
        float x = 3.14159265359F * animationFraction(time,duration,partialRenderTicks);
        return MathUtils.sin(x + MathUtils.sin(x));
    }

    public static float progressTemporaryInversed(float time,float duration,float partialRenderTicks) {
        float x = 6.28318530718F * animationFraction(time,duration,partialRenderTicks);
        return 0.5F + 0.5F * MathUtils.cos(x + MathUtils.sin(x));
    }
}
