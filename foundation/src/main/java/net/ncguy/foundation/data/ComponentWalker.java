package net.ncguy.foundation.data;

import net.ncguy.foundation.data.components.EntityComponent;
import net.ncguy.foundation.data.components.SceneComponent;

import java.util.function.Consumer;
import java.util.function.Predicate;

public class ComponentWalker {

    public static void traverse(Entity root, Consumer<EntityComponent<?>> task) {
        traverse(root, e -> true, task);
    }

    public static void traverse(Entity root, Predicate<EntityComponent<?>> filter, Consumer<EntityComponent<?>> task) {
        traverse(root.getRootComponent(), filter, task);
    }

    public static void traverse(EntityComponent<?> root, Consumer<EntityComponent<?>> task) {
        traverse(root, e -> true, task);
    }

    public static void traverse(EntityComponent<?> root, Predicate<EntityComponent<?>> filter, Consumer<EntityComponent<?>> task) {
        if(filter.test(root))
            task.accept(root);

        if(root instanceof SceneComponent)
            ((SceneComponent<?>) root).childComponents.forEach(c -> traverse(c, filter, task));
    }

}
