package org.astemir.api.client;


import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

public class SkillsRenderTypes extends RenderType{

    private static final Function<ResourceLocation, RenderType> EYES_TRANSPARENT = Util.memoize((p_173255_) -> {
        RenderStateShard.TextureStateShard textureStateShard = new RenderStateShard.TextureStateShard(p_173255_, false, false);
        return RenderType.create("eyes_transparent", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_EYES_SHADER).setTextureState(textureStateShard).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setWriteMaskState(COLOR_WRITE).createCompositeState(false));
    });

    private static final Function<ResourceLocation, RenderType> EYES_TRANSPARENT_BLURRY = Util.memoize((p_173255_) -> {
        RenderStateShard.TextureStateShard textureStateShard = new RenderStateShard.TextureStateShard(p_173255_, true, false);
        return RenderType.create("eyes_transparent", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_EYES_SHADER).setTextureState(textureStateShard).setTransparencyState(TRANSLUCENT_TRANSPARENCY).setWriteMaskState(COLOR_WRITE).createCompositeState(false));
    });

    private static final Function<ResourceLocation, RenderType> EYES_BLURRY = Util.memoize((p_173255_) -> {
        RenderStateShard.TextureStateShard textureStateShard = new RenderStateShard.TextureStateShard(p_173255_, true, false);
        return RenderType.create("eyes_transparent", DefaultVertexFormat.NEW_ENTITY, VertexFormat.Mode.QUADS, 256, false, true, RenderType.CompositeState.builder().setShaderState(RENDERTYPE_EYES_SHADER).setTextureState(textureStateShard).setTransparencyState(ADDITIVE_TRANSPARENCY).setWriteMaskState(COLOR_WRITE).createCompositeState(false));
    });


    public SkillsRenderTypes(String pName, VertexFormat pFormat, VertexFormat.Mode pMode, int pBufferSize, boolean pAffectsCrumbling, boolean pSortOnUpload, Runnable pSetupState, Runnable pClearState) {
        super(pName, pFormat, pMode, pBufferSize, pAffectsCrumbling, pSortOnUpload, pSetupState, pClearState);
    }

    public static RenderType eyesTransparent(ResourceLocation p_110489_) {
        return EYES_TRANSPARENT.apply(p_110489_);
    }

    public static RenderType eyesTransparentBlurry(ResourceLocation p_110489_) {
        return EYES_TRANSPARENT_BLURRY.apply(p_110489_);
    }

    public static RenderType eyesBlurry(ResourceLocation p_110489_) {
        return EYES_BLURRY.apply(p_110489_);
    }

}
