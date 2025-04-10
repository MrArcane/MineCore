package me.arkallic.minecore.listeners;

import me.arkallic.minecore.managers.playerdata.PlayerData;
import me.arkallic.minecore.managers.playerdata.PlayerDataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import static me.arkallic.minecore.utils.ChatUtils.sendChat;

public class PlayerChatListener implements Listener {

    private final PlayerDataManager playerDataManager;

    public PlayerChatListener(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        PlayerData pd = playerDataManager.get(p.getUniqueId());

        if (pd.isMuted()) {
            e.setCancelled(true);
            sendChat(p, "&cYou're muted and cannot speak in chat.");
            return;
        }
    }
}
