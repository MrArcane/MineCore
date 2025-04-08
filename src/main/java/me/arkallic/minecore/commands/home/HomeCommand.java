package me.arkallic.minecore.commands.home;

import me.arkallic.minecore.MineCore;
import me.arkallic.minecore.managers.HomeManager;
import me.arkallic.minecore.models.Home;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.logging.Level;

import static me.arkallic.minecore.utils.ServerUtils.*;

public class HomeCommand implements CommandExecutor {

    private final HomeManager homeManager;
    private final MineCore mineCore;

    public HomeCommand(MineCore mineCore, HomeManager homeManager) {
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
            log(Level.INFO, PLAYERSONLYCOMMAND);
            return true;
        }

        HashMap<String, Home> homes = homeManager.getHomes(p.getUniqueId());

        if (args.length == 0) {
            if (homes.isEmpty()) {
                sendChat(p, homeManager.getNoHomeMessage());
                return true;
            }

            if (homes.size() > 1) {
                sendChat(p, "&cUSAGE: /home <NAME>");
                homeManager.getHomesList(p.getUniqueId());
                return true;
            }

            //If the player only has 1 home.
            for (Home home : homes.values()) {
                homeManager.teleportHome(p.getUniqueId(), home);
            }
            return true;
        }

        Home home = homeManager.getHome(p.getUniqueId(), args[0].toLowerCase());

        if (home == null) {
            sendChat(p, "&cINVALID HOME.");
            return true;
        }

        homeManager.teleportHome(p.getUniqueId(), home);

        return true;
    }

}
