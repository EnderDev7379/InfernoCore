package net.gooseman.inferno_utils;

import net.minecraft.block.Block;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

public class ModTags {
    public static class BlockTags {
        public static final TagKey<Block> FARMABLE = TagKey.of(RegistryKeys.BLOCK, Identifier.of(InfernoUtils.MOD_ID, "farmable"));
    }
}
