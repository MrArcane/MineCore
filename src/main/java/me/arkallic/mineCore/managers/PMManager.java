package me.arkallic.mineCore.managers;

import me.arkallic.mineCore.MineCore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

import static me.arkallic.mineCore.loggers.PMLogger.logMessage;
import static me.arkallic.mineCore.utils.ServerUtils.sendChat;

public class PMManager {

    private final PlayerDataManager playerDataManager;
    private final HashMap<UUID, UUID> lastMessaged = new HashMap<>();
    private final MineCore mineCore;

    public PMManager(MineCore mineCore, PlayerDataManager playerDataManager) {
        this.mineCore = mineCore;
        this.playerDataManager = playerDataManager;
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
