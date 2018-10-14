package net.ncguy.foundation.data;

import java.util.ArrayList;
import java.util.List;

public class World {

    private final List<Entity> entityList;

    public World() {
        entityList = new ArrayList<>();
    }

    /** @return A new list instance containing the contents of entityList at the time of invocation */
    public List<Entity> getEntities() {
        synchronized (entityList) {
            return new ArrayList<>(entityList);
        }
    }

}
