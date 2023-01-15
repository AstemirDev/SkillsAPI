package org.astemir.api.math;

import com.mojang.math.Vector3f;
import net.minecraft.world.phys.Vec3;
import org.astemir.api.utils.MathUtils;
import org.checkerframework.checker.units.qual.C;

public class Color {


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

    public int[] toArray(){
        int[] colors = new int[4];
        colors[0] = (int)(r*255);
        colors[1] = (int)(g*255);
        colors[2] = (int)(b*255);
        colors[3] = (int)(a*255);
        return colors;
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
        return new Color((float)(((float)r)/255f),(float)(((float)g)/255f),(float)(((float)b)/255f),(float)(((float)a)/255f));
    }

    public Color interpolate(Color color, float t){
        return new Color(MathUtils.lerp(r,color.r,t),MathUtils.lerp(g,color.g,t),MathUtils.lerp(b,color.b,t),MathUtils.lerp(a,color.a,t));
    }

    public boolean equalsApprox(Color color){
        return MathUtils.equalsApprox(r,color.r) && MathUtils.equalsApprox(g,color.g) && MathUtils.equalsApprox(b,color.b) && MathUtils.equalsApprox(a,color.a);
    }
}
