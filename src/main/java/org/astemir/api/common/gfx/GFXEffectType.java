package org.astemir.api.common.gfx;

public enum GFXEffectType {

    ROUGH_SHAKE,SOFT_SHAKE,BLACK_OUT,BLACK_IN,BLACK_IN_OUT;

    GFXEffectType() {
    }

    public static GFXEffect getEffectInstance(GFXEffectType type){
        switch (type){
            case SOFT_SHAKE ->{
                return new GFXSoftShake();
            }
            case ROUGH_SHAKE -> {
                return new GFXRoughShake();
            }
            case BLACK_OUT ->{
                return new GFXBlackOut();
            }
            case BLACK_IN ->{
                return new GFXBlackIn();
            }
            case BLACK_IN_OUT ->{
                return new GFXBlackInOut();
            }
        }
        return null;
    }
}
