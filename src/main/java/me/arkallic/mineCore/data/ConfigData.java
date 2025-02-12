package me.arkallic.mineCore.data;

import org.bukkit.Location;

public class ConfigData {

    private Location spawnLocation;

    public ConfigData(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }
}
