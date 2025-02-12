package me.arkallic.mineCore.listeners;

import me.arkallic.mineCore.managers.TPAManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final TPAManager tpaManager;

    public PlayerQuitListener(TPAManager tpaManager) {
        this.tpaManager = tpaManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        tpaManager.removeTPARequest(e.getPlayer().getUniqueId());
    }
}
