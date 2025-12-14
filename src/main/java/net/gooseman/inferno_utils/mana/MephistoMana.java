package net.gooseman.inferno_utils.mana;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents.AfterDamage;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents.AfterDeath;
import net.gooseman.inferno_utils.component.mana.ManaHolderComponent;
import net.gooseman.inferno_utils.config.InfernoConfig;
import net.gooseman.inferno_utils.utils.ModifiableAttributeModifier;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import org.mvel2.MVEL;

import java.util.HashMap;
import java.util.Map;

import static net.gooseman.inferno_utils.InfernoUtils.LOGGER;
import static net.gooseman.inferno_utils.InfernoUtils.MOD_ID;

public class MephistoMana extends PlayerMana {
    ResourceLocation mephstioId = ResourceLocation.fromNamespaceAndPath(MOD_ID, "mephisto");

    AttributeInstance maxHealthAttribute;
    AttributeInstance jumpAttribute;
    AttributeInstance safeFallAttribute;

    ModifiableAttributeModifier maxHealthModifier = new ModifiableAttributeModifier(mephstioId, 0, AttributeModifier.Operation.ADD_VALUE);
    ModifiableAttributeModifier jumpModifier = new ModifiableAttributeModifier(mephstioId, 0, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    ModifiableAttributeModifier safeFallModifier = new ModifiableAttributeModifier(mephstioId, 0, AttributeModifier.Operation.ADD_VALUE);

    public MephistoMana(Player player) { this(player, 0, 0, 100, 100); }

    public MephistoMana(Player player, float mana) { this(player, mana, 0, 100, 100); }

    public MephistoMana(Player player, float mana, float manaLimit) { this(player, mana, 0, manaLimit, manaLimit); }

    public MephistoMana(Player player, float mana, float manaLimit, float overflowLimit) { this(player, mana, 0, manaLimit, overflowLimit); }

    public MephistoMana(Player player, float mana, float underflowLimit, float manaLimit, float overflowLimit) {
        super(player, mana, underflowLimit, manaLimit, overflowLimit);
        maxHealthAttribute = player.getAttribute(Attributes.MAX_HEALTH);
        jumpAttribute = player.getAttribute(Attributes.JUMP_STRENGTH);
        safeFallAttribute = player.getAttribute(Attributes.SAFE_FALL_DISTANCE);

        maxHealthAttribute.addTransientModifier(maxHealthModifier.toModifier());
        jumpAttribute.addTransientModifier(jumpModifier.toModifier());
        safeFallAttribute.addTransientModifier(safeFallModifier.toModifier());
        shouldRender = true;
    }

    @Override
    public boolean shouldTick() {
        return true;
    }

    @Override
    public void tick() {
        Map<String, Object> varMap = new HashMap<>();
        varMap.put("mana", mana);
        varMap.put("manaLimit", manaLimit);
        varMap.put("overflowLimit", overflowLimit);
        varMap.put("underflowLimit", underflowLimit);
        float prevMana = mana;
        if (mana < 0f)
            setMana(mana + 0.025f, true, false);
        else if (mana > manaLimit)
            setMana(mana - 0.1f,  false, true);
        else if (mana > 0f)
            setMana(mana - 0.05f, false, false);

        if (mana != prevMana) {
//            double heightMult = 1 + Math.max(0, mana) / 40;
//            maxHealthModifier.amount = mana / 5;
//            jumpModifier.amount = sqrt(heightMult) - 1;
//            safeFallModifier.amount = heightMult - 1;
            LOGGER.warn("Started doing math");
            double heightMult = Double.parseDouble(MVEL.evalToString(InfernoConfig.config.getOrDefault("onTick_heightMult", "1 + Math.max(0, mana) / 40"), varMap));
            LOGGER.warn("Calculated heightMult");
            varMap.put("heightMult", heightMult);
            maxHealthModifier.amount = Double.parseDouble(MVEL.evalToString(InfernoConfig.config.getOrDefault("onTick_maxHealth", "mana / 5"), varMap));
            LOGGER.warn("Calculated maxHealthModifier");
            jumpModifier.amount = Double.parseDouble(MVEL.evalToString(InfernoConfig.config.getOrDefault("onTick_jumpForce", "Math.sqrt(heightMult) - 1"), varMap));
            LOGGER.warn("Calculated jumpModifier");
            safeFallModifier.amount = Double.parseDouble(MVEL.evalToString(InfernoConfig.config.getOrDefault("onTick_safeFall", "heightMult - 1"), varMap));
            LOGGER.warn("Calculated safeFallModifier");

            maxHealthAttribute.addOrUpdateTransientModifier(maxHealthModifier.toModifier());
            jumpAttribute.addOrUpdateTransientModifier(jumpModifier.toModifier());
            safeFallAttribute.addOrUpdateTransientModifier(safeFallModifier.toModifier());
        }
        ManaHolderComponent.KEY.sync(player);
    }

    @Override
    public void reset() {
        maxHealthAttribute.removeModifier(mephstioId);
        jumpAttribute.removeModifier(mephstioId);
        safeFallAttribute.removeModifier(mephstioId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> T getEventHandler(Class<T> functionalInterface) {
        if (functionalInterface.equals(AfterDamage.class)) {
            return (T) (AfterDamage) (LivingEntity entity, DamageSource source, float baseDamageTaken, float damageTaken, boolean blocked) -> {
                if (!(source.getEntity() instanceof Player attacker) || !attacker.equals(this.player)) return;
                Map<String, Object> varMap = Map.of(
                        "mana", mana,
                        "manaLimit", manaLimit,
                        "overflowLimit", overflowLimit,
                        "underflowLimit", underflowLimit,
                        "damage", damageTaken,
                        "baseDamage", baseDamageTaken,
                        "blocked", blocked);
                setMana(mana + damageTaken * 2.5f);
                setMana(Float.parseFloat(MVEL.evalToString(InfernoConfig.config.getOrDefault("onDamage_manaExp", "mana + damage * 2.5"), varMap)));
                attacker.heal(Float.parseFloat(MVEL.evalToString(InfernoConfig.config.getOrDefault("onDamage_healExp", "damage"), varMap)));
            };
        } else if (functionalInterface.equals(AfterDeath.class)) {
            return (T) (AfterDeath) (LivingEntity entity, DamageSource source) -> {

            };
        }
        return null;
    }
}
