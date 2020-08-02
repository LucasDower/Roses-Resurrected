package rosesresurrected;

import com.google.common.collect.ImmutableList;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.block.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.decorator.CountDecoratorConfig;
import net.minecraft.world.gen.decorator.Decorator;
import net.minecraft.world.gen.placer.SimpleBlockPlacer;
import net.minecraft.world.gen.stateprovider.SimpleBlockStateProvider;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RosesResurrected implements ModInitializer {

    public static final String MOD_ID = "rosesresurrected";
    public static final String MOD_NAME = "Roses Resurrected";

    private static final Block ROSE = new FlowerBlock(StatusEffects.WEAKNESS, 3, AbstractBlock.Settings.of(Material.PLANT).noCollision().breakInstantly().sounds(BlockSoundGroup.GRASS).nonOpaque());
    private static final Block POTTED_ROSE = new FlowerPotBlock(ROSE, AbstractBlock.Settings.of(Material.SUPPORTED).breakInstantly().nonOpaque());

    private static final RandomPatchFeatureConfig BASIC_FLOWER_CONFIG = (new net.minecraft.world.gen.feature.RandomPatchFeatureConfig.Builder(new SimpleBlockStateProvider(ROSE.getDefaultState()), SimpleBlockPlacer.field_24871)).tries(64).build();

    @Override
    public void onInitialize() {
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "rose"), ROSE);
        Registry.register(Registry.BLOCK, new Identifier(MOD_ID, "potted_rose"), POTTED_ROSE);
        Registry.register(Registry.ITEM, new Identifier(MOD_ID, "rose"), new BlockItem(ROSE, new Item.Settings().group(ItemGroup.DECORATIONS)));

        BlockRenderLayerMap.INSTANCE.putBlock(ROSE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(POTTED_ROSE, RenderLayer.getCutout());

        Registry.BIOME.forEach(biome -> {
            if (biome.getCategory() == Biome.Category.FOREST || biome.getCategory() == Biome.Category.PLAINS) {
                biome.addFeature(GenerationStep.Feature.VEGETAL_DECORATION, net.minecraft.world.gen.feature.Feature.RANDOM_RANDOM_SELECTOR.configure(new RandomRandomFeatureConfig(ImmutableList.of(net.minecraft.world.gen.feature.Feature.FLOWER.configure(BASIC_FLOWER_CONFIG)), 0)).createDecoratedFeature(Decorator.COUNT_HEIGHTMAP_32.configure(new CountDecoratorConfig(5))));
            }
        });
    }
}