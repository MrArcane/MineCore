package me.arkallic.mineCore.managers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

import static me.arkallic.mineCore.utils.ServerUtils.sendChat;

public class TPAManager {

    private final HashMap<UUID, UUID> tpaMap = new HashMap<>();

    public void sendTPARequest(UUID senderUUID, UUID targetUUID) {

        Player sender = Bukkit.getPlayer(senderUUID);

        if (tpaMap.containsKey(targetUUID)) {
            sendChat(sender, "&cThat player already has a pending TPA request.");
            return;
        }

        Player target = Bukkit.getPlayer(targetUUID);

        if (target == null || !target.isOnline()) {
            sendChat(sender, "&cCouldn't find player.");
            return;
        }

        tpaMap.put(targetUUID, senderUUID);

        sendChat(sender, "&aTPA request sent to &7" + target.getName());
        sendChat(target, String.format("&7%s &aWants to teleport to you", sender.getName()));
        sendChat(target, "&eType &7/tpaccept &e to accpet or &7/tpadeny &eto deny");
    }

    public void acceptTPARequest(UUID targetUUID) {

        UUID senderUUID = tpaMap.get(targetUUID);
        Player target = Bukkit.getPlayer(targetUUID);

        if (target == null || !target.isOnline()) return;

        if (senderUUID == null) {
            sendChat(target, "&cYou have no TPA requests.");
            return;
        }

        Player sender = Bukkit.getPlayer(senderUUID);

        if (sender == null || !sender.isOnline()) {
            sendChat(target, "&cThe player that sent the request is no longer online.");
            return;
        }

        sender.teleport(target.getLocation());
        sendChat(sender, "&aYour teleport request was accepted!");
        sendChat(target, "&aYou accepted &7" + sender.getName() + "&a's request!");
        tpaMap.remove(targetUUID);
        tpaMap.remove(senderUUID);
    }

    public void denyTPARequest(UUID targetUUID) {

        UUID senderUUID = tpaMap.get(targetUUID);
        Player target = Bukkit.getPlayer(targetUUID);

        if (target == null || !target.isOnline()) return;

        if (senderUUID == null) {
            sendChat(target, "&cYou have no TPA requests.");
            return;
        }

        Player sender = Bukkit.getPlayer(senderUUID);

        if (sender != null) {
            sendChat(sender, "&aYour TPA request to &7" + target.getName() + " &ahas been denied.");
        }

        sendChat(target, "&aYou denied the TPA request from &7" + sender.getName());
        tpaMap.remove(targetUUID);
        tpaMap.remove(senderUUID);

    }

    public void removeTPARequest(UUID uuid) {
        tpaMap.remove(uuid);
    }
}
