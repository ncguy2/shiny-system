package net.ncguy.foundation.data;

import net.ncguy.foundation.data.components.SceneComponent;

import java.util.ArrayList;
import java.util.List;

public class World {

    private final List<Entity> entityList;

    public World() {
        entityList = new ArrayList<>();
    }

    public Entity add() {
        Entity e = new Entity();
        synchronized (entityList) {
            entityList.add(e);
        }
        return e;
    }

    public Entity add(SceneComponent<?> rootComponent) {
        Entity e = add();
        e.RootComponent(rootComponent);
        return e;
    }

    /** @return A new list instance containing the contents of entityList at the time of invocation */
    public List<Entity> getEntities() {
        synchronized (entityList) {
            return new ArrayList<>(entityList);
        }
    }

    public void update(float delta) {
        getEntities().forEach(e -> e.update(delta));
    }
}
