package net.ncguy.editor.editor.ui;

import net.ncguy.editor.editor.ui.registry.ItemRegistryElement;
import net.ncguy.foundation.data.World;
import net.ncguy.foundation.data.aspect.Aspect;
import net.ncguy.foundation.data.aspect.AspectKey;
import net.ncguy.foundation.data.aspect.IAspectProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class EditorRegistry implements IAspectProvider {

    protected final List<ItemRegistryElement<?, ?>> registry;
    protected World world;

    public EditorRegistry(World world) {
        registry = new ArrayList<>();
        this.world = world;
    }

    public void register(ItemRegistryElement<?, ?> element) {
        registry.add(element);
    }

    public <T extends ItemRegistryElement<?, ?>> Stream<T> streamOf(Class<T> type) {
        return registry.stream()
                .filter(type::isInstance)
                .map(type::cast);
    }
    public <T extends ItemRegistryElement<?, ?>> List<T> itemsOf(Class<T> type) {
        return streamOf(type).collect(Collectors.toList());
    }

    @Override
    public <T> Aspect<T> provideAspect(AspectKey<T> key) {

        if(key.type == World.class && world != null) {
            return Aspect.of(this, key, (T) world);
        }

        return null;
    }
}
