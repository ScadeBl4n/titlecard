package net.scade.titlecard.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class TitleConfig {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File("./config/titlecard.json");

    public static class Config {
        public Boolean enabled = true;
        public String formatPattern = "%M %V - %S";
    }

    public static Config config = new Config();

    public static void load() {
        try {
            if (!CONFIG_FILE.exists()) {
                save();
                return;
            }

            FileReader reader = new FileReader(CONFIG_FILE);
            config = GSON.fromJson(reader, Config.class);
            reader.close();
        } catch (Exception e) {
            System.err.println("Failed to load config!");
            e.printStackTrace();
        }
    }

    public static void save() {
        try {
            if (!CONFIG_FILE.getParentFile().exists()) {
                CONFIG_FILE.getParentFile().mkdirs();
            }

            FileWriter writer = new FileWriter(CONFIG_FILE);
            GSON.toJson(config, writer);
            writer.close();
        } catch (Exception e) {
            System.err.println("Failed to save config!");
            e.printStackTrace();
        }
    }
}
