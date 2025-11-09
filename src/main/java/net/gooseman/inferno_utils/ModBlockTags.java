package net.gooseman.inferno_utils;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModBlockTags {
        public static final TagKey<Block> FARMABLE = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(InfernoUtils.MOD_ID, "farmable"));
}
