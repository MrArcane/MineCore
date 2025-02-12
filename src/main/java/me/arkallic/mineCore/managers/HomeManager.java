package me.arkallic.mineCore.managers;

import me.arkallic.mineCore.data.PlayerData;
import me.arkallic.mineCore.objects.Home;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

import static me.arkallic.mineCore.utils.ServerUtils.sendChat;

public class HomeManager {

    private final PlayerDataManager playerDataManager;

    public HomeManager(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }

    public void setHome(UUID uuid, String name, Location location) {
        PlayerData pd = playerDataManager.get(uuid);

        pd.getHomes().removeIf(home -> home.name().equalsIgnoreCase(name));
        pd.getHomes().add(new Home(name.toLowerCase(), location));

        sendChat(Bukkit.getPlayer(uuid), "&aHome: &7" +name+ " &ahas been set to your location successfully!");
    }

    public void deleteHome(UUID uuid, String name) {
        PlayerData pd = playerDataManager.get(uuid);

        pd.getHomes().removeIf(home -> home.name().equalsIgnoreCase(name));
        sendChat(Bukkit.getPlayer(uuid), "&aHome: &7" +name+ " &ahas been removed from your home list successfully!");
    }

    public List<Home> getHomes(UUID uuid) {
        PlayerData pd = playerDataManager.get(uuid);
        return pd.getHomes();
    }

    public Home getHome(UUID uuid, String homeName) {
        for (Home home : getHomes(uuid)) {
            if (home.name().equalsIgnoreCase(homeName)) {
                return home;
            }
        }
        return new Home("", null);
    }

    public void teleportHome(UUID uuid, Home home) {
        Player p = Bukkit.getPlayer(uuid);
        p.teleport(home.location());
        sendChat(p, "&aWelcome home! You have been teleported to: &7" + home.name() + "&a.");
    }

    public boolean canSetHome(UUID uuid) {
        return getHomes(uuid).size() < playerDataManager.get(uuid).getHomeLimit();
    }

    public int homeLimit(UUID uuid) {
        return playerDataManager.get(uuid).getHomeLimit();
    }

    public String getNoHomeMessage() {
        return "&cYou haven't set any home locations yet.";
    }

    public String getHomeLimitMessage() {
        return "&cYou have reached your home limit and cannot set another home.";
    }
}
