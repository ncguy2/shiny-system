package net.ncguy.foundation.data.components.mesh;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.graphics.g3d.ModelInstance;
import net.ncguy.foundation.data.asset.AssetHandler;

import java.util.Objects;

public class AssetMeshComponent extends MeshComponent<AssetMeshComponent> {

    public static AssetMeshComponent of(String path) {
        AssetMeshComponent c = new AssetMeshComponent();
        c.setAssetPath(path);
        return c;
    }

    protected String assetPath;

    public AssetMeshComponent() {}


    public void setAssetPath(String path) {
        if(Objects.equals(this.assetPath, path)) {
            return;
        }

        this.assetPath = path;
        reload();
    }

    public void reload() {
        instance = null;
    }

    @Override
    public ModelInstance buildInstance() {
        AssetHandler.instance().GetAsync(this.assetPath, Model.class, m -> Gdx.app.postRunnable(() -> this.instance = new ModelInstance(m)));
        return null;
    }

}
