package org.astemir.api.common.entity;

import org.astemir.api.math.MathUtils;

public class ClientSideValue {

    private float value = 1;
    private float valueTo = 1;
    public float valueO;

    public ClientSideValue(float value, float valueTo) {
        this.value = value;
        this.valueO = value;
        this.valueTo = valueTo;
    }

    public void update(float speed){
        valueO = value;
        value = MathUtils.lerp(value,valueTo,speed);
    }

    public ClientSideValue setTo(float value){
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
