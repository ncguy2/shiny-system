package net.ncguy.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import net.ncguy.core.GameLauncher;

/** Launches the desktop (LWJGL) application. */
public class DesktopLauncher {
    public static void main(String[] args) {
        createApplication();
    }
//
//    private static LwjglApplication createApplication() {
//        return new LwjglApplication(new GameLauncher(), getDefaultConfiguration());
//    }
//
//    private static LwjglApplicationConfiguration getDefaultConfiguration() {
//        LwjglApplicationConfiguration configuration = new LwjglApplicationConfiguration();
//        int[] sizes = {128, 64, 32, 16};
//        String[] paths = new String[sizes.length];
//        for (int i = 0; i < sizes.length; i++) {
//            paths[i] = "libgdx" + sizes[i] + ".png";
//        }
//
//        configuration.useGL30 = true;
//        configuration.title=  "Game";
//        configuration.width = 1600;
//        configuration.height = 900;
//
//        for (String path : paths) {
//            configuration.addIcon(path, FileType.Internal);
//        }
//
//        configuration.vSyncEnabled = true;
//        return configuration;
//    }

    private static Lwjgl3Application createApplication() {
        return new Lwjgl3Application(new GameLauncher(), getDefaultConfiguration());
    }

    private static Lwjgl3ApplicationConfiguration getDefaultConfiguration() {
        Lwjgl3ApplicationConfiguration configuration = new Lwjgl3ApplicationConfiguration();
        int[] sizes = {128, 64, 32, 16};
        String[] paths = new String[sizes.length];
        for (int i = 0; i < sizes.length; i++) {
            paths[i] = "libgdx" + sizes[i] + ".png";
        }

        configuration.useOpenGL3(true, 4, 5);
        configuration.setTitle("Game");
        configuration.setWindowedMode(1600, 900);
//        configuration.setWindowIcon(FileType.Internal, paths);
        configuration.useVsync(true);
        configuration.disableAudio(true);

        return configuration;
    }
}