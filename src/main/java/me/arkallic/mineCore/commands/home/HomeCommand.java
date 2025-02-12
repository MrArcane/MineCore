package me.arkallic.mineCore.commands.home;

import me.arkallic.mineCore.MineCore;
import me.arkallic.mineCore.managers.HomeManager;
import me.arkallic.mineCore.objects.Home;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.logging.Level;

import static me.arkallic.mineCore.utils.ServerUtils.*;

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
            log(mineCore, Level.INFO, ONLYPLAYERSCOMMAND);
            return true;
        }

        List<Home> homes = homeManager.getHomes(p.getUniqueId());

        if (args.length == 0) {
            if (homes.isEmpty()) {
                sendChat(p, homeManager.getNoHomeMessage());
                return true;
            }

            if (homes.size() > 1) {
                sendChat(p, "&cUSAGE: /home <NAME>");
                for (Home home : homes) {
                    sendChat(p, "&a" + home.name() + " &7" + home.location().getWorld().getName());
                }
                return true;
            }

            //If the player only has 1 home.
            homeManager.teleportHome(p.getUniqueId(), homes.get(0));
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
