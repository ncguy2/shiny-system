package net.ncguy.foundation.data;

import net.ncguy.foundation.data.components.SceneComponent;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Entity {

    private List<Consumer<Float>> updateFuncs;
    private SceneComponent<?> rootComponent;

    public Entity() {
        updateFuncs = new ArrayList<>();
        RootComponent(new SceneComponent<>()).Name("Root");
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

    public void update(float delta) {
        updateFuncs.forEach(f -> f.accept(delta));
        rootComponent.update(delta);
    }
}
