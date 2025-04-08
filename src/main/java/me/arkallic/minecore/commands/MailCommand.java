package me.arkallic.minecore.commands;

import me.arkallic.minecore.managers.playerdata.PlayerData;
import me.arkallic.minecore.managers.PMManager;
import me.arkallic.minecore.managers.playerdata.PlayerDataManager;
import me.arkallic.minecore.utils.NumberUtils;
import me.arkallic.minecore.utils.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

import static me.arkallic.minecore.utils.ChatUtils.log;
import static me.arkallic.minecore.utils.ChatUtils.sendChat;

public class MailCommand implements CommandExecutor {

    private final PlayerDataManager playerDataManager;
    private final PMManager pmManager;

    public MailCommand(PlayerDataManager playerDataManager, PMManager pmManager) {
        this.playerDataManager = playerDataManager;
        this.pmManager = pmManager;
    }

    private void sendHelpMessage(Player p) {
        sendChat(p, "&aInbox");
        sendChat(p, "/mail read - Check your inbox");
        sendChat(p, "/mail send <player> <messsage> - sends a message to a player");
        sendChat(p, "/mail remove <int> - Remove a message by its number ");
        sendChat(p, "/mail clear - Clears your inbox");
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
            log(Level.INFO, ChatUtils.PLAYERSONLYCOMMAND);
            return true;
        }

        PlayerData pd = playerDataManager.get(p.getUniqueId());

        if (args.length == 0) {
            sendHelpMessage(p);
            return true;
        }

        String subCommand = args[0].toLowerCase();

        switch (subCommand) {
            case "read":
                int page = 1;

                // If a page argument was given, parse it
                if (args.length > 1) {
                    try {
                        page = Integer.parseInt(args[1]);
                    } catch (NumberFormatException e) {
                        sendChat(p, "&cInvalid page number.");
                        return true;
                    }
                }

                playerDataManager.displayInbox(p, page);
                break;

            case "send":
                OfflinePlayer target = Bukkit.getOfflinePlayer(args[1]);
                StringBuilder message = new StringBuilder();

                for (int i = 2; i < args.length; i++) {
                    message.append(args[i]).append(" ");
                }

                pmManager.sendMail(p, target, message.toString().trim());
                break;

            case "remove":

                if (!NumberUtils.isInt(args[1])) {
                    sendChat(p, "&cUSAGE: /mail remove <integer>");
                    return true;
                }

                playerDataManager.removeFromInbox(p, Integer.parseInt(args[1]));
                break;

            case "clear":
                playerDataManager.clearInbox(p);
                break;

            default:
                sendHelpMessage(p);
                return true;
        }
        return false;
    }
}
