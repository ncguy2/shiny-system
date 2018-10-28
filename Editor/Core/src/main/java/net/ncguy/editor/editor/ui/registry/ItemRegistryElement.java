package net.ncguy.editor.editor.ui.registry;

import java.util.function.Function;

public class ItemRegistryElement<SELF extends ItemRegistryElement, T> {

    public String name;
    public String semanticPath;
    public Function<SELF, T> builder;

    public ItemRegistryElement() {
    }

    public ItemRegistryElement(String name, String semanticPath, Function<SELF, T> builder) {
        this.name = name;
        this.semanticPath = semanticPath;
        this.builder = builder;
    }

    @SuppressWarnings("unchecked")
    public T build() {
        if(builder == null) {
            return null;
        }

        return builder.apply((SELF) this);
    }

}
