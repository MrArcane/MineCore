package me.arkallic.minecore.utils;

import me.arkallic.minecore.MineCore;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

public class ConfigData {

    /** NOTE TO SELF
     * You must add any value to the config.yml in Resources to add it on config creation :(
     */
    //DEFAULT VALUES
    private static final int DEFAULT_PLAYER_HOME_LIMIT = 1;
    private static final boolean DEFAULT_PLAYER_PVP = true;
    private static final boolean DISPLAY_INBOX_ON_JOIN = true;
    private static final Location SPAWN_LOCATION = null;

    private final MineCore mineCore;

    //Server values
    private Location spawnLocation;
    private boolean displayInboxOnJoin;

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

        //Server settings
        this.spawnLocation = getConfig().getLocation("Spawn.Location");
        this.displayInboxOnJoin = getServerSettings().getBoolean("Display-inbox-on-join", DISPLAY_INBOX_ON_JOIN);



        //Player settings
        this.playerHomeLimit = getPlayerSettings().getInt("Home-limit", DEFAULT_PLAYER_HOME_LIMIT);
        this.defaultPVPEnabled = getPlayerSettings().getBoolean("PVP-enabled", DEFAULT_PLAYER_PVP);
        mineCore.saveDefaultConfig();

    }

    public void saveConfigData() {
        // Server settings
        if (getConfig().get("Spawn.Location") == null) {
            getConfig().set("Spawn.Location", this.spawnLocation);
        }
        if (getServerSettings().get("Display-inbox-on-join") == null) {
            getServerSettings().set("Display-inbox-on-join", this.displayInboxOnJoin);
        }

        // Player settings
        if (getPlayerSettings().get("Home-limit") == null) {
            getPlayerSettings().set("Home-limit", this.playerHomeLimit);
        }
        if (getPlayerSettings().get("PVP-enabled") == null) {
            getPlayerSettings().set("PVP-enabled", this.defaultPVPEnabled);
        }

        mineCore.saveConfig();
    }

    public void reloadConfig() {
        this.mineCore.reloadConfig();
        this.saveConfigData();
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

    public boolean displayInboxOnJoin() {
        return displayInboxOnJoin;
    }

    public void setDisplayInboxOnJoin(boolean displayInboxOnJoin) {
        this.displayInboxOnJoin = displayInboxOnJoin;
    }
}
