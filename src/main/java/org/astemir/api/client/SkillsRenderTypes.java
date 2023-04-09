package org.astemir.api.client;


import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.Util;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import java.util.function.BiFunction;

public class SkillsRenderTypes extends RenderType{

    public SkillsRenderTypes(String pName, VertexFormat pFormat, VertexFormat.Mode pMode, int pBufferSize, boolean pAffectsCrumbling, boolean pSortOnUpload, Runnable pSetupState, Runnable pClearState) {
        super(pName, pFormat, pMode, pBufferSize, pAffectsCrumbling, pSortOnUpload, pSetupState, pClearState);
    }

    private static TransparencyStateShard TRANSLUCENT_TRANSPARENCY_BRIGHT = new TransparencyStateShard("bright_translucent_transparency", () -> {
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE, GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE);
    }, () -> {
        RenderSystem.disableBlend();
        RenderSystem.defaultBlendFunc();
    });

    private static final BiFunction<ResourceLocation, Boolean, RenderType> EYES_TRANSPARENT_NO_CULL = Util.memoize((p_234333_, p_234334_) -> {
        CompositeState textureStateShard = CompositeState.builder().
                setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_EMISSIVE_SHADER).
                setTextureState(new TextureStateShard(p_234333_, false, false)).
                setTransparencyState(TRANSLUCENT_TRANSPARENCY_BRIGHT).
                setCullState(NO_CULL).
                setWriteMaskState(COLOR_DEPTH_WRITE).
                createCompositeState(true);
        return create("eyes_transparent_no_cull",
                DefaultVertexFormat.NEW_ENTITY,
                VertexFormat.Mode.QUADS,
                256,
                true,
                true,
                textureStateShard);
    });

    private static final BiFunction<ResourceLocation, Boolean, RenderType> EYES_TRANSPARENT = Util.memoize((p_234333_, p_234334_) -> {
        CompositeState textureStateShard = CompositeState.builder().
                setShaderState(RENDERTYPE_ENTITY_TRANSLUCENT_EMISSIVE_SHADER).
                setTextureState(new TextureStateShard(p_234333_, false, false)).
                setTransparencyState(TRANSLUCENT_TRANSPARENCY_BRIGHT).
                setCullState(CULL).
                setWriteMaskState(COLOR_DEPTH_WRITE).
                createCompositeState(true);
        return create("eyes_transparent_no_cull",
                DefaultVertexFormat.NEW_ENTITY,
                VertexFormat.Mode.QUADS,
                256,
                true,
                true,
                textureStateShard);
    });


    public static RenderType eyesTransparentNoCull(ResourceLocation p_110489_) {
        return EYES_TRANSPARENT_NO_CULL.apply(p_110489_,false);
    }

    public static RenderType eyesTransparent(ResourceLocation p_110489_) {
        return EYES_TRANSPARENT.apply(p_110489_,false);
    }
}
