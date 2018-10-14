package net.ncguy.foundation.data.components;

import net.ncguy.foundation.data.aspect.Aspect;
import net.ncguy.foundation.data.aspect.AspectKey;
import net.ncguy.foundation.data.aspect.IAspectProvider;
import net.ncguy.foundation.io.Json;
import net.ncguy.foundation.io.RuntimeTypeAdapterFactory;

import java.util.HashSet;

public abstract class EntityComponent<T extends EntityComponent> implements IAspectProvider {

    // Serialization stuffs
    protected static final RuntimeTypeAdapterFactory<EntityComponent> adapter = RuntimeTypeAdapterFactory.of(EntityComponent.class, "Internal_ComponentType");
    protected static final HashSet<Class<? extends EntityComponent>> registeredClasses = new HashSet<>();

    static {
        Json.Register(adapter);
    }

    private synchronized void _RegisterClass() {
        Class<? extends EntityComponent> cls = getClass();
        if (registeredClasses.contains(cls))
            return;

        registeredClasses.add(cls);
        adapter.registerSubtype(cls);
    }

    protected SceneComponent<?> parentComponent;
    protected String name;

    public EntityComponent() {
        _RegisterClass();
        name = getClass().getSimpleName();
    }

    public void update(float delta) {}

    public void _onRemoveFromComponent(SceneComponent<?> oldParent) {
        this.parentComponent = null;
    }
    public void _onAddToComponent(SceneComponent<?> newParent) {
        if(this.parentComponent != null) {
            parentComponent.remove(this);
        }
        this.parentComponent = newParent;
    }

    @Override
    public <T> Aspect<T> provideAspect(AspectKey<T> key) {
        return null;
    }

    public void Name(String name) {
        this.name = name;
    }

    public String Name() {
        return name;
    }
}