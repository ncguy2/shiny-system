package net.ncguy.foundation.data.components.mesh;

import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import com.badlogic.gdx.graphics.g3d.Renderable;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pool;
import net.ncguy.foundation.data.components.MeshComponent;

public class CubeComponent extends MeshComponent<CubeComponent> {

    @Override
    public ModelInstance buildInstance() {
        ModelBuilder mb = new ModelBuilder();
        Model box = mb.createBox(1, 1, 1, new Material(), VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
        return new ModelInstance(box);
    }

    @Override
    public void getRenderables(Array<Renderable> renderables, Pool<Renderable> pool) {
        getInstance().getRenderables(renderables, pool);
    }
}
