package net.ncguy.foundation.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.kotcrab.vis.ui.widget.VisLabel;
import com.kotcrab.vis.ui.widget.VisTable;
import com.kotcrab.vis.ui.widget.VisTextField;
import com.kotcrab.vis.ui.widget.VisTree;
import net.ncguy.foundation.data.interfaces.IVisitable;
import net.ncguy.foundation.data.interfaces.IVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

public class SelectionTree<T> extends VisTable {

    VisTextField filterField;
    VisTree itemTree;
    Function<T, Actor> nodeBuilder;
    IVisitable<T> data;
    List<Consumer<T>> onSelectionListeners;

    public SelectionTree() {
        filterField = new VisTextField();
        itemTree = new VisTree();
        onSelectionListeners = new ArrayList<>();

        filterField.setMessageText("Filter");

        add(filterField).growX().row();
        add(itemTree).grow().row();

        setNodeBuilder(t -> {
            VisLabel visLabel = new VisLabel(Objects.toString(t));
            visLabel.setUserObject(t);
            return visLabel;
        });

        filterField.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                filter(filterField.getText());
            }
        });
    }

    Optional<Tree.Node> getNode(T data) {
        return Optional.ofNullable(itemTree.findNode(data));
    }

    private void filter(String filterText) {
        if (filterText == null || filterText.isEmpty()) {
            data.accept(new IVisitor<T>() {
                @Override
                public IVisitor<T> visit(IVisitable<T> visitable) {
                    return this;
                }

                @Override
                public void visit(IVisitable<T> visitable, T data) {
                    getNode(data).ifPresent(SelectionTree.this::show);
                }
            });
            return;
        }

        data.accept(new IVisitor<T>() {
            @Override
            public IVisitor<T> visit(IVisitable<T> visitable) {
                return this;
            }

            @Override
            public void visit(IVisitable<T> visitable, T data) {
                getNode(data).ifPresent(n -> {
                    String s = data.toString().toLowerCase().replaceAll(" ", "");
                    if (s.contains(filterText.toLowerCase().replace(" ", ""))) {

                        Tree.Node node = n;
                        while (node != null) {
                            show(node);
                            node = node.getParent();
                        }
                    } else {
                        hide(n);
                    }
                });
            }
        });
    }

    public void addOnSelectionListener(Consumer<T> onSelectionListener) {
        this.onSelectionListeners.add(onSelectionListener);
    }

    public void show(Tree.Node node) {
        node.getActor().addAction(Actions.alpha(1, .15f));
    }

    public void hide(Tree.Node node) {
        node.getActor().addAction(Actions.alpha(.3f, .15f));
    }

    public void setNodeBuilder(Function<T, Actor> nodeBuilder) {
        this.nodeBuilder = t -> {
            Actor actor = nodeBuilder.apply(t);
            actor.setUserObject(t);
            return actor;
        };
    }

    public void setData(IVisitable<T> data) {
        this.data = data;
    }

    public void build() {
        itemTree.clearChildren();

        if (data == null) {
            System.err.println("No data given");
            return;
        }

        TreePopulatorVisitor<T> visitor = new TreePopulatorVisitor<T>(itemTree, nodeBuilder.andThen(a -> {
            a.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (getTapCount() == 2) {
                        onSelectionListeners.forEach(l -> l.accept((T) a.getUserObject()));
                    }
                }
            });
            return a;
        }));
        data.accept(visitor);
    }

}
