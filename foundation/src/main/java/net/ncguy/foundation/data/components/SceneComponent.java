package net.ncguy.foundation.data.components;

import net.ncguy.foundation.data.Transform;
import net.ncguy.foundation.data.UpdatePhase;
import net.ncguy.foundation.data.aspect.Aspect;
import net.ncguy.foundation.data.aspect.AspectKey;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

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

    public <T extends EntityComponent<?>> T add(Class<T> componentClass) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
        Constructor<T> ctor = componentClass.getConstructor();
        T inst = ctor.newInstance();
        return add(inst);
    }

    public <T extends EntityComponent<?>> List<T> get(Class<T> type, boolean includeSubtypes) {
        Predicate<Class<?>> filter = includeSubtypes ? type::isInstance : type::equals;

        return childComponents.stream()
                .filter(c -> filter.test(c.getClass()))
                .map(type::cast)
                .collect(Collectors.toList());
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
    public void update(UpdatePhase phase, float delta) {
        childComponents.forEach(c -> c.update(phase, delta));
        super.update(phase, delta);
    }

}
