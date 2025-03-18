package me.arkallic.minecore.commands.home;

import me.arkallic.minecore.MineCore;
import me.arkallic.minecore.managers.HomeManager;
import me.arkallic.minecore.utils.ServerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

import static me.arkallic.minecore.utils.ServerUtils.sendChat;

public class SetHomeCommand implements CommandExecutor {

    private MineCore mineCore;
    private HomeManager homeManager;

    public SetHomeCommand(MineCore mineCore, HomeManager homeManager) {
        this.mineCore = mineCore;
        this.homeManager = homeManager;
    }

    /**
     * @param sender
     * @param cmd
     * @param label
     * @param args
     * @return
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player p)) {
            ServerUtils.log(mineCore, Level.INFO, ServerUtils.playersOnlyCommand);
            return true;
        }

        if (args.length != 1) {
            sendChat(p, "&cINVALID USAGE: /sethome <NAME>");
            return true;
        }

        if (!homeManager.canSetHome(p.getUniqueId())) {
            sendChat(sender, homeManager.getHomeLimitMessage());
            return true;
        }

        homeManager.setHome(p.getUniqueId(), args[0], p.getLocation());
        return true;
    }
}
