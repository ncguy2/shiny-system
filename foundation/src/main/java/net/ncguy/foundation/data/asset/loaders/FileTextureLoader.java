package net.ncguy.foundation.data.asset.loaders;

import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;
import com.badlogic.gdx.utils.Array;

import net.ncguy.foundation.data.asset.FileTexture;

public class FileTextureLoader extends AsynchronousAssetLoader<Texture, TextureLoader.TextureParameter> {

    public FileTextureLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    static public class TextureLoaderInfo {
        String filename;
        TextureData data;
        FileTexture texture;
    };

    TextureLoaderInfo info = new TextureLoaderInfo();

    @Override
    public void loadAsync(AssetManager manager, String fileName, FileHandle file, TextureLoader.TextureParameter parameter) {
        info.filename = fileName;
        if (parameter == null || parameter.textureData == null) {
            Pixmap pixmap = null;
            Pixmap.Format format = null;
            boolean genMipMaps = false;
            info.texture = null;

            if (parameter != null) {
                format = parameter.format;
                genMipMaps = parameter.genMipMaps;
                info.texture = (FileTexture) parameter.texture;
            }

            info.data = TextureData.Factory.loadFromFile(file, format, genMipMaps);
        } else {
            info.data = parameter.textureData;
            info.texture = (FileTexture) parameter.texture;
        }


        if (!info.data.isPrepared()) info.data.prepare();
    }

    @Override
    public Texture loadSync(AssetManager manager, String fileName, FileHandle file, TextureLoader.TextureParameter parameter) {
        if (info == null) return null;
        FileTexture texture = info.texture;
        if (texture != null) {
            texture.load(info.data);
        } else {
            texture = new FileTexture(fileName, info.data);
        }
        if (parameter != null) {
            texture.setFilter(parameter.minFilter, parameter.magFilter);
            texture.setWrap(parameter.wrapU, parameter.wrapV);
        }

        return texture;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, TextureLoader.TextureParameter parameter) {
        return null;
    }
}