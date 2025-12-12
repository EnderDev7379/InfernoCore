package net.gooseman.inferno_utils.mana;

public class Mana {
    protected float mana;
    protected float underflowLimit;
    protected float manaLimit;
    protected float overflowLimit;

    public Mana() { this(0, 0, 0, 0); }

    public Mana(float mana) { this(mana, 0, 0, 0); }

    public Mana(float mana, float manaLimit) { this(mana, 0, manaLimit, manaLimit); }

    public Mana(float mana, float manaLimit, float overflowLimit) { this(mana, 0, manaLimit, overflowLimit); }

    public Mana(float mana, float underflowLimit, float manaLimit, float overflowLimit) {
        this.underflowLimit = Math.min(0f, underflowLimit);
        this.manaLimit = manaLimit;
        this.overflowLimit = Math.max(manaLimit, overflowLimit);
        this.mana = Math.clamp(mana, this.underflowLimit, this.overflowLimit);
    }

    public float getMana() { return this.mana; }

    public float getUnderflowLimit() { return this.underflowLimit; }
    public float getManaLimit() { return this.manaLimit; }
    public float getOverflowLimit() { return this.overflowLimit; }

    public void setMana(float value) { setMana(value, false, false); }
    public void setMana(float value, boolean allowOverflow) { setMana(value, false, allowOverflow); }
    public void setMana(float value, boolean allowUnderflow, boolean allowOverflow) { this.mana = Math.clamp(value, allowUnderflow ? underflowLimit : 0f, allowOverflow ? overflowLimit : manaLimit); }

    public void setUnderflowLimit(float limit) { this.underflowLimit = Math.min(limit, 0f); }
    public void setManaLimit(float limit) { this.manaLimit = Math.clamp(limit, 0f, overflowLimit); }
    public void setOverflowLimit(float limit) { this.overflowLimit = Math.max(manaLimit, limit); }

    public boolean shouldTick() { return false; }
    public void tick() {}
    public void reset() {}

    public <T> T getEventHandler(Class<T> functionalInterface) { return null; }
}
