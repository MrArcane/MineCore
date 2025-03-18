package me.arkallic.minecore.listeners;

import me.arkallic.minecore.MineCore;
import me.arkallic.minecore.managers.PMManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class PlayerJoinListener implements Listener {

    private final MineCore mineCore;
    private final PMManager pmManager;

    public PlayerJoinListener(MineCore mineCore, PMManager pmManager) {
        this.mineCore = mineCore;
        this.pmManager = pmManager;
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        UUID playerUUID = e.getPlayer().getUniqueId();

        pmManager.getLastMessaged().remove(playerUUID);
    }
}
