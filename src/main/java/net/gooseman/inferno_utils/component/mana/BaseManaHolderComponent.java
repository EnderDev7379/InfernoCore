package net.gooseman.inferno_utils.component.mana;

import net.gooseman.inferno_utils.mana.Mana;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

public class BaseManaHolderComponent implements ManaHolderComponent {

    Mana mana = new Mana(0, 0);

    @Override
    public Mana get() {
        return this.mana;
    }

    @Override
    public void set(Mana mana) {
        this.mana = mana;
    }

    @Override
    public void readData(ValueInput readView) {
        mana.setUnderflowLimit(readView.getFloatOr("underflow_limit", 0));
        mana.setOverflowLimit(readView.getFloatOr("overflow_limit", 0));
        mana.setManaLimit(readView.getFloatOr("mana_limit", 0));
        mana.setMana(readView.getFloatOr("mana", 0), true, true);
    }

    @Override
    public void writeData(ValueOutput writeView) {
        writeView.putFloat("underflow_limit", mana.getUnderflowLimit());
        writeView.putFloat("overflow_limit", mana.getOverflowLimit());
        writeView.putFloat("mana_limit", mana.getManaLimit());
        writeView.putFloat("mana", mana.getMana());
    }

    @Override
    public void serverTick() {
        if (this.mana.shouldTick()) {
            mana.tick();
        }
    }
}
