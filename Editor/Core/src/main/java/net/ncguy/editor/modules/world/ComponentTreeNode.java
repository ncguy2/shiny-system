package net.ncguy.editor.modules.world;

import com.kotcrab.vis.ui.widget.VisTree;
import net.ncguy.foundation.data.components.EntityComponent;
import net.ncguy.foundation.data.components.SceneComponent;

public class ComponentTreeNode<T extends EntityComponent<?>> extends VisTree.Node {

    private final T component;

    public ComponentTreeNode(T component) {
        super(new ComponentTreeActor<>(component));
        this.component = component;
        updateChildren();
    }

    public T getComponent() {
        return component;
    }

    public Class<T> getComponentClass() {
        return (Class<T>) getComponent().getClass();
    }

    protected void loadChildren() {
        getChildren().clear();

        if(component instanceof SceneComponent) {
            SceneComponent<?> c = (SceneComponent<?>) component;
            for (EntityComponent<?> child : c.childComponents) {
                ComponentTreeNode<?> value = new ComponentTreeNode<>(child);
                getChildren().add(value);
            }
        }
    }

    @Override
    public void updateChildren() {
        loadChildren();
        super.updateChildren();
    }

}
