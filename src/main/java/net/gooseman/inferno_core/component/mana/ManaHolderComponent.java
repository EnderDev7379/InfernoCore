package net.gooseman.inferno_core.component.mana;

import net.gooseman.inferno_core.mana.Mana;
import net.minecraft.resources.ResourceLocation;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

import static net.gooseman.inferno_core.InfernoCore.MOD_ID;

public interface ManaHolderComponent extends Component, ServerTickingComponent {
    ComponentKey<ManaHolderComponent> KEY = ComponentRegistry.getOrCreate(ResourceLocation.parse(MOD_ID + ":mana_holder"), ManaHolderComponent.class);

    Mana get();
    void set(Mana mana);
}
