package org.astemir.api.math.components;

import org.astemir.api.math.MathUtils;
import org.astemir.api.math.components.Vector3;

public class FDG {

    public void circle(float radius, float step, Vector3 rotation) {
        for (float i = 0;i<Math.PI*2;i+=step){
            Vector3 vector = new Vector3(MathUtils.cos(i)*radius, MathUtils.sin(i)*radius,0);
            vector = vector.rotateAroundX(rotation.x).rotateAroundY(rotation.y).rotateAroundZ(rotation.z);
            onExecute(vector);
        }
    }

    public void sphere(float radius,float step,Vector3 rotation) {
        for (float i = 0;i<Math.PI*2;i+=step){
            for (float j = 0;j<Math.PI*2;j+=step) {
                Vector3 vector = new Vector3(MathUtils.cos(i) * radius, MathUtils.sin(i) * radius, 0);
                vector = vector.rotateAroundX(rotation.x+j).rotateAroundY(rotation.y+j).rotateAroundZ(rotation.z);
                onExecute(vector);
            }
        }
    }

    public void cube(Vector3 size,float step,Vector3 rotation){
        for (float i = -size.x/2;i<=size.x/2;i+=step){
            for (float j = -size.y/2;j<=size.y/2;j+=step){
                for (float k = -size.z/2;k<=size.z/2;k+=step){
                    Vector3 vector = new Vector3(i,j,k);
                    vector = vector.rotateAroundX(rotation.x).rotateAroundY(rotation.y).rotateAroundZ(rotation.z);
                    onExecute(vector);
                }
            }
        }
    }

    public void hollowCube(Vector3 size,float step,Vector3 rotation){
        for (float i = -size.x/2;i<=size.x/2;i+=step){
            for (float j = -size.y/2;j<=size.y/2;j+=step){
                for (float k = -size.z/2;k<=size.z/2;k+=step){
                    if (MathUtils.equalsApprox(i,Math.abs(size.x/2)) || MathUtils.equalsApprox(j,Math.abs(size.y/2)) || MathUtils.equalsApprox(k,Math.abs(size.z/2))) {
                        Vector3 vector = new Vector3(i, j, k);
                        vector = vector.rotateAroundX(rotation.x).rotateAroundY(rotation.y).rotateAroundZ(rotation.z);
                        onExecute(vector);
                    }
                }
            }
        }
    }


    public void onExecute(Vector3 point){};
}
