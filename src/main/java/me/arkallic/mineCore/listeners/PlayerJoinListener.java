package me.arkallic.mineCore.listeners;

import me.arkallic.mineCore.MineCore;
import me.arkallic.mineCore.managers.PMManager;
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
        pmManager.getLastMessaged().values().removeIf(uuid -> uuid.equals(playerUUID));
    }
}
