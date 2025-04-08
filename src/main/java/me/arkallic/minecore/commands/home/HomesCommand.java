package me.arkallic.minecore.commands.home;

import me.arkallic.minecore.MineCore;
import me.arkallic.minecore.managers.HomeManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

import static me.arkallic.minecore.utils.ServerUtils.*;

public class HomesCommand implements CommandExecutor {

    private final MineCore mineCore;
    private final HomeManager homeManager;

    public HomesCommand(MineCore mineCore, HomeManager homeManager) {
        this.mineCore = mineCore;
        this.homeManager = homeManager;
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
        homeManager.getHomesList(p.getUniqueId());
        return true;
    }
}
