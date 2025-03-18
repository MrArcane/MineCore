package me.arkallic.minecore.commands;

import me.arkallic.minecore.MineCore;
import me.arkallic.minecore.managers.PMManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

import static me.arkallic.minecore.utils.ServerUtils.playersOnlyCommand;
import static me.arkallic.minecore.utils.ServerUtils.sendChat;

public class ReplyCommand implements CommandExecutor {

    private final MineCore mineCore;
    private final PMManager pmManager;

    public ReplyCommand(MineCore mineCore, PMManager pmManager) {
        this.mineCore = mineCore;
        this.pmManager = pmManager;
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
        StringBuilder message = new StringBuilder();

        if (!(sender instanceof Player p)) {
            sendChat(sender, playersOnlyCommand);
            return true;
        }

        if (args.length == 0) {
            sendChat(p, "&cINVALID USAGE: /reply <message>");
            return true;
        }

        UUID lastMessaged = pmManager.getLastMessaged(p.getUniqueId());

        if (lastMessaged == null || !pmManager.getLastMessaged().containsKey(p.getUniqueId())) {
            sendChat(sender, "&cYou don’t have any messages to reply to!");
            return true;
        }

        for (String arg : args) {
            message.append(arg).append(" ");
        }
        pmManager.reply(p.getUniqueId(), message.toString().trim());
        return false;
    }
}
