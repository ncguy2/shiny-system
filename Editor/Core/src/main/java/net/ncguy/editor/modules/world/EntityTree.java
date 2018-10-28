package net.ncguy.editor.modules.world;

import com.badlogic.gdx.Gdx;
import com.kotcrab.vis.ui.widget.VisTree;
import net.ncguy.foundation.data.Entity;
import net.ncguy.foundation.data.World;

import java.util.List;
import java.util.stream.Collectors;

public class EntityTree extends VisTree {

    List<Entity> entities;
    List<EntityTreeNode> nodes;

    public EntityTree(World world) {
        entities = world.getEntities();
        world.addListener(c -> {
            entities = world.getEntities();
            Gdx.app.postRunnable(this::update);
        });
        update();
    }

    public void update() {
        getNodes().clear();
        nodes = entities.stream().map(EntityTreeNode::new).collect(Collectors.toList());
        nodes.forEach(this::add);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }
}
