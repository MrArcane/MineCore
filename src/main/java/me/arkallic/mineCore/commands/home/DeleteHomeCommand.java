package me.arkallic.mineCore.commands.home;

import me.arkallic.mineCore.MineCore;
import me.arkallic.mineCore.managers.HomeManager;
import me.arkallic.mineCore.utils.ServerUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

import static me.arkallic.mineCore.utils.ServerUtils.sendChat;

public class DeleteHomeCommand implements CommandExecutor {

    private final MineCore mineCore;
    private final HomeManager homeManager;

    public DeleteHomeCommand(MineCore mineCore, HomeManager homeManager) {
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
            ServerUtils.log(mineCore, Level.INFO, ServerUtils.ONLYPLAYERSCOMMAND);
            return true;
        }


        if (args.length != 1) {
            sendChat(p, "&cINVALID USAGE: /deletehome <NAME>");
            return true;
        }

        homeManager.deleteHome(p.getUniqueId(), args[0]);
        return true;
    }
}
