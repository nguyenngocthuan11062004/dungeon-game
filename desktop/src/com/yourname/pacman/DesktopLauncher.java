package com.yourname.pacman;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.yourname.pacman.MyGdxGame;

public class DesktopLauncher {
    public static void main (String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setForegroundFPS(60);
        config.setTitle("dungeon game project");

        // Thiết lập kích thước cửa sổ mong muốn
        config.setWindowedMode(1280, 720); 

        new Lwjgl3Application(new MyGdxGame(), config);
    }
}
