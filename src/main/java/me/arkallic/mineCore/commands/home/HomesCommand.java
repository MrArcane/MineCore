package me.arkallic.mineCore.commands.home;

import me.arkallic.mineCore.MineCore;
import me.arkallic.mineCore.managers.HomeManager;
import me.arkallic.mineCore.objects.Home;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

import static me.arkallic.mineCore.utils.ServerUtils.*;

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
            log(mineCore, Level.INFO, ONLYPLAYERSCOMMAND);
            return true;
        }
        List<Home> homes = homeManager.getHomes(p.getUniqueId());
        int homeCount = homes.size();
        int homeLimit = homeManager.homeLimit(p.getUniqueId());

        if (homes.isEmpty()) {
            sendChat(p, "&c❌ You have no homes set! Use &e/sethome <name> &cto create one.");
        } else {
            sendChat(p, String.format("&6✦ &e%s's Homes &7( &a&l%d &7/ &c&l%d &7)", p.getName(), homeCount, homeLimit));
            homes.forEach(home -> sendChat(p, String.format("&7▪ &b%s", home.name())));
        }
        return true;
    }
}
