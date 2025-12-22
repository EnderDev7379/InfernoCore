package net.gooseman.inferno_core;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;

public class ModBlockTags {
        public static final TagKey<Block> FARMABLE = TagKey.create(Registries.BLOCK, ResourceLocation.fromNamespaceAndPath(InfernoCore.MOD_ID, "farmable"));
}
