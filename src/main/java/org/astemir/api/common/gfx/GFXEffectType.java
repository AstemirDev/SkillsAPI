package org.astemir.api.common.gfx;

public enum GFXEffectType {

    SCREEN_SHAKE,BLACK_OUT,BLACK_IN,BLACK_IN_OUT;


    public static GFXEffect getEffectInstance(GFXEffectType type){
        switch (type){
            case SCREEN_SHAKE -> {
                return new GFXScreenShake();
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
