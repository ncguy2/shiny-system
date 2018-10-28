package net.ncguy.editor.editor.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.kotcrab.vis.ui.widget.MultiSplitPane;
import com.kotcrab.vis.ui.widget.VisTable;

public class EditorRoot extends VisTable {

    EditorMenuBar menuBar;

    MultiSplitPane splitPane;
    EditorSidebar leftSidebar;
    EditorSidebar rightSidebar;

    public EditorRoot(final EditorRegistry registry) {
        menuBar = new EditorMenuBar(registry);
        splitPane = new MultiSplitPane(false);
        leftSidebar = new EditorSidebar(registry, s -> s.equalsIgnoreCase("sidebar/left"));
        rightSidebar = new EditorSidebar(registry, s -> s.equalsIgnoreCase("sidebar/right"));

        splitPane.setWidgets(leftSidebar, new Actor(), rightSidebar);
        splitPane.setSplit(0, .2f);
        splitPane.setSplit(1, .8f);

        add(menuBar.getTable()).growX().row();
        add(splitPane).grow().row(); // Empty space
    }

    public void setContent(Actor content) {
        splitPane.setWidgets(leftSidebar, content, rightSidebar);
    }
}
