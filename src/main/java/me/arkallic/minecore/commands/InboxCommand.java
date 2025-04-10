package me.arkallic.minecore.commands;

import me.arkallic.minecore.managers.playerdata.PlayerDataManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

import static me.arkallic.minecore.utils.ChatUtils.*;

public class InboxCommand implements CommandExecutor {

    private final PlayerDataManager playerDataManager;

    public InboxCommand(PlayerDataManager playerDataManager) {
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
            log(Level.INFO, PLAYERSONLYCOMMAND);
            return true;
        }

        if (!p.hasPermission("minecore.inbox")) {
            sendChat(p, "&cYou don't have permission to use this command.");
            return true;
        }

        int page = 1;

        // If a page argument was given, parse it
        if (args.length > 0) {
            try {
                page = Integer.parseInt(args[0]);
            } catch (NumberFormatException e) {
                sendChat(p, "&cInvalid page number.");
                return true;
            }
        }

        playerDataManager.displayInbox(p, page);
        return false;
    }
}
