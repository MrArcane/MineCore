package me.arkallic.minecore.commands;

import me.arkallic.minecore.MineCore;
import me.arkallic.minecore.managers.PMManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.arkallic.minecore.utils.ChatUtils.sendChat;

public class MessageCommand implements CommandExecutor {

    private final MineCore mineCore;
    private final PMManager pmManager;

    public MessageCommand(MineCore mineCore, PMManager pmManager) {
        this.mineCore = mineCore;
        this.pmManager = pmManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        StringBuilder message = new StringBuilder();



        if (args.length <= 1) {
            sendChat(sender, "&cINVALID USAGE: /message <PLAYER> <MESSAGE>");
            return true;
        }

        Player p = (Player) sender;
        Player recipient = Bukkit.getPlayer(args[0]);

        if (!p.hasPermission("minecore.message")) {
            sendChat(p, "&cYou don't have permission to use this command.");
            return true;
        }

        if (recipient == null || !recipient.isOnline()) {
            sendChat(sender, "&cPlayer is not online.");
            return true;
        }

        if (recipient.getUniqueId().equals(p.getUniqueId())) {
            sendChat(p, "&cYou cannot message yourself!");
            return true;
        }

        for (int i = 1; i < args.length; i++) {
            message.append(args[i]).append(" ");
        }

        pmManager.send(p.getUniqueId(), recipient.getUniqueId(), message.toString().trim());
        return true;
    }
}
