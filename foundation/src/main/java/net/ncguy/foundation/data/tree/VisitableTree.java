package net.ncguy.foundation.data.tree;

import net.ncguy.foundation.data.interfaces.IVisitable;
import net.ncguy.foundation.data.interfaces.IVisitor;

import java.util.HashSet;
import java.util.Set;

public class VisitableTree<T> implements IVisitable<T> {

    public final Set<VisitableTree<T>> children = new HashSet<>();
    public final T data;
    public final IVisitable<T> parent;

    public VisitableTree(T data) {
        this(data, null);
    }

    public VisitableTree(T data, IVisitable<T> parent) {
        this.data = data;
        this.parent = parent;
    }

    @Override
    public IVisitable<T> parent() {
        return parent;
    }

    @Override
    public void accept(IVisitor<T> visitor) {
        visitor.visit(this, data());
        for (VisitableTree<T> child : children) {
            child.accept(visitor.visit(child));
        }
    }

    @Override
    public T data() {
        return this.data;
    }

    public VisitableTree<T> child(T data) {
        for (VisitableTree<T> child : children) {
            if (child.data().equals(data))
                return child;
        }
        return child(new VisitableTree<>(data));
    }

    public VisitableTree<T> child(VisitableTree<T> child) {
        children.add(child);
        return child;
    }

}
