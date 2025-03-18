package me.arkallic.minecore.managers;

import me.arkallic.minecore.data.PlayerData;
import me.arkallic.minecore.models.Home;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.LinkedHashMap;
import java.util.UUID;

import static me.arkallic.minecore.utils.ServerUtils.sendChat;

public class HomeManager {

    private final PlayerDataManager playerDataManager;

    public HomeManager(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }

    public void setHome(UUID uuid, String name, Location location) {
        PlayerData pd = playerDataManager.get(uuid);

        pd.addHome(new Home(name, location));
        sendChat(Bukkit.getPlayer(uuid), "&aHome: &7" +name+ " &ahas been set to your location successfully!");
    }

    public void getHomesList(UUID uuid) {
        Player p = Bukkit.getPlayer(uuid);
        LinkedHashMap<String, Home> homes = getHomes(p.getUniqueId());
        int homeCount = homes.size();
        int homeLimit = homeLimit(p.getUniqueId());

        if (homes.isEmpty()) {
            sendChat(p, "&c❌ You have no homes set! Use &e/sethome <name> &cto create one.");
        } else {
            sendChat(p, String.format("&6✦ &e%s's Homes &7( &a&l%d &7/ &c&l%d &7)", p.getName(), homeCount, homeLimit));

            for (Home home : getHomes(p.getUniqueId()).values()) {
                sendChat(p, String.format("&7▪ &b%s", home.name()));
            }
        }
    }

    public void deleteHome(UUID uuid, String name) {
        PlayerData pd = playerDataManager.get(uuid);
        pd.deleteHome(name);
        sendChat(Bukkit.getPlayer(uuid), "&aHome: &7" +name+ " &ahas been removed from your home list successfully!");
    }

    public LinkedHashMap<String, Home> getHomes(UUID uuid) {
        return this.playerDataManager.get(uuid).getHomes();
    }

    public Home getHome(UUID uuid, String homeName) {
        return this.playerDataManager.get(uuid).getHomes().get(homeName);
    }

    public void teleportHome(UUID uuid, Home home) {
        Player p = Bukkit.getPlayer(uuid);
        p.teleport(home.location());
        sendChat(p, "&aWelcome home! You have been teleported to: &7" + home.name() + "&a.");
    }

    public boolean canSetHome(UUID uuid) {
        return playerDataManager.get(uuid).getHomes().size() < playerDataManager.get(uuid).getHomeLimit();
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
