package me.arkallic.minecore.listeners;

import me.arkallic.minecore.MineCore;
import me.arkallic.minecore.data.PlayerData;
import me.arkallic.minecore.managers.PMManager;
import me.arkallic.minecore.managers.PlayerDataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.UUID;
import java.util.logging.Level;

import static me.arkallic.minecore.utils.ServerUtils.sendChat;

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

        PlayerData pd = playerDataManager.get(p.getUniqueId());
        if (!pd.getMail().isEmpty()) {
            int unreadMessages = pd.getMail().size();
            sendChat(p, "&eYou have &7" + unreadMessages + " &eunread message" + (unreadMessages == 1 ? "" : "s") + "!");
            sendChat(p, "&eType &7/mail read &eto view your messages.");
        }
    }
}
