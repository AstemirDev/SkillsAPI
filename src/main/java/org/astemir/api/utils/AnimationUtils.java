package org.astemir.api.utils;
import org.astemir.api.client.animation.AnimationFrame;
import org.astemir.api.math.Vector3;

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

    public static float catmullrom(float p_216245_, float p_216246_, float p_216247_, float p_216248_, float p_216249_) {
        return 0.5F * (2.0F * p_216247_ + (p_216248_ - p_216246_) * p_216245_ + (2.0F * p_216246_ - 5.0F * p_216247_ + 4.0F * p_216248_ - p_216249_) * p_216245_ * p_216245_ + (3.0F * p_216247_ - p_216246_ - 3.0F * p_216248_ + p_216249_) * p_216245_ * p_216245_ * p_216245_);
    }

    public static Vector3 interpolateTestPoints(AnimationFrame[] points,float time){
        if (points.length > 0) {
            AnimationFrame current = points[0];
            for (AnimationFrame point : points) {
                if (time >= point.getPosition()) {
                    current = point;
                }
            }
            return current.getValue();
        }
        return Vector3.ZERO;
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
