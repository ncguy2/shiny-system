package net.ncguy.foundation.data;

import net.ncguy.foundation.data.components.SceneComponent;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class Entity {

    private List<Consumer<Float>> updateFuncs;
    private SceneComponent<?> rootComponent;
    private boolean active;

    public Entity() {
        updateFuncs = new ArrayList<>();
        RootComponent(new SceneComponent<>()).Name("Root");
        active = true;
    }

    public void onUpdate(Consumer<Float> func) {
        updateFuncs.add(func);
    }

    public Transform transform() {
        return rootComponent.transform;
    }

    public SceneComponent<?> getRootComponent() {
        return rootComponent;
    }

    public <T extends SceneComponent<?>> T RootComponent(T rootComponent) {
        this.rootComponent = rootComponent;
        return rootComponent;
    }

    public <T extends SceneComponent<?>> T RootComponent(Class<T> componentClass) throws IllegalAccessException, InvocationTargetException, InstantiationException, NoSuchMethodException {
        Constructor<T> ctor = componentClass.getConstructor();
        T inst = ctor.newInstance();
        return RootComponent(inst);
    }

    public void update(UpdatePhase phase, float delta) {
        if (active) {
            updateFuncs.forEach(f -> f.accept(delta));
            rootComponent.update(phase, delta);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Entity entity = (Entity) o;
        return Objects.equals(rootComponent, entity.rootComponent);
    }

    @Override
    public int hashCode() {
        return Objects.hash(rootComponent);
    }
}
