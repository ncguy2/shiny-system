package net.ncguy.foundation.data.interfaces;

public interface IVisitable<T> {

    IVisitable<T> parent();

    void accept(IVisitor<T> visitor);

    T data();

}
