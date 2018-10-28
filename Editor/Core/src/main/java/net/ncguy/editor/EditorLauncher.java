package net.ncguy.editor;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import net.ncguy.desktop.DesktopLauncher;
import net.ncguy.foundation.Initialiser;

import java.lang.management.ManagementFactory;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class EditorLauncher {

    public static void main(String[] args) {

        System.out.println(ManagementFactory.getRuntimeMXBean().getName());

        List<String> argList = Arrays.asList(args);
        if(argList.contains("--renderdoc") || argList.contains("-r")) {
            System.out.println("Renderdoc injection point");
            new Scanner(System.in).next();
        }

        Initialiser.Initialize();

        Lwjgl3ApplicationConfiguration cfg = DesktopLauncher.getDefaultConfiguration();
        cfg.setTitle("Editor");

        new Lwjgl3Application(new EditorGame(), cfg);
    }

}
