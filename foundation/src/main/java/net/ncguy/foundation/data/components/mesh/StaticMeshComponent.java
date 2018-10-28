package net.ncguy.foundation.data.components.mesh;

public abstract class StaticMeshComponent<T extends StaticMeshComponent> extends MeshComponent<T> {

    @Override
    public RendererType getRenderType() {
        return RendererType.STATIC;
    }
}
