package net.ncguy.editor.modules.world.adapter;

import com.kotcrab.vis.ui.widget.VisTable;
import net.ncguy.foundation.data.components.modifiers.RotationComponent;

import static net.ncguy.editor.modules.world.adapter.widget.FloatInput.spinner;
import static net.ncguy.editor.modules.world.adapter.widget.Layout.addLabeledWidget;
import static net.ncguy.editor.modules.world.adapter.widget.Layout.section;
import static net.ncguy.editor.modules.world.adapter.widget.VectorInput.direction;

public class RotationComponentAdapter extends ModifierComponentAdapter<RotationComponent> {

    @Override
    public void buildControls(VisTable table, RotationComponent component) {

        section(table, "Rotation", true, t -> {
            addLabeledWidget(t, "Rotation axis", direction(component::getAxis));
            addLabeledWidget(t, "Rotation speed", spinner(1, component::getSpeed, component::setSpeed));
        });

        super.buildControls(table, component);
    }
}
