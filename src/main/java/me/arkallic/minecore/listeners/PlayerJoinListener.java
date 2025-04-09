package me.arkallic.minecore.listeners;

import me.arkallic.minecore.MineCore;
import me.arkallic.minecore.managers.playerdata.PlayerData;
import me.arkallic.minecore.managers.PMManager;
import me.arkallic.minecore.managers.playerdata.PlayerDataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import static me.arkallic.minecore.utils.ChatUtils.sendChat;

public class PlayerJoinListener implements Listener {

    private final PlayerDataManager playerDataManager;

    public PlayerJoinListener(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        playerDataManager.register(p.getUniqueId());

        PlayerData pd = playerDataManager.get(p.getUniqueId());
        if (!pd.getMail().isEmpty()) {
            int unreadMessages = pd.getMail().size();
            sendChat(p, "&eYou have &7" + unreadMessages + " &eunread message" + (unreadMessages == 1 ? "" : "s") + "!");
            sendChat(p, "&eType &7/inbox &eto view your messages.");
        }
    }
}
