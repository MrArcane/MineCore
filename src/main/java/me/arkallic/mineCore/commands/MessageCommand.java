package me.arkallic.mineCore.commands;

import me.arkallic.mineCore.MineCore;
import me.arkallic.mineCore.managers.PMManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static me.arkallic.mineCore.utils.ServerUtils.sendChat;

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
