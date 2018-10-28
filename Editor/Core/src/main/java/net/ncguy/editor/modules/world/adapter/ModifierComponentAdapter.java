package net.ncguy.editor.modules.world.adapter;

import com.kotcrab.vis.ui.widget.VisTable;
import net.ncguy.foundation.data.components.modifiers.ModifierComponent;

import static net.ncguy.editor.modules.world.adapter.widget.BooleanInput.checkbox;
import static net.ncguy.editor.modules.world.adapter.widget.Layout.addLabeledWidget;

public class ModifierComponentAdapter<T extends ModifierComponent<?>> extends ComponentAdapter<T> {

    @Override
    public void buildControls(VisTable table, T component) {

        addLabeledWidget(table, "Active", checkbox(component::Active, component::Active));

    }

}
