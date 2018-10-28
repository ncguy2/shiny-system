package net.ncguy.editor.modules.world.adapter;

import com.kotcrab.vis.ui.widget.VisTable;
import net.ncguy.editor.modules.world.adapter.widget.Layout;
import net.ncguy.editor.modules.world.adapter.widget.StringInput;
import net.ncguy.foundation.data.components.EntityComponent;

public abstract class ComponentAdapter<T extends EntityComponent<?>> {

    public abstract void buildControls(VisTable table, T component);

    public final VisTable buildControls(T component) {
        VisTable table = new VisTable();

        table.defaults().pad(4);

        // Name field
        Layout.addLabeledWidget(table, "Name", StringInput.textField("Name", component::Name, component::Name));

        buildControls(table, component);

        return table;
    }



    public static class Default extends ComponentAdapter {

        @Override
        public void buildControls(VisTable table, EntityComponent component) {

        }
    }

}
