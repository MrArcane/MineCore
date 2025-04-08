package me.arkallic.minecore.utils;

import me.arkallic.minecore.MineCore;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigData {

    private static final int DEFAULT_PLAYER_HOME_LIMIT = 1;
    private static final boolean DEFAULT_PLAYER_PVP = false;

    private final MineCore mineCore;

    //Server values
    private Location spawnLocation;

    //Player values
    private int playerHomeLimit;
    private boolean defaultPVPEnabled;

    public ConfigData(MineCore mineCore) {
        this.mineCore = mineCore;
    }

    private FileConfiguration getConfig() {
        return mineCore.getConfig();
    }

    public ConfigurationSection getServerSettings() {
        ConfigurationSection section = getConfig().getConfigurationSection("Server-settings");
        if (section == null) {
            section = getConfig().createSection("Server-settings");
        }
        return section;
    }

    public ConfigurationSection getPlayerSettings() {
        ConfigurationSection section = getConfig().getConfigurationSection("Default-player-settings");
        if (section == null) {
            section = getConfig().createSection("Default-player-settings");
        }
        return section;
    }

    public void loadConfigData() {

        this.spawnLocation = getConfig().getLocation("Spawn.Location");

        //Player settings
        this.playerHomeLimit = getPlayerSettings().getInt("Home-limit", DEFAULT_PLAYER_HOME_LIMIT);
        this.defaultPVPEnabled = getPlayerSettings().getBoolean("PVP-enabled", DEFAULT_PLAYER_PVP);

    }

    public void saveConfigData() {
        //Server settings
        this.getConfig().set("Spawn.Location", this.spawnLocation);

        //Player settings
        getPlayerSettings().set("Home-limit", this.playerHomeLimit);
        getPlayerSettings().set("Pvp-enabled", this.defaultPVPEnabled);
        mineCore.saveConfig();
    }

    public void reloadConfig() {
        this.mineCore.reloadConfig();
        this.loadConfigData();
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public void saveSpawnLocation() {
        getConfig().set("Spawn.Location", this.spawnLocation);
        mineCore.saveConfig();
    }

    public void setPlayerHomeLimit(int playerHomeLimit) {
        this.playerHomeLimit = playerHomeLimit;
    }

    public int getPlayerHomeLimit() {
        return this.playerHomeLimit;
    }

    public boolean isDefaultPVPEnabled() {
        return defaultPVPEnabled;
    }

    public void setDefaultPVPEnabled(boolean defaultPVPEnabled) {
        this.defaultPVPEnabled = defaultPVPEnabled;
    }
}
