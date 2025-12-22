package net.gooseman.inferno_core.utils;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

//ResourceLocation id, double amount, AttributeModifier.Operation operation
public class ModifiableAttributeModifier {
    public ResourceLocation id;
    public double amount;
    public AttributeModifier.Operation operation;

    public ModifiableAttributeModifier(ResourceLocation id, double amount, AttributeModifier.Operation operation) {
        this.id = id;
        this.amount = amount;
        this.operation = operation;
    }

    public ModifiableAttributeModifier(AttributeModifier attributeModifier) {
        this.id = attributeModifier.id();
        this.amount = attributeModifier.amount();
        this.operation = attributeModifier.operation();
    }

    public AttributeModifier toModifier() {
        return new AttributeModifier(id, amount, operation);
    }
}