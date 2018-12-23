package net.ncguy.foundation.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.kotcrab.vis.ui.widget.VisWindow;
import net.ncguy.foundation.data.interfaces.IVisitable;

import java.util.function.Consumer;
import java.util.function.Function;

public class SelectionDialog<T> extends VisWindow {

    SelectionTree<T> selectionTree;

    public SelectionDialog(String title) {
        super(title);
        selectionTree = new SelectionTree<>();

        closeOnEscape();
        addCloseButton();

        selectionTree.addOnSelectionListener(t -> fadeOut());

        add(new ScrollPane(selectionTree)).width(384).height(512).row();
        pack();
    }

    public void addSelectionListener(Consumer<T> listener) {
        selectionTree.addOnSelectionListener(listener);
    }

    public void setNodeBuilder(Function<T, Actor> nodeBuilder) {
        selectionTree.setNodeBuilder(nodeBuilder);
    }

    public void setData(IVisitable<T> data) {
        selectionTree.setData(data);
    }

    public SelectionDialog<T> show() {
        selectionTree.build();
        fadeIn();
        return this;
    }

}
