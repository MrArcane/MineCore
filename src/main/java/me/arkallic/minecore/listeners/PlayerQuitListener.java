package me.arkallic.minecore.listeners;

import me.arkallic.minecore.managers.PMManager;
import me.arkallic.minecore.managers.playerdata.PlayerDataManager;
import me.arkallic.minecore.managers.TPAManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener {

    private final TPAManager tpaManager;
    private final PMManager pmManager;
    private final PlayerDataManager playerDataManager;

    public PlayerQuitListener(TPAManager tpaManager, PMManager pmManager, PlayerDataManager playerDataManager) {
        this.tpaManager = tpaManager;
        this.pmManager = pmManager;
        this.playerDataManager = playerDataManager;
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        Player p = e.getPlayer();
        tpaManager.removeTPARequest(p.getUniqueId());
        pmManager.getLastMessaged().remove(p.getUniqueId());
        playerDataManager.unregister(p.getUniqueId());
    }
}
