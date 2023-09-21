package org.astemir.api.math.components;

import com.mojang.math.Vector3f;
import net.minecraft.world.phys.Vec3;
import org.astemir.api.math.MathUtils;

public class Color {

    private static final String HEX_PREFIX = "#";
    public static final Color RED = new Color(1f,0f,0f,1f);
    public static final Color GREEN = new Color(0f,1f,0f,1f);
    public static final Color DARK_GREEN = new Color(0.5f,0f,0f,0.5f);
    public static final Color BLUE = new Color(0f,0f,1f,1f);
    public static final Color DARK_BLUE = new Color(0f,0f,0.5f,1f);
    public static final Color YELLOW = new Color(1f,1f,0f,1f);
    public static final Color GOLD = new Color(1f,0.823529f,0.0392156f,1f);
    public static final Color ORANGE = new Color(1f,0.5f,0f,1f);
    public static final Color AQUA = new Color(0f,1f,1f,1f);
    public static final Color CYAN = new Color(0f,0.5f,0.5f,1f);
    public static final Color PURPLE = new Color(1f,0f,1f,1f);
    public static final Color PINK = new Color(1f,0.5f,1f,1f);
    public static final Color WHITE = new Color(1f,1f,1f,1f);
    public static final Color BLACK = new Color(0f,0f,0f,1f);
    public static final Color MAGENTA = new Color(1f,0f,0.78431372549f,1f);
    public static final Color LIGHT_GREEN = new Color(0.51764705882f,1,0,1);

    public float r;
    public float g;
    public float b;
    public float a;


    public Color(float r, float g, float b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public Color(int rgba) {
        this((float)(rgba >> 16 & 255) / 255.0F,(float)(rgba >> 8 & 255) / 255.0F,(float)(rgba & 255) / 255.0F,(float)(rgba >> 24 & 255) / 255.0F);
    }

    public int toRGB() {
        return (((int)(r * 255.0F) & 255) << 16) | (((int)(g * 255.0F) & 255) << 8) | ((int)(b * 255.0F) & 255);
    }

    public int getRGBA() {
        return (((int)(a * 255.0F) & 255) << 24) | (((int)(r * 255.0F) & 255) << 16) | (((int)(g * 255.0F) & 255) << 8) | ((int)(b * 255.0F) & 255);
    }

    public java.awt.Color toAWTColor(){
        return new java.awt.Color(r,g,b,a);
    }

    public int[] toArray(){
        int[] colors = new int[4];
        colors[0] = (int)(r*255);
        colors[1] = (int)(g*255);
        colors[2] = (int)(b*255);
        colors[3] = (int)(a*255);
        return colors;
    }

    public HSV getHSV(){
        float[] floats = new float[3];
        java.awt.Color.RGBtoHSB((int)(r*255),(int)(g*255),(int)(b*255),floats);
        return new HSV(floats[0],floats[1],floats[2]);
    }

    public void setHSV(HSV hsv){
        java.awt.Color color = new java.awt.Color(java.awt.Color.HSBtoRGB(hsv.h,hsv.s,hsv.v));
        r = ((float)color.getRed())/255f;
        g = ((float)color.getGreen())/255f;
        b = ((float)color.getBlue())/255f;
    }

    public Vector3f toVector3f(){
        return new Vector3f(r,g,b);
    }

    public Vec3 toVec3(){
        return new Vec3(r,g,b);
    }

    public Color copy(){
        return new Color(r,g,b,a);
    }

    public static Color fromArray(int[] array){
        return convert(array[0],array[1],array[2],array[3]);
    }

    public static Color convert(int r, int g, int b, int a) {
        return new Color(((float)r)/255f, ((float)g)/255f, ((float)b)/255f, ((float)a)/255f);
    }

    public Color interpolate(Color color, float t){
        return new Color(MathUtils.lerp(r,color.r,t),MathUtils.lerp(g,color.g,t),MathUtils.lerp(b,color.b,t),MathUtils.lerp(a,color.a,t));
    }

    public boolean equalsApprox(Color color){
        return MathUtils.equalsApprox(r,color.r) && MathUtils.equalsApprox(g,color.g) && MathUtils.equalsApprox(b,color.b) && MathUtils.equalsApprox(a,color.a);
    }

    public class HSV{
        public float h;
        public float s;
        public float v;

        public HSV(float h, float s, float v) {
            this.h = h;
            this.s = s;
            this.v = v;
        }
    }

    public static Color fromHexString(String string) {
        if (string.startsWith(HEX_PREFIX)) {
            try {
                final int hex = Integer.parseInt(string.substring(1), 16);
                return new Color(hex);
            } catch (final NumberFormatException e) {
                return null;
            }
        }
        return null;
    }
    public static Color fromName(String name){
        switch (name.toLowerCase()){
            case "red": return Color.RED;
            case "green": return Color.GREEN;
            case "light_green": return Color.LIGHT_GREEN;
            case "dark_green": return Color.DARK_GREEN;
            case "blue": return Color.BLUE;
            case "dark_blue": return Color.DARK_BLUE;
            case "yellow": return Color.YELLOW;
            case "gold": return Color.GOLD;
            case "orange": return Color.ORANGE;
            case "aqua": return Color.AQUA;
            case "cyan": return Color.CYAN;
            case "purple": return Color.PURPLE;
            case "pink": return Color.PINK;
            case "magenta": return Color.MAGENTA;
            case "white": return Color.WHITE;
            case "black": return Color.BLACK;
        }
        return Color.BLACK;
    }
}
