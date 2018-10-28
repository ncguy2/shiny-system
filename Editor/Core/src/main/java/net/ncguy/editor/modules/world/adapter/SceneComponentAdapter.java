package net.ncguy.editor.modules.world.adapter;

import com.kotcrab.vis.ui.widget.VisTable;
import net.ncguy.editor.modules.world.adapter.widget.Layout;
import net.ncguy.editor.modules.world.adapter.widget.VectorInput;
import net.ncguy.foundation.data.components.SceneComponent;

public class SceneComponentAdapter<T extends SceneComponent<?>> extends ComponentAdapter<T> {

    @Override
    public void buildControls(VisTable table, T component) {

        Layout.section(table, "Transform", true, t -> {
            Layout.addLabeledWidget(t, "Translation", VectorInput.direction(component.transform::getTranslation));
            Layout.addLabeledWidget(t, "Scale", VectorInput.direction(component.transform::getScale));
        });

    }

}
