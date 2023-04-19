package org.astemir.api.client.registry;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.astemir.api.io.ResourceUtils;
import org.jetbrains.annotations.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public class TESRModelsRegistry {


    public static Map<String,String> MODELS = new HashMap<>();



    @SubscribeEvent
    public static void onModelRegistryInit(ModelEvent.RegisterAdditional modelRegistryEvent) {
        MODELS.forEach((path,modelPath)->{
            modelRegistryEvent.register(new ModelResourceLocation(modelPath,"inventory"));
        });
    }

    @SubscribeEvent
    public static void onModelBakeEvent(ModelEvent.BakingCompleted event) {
        MODELS.forEach((path,modelPath)->{
            bakeModelReplacement(event.getModels(),path,modelPath);
        });
    }

    public static void addModelReplacement(String itemPath,String handModelPath){
        MODELS.put(itemPath,handModelPath);
    }


    public static void addModelReplacement(Item item, String handModelPath){
        MODELS.put(ResourceUtils.getItemId(item),handModelPath);
    }


    public static void addModelReplacement(Supplier<Item> item, String handModelPath){
        MODELS.put(ResourceUtils.getItemId(item.get()),handModelPath);
    }




    private static void bakeModelReplacement(Map<ResourceLocation, BakedModel> modelsMap, String inventoryPath, String inHandPath) {
        ResourceLocation modelInventory = new ModelResourceLocation(inventoryPath, "inventory");
        ResourceLocation modelHand = new ModelResourceLocation(inHandPath, "inventory");
        BakedModel bakedModelDefault = modelsMap.get(modelInventory);
        BakedModel bakedModelHand = modelsMap.get(modelHand);
        BakedModel modelWrapper = new BakedModel() {

            @Override
            public List<BakedQuad> getQuads(@Nullable BlockState p_235039_, @Nullable Direction p_235040_, RandomSource p_235041_) {
                return bakedModelDefault.getQuads(p_235039_,p_235040_,p_235041_);
            }

            @Override
            public boolean useAmbientOcclusion() {
                return bakedModelDefault.useAmbientOcclusion();
            }

            @Override
            public boolean isGui3d() {
                return bakedModelDefault.isGui3d();
            }

            @Override
            public boolean usesBlockLight() {
                return bakedModelDefault.usesBlockLight();
            }

            @Override
            public boolean isCustomRenderer() {
                return bakedModelDefault.isCustomRenderer();
            }

            @Override
            public TextureAtlasSprite getParticleIcon() {
                return bakedModelDefault.getParticleIcon();
            }


            @Override
            public ItemOverrides getOverrides() {
                return bakedModelDefault.getOverrides();
            }



            @Override
            public BakedModel applyTransform(ItemTransforms.TransformType transformType, PoseStack poseStack, boolean applyLeftHandTransform) {
                BakedModel modelToUse = bakedModelDefault;
                if (transformType == ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND || transformType == ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND
                        || transformType == ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND || transformType == ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND) {
                    modelToUse = bakedModelHand;
                }
                return ForgeHooksClient.handleCameraTransforms(poseStack,modelToUse,transformType,applyLeftHandTransform);
            }
        };
        modelsMap.put(modelInventory, modelWrapper);
    }

}
