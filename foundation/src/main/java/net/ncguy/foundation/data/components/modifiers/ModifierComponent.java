package net.ncguy.foundation.data.components.modifiers;

import net.ncguy.foundation.data.components.EntityComponent;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Modifies the owning component
 *
 * @param <T>
 */
public abstract class ModifierComponent<T extends ModifierComponent> extends EntityComponent<T> {

    public boolean active = true;
    public ModificationStrategy strategy = ModificationStrategy.PUSH;

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
    public void updatePrePhysics(float delta) {
        super.updatePrePhysics(delta);
        if (active && strategy.equals(ModificationStrategy.PUSH)) {
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

    public enum ModificationStrategy {
        /**
         * Pushes the modifications to the parent
         */
        PUSH,
        /**
         * Modifications are pulled up from the parent
         */
        PULL
    }

}
