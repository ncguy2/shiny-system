package net.ncguy.foundation.data.components.modifiers;

import net.ncguy.foundation.data.components.EntityComponent;

import java.util.List;
import java.util.stream.Collectors;

public abstract class ModifierComponent<T extends ModifierComponent> extends EntityComponent<T> {

    public boolean active = true;

    public int getSelfIndex() {
        if(parentComponent == null)
            return -1;

        List<? extends ModifierComponent> collect = parentComponent.childComponents.stream()
                .filter(getClass()::isInstance)
                .map(getClass()::cast)
                .collect(Collectors.toList());

        return collect.indexOf(this);
    }

    @Override
    public void update(float delta) {
        super.update(delta);
        if(active) {
            applyModification(delta);
        }
    }

    public abstract void applyModification(float delta);

    public Boolean Active() {
        return active;
    }

    public void Active(Boolean active) {
        this.active = active;
    }
}
