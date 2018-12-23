package net.ncguy.editor.modules.world;

import com.badlogic.gdx.Gdx;
import com.kotcrab.vis.ui.widget.VisTree;
import com.sun.javafx.collections.ObservableListWrapper;
import javafx.collections.ListChangeListener;
import net.ncguy.editor.utils.IMenuProvider;
import net.ncguy.foundation.data.Entity;
import net.ncguy.foundation.data.World;
import net.ncguy.foundation.data.components.SceneComponent;
import net.ncguy.foundation.ui.MenuBuilder;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

import static net.ncguy.editor.modules.world.ComponentTreeActor.selectComponent;

public class EntityTree extends VisTree implements IMenuProvider {

    private final ObservableListWrapper<Entity> entities;
    private final World world;
    private final List<EntityTreeNode> nodes;

    public EntityTree(World world) {
        entities = world.getObservableEntityList();
        this.world = world;
        nodes = new ArrayList<>();
        world.addListener(this::onListChange);
        update();

        IMenuProvider.addMenuListener(this);
    }

    public void update() {
        Gdx.app.postRunnable(() -> {
            clearChildren();
            entities.stream()
                    .map(EntityTreeNode::new)
                    .forEach(this::add);
        });
    }

    @Override
    public void act(float delta) {
        super.act(delta);
    }

    @Override
    public void add(Node node) {
        super.add(node);
        if (node instanceof EntityTreeNode) {
            nodes.add((EntityTreeNode) node);
        }
    }

    @Override
    public void remove(Node node) {
        super.remove(node);
        nodes.remove(node);
    }

    @Override
    public void clearChildren() {
        super.clearChildren();
        nodes.clear();
    }

    private void onListChange(ListChangeListener.Change<? extends Entity> change) {
        while (change.next()) {
            if (change.wasRemoved()) {
                removeNodes(change.getRemoved());
            }
            if (change.wasAdded()) {
                addNodes(change.getAddedSubList());
            }
        }
    }

    private void addNodes(List<? extends Entity> additions) {
        additions.stream().map(EntityTreeNode::new).forEach(this::add);
    }

    private void removeNodes(List<? extends Entity> removals) {
        for (EntityTreeNode node : nodes) {
            if (removals.contains(node.getEntity())) {
                remove(node);
            }
        }
    }

    @Override
    public void provideMenu(MenuBuilder.MenuNode node) {
        node.Add("Add Entity", () -> {
            selectComponent(getStage(), t -> {
                try {
                    world.add((Class<? extends SceneComponent>) t);
                } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
        });
    }
}
