package me.arkallic.minecore;

import me.arkallic.minecore.commands.*;
import me.arkallic.minecore.commands.home.*;
import me.arkallic.minecore.commands.TPACommand;
import me.arkallic.minecore.data.ConfigData;
import me.arkallic.minecore.listeners.EntityDamageListener;
import me.arkallic.minecore.listeners.PlayerJoinListener;
import me.arkallic.minecore.listeners.PlayerQuitListener;
import me.arkallic.minecore.loggers.PMLogger;
import me.arkallic.minecore.managers.HomeManager;
import me.arkallic.minecore.managers.PMManager;
import me.arkallic.minecore.managers.PlayerDataManager;
import me.arkallic.minecore.managers.TPAManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Level;

public final class MineCore extends JavaPlugin {

    private final PlayerDataManager playerDataManager = new PlayerDataManager(this);
    private final HomeManager homeManager = new HomeManager(playerDataManager);
    private final TPAManager tpaManager = new TPAManager(this);
    private final PMManager pmManager = new PMManager(this, playerDataManager);

    private ConfigData configData;
    private final MineCoreAPI api = new MineCoreAPI(this);

    @Override
    public void onEnable() {

        this.saveDefaultConfig();
        this.loadConfigData();
        this.registerCommands();
        this.registerListeners();
        PMLogger.cleanOldLogs(30);

        for (Player p : Bukkit.getOnlinePlayers()) {
            playerDataManager.register(p.getUniqueId());
        }
        getLogger().log(Level.INFO, "MineCore loaded successfully!");
    }

    @Override
    public void onDisable() {
        this.saveConfigData();
        for (Player p : Bukkit.getOnlinePlayers()) {
            playerDataManager.unregister(p.getUniqueId());
        }
    }

    private void registerCommands() {
        Bukkit.getPluginCommand("home").setExecutor(new HomeCommand(this, homeManager));
        Bukkit.getPluginCommand("homes").setExecutor(new HomesCommand(this, homeManager));
        Bukkit.getPluginCommand("sethome").setExecutor(new SetHomeCommand(this, homeManager));
        Bukkit.getPluginCommand("deletehome").setExecutor(new DeleteHomeCommand(this, homeManager));
        Bukkit.getPluginCommand("homelimit").setExecutor(new HomeLimitCommand(playerDataManager));
        Bukkit.getPluginCommand("message").setExecutor(new MessageCommand(this, pmManager));
        Bukkit.getPluginCommand("reply").setExecutor(new ReplyCommand(this, pmManager));
        Bukkit.getPluginCommand("spawn").setExecutor(new SpawnCommand(this));
        Bukkit.getPluginCommand("setspawn").setExecutor(new SetSpawnCommand(this));
        Bukkit.getPluginCommand("announce").setExecutor(new AnnounceCommand());
        Bukkit.getPluginCommand("tpa").setExecutor(new TPACommand(this, tpaManager));
        Bukkit.getPluginCommand("tpaccept").setExecutor(new TPACommand(this, tpaManager));
        Bukkit.getPluginCommand("tpadeny").setExecutor(new TPACommand(this, tpaManager));
        Bukkit.getPluginCommand("tpacancel").setExecutor(new TPACommand(this, tpaManager));
        Bukkit.getPluginCommand("ignore").setExecutor(new IgnoreCommand(playerDataManager));
        Bukkit.getPluginCommand("unignore").setExecutor(new UnIgnoreCommand(playerDataManager));
        Bukkit.getPluginCommand("pvp").setExecutor(new PVPCommand(playerDataManager));
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this, pmManager, playerDataManager), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(tpaManager, pmManager, playerDataManager), this);
        Bukkit.getPluginManager().registerEvents(new EntityDamageListener(playerDataManager), this);
    }

    public MineCoreAPI getAPI() {
        return api;
    }

    public PlayerDataManager getPlayerDataManager() {
        return this.playerDataManager;
    }

    public HomeManager getHomeManager() {
        return this.homeManager;
    }

    private void loadConfigData() {
        Location spawnLocation = this.getConfig().getLocation("Spawn.Location");
        configData = new ConfigData(spawnLocation);
    }

    private void saveConfigData() {
        this.getConfig().set("Spawn.Location", this.configData.getSpawnLocation());
        this.saveConfig();
    }

    public ConfigData getConfigData() {
        return configData;
    }
}
