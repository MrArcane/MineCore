package me.arkallic.minecore.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.arkallic.minecore.utils.ChatUtils.broadcast;
import static me.arkallic.minecore.utils.ChatUtils.sendChat;

public class BroadcastCommand implements CommandExecutor {
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
        StringBuilder message = new StringBuilder();

        if (sender instanceof Player player && !player.hasPermission("minecore.broadcast")) {
            sendChat(player, "&cYou don't have permission to use this command.");
            return true;
        }
        for (String arg : args) {
            message.append(arg).append(" ");
        }

        broadcast(message.toString().trim());
        return true;
    }
}
