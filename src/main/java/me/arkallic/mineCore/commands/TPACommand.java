package me.arkallic.mineCore.commands;

import me.arkallic.mineCore.MineCore;
import me.arkallic.mineCore.managers.TPAManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

import static me.arkallic.mineCore.utils.ServerUtils.*;

public class TPACommand implements CommandExecutor {

    private final TPAManager tpaManager;
    private final MineCore mineCore;

    public TPACommand(MineCore mineCore, TPAManager tpaManager) {
        this.tpaManager = tpaManager;
        this.mineCore = mineCore;
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
            log(mineCore, Level.INFO, ONLYPLAYERSCOMMAND);
            return true;
        }
        if (command.getName().equalsIgnoreCase("tpa")) {

            if (args.length == 0) {
                sendChat(sender, "&cUSAGE: /tpa <PLAYER>");
                return true;
            }

            Player target = Bukkit.getPlayer(args[0]);

            if (target == null || !target.isOnline()) {
                sendChat(sender, "&cCouldn't find player.");
                return true;
            }

            tpaManager.sendTPARequest(p.getUniqueId(), target.getUniqueId());

            return true;
        }

        if (command.getName().equalsIgnoreCase("tpaccept")) {
            tpaManager.acceptTPARequest(p.getUniqueId());
            return true;
        }

        if (command.getName().equalsIgnoreCase("tpadeny")) {
            tpaManager.denyTPARequest(p.getUniqueId());
            return true;
        }

        return false;
    }
}
