package me.arkallic.minecore.managers;

import me.arkallic.minecore.MineCore;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Text;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.UUID;

import static me.arkallic.minecore.utils.ChatUtils.sendChat;

public class TPAManager {


    private final HashMap<UUID, UUID> tpaMap = new HashMap<>();
    private final HashMap<UUID, BukkitRunnable> tpaTimers = new HashMap<>();
    private final MineCore mineCore;

    public TPAManager(MineCore mineCore) {
        this.mineCore = mineCore;
    }

    public void sendTPARequest(UUID senderUUID, UUID targetUUID) {
        Player sender = Bukkit.getPlayer(senderUUID);
        Player target = Bukkit.getPlayer(targetUUID);

        if (tpaMap.containsKey(targetUUID)) {
            sendTPAMessage(MessageType.ERROR, sender.getUniqueId(), "That player already has a pending TPA request.");
            return;
        }

        if (target == null || !target.isOnline()) {
            sendTPAMessage(MessageType.ERROR, sender.getUniqueId(), "Couldn't find player.");
            return;
        }

        tpaMap.put(targetUUID, senderUUID);
        startTimer(senderUUID, targetUUID);

        sendTPAMessage(MessageType.SUCCESS, sender.getUniqueId(), "TPA request sent to " + target.getName());
        sendTPAMessage(MessageType.ERROR, sender.getUniqueId(), "You can cancel this request by typing /tpacancel");

        // Fancy formatted clickable message with TextComponent
        TextComponent requestMessage = new TextComponent(sender.getName() + " wants to teleport to you.");
        TextComponent acceptCommand = new TextComponent(ChatColor.GREEN + " [✔ Accept]");
        TextComponent denyCommand = new TextComponent(ChatColor.RED + " [✖ Deny]");

        acceptCommand.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpaccept"));
        denyCommand.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tpadeny"));
        acceptCommand.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to accept the TPA request!")));
        denyCommand.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new Text("Click to deny the TPA request!")));

        requestMessage.addExtra(acceptCommand);
        requestMessage.addExtra(denyCommand);

        target.spigot().sendMessage(requestMessage);
        this.sendTPAMessage(MessageType.TPA_REQUEST, target.getUniqueId(), "You have received a TPA request!");
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
            this.sendTPAMessage(MessageType.WARNING, target.getUniqueId(), "The player that sent the request is no longer online.");
            return;
        }

        // Cancel the timer if the request is accepted
        cancelTimer(targetUUID);

        sender.teleport(target.getLocation());
        sendTPAMessage(MessageType.SUCCESS, sender.getUniqueId(), "Your teleport request was accepted!");
        sendTPAMessage(MessageType.SUCCESS, target.getUniqueId(), "You accepted " + sender.getName() + "'s request!");

        removeTPARequest(targetUUID);
        removeTPARequest(senderUUID);
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
            sendTPAMessage(MessageType.ERROR, sender.getUniqueId(), "Your TPA request to " + target.getName() + " has been denied.");
        }

        sendTPAMessage(MessageType.ERROR, target.getUniqueId(), "You denied the TPA request from " + sender.getName());

        cancelTimer(targetUUID);

        removeTPARequest(targetUUID);
        removeTPARequest(senderUUID);
    }

    public void cancelTPARequest(UUID playerUUID) {
        Player player = Bukkit.getPlayer(playerUUID);
            if (!tpaMap.containsValue(playerUUID)) {
                sendChat(player, "You have no pending TPA requests.");
                return;
            }

            // Find the target player who received the request
            UUID targetUUID = null;
            for (UUID key : tpaMap.keySet()) {
                if (tpaMap.get(key).equals(playerUUID)) {
                    targetUUID = key;
                    break;
                }
            }

            if (targetUUID == null) {
                sendTPAMessage(MessageType.ERROR, player.getUniqueId(), "Could not find your TPA request.");
                return;
            }

            Player target = Bukkit.getPlayer(targetUUID);
            sendChat(target, "&c" + player.getName() + " has canceled their TPA request.");
            sendTPAMessage(MessageType.WARNING, player.getUniqueId(), "You canceled your TPA request.");
            removeTPARequest(targetUUID);
            removeTPARequest(playerUUID);
        }

    public void removeTPARequest(UUID uuid) {
        tpaMap.remove(uuid);
        cancelTimer(uuid);
    }

    private void startTimer(UUID senderUUID, UUID targetUUID) {
        BukkitRunnable timerTask = new BukkitRunnable() {
            int timeLeft = 30;

            @Override
            public void run() {
                if (timeLeft <= 0) {
                    Player sender = Bukkit.getPlayer(senderUUID);
                    Player target = Bukkit.getPlayer(targetUUID);

                    if (sender != null) {
                        sendTPAMessage(MessageType.ERROR, sender.getUniqueId(), "Your TPA request to " + target.getName() + " expired.");
                    }
                    if (target != null) {
                        sendTPAMessage(MessageType.ERROR, target.getUniqueId(), "TPA request from " + sender.getName() + " expired.");
                    }

                    removeTPARequest(targetUUID);
                    removeTPARequest(senderUUID);
                    this.cancel();
                    return;
                }

                // Send countdown messages at key intervals
                if (timeLeft == 10 || timeLeft == 5 || timeLeft <= 3) {
                    Player sender = Bukkit.getPlayer(senderUUID);
                    Player target = Bukkit.getPlayer(targetUUID);

                    if (sender != null) {
                        sendTPAMessage(MessageType.WARNING, sender.getUniqueId(), "Your TPA request expires in " + timeLeft + " seconds!");
                    }
                    if (target != null) {
                        sendTPAMessage(MessageType.WARNING, target.getUniqueId(), "TPA request from " + sender.getName() + " expires in " + timeLeft + " seconds!");
                    }
                }

                timeLeft--;
            }
        };

        timerTask.runTaskTimer(mineCore, 0L, 20L);
        tpaTimers.put(targetUUID, timerTask);
    }

    private void cancelTimer(UUID targetUUID) {
        BukkitRunnable timer = tpaTimers.remove(targetUUID);
        if (timer != null) {
            timer.cancel();
        }
    }

    public enum MessageType {
        SUCCESS, WARNING, ERROR, TPA_REQUEST
    }

    public void sendTPAMessage(MessageType type, UUID uuid, String message) {
        Player player = Bukkit.getPlayer(uuid);
        TextComponent textComponent = new TextComponent(message);
        ChatColor color;
        Sound sound;

        switch (type) {
            case SUCCESS:
                color = ChatColor.GREEN;
                sound = Sound.ENTITY_PLAYER_LEVELUP;
                break;
            case WARNING:
                color = ChatColor.YELLOW;
                sound = Sound.BLOCK_NOTE_BLOCK_BASS;
                break;
            case ERROR:
                color = ChatColor.RED;
                sound = Sound.ENTITY_VILLAGER_NO;
                break;
            case TPA_REQUEST:
                color = ChatColor.AQUA;
                sound = Sound.ENTITY_EXPERIENCE_ORB_PICKUP;
                break;
            default:
                color = ChatColor.GRAY;
                sound = Sound.UI_BUTTON_CLICK;
                break;
        }

        textComponent.setColor(color);
        player.spigot().sendMessage(textComponent);
        player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
    }
}
