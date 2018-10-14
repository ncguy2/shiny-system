package net.ncguy.foundation.data;

import net.ncguy.foundation.data.components.SceneComponent;

public class Entity {

    public final Transform transform;
    private SceneComponent<?> rootComponent;

    public Entity() {
        transform = new Transform();
        RootComponent(new SceneComponent<>()).Name("Root");
    }

    public SceneComponent<?> getRootComponent() {
        return rootComponent;
    }

    public <T extends SceneComponent<?>> T RootComponent(T rootComponent) {
        this.rootComponent = rootComponent;
        return rootComponent;
    }

    public void update(float delta) {
        rootComponent.update(delta);
    }
}
