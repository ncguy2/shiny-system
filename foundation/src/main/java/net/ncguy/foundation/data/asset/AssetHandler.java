package net.ncguy.foundation.data.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.TextureLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g3d.Model;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.GdxRuntimeException;
import net.ncguy.foundation.data.asset.loaders.FileTextureLoader;
import net.ncguy.foundation.tools.profile.ProfilerHost;
import net.ncguy.foundation.utils.ReflectionUtils;
import net.ncguy.foundation.utils.SpriteCache;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class AssetHandler implements Disposable {

    public static final Map<String, Class> extMapping = new HashMap<>();

    static {
        // Texture
        extMapping.put("png", Texture.class);
        extMapping.put("jpg", Texture.class);
        extMapping.put("jpeg", Texture.class);
        extMapping.put("bmp", Texture.class);

        // Model
        extMapping.put("g3dj", Model.class);
        extMapping.put("g3db", Model.class);
//        extMapping.put("fbx", Model.class);
        extMapping.put("obj", Model.class);
    }

    public static void Start() {
        instance();
    }
    public static void Dispose() {
        WithInstanceIfExists(AssetHandler::dispose);
    }


    private static AssetHandler instance;

    public static AssetHandler instance() {
        if (instance == null)
            instance = new AssetHandler();
        return instance;
    }

    public static void WithInstanceIfExists(Consumer<AssetHandler> task) {
        if (instance != null)
            task.accept(instance);
    }

    public static Class<?> GetType(String ext) {
        if(ext.startsWith("."))
            ext = ext.substring(1);
        return extMapping.getOrDefault(ext, Object.class);
    }

    public boolean generateMipmaps = true;
    boolean isLoading = false;

    Array<AssetDescriptor> tasks;

    private AssetHandler() {
        manager = new AssetManager();
        asyncRequests = new HashMap<>();

        manager.setErrorListener((asset, throwable) -> {
            throwable.printStackTrace();
            asyncRequests.remove(asset.fileName);
        });

        manager.setLoader(Texture.class, new FileTextureLoader(manager.getFileHandleResolver()));

        //noinspection unchecked
        tasks = (Array<AssetDescriptor>) ReflectionUtils.GetPrivateField(manager, "loadQueue")
                .orElse(null);
    }

    public Optional<AssetDescriptor> GetNextTask() {
        if (tasks.size <= 0) return Optional.empty();
        return Optional.ofNullable(tasks.peek());
    }

    protected Map<String, Consumer<?>> asyncRequests;
    protected AssetManager manager;

    public boolean IsLoading() {
        return this.isLoading;
    }

    public float GetProgress() {
        return manager.getProgress();
    }

    public void Update() {

        ProfilerHost.Start("Asset manager");

        try {
            isLoading = !manager.update();
        } catch (GdxRuntimeException gre) {
//            gre.printStackTrace();
        }

        ProfilerHost.Start("Async requests");

        asyncRequests.entrySet()
                .stream()
                .filter(e -> manager.isLoaded(e.getKey()))
                .peek(e -> e.getValue()
                        .accept(manager.get(e.getKey())))
                .map(Map.Entry::getKey)
                .collect(Collectors.toList())
                .forEach(asyncRequests::remove);

        ProfilerHost.End("Async requests");

        ProfilerHost.End("Asset manager");

    }


    public <T> T Get(String pth, Class<T> type) {

        if (pth == null || pth.isEmpty())
            return null;

        final String path = pth;

        if (!manager.isLoaded(path, type)) {

            FileHandle handle = Gdx.files.internal(path);

            if (!handle.exists())
                handle = Gdx.files.external(path);

            if (!handle.exists())
                return null;

            AtomicReference<T> item = new AtomicReference<>();

            FileHandle finalHandle = handle;
            AssetDescriptor tAssetDescriptor;
            if (type.equals(Texture.class)) {
                TextureLoader.TextureParameter p = new TextureLoader.TextureParameter();
                p.genMipMaps = generateMipmaps;
                p.minFilter = Texture.TextureFilter.MipMapLinearLinear;
                p.magFilter = Texture.TextureFilter.Linear;
                p.wrapU = Texture.TextureWrap.Repeat;
                p.wrapV = Texture.TextureWrap.Repeat;
                tAssetDescriptor = new AssetDescriptor<>(finalHandle, Texture.class, p);
            } else tAssetDescriptor = new AssetDescriptor<>(finalHandle, type);
            manager.load(tAssetDescriptor);
            manager.finishLoadingAsset(path);
            item.set(manager.get(path, type));

            return item.get();
        }

        return manager.get(path, type);
    }

    public <T> void GetAsync(String path, Class<T> type, Consumer<T> func) {

        if (path == null || path.isEmpty())
            return;

        if (manager.isLoaded(path, type)) {
            func.accept(manager.get(path, type));
            return;
        }

        FileHandle handle = Gdx.files.internal(path);

        if(handle.exists() && handle.isDirectory()) {
            return;
        }


        if (!handle.exists())
            handle = Gdx.files.external(path);

        if (!handle.exists() || handle.isDirectory() || handle.extension().isEmpty())
            return;

        path = handle.path();

        if (manager.isLoaded(path, type)) {
            func.accept(manager.get(path, type));
            return;
        }

        if (asyncRequests.containsKey(path)) return;
        if (IsAbsolutePath(path)) {
            if (handle.exists() && !handle.isDirectory())
                manager.load(new AssetDescriptor<>(handle, type));
        } else {
            if (generateMipmaps && type.equals(Texture.class)) {
                TextureLoader.TextureParameter p = new TextureLoader.TextureParameter();
                p.genMipMaps = true;
                p.minFilter = Texture.TextureFilter.MipMapLinearLinear;
                p.magFilter = Texture.TextureFilter.Linear;
                p.wrapU = Texture.TextureWrap.Repeat;
                p.wrapV = Texture.TextureWrap.Repeat;
                manager.load(path, Texture.class, p);
            } else manager.load(path, type);
        }

        asyncRequests.put(path, func);
    }

    public <T> List<T> GetOfType(Class<T> type) {
        Array<T> objects = new Array<>();
        manager.getAll(type, objects);
        List<T> list = new ArrayList<>();
        objects.forEach(list::add);
        return list;
    }

    public boolean IsAbsolutePath(String path) {
        if (path.length() >= 2)
            return path.charAt(1) == ':';
        return false;
    }

    public boolean IsLoaded(String path) {
        return manager.isLoaded(path);
    }

    public boolean IsLoaded(String path, Class<?> cls) {
        return manager.isLoaded(path, cls);
    }

    public void UsingManager(Consumer<AssetManager> func) {
        func.accept(manager);
    }

    public <T> String GetAssetFileName(T asset) {
        return manager.getAssetFileName(asset);
    }

    public <T> List<T> AllAssetsOfTypeInDirectory(FileHandle root, Class<T> type) {
        List<T> list = new ArrayList<>();
        AllAssetsOfTypeInDirectory(root, type, list);
        return list;
    }

    public <T> void AllAssetsOfTypeInDirectory(FileHandle root, Class<T> type, List<T> list) {
        String ext = root.extension();
        if (extMapping.containsKey(ext)) {
            Class extCls = extMapping.get(ext);
            if (extCls.equals(type)) {
                T t = Get(root.path(), type);
                if (t != null)
                    list.add(t);
            }
        }

        for (FileHandle child : root.list())
            AllAssetsOfTypeInDirectory(child, type, list);
    }

    public <T> List<T> AllAssetsOfTypeInRegistry(Class<T> type) {
        List<T> list = new ArrayList<>();
        AllAssetsOfTypeInRegistry(type, list);
        return list;
    }

    public <T> void AllAssetsOfTypeInRegistry(Class<T> type, List<T> list) {
        Array<T> out = new Array<>();
        manager.getAll(type, out);
        out.forEach(list::add);
    }

    @Override
    public void dispose() {
        manager.dispose();
        instance = null;
    }

    public Texture DefaultTexture() {
        return SpriteCache.Default()
                .getTexture();
    }
}
