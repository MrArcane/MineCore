package me.arkallic.minecore.managers;

import me.arkallic.minecore.MineCore;
import me.arkallic.minecore.managers.playerdata.PlayerData;
import me.arkallic.minecore.managers.playerdata.PlayerDataManager;
import me.arkallic.minecore.models.Mail;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

import static me.arkallic.minecore.utils.PMLogger.logMessage;
import static me.arkallic.minecore.utils.ChatUtils.sendChat;

public class PMManager {

    private final PlayerDataManager playerDataManager;
    private final HashMap<UUID, UUID> lastMessaged = new HashMap<>();
    private final MineCore mineCore;

    public PMManager(MineCore mineCore, PlayerDataManager playerDataManager) {
        this.mineCore = mineCore;
        this.playerDataManager = playerDataManager;
    }

    public void sendMail(Player player, OfflinePlayer target, String message) {
        UUID targetID = target.getUniqueId();

        if (!playerDataManager.exists(targetID)) {
            sendChat(player, "&cCouldn't find player: " + target.getName());
            return;
        }
        PlayerData pd = playerDataManager.getOrLoad(targetID);
        pd.getMail().add(new Mail(player.getName(), message));

        if (!target.isOnline()) {
            playerDataManager.unregister(targetID);
        }

        sendChat(player, "&aMessage sent successfully!");
    }

    public void send(UUID senderUUID, UUID recipientUUID, String message) {
        Player sender = Bukkit.getPlayer(senderUUID);
        Player recipient = Bukkit.getPlayer(recipientUUID);

        if (recipient == null || !recipient.isOnline()) {
            sendChat(sender, "&cThat player is currently offline.");
            return;
        }

        if (isIgnored(senderUUID, recipientUUID)) {
            sendChat(sender, "&cYou are ignoring that player and cannot send them a message.");
            return;
        }

        if (isIgnored(recipientUUID, senderUUID)) {
            sendChat(sender, "&cYou cannot message this player because they have ignored you.");
            return;
        }

        sendChat(sender, "&8[&cPM&8] &7To " + recipient.getName() + ": " + message);
        sendChat(recipient, "&8[&cPM&8] &7From " + sender.getName() + ": " + message);
        setLastMessaged(senderUUID, recipientUUID);
        logMessage(senderUUID, recipientUUID, message);
    }

    public void reply(UUID senderUUID, String message) {
        Player sender = Bukkit.getPlayer(senderUUID);
        UUID recipientUUID = this.lastMessaged.get(senderUUID);

        if (senderUUID == null) {
            sendChat(sender, "&cYou have no one to reply to!");
            return;
        }

        this.send(senderUUID, recipientUUID, message);
        setLastMessaged(senderUUID, recipientUUID);
    }

    public HashMap<UUID, UUID> getLastMessaged() {
        return lastMessaged;
    }

    public void setLastMessaged(UUID senderUUID, UUID recipientUUID) {
        this.lastMessaged.put(senderUUID, recipientUUID);
        this.lastMessaged.put(recipientUUID, senderUUID);
    }

    public UUID getLastMessaged(UUID senderUUID) {
       return this.lastMessaged.get(senderUUID);
    }

    public boolean isIgnored(UUID playerUUID, UUID blockedUUID) {
        return playerDataManager.get(playerUUID).getIgnoredList().contains(blockedUUID);
    }
}
