package me.arkallic.minecore.listeners;

import me.arkallic.minecore.MineCore;
import me.arkallic.minecore.managers.PMManager;
import me.arkallic.minecore.managers.PlayerDataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.logging.Level;

public class PlayerJoinListener implements Listener {

    private final MineCore mineCore;
    private final PMManager pmManager;
    private final PlayerDataManager playerDataManager;

    public PlayerJoinListener(MineCore mineCore, PMManager pmManager, PlayerDataManager playerDataManager) {
        this.mineCore = mineCore;
        this.pmManager = pmManager;
        this.playerDataManager = playerDataManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        playerDataManager.register(p.getUniqueId());
    }
}
