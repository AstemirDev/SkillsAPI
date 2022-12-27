package org.astemir.api.utils;
import com.mojang.math.Vector3f;
import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.util.Mth;
import org.astemir.api.client.animation.AnimationFrame;
import org.astemir.api.math.Vector3;
import org.lwjgl.system.MathUtil;

public class AnimationUtils {

    public static float length(AnimationFrame[] points){
        return points[points.length-1].getPosition();
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

    public static float catmullrom(float previous, float current, float next, float nextNext,float f) {
        return 0.5F * (2.0F * current + (next - previous) * f + (2.0F * previous - 5.0F * current + 4.0F * next - nextNext) * f * f + (3.0F * current - previous - 3.0F * next + nextNext) * f * f * f);
    }


    public static int getCurrentIndex(AnimationFrame[] frames,float time){
        for (int i = 0; i < frames.length; i++) {
            AnimationFrame frame = frames[i];
            if (time <= frame.getPosition()){
                return i;
            }
        }
        return frames.length-1;
    }

    public static int getNextIndex(AnimationFrame[] frames,float time){
        int cur = getCurrentIndex(frames,time);
        if (cur+1 > frames.length-1){
            return 0;
        }
        return cur+1;
    }

    public static Vector3 interpolatePointsCatmullRom(AnimationFrame[] frames, float position){
        if (frames.length > 1) {
            float value = 1.0f;
            int i = Math.max(0, Mth.binarySearch(0, frames.length, (p_232315_) -> {
                return position <= frames[p_232315_].getPosition();
            }) - 1);
            int j = Math.min(frames.length - 1, i + 1);
            AnimationFrame keyframe = frames[i];
            AnimationFrame keyframe1 = frames[j];
            float f1 = position - keyframe.getPosition();
            float f2 = Mth.clamp(f1 / (keyframe1.getPosition() - keyframe.getPosition()), 0.0F, 1.0F);
            Vector3 previousValue = frames[Math.max(0, i - 1)].getValue();
            Vector3 currentValue = frames[i].getValue();
            Vector3 nextValue = frames[j].getValue();
            Vector3 nextNextValue = frames[Math.min(frames.length - 1, j + 1)].getValue();
            return new Vector3(catmullrom(previousValue.x, currentValue.x, nextValue.x, nextNextValue.x, f2) * value, catmullrom(previousValue.y, currentValue.y, nextValue.y, nextNextValue.y, f2) * value, catmullrom(previousValue.z, currentValue.z, nextValue.z, nextNextValue.z, f2) * value);
        }else{
            return interpolatePoints(frames,position);
        }
    }

    public static Vector3 interpolatePoints(AnimationFrame[] points, float position){
        float delta = 0;
        if (points.length == 0) {
            return Vector3.ZERO;
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
            AnimationFrame point = points[i];
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
        return Vector3.ZERO;
    }


}
