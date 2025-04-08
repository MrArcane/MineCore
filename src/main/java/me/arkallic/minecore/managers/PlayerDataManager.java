package me.arkallic.minecore.managers;

import me.arkallic.minecore.MineCore;
import me.arkallic.minecore.data.PlayerData;
import me.arkallic.minecore.models.Mail;
import me.arkallic.minecore.utils.Paginator;
import me.arkallic.minecore.wrappers.YMLFileWrapper;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import java.util.*;
import java.util.logging.Level;

import static me.arkallic.minecore.utils.ServerUtils.log;
import static me.arkallic.minecore.utils.ServerUtils.sendChat;

public class PlayerDataManager {

    private final Map<UUID, PlayerData> playerMap = new HashMap<>();
    private final MineCore mineCore;

    public PlayerDataManager(MineCore mineCore) {
        this.mineCore = mineCore;
    }

    public void register(UUID uuid) {
        this.playerMap.computeIfAbsent(uuid, _ -> new PlayerData(uuid, mineCore));
        PlayerData pd = playerMap.get(uuid);
        pd.loadPlayerData();
    }

    public void unregister(UUID uuid) {
        PlayerData pd = this.playerMap.get(uuid);
        if (pd != null) {
            pd.saveData();
            this.playerMap.remove(uuid);
        } else {
            log(Level.SEVERE, "Tried to unregister non loaded player: " + uuid);
        }
    }

    public PlayerData get(UUID uuid) {
        return this.playerMap.get(uuid);
    }

    public PlayerData getOrLoad(UUID uuid) {
        return playerMap.computeIfAbsent(uuid, id -> {
            PlayerData pd = new PlayerData(id, mineCore);
            pd.loadPlayerData();
            return pd;
        });
    }

    public boolean exists(UUID uuid) {
        OfflinePlayer target = Bukkit.getOfflinePlayer(uuid);
        return  target.hasPlayedBefore();
    }

    public boolean isLoaded(UUID uuid) {
        return this.playerMap.containsKey(uuid);
    }


    public void displayInbox(Player player, int page) {
        PlayerData pd = get(player.getUniqueId());
        List<Mail> mailList = pd.getMail();

        Paginator<Mail> paginator = new Paginator<>(mailList, 5); // 5 per page

        if (paginator.isEmpty()) {
            sendChat(player, "&cInbox is empty.");
            return;
        }

        int totalPages = paginator.getTotalPages();
        if (page < 1 || page > totalPages) {
            sendChat(player, "&cInvalid page number. There are only " + totalPages + " pages.");
            return;
        }

        sendChat(player, "&6--- Inbox Page " + page + "/" + totalPages + " ---");

        List<Mail> pageItems = paginator.getPage(page);
        int globalIndexStart = (page - 1) * 5;

        for (int i = 0; i < pageItems.size(); i++) {
            Mail msg = pageItems.get(i);
            sendChat(player, (globalIndexStart + i + 1) + ". &e" + msg.getAuthor() + "&7: " + msg.getMessage());
        }
    }

    public void removeFromInbox(Player player, int index) {
        PlayerData pd = get(player.getUniqueId());

        if (pd.getMail().isEmpty()) {
            sendChat(player, "&cInbox is empty.");
            return;
        }

        if (index < 1 || index > pd.getMail().size()) {
            sendChat(player, "&cIncorrect index, try again!");
            displayInbox(player, 1);
            return;
        }

        pd.getMail().remove(index - 1);
        sendChat(player, "&aMessage removed successfully!");
    }


    public void clearInbox(Player player) {
        PlayerData pd = get(player.getUniqueId());
        if (pd.getMail().isEmpty()) {
            sendChat(player, "&cYour inbox is already empty.");
            return;
        }

        pd.getMail().clear();
        sendChat(player, "&aYour inbox has been cleared successfully!");
    }
}
