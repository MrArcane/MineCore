package me.arkallic.minecore.commands;

import me.arkallic.minecore.managers.playerdata.PlayerData;
import me.arkallic.minecore.managers.playerdata.PlayerDataManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.arkallic.minecore.utils.ChatUtils.PLAYERSONLYCOMMAND;
import static me.arkallic.minecore.utils.ChatUtils.sendChat;

public class PVPCommand implements CommandExecutor {

    private final PlayerDataManager playerDataManager;

    public PVPCommand(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
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
            sendChat(sender, PLAYERSONLYCOMMAND);
            return true;
        }

        if (!p.hasPermission("minecore.pvp")) {
            sendChat(p, "&cYou don't have permission to use this command.");
            return true;
        }

        PlayerData pd = playerDataManager.get(p.getUniqueId());
        String pvpStatus = (pd.pvpEnabled() ? "Disabled" : "Enabled");

        if (args.length == 0) {
            sendChat(sender, "&eYour PVP status is: &7" + pvpStatus);
            return true;
        }

        if (args[0].equalsIgnoreCase("toggle")) {
            if (pd.pvpEnabled()) {
                pd.setPVP(false);
                sendChat(sender, "&ePVP Disabled, Players can no longer attack you.");
                return true;
            }

            pd.setPVP(true);
            sendChat(sender, "&ePVP Enabled, Players can now attack you.");
            return true;
        }

        sendChat(sender, "&cUsage: /pvp [toggle]");
        return false;
    }
}
