package net.ncguy.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import net.ncguy.core.GameLauncher;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/** Launches the desktop (LWJGL) application. */
public class DesktopLauncher {
    public static void main(String[] args) {
        long pid = ProcessHandle.current().pid();
        System.out.println("Process id: " + pid);

        List<String> argList = Arrays.asList(args);
        if(argList.contains("--renderdoc") || argList.contains("-r")) {
            System.out.println("Renderdoc injection point");
            new Scanner(System.in).next();
        }

        createApplication();
    }

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