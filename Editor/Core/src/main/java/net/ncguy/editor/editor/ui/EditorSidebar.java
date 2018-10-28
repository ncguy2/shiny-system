package net.ncguy.editor.editor.ui;

import com.badlogic.gdx.graphics.Color;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.tabbedpane.Tab;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPane;
import com.kotcrab.vis.ui.widget.tabbedpane.TabbedPaneAdapter;
import net.ncguy.editor.editor.ui.registry.TabPageRegistry;

import java.util.function.Predicate;

public class EditorSidebar extends VisTable {

    Predicate<String> semanticFilter;
    TabbedPane tabbedPane;
    VisTable content;

    public EditorSidebar(final EditorRegistry registry, Predicate<String> semanticFilter) {
        this.semanticFilter = semanticFilter;
        setBackground("white");
        setColor(Color.DARK_GRAY);

        tabbedPane = new TabbedPane();
        content = new VisTable();

        tabbedPane.addListener(new TabbedPaneAdapter() {
            @Override
            public void switchedTab(Tab tab) {
                content.clear();
                if (tab == null) {
                    return;
                }

                content.add(tab.getContentTable()).grow().row();
            }
        });

        add(tabbedPane.getTable()).growX().row();
        add(content).grow().row();

        registry.streamOf(TabPageRegistry.class)
                .filter(r -> semanticFilter.test(r.semanticPath))
                .map(TabPageRegistry::build)
                .forEach(tabbedPane::add);

    }

}