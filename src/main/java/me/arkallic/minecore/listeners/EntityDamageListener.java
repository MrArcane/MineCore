package me.arkallic.minecore.listeners;

import me.arkallic.minecore.managers.playerdata.PlayerData;
import me.arkallic.minecore.managers.playerdata.PlayerDataManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import static me.arkallic.minecore.utils.ChatUtils.sendChat;

public class EntityDamageListener implements Listener {

    private final PlayerDataManager playerDataManager;

    public EntityDamageListener(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }

    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player victim && e.getDamager() instanceof Player damager) {
            PlayerData pd = playerDataManager.get(victim.getUniqueId());

            if (!pd.pvpEnabled()) {
                e.setCancelled(true);
                sendChat(damager, "&cThat player has pvp disabled.");
            }
        }
    }
}
