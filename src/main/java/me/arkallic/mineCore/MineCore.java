package me.arkallic.mineCore;

import me.arkallic.mineCore.commands.*;
import me.arkallic.mineCore.commands.home.*;
import me.arkallic.mineCore.commands.TPACommand;
import me.arkallic.mineCore.data.ConfigData;
import me.arkallic.mineCore.listeners.PlayerJoinListener;
import me.arkallic.mineCore.listeners.PlayerQuitListener;
import me.arkallic.mineCore.loggers.PMLogger;
import me.arkallic.mineCore.managers.HomeManager;
import me.arkallic.mineCore.managers.PMManager;
import me.arkallic.mineCore.managers.PlayerDataManager;
import me.arkallic.mineCore.managers.TPAManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class MineCore extends JavaPlugin {

    private final PlayerDataManager playerDataManager = new PlayerDataManager(this);
    private final HomeManager homeManager = new HomeManager(playerDataManager);
    private final TPAManager tpaManager = new TPAManager();
    private final PMManager pmManager = new PMManager(this, playerDataManager);

    private ConfigData configData;

    @Override
    public void onEnable() {

        this.saveDefaultConfig();
        MineCoreAPI mineCoreAPI = new MineCoreAPI(this);
        this.loadConfigData();
        this.registerCommands();
        this.registerListeners();
        PMLogger.cleanOldLogs(30);

        for (Player p : Bukkit.getOnlinePlayers()) {
            playerDataManager.register(p.getUniqueId());
        }
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
        Bukkit.getPluginCommand("ignore").setExecutor(new IgnoreCommand(playerDataManager));
        Bukkit.getPluginCommand("unignore").setExecutor(new UnIgnoreCommand(playerDataManager));
    }

    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(this, pmManager), this);
        Bukkit.getPluginManager().registerEvents(new PlayerQuitListener(tpaManager), this);
    }

    public PlayerDataManager getPlayerDataManager() {
        return this.playerDataManager;
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
