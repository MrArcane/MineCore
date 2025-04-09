package me.arkallic.minecore.commands.home;

import me.arkallic.minecore.managers.playerdata.PlayerData;
import me.arkallic.minecore.managers.playerdata.PlayerDataManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

import static me.arkallic.minecore.utils.ChatUtils.log;
import static me.arkallic.minecore.utils.ChatUtils.sendChat;

public class MuteCommand implements CommandExecutor {

    private final PlayerDataManager playerDataManager;

    public MuteCommand(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }

    /**
     * Executes the given command, returning its success.
     * <br>
     * If false is returned, then the "usage" plugin.yml entry for this command
     * (if defined) will be sent to the player.
     *
     * @param sender  Source of the command
     * @param command Command which was executed
     * @param label   Alias of the command which was used
     * @param args    Passed command arguments
     * @return true if a valid command, otherwise false
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (sender instanceof Player player && !player.hasPermission("minecore.mute")) {
            sendChat(player, "&cYou don't have permission to use this command.");
            return true;
        }

        if (args.length < 1) {
            sendChat(sender, "&cUSAGE: /mute <player>");
            return true;
        }

        OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
        PlayerData pd = playerDataManager.get(offlinePlayer.getUniqueId());

        if (pd == null || !offlinePlayer.hasPlayedBefore() && !offlinePlayer.isOnline()) {
            String noDataMessage = "&cThat player has never joined before.";

            if (!(sender instanceof Player player)) {
                log(Level.INFO, noDataMessage);
                return true;
            }
            sendChat(player, noDataMessage);
            return true;
        }

        boolean isMuted = !pd.isMuted();
        pd.setMuted(isMuted);

        String mutedMessage = "&a" + args[0] + (isMuted ? " has been muted." : " has been unmuted.");
        log(Level.INFO, ChatColor.stripColor(mutedMessage));
        sendChat(sender, mutedMessage);
        return true;
    }
}
