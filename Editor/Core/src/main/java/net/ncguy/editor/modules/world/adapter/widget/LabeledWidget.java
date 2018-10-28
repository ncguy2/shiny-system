package net.ncguy.editor.modules.world.adapter.widget;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kotcrab.vis.ui.widget.Separator;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;

public class LabeledWidget<T extends Actor> extends VisTable {

    public VisLabel label;
    public T actor;

    public LabeledWidget(String label, T actor) {
        this.label = new VisLabel(label);
        this.actor = actor;

        add(label).growX().padRight(4);
        add(new Separator()).growY();
        add(actor).growX().padLeft(4).row();
    }

}
