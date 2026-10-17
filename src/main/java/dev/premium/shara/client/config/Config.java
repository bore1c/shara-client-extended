package dev.premium.shara.client.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Config {
    public boolean fly = false;
    public boolean speed = false;
    public boolean fullbright = false;
    public boolean noclip = false;

    public boolean esp = true; // show entity list on HUD
    public boolean killaura = false;
    public double killauraRange = 4.0;

    public boolean xray = false; // placeholder toggle
    public boolean autoArmor = false; // basic
    public boolean autoClicker = false;
    public int autoClickDelayMs = 100; // ms between clicks

    public int keyFly = 0; // unknown default
    public int keySpeed = 0;
    public int keyFullbright = 0;
    public int keyNoClip = 0;
    public int keyESP = 0;
    public int keyKillaura = 0;
    public int keyXray = 0;
    public int keyAutoArmor = 0;
    public int keyAutoClicker = 0;

    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public static File getConfigFile() {
        String configDir = System.getProperty("user.home");
        // Try to detect Minecraft config location. On Windows this will be %appdata%/.minecraft
        String home = System.getenv("APPDATA");
        if (home == null) home = configDir;
        File dir = new File(home, ".minecraft/config");
        if (!dir.exists()) dir.mkdirs();
        return new File(dir, "shara-client-extended.json");
    }

    public static Config load() {
        File f = getConfigFile();
        if (!f.exists()) return new Config();
        try (FileReader r = new FileReader(f)) {
            return GSON.fromJson(r, Config.class);
        } catch (IOException e) {
            e.printStackTrace();
            return new Config();
        }
    }

    public void save() {
        File f = getConfigFile();
        try (FileWriter w = new FileWriter(f)) {
            GSON.toJson(this, w);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
