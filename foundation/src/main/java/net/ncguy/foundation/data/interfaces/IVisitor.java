package net.ncguy.foundation.data.interfaces;

public interface IVisitor<T> {

    IVisitor<T> visit(IVisitable<T> visitable);

    void visit(IVisitable<T> visitable, T data);

}
