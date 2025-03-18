package me.arkallic.minecore.commands;

import me.arkallic.minecore.data.PlayerData;
import me.arkallic.minecore.managers.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.arkallic.minecore.utils.ServerUtils.playersOnlyCommand;
import static me.arkallic.minecore.utils.ServerUtils.sendChat;

public class IgnoreCommand implements CommandExecutor {

    private final PlayerDataManager playerDataManager;

    public IgnoreCommand(PlayerDataManager playerDataManager) {
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

        if (!(sender instanceof Player p)) {
            sendChat(sender, playersOnlyCommand);
            return true;
        }

        if (args.length != 1) {
            sendChat(sender, "&cUSAGE: /ignore <PLAYER>");
            return true;
        }

        PlayerData pd = playerDataManager.get(p.getUniqueId());

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        if (!target.hasPlayedBefore()) {
            sendChat(sender, "&cPlayer not found!");
            return true;
        }

        if (pd.getIgnoredList().contains(target.getUniqueId())) {
            sendChat(sender, "&cYou are already ignoring that player.");
            return true;
        }

        pd.getIgnoredList().add(target.getUniqueId());
        sendChat(sender, "&aYou are now ignoring " + target.getName() + " and will no longer receive messages from them.");
        return true;
    }
}
