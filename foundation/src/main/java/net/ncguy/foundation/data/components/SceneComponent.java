package net.ncguy.foundation.data.components;

import net.ncguy.foundation.data.Transform;
import net.ncguy.foundation.data.aspect.Aspect;
import net.ncguy.foundation.data.aspect.AspectKey;

import java.util.ArrayList;
import java.util.List;

public class SceneComponent<T extends SceneComponent> extends EntityComponent<T> {

    public final Transform transform;
    public final List<EntityComponent<?>> childComponents;

    public SceneComponent() {
        transform = new Transform();
        childComponents = new ArrayList<>();
    }

    public <T extends EntityComponent<?>> T add(T component) {
        childComponents.add(component);
        component._onAddToComponent(this);
        return component;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Aspect<T> provideAspect(AspectKey<T> key) {

        if(key.type == Transform.class) {
            return Aspect.of(this, key, (T) transform);
        }

        return null;
    }

    @Override
    public void _onAddToComponent(SceneComponent<?> newParent) {
        super._onAddToComponent(newParent);
        transform.setParentTransform(newParent.transform);
    }

    public void remove(EntityComponent<?> child) {
        if(childComponents.contains(child)) {
            childComponents.remove(child);
            child._onRemoveFromComponent(this);
        }
    }

    @Override
    public void update(float delta) {
        childComponents.forEach(c -> c.update(delta));
        super.update(delta);
    }
}
