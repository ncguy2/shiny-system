package net.ncguy.editor.editor.ui.registry;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;

import java.util.function.Supplier;

public class TabPageRegistry extends ItemRegistryElement<TabPageRegistry, Tab> {

    public TabPageRegistry(String name, String semanticPath, Supplier<VisTable> contentBuilder) {
        super(name, semanticPath, r -> new Tab() {
            @Override
            public String getTabTitle() {
                return name;
            }

            @Override
            public Table getContentTable() {
                return contentBuilder.get();
            }
        });
    }

}
