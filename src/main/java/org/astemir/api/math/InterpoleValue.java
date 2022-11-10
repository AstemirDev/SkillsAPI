package org.astemir.api.math;

import org.astemir.api.utils.MathUtils;

public class InterpoleValue {

    private float value = 1;
    private float valueTo = 1;
    public float valueO;

    public InterpoleValue(float value, float valueTo) {
        this.value = value;
        this.valueO = value;
        this.valueTo = valueTo;
    }

    public void update(float speed){
        valueO = value;
        value = MathUtils.lerp(value,valueTo,speed);
    }

    public InterpoleValue setTo(float value){
        this.valueTo = value;
        return this;
    }

    public float value(float partialTicks) {
        float res = MathUtils.lerp(valueO,value,partialTicks);
        if (MathUtils.equalsApprox(valueO,value)){
            return value;
        }else {
            return res;
        }
    }
}
