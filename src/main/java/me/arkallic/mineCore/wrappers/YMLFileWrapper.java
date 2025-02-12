package me.arkallic.mineCore.wrappers;

import me.arkallic.mineCore.MineCore;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

import static me.arkallic.mineCore.utils.ServerUtils.log;


public class YMLFileWrapper {
    private File file;
    private final String fileName;
    private FileConfiguration config;
    private final MineCore mineCore;

    public YMLFileWrapper(String folder, String fileName, MineCore mineCore) {
        this.fileName = fileName;
        this.mineCore = mineCore;
        this.file = new File(mineCore.getDataFolder(), folder + File.separator + fileName + ".yml");

        // Ensure the file exists before loading
        createFileIfNotExists();

        this.config = YamlConfiguration.loadConfiguration(file);
    }

    private void createFileIfNotExists() {
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs(); // Ensure directories exist
                file.createNewFile(); // Create the file if missing

                try (InputStream inputStream = mineCore.getResource(fileName + ".yml")) {
                    if (inputStream != null) {
                        try (Reader reader = new InputStreamReader(inputStream, StandardCharsets.UTF_8);
                             Writer writer = new FileWriter(file)) {
                            char[] buffer = new char[1024];
                            int bytesRead;
                            while ((bytesRead = reader.read(buffer)) != -1) {
                                writer.write(buffer, 0, bytesRead);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                Bukkit.getLogger().log(Level.SEVERE, "Could not create or copy " + fileName, e);
            }
        }
    }

    public String getFileName() {
        return this.fileName;
    }

    public File getFile() {
        return this.file;
    }

    public FileConfiguration getConfig() {
        return config;
    }

    public void save() {
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save YAML file: " + fileName, e);
        }
    }

    public void reload() {
        config = YamlConfiguration.loadConfiguration(file);
    }
}
