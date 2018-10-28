package net.ncguy.editor.modules.world.adapter.widget;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.CollapsibleWidget;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextButton;

import java.util.function.Consumer;

public class Layout {

    public static void section(VisTable table, String name, boolean openAtStart, Consumer<VisTable> contentBuilder) {
        VisTextButton btn = new VisTextButton(name);
        CollapsibleWidget w = new CollapsibleWidget();
        w.setCollapsed(!openAtStart, false);
        VisTable content = new VisTable();
        content.setBackground("window-border-bg");
        content.setFillParent(true);
        content.defaults().pad(4);

        contentBuilder.accept(content);
        w.setTable(content);

        btn.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                w.setCollapsed(!w.isCollapsed());
            }
        });

        table.add(btn).colspan(2).growX().row();
        table.add(w).colspan(2).growX().row();
    }

    public static <T extends Actor> void addLabeledWidget(VisTable table, String label, T widget) {
        LabeledWidget<T> w = new LabeledWidget<>(label, widget);
        table.add(w).growX().row();
    }

}
