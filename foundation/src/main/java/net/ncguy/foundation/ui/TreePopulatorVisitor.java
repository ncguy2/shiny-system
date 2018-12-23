package net.ncguy.foundation.ui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Tree;
import com.kotcrab.vis.ui.widget.VisTree;
import net.ncguy.foundation.data.interfaces.IVisitable;
import net.ncguy.foundation.data.interfaces.IVisitor;

import java.util.function.Function;

public class TreePopulatorVisitor<T> implements IVisitor<T> {

    private final VisTree tree;
    private final VisTree.Node parentNode;
    private final Function<T, Actor> nodeBuilder;

    private VisTree.Node currentNode;

    public TreePopulatorVisitor(VisTree tree, Function<T, Actor> nodeBuilder) {
        this(tree, null, nodeBuilder);
    }

    public TreePopulatorVisitor(VisTree tree, VisTree.Node parentNode, Function<T, Actor> nodeBuilder) {
        this.tree = tree;
        this.parentNode = parentNode;
        this.nodeBuilder = nodeBuilder;
    }

    @Override
    public IVisitor<T> visit(IVisitable<T> visitable) {
        return new TreePopulatorVisitor<>(tree, currentNode, nodeBuilder);
    }

    @Override
    public void visit(IVisitable<T> visitable, T data) {
        currentNode = new Tree.Node(nodeBuilder.apply(data));
        currentNode.setObject(data);
        if (parentNode == null) {
            tree.add(currentNode);
        } else {
            parentNode.add(currentNode);
        }
    }
}
