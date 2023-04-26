package org.astemir.api.math;


import com.mojang.math.Quaternion;
import net.minecraft.util.Mth;
import net.minecraftforge.common.util.TransformationHelper;
import org.astemir.api.math.components.Rect2;
import org.astemir.api.math.components.Vector3;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class MathUtils {

    public static float PI = 3.14159265358979323846f;

    public static float PI_HALF = 3.14159265358979323846f/2f;

    public static float PI2 = PI*2;

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

    public static float acos(float value){
        return (float)Math.acos(value);
    }

    public static float atan2(float y,float x){
        return (float)Math.atan2(y,x);
    }

    public static float asin(float a){
        return (float)Math.asin(a);
    }

    public static float lerp(float a, float b, float t){
        return (1 - t) * a + t * b;
    }


    public static float lerpRot(float a, float b, float t) {
        float wrap = MathUtils.rad(wrapDegrees(MathUtils.deg(b)-MathUtils.deg(a)));
        return a +t*wrap;
    }

    public static float floatSafe(float f){
        if (Float.isNaN(f)){
            return 0;
        }
        return f;
    }


    public static Quaternion quaternionFromAngle(Vector3 vector){
        float qx = sin(vector.z/2) * cos(vector.x/2) * cos(vector.y/2) - cos(vector.z/2) * sin(vector.x/2) * sin(vector.y/2);
        float qy = cos(vector.z/2) * sin(vector.x/2) * cos(vector.y/2) + sin(vector.z/2) * cos(vector.x/2) * sin(vector.y/2);
        float qz = cos(vector.z/2) * cos(vector.x/2) * sin(vector.y/2) - sin(vector.z/2) * sin(vector.x/2) * cos(vector.y/2);
        float qw = cos(vector.z/2) * cos(vector.x/2) * cos(vector.y/2) + sin(vector.z/2) * sin(vector.x/2) * sin(vector.y/2);
        return new Quaternion(qx,qy,qz,qw);
    }

    public static Vector3 quaternionToAngle(Quaternion quaternion){
        float x = quaternion.i();
        float y = quaternion.j();
        float z = quaternion.k();
        float w = quaternion.r();
        float t0 = (float) (+2.0 * (w * x + y * z));
        float t1 = (float) (+1.0 - 2.0 * (x * x + y * y));
        float roll = atan2(t0, t1);
        float t2 = (float) (+2.0 * (w * y - z * x));
        if (t2 > 1.0f){
            t2 = 1.0f;
        }else
        if (t2 < -1.0f){
            t2 = -1.0f;
        }
        float pitch = asin(t2);
        float t3 = (float) (+2.0 * (w * z + x * y));
        float t4 = (float) (+1.0 - 2.0 * (y * y + z * z));
        float yaw = atan2(t3, t4);
        return new Vector3(yaw,pitch,roll);
    }



    public static Quaternion slerp(Quaternion from,Quaternion to,float alpha){
        float cosom = Math.fma(from.i(), to.i(), Math.fma(from.j(), to.j(), Math.fma(from.k(), to.k(), from.r() * to.r())));
        float absCosom = Math.abs(cosom);
        float scale0, scale1;
        if (1.0f - absCosom > 1E-6f) {
            float sinSqr = 1.0f - absCosom * absCosom;
            float sinom = 1.0f/MathUtils.sqrt(sinSqr);
            float omega = (float) Math.atan2(sinSqr * sinom, absCosom);
            scale0 = (float) (Math.sin((1.0 - alpha) * omega) * sinom);
            scale1 = (float) (Math.sin(alpha * omega) * sinom);
        } else {
            scale0 = 1.0f - alpha;
            scale1 = alpha;
        }
        scale1 = cosom >= 0.0f ? scale1 : -scale1;
        return new Quaternion(Math.fma(scale0, from.i(), scale1 * to.i()),Math.fma(scale0, from.j(), scale1 * to.j()),Math.fma(scale0, from.k(), scale1 * to.k()),Math.fma(scale0, from.r(), scale1 * to.r()));
    }


    public static double cosFromSin(double sin, double angle) {
        double cos = sqrt((float) (1.0 - sin * sin));
        double a = angle + PI_HALF;
        double b = a - (int)(a / PI2) * PI2;
        if (b < 0.0)
            b = PI2 + b;
        if (b >= PI)
            return -cos;
        return cos;
    }

    public static float shortestAngle(float a,float b){
        return ((b-a) + 180) % 360 - 180;
    }

    public static float wrapRadians(float radians){
        return MathUtils.rad(wrapDegrees(MathUtils.deg(radians)));
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

    public static float catmullrom(float previous, float current, float next, float nextNext,float f) {
        return 0.5F * (2.0F * current + (next - previous) * f + (2.0F * previous - 5.0F * current + 4.0F * next - nextNext) * f * f + (3.0F * current - previous - 3.0F * next + nextNext) * f * f * f);
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


    public static List<Rect2> splitRectangleToSmaller(Rect2 rectangle, Rect2 smallRect){
        List<Rect2> rects = new ArrayList<>();
        int maxHorizontalSquares = Math.round(((float)rectangle.getWidth())/((float)smallRect.getWidth()));
        int maxVerticalSquares = Math.round(((float)rectangle.getHeight())/((float)smallRect.getHeight()));
        int posX = 0;
        for (int i = 1;i<maxHorizontalSquares+1;i++){
            int posY = 0;
            int newWidth = (int) (i*smallRect.getWidth());
            if (newWidth > rectangle.getWidth()){
                newWidth = (int) (newWidth-(newWidth-rectangle.getWidth()));
            }
            int width = newWidth-posX;
            for (int j = 1;j<maxVerticalSquares+1;j++){
                int newHeight = (int) (j*smallRect.getHeight());
                if (newHeight > rectangle.getHeight()){
                    newHeight = (int) (newHeight-(newHeight-rectangle.getHeight()));
                }
                int height = newHeight-posY;
                rects.add(new Rect2(posX,posY,width,height));
                posY+=height;
            }
            posX+=width;
        }
        return rects;
    }

    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ') nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length()) return 0;
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)` | number
            //        | functionName `(` expression `)` | functionName factor
            //        | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (;;) {
                    if      (eat('+')) x += parseTerm(); // addition
                    else if (eat('-')) x -= parseTerm(); // subtraction
                    else return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (;;) {
                    if      (eat('*')) x *= parseFactor(); // multiplication
                    else if (eat('/')) x /= parseFactor(); // division
                    else return x;
                }
            }

            double parseFactor() {
                if (eat('+')) return +parseFactor();
                if (eat('-')) return -parseFactor();

                double x;
                int startPos = this.pos;
                if (eat('(')) {
                    x = parseExpression();
                    if (!eat(')')) return 0;
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.') nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z') nextChar();
                    String func = str.substring(startPos, this.pos);
                    if (eat('(')) {
                        x = parseExpression();
                        if (!eat(')')) return 0;
                    } else {
                        x = parseFactor();
                    }
                    if (func.equals("sqrt")) x = Math.sqrt(x);
                    else if (func.equals("sin")) x = Math.sin(Math.toRadians(x));
                    else if (func.equals("cos")) x = Math.cos(Math.toRadians(x));
                    else if (func.equals("tan")) x = Math.tan(Math.toRadians(x));
                    return 0;
                } else {
                    return 0;
                }

                if (eat('^')) x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }
}

