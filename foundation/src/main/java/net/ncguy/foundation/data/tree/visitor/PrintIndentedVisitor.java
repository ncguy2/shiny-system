package net.ncguy.foundation.data.tree.visitor;

import net.ncguy.foundation.data.interfaces.IVisitable;
import net.ncguy.foundation.data.interfaces.IVisitor;

public class PrintIndentedVisitor<T> implements IVisitor<T> {

    private final int depth;
    private final int indentPerDepth;

    public PrintIndentedVisitor(int depth) {
        this(depth, 2);
    }

    public PrintIndentedVisitor(int depth, int indentPerDepth) {
        this.depth = depth;
        this.indentPerDepth = indentPerDepth;
    }

    @Override
    public IVisitor<T> visit(IVisitable<T> visitable) {
        return new PrintIndentedVisitor<>(this.depth + 1, indentPerDepth);
    }

    @Override
    public void visit(IVisitable<T> visitable, T data) {
        for (int i = 0; i < depth * indentPerDepth; i++) {
            System.out.print(" ");
        }
        System.out.println(data.toString());
    }
}
