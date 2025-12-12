package net.gooseman.inferno_utils;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.gooseman.inferno_utils.role.Role;
import net.gooseman.inferno_utils.role.RoleType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class ModRegistries {

    public static ResourceKey<Registry<RoleType<? extends Role>>> ROLE_KEY = ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath(InfernoUtils.MOD_ID, "role"));
    public static Registry<RoleType<? extends Role>> ROLE = FabricRegistryBuilder.createSimple(ROLE_KEY).buildAndRegister();
}
