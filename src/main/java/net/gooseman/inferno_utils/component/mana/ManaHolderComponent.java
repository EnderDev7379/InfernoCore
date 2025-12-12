package net.gooseman.inferno_utils.component.mana;

import net.gooseman.inferno_utils.mana.Mana;
import net.minecraft.resources.ResourceLocation;
import org.ladysnake.cca.api.v3.component.Component;
import org.ladysnake.cca.api.v3.component.ComponentKey;
import org.ladysnake.cca.api.v3.component.ComponentRegistry;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;

public interface ManaHolderComponent extends Component, ServerTickingComponent {
    ComponentKey<ManaHolderComponent> KEY = ComponentRegistry.getOrCreate(ResourceLocation.parse("inferno_utils:mana_holder"), ManaHolderComponent.class);

    Mana get();
    void set(Mana mana);
}
