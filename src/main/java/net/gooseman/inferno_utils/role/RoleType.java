package net.gooseman.inferno_utils.role;

import net.gooseman.inferno_utils.InfernoUtils;
import net.gooseman.inferno_utils.ModRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class RoleType<T extends Role> {
    public static RoleType<BaseRole> BASE;
    public static RoleType<MephistoRole> MEPHISTO;
    private final RoleFactory<T> factory;

    public RoleType(RoleFactory<T> factory) {
        this.factory = factory;
    }

    public T create(Player player) {
        return factory.create(player);
    }

    public static <T extends Role> RoleType<T> register(String id, RoleType<T> type) {
        return Registry.register(ModRegistries.ROLE, ResourceLocation.fromNamespaceAndPath(InfernoUtils.MOD_ID, id), type);
    }

    public static void register() {
        BASE = register("base", new RoleType<>(BaseRole::new));
        MEPHISTO = register("mephisto", new RoleType<>(MephistoRole::new));
    }

    @FunctionalInterface
    public interface RoleFactory<T extends Role> {
        T create(Player player);
    }
}
