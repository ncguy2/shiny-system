package net.ncguy.editor.modules.world;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import net.ncguy.foundation.data.components.EntityComponent;

public class ComponentTreeActor<T extends EntityComponent<?>> extends VisTable {

    protected T component;
    protected VisLabel nameLabel;

    public ComponentTreeActor(T component) {
        this.component = component;
        nameLabel = new VisLabel();
        add(nameLabel).padLeft(4).grow().row();
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        nameLabel.setText(component.Name());
    }

    @Override
    public void draw(Batch batch, float a) {
        super.draw(batch, a);
    }
}
