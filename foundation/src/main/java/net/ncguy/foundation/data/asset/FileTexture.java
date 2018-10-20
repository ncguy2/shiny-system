package net.ncguy.foundation.data.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.TextureData;

public class FileTexture extends Texture {

    protected FileHandle handle;

    public FileTexture(String internalPath) {
        this(Gdx.files.internal(internalPath));
    }

    public FileTexture(FileHandle file) {
        this(file, false);
    }

    public FileTexture(FileHandle file, boolean useMipMaps) {
        this(file, null, useMipMaps);
    }

    public FileTexture(FileHandle file, Pixmap.Format format, boolean useMipMaps) {
        super(file, format, useMipMaps);
        this.handle = file;
    }

    public FileTexture(String fileName, TextureData data) {
        this(Gdx.files.internal(fileName), data);
    }

    public FileTexture(FileHandle handle, TextureData data) {
        super(data);
        this.handle = handle;
    }

    public FileHandle GetHandle() {
        return this.handle;
    }

}
