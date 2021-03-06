package net.ncguy.editor.modules.world;

import net.ncguy.foundation.data.Entity;

public class EntityTreeNode extends ComponentTreeNode {

    private final Entity entity;

    public EntityTreeNode(Entity entity) {
        super(entity.getRootComponent());
        this.entity = entity;
    }

    public boolean containsEntity(Entity e) {
        return this.entity.equals(e);
    }

    public Entity getEntity() {
        return entity;
    }
}
