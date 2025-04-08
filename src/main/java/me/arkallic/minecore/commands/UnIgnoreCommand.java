package me.arkallic.minecore.commands;

import me.arkallic.minecore.managers.playerdata.PlayerData;
import me.arkallic.minecore.managers.playerdata.PlayerDataManager;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static me.arkallic.minecore.utils.ChatUtils.PLAYERSONLYCOMMAND;
import static me.arkallic.minecore.utils.ChatUtils.sendChat;

public class UnIgnoreCommand implements CommandExecutor {

    private final PlayerDataManager playerDataManager;

    public UnIgnoreCommand(PlayerDataManager playerDataManager) {
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

        if (args.length != 1) {
            sendChat(sender, "&cUSAGE: /unignore <PLAYER>");
            return true;
        }

        PlayerData pd = playerDataManager.get(p.getUniqueId());

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);

        if (!target.hasPlayedBefore()) {
            sendChat(p, "&cCouldn't find that player.");
            return true;
        }

        if (!pd.getIgnoredList().contains(target.getUniqueId())) {
            sendChat(p, "&cYou haven't ignored this player.");
            return true;
        }

        pd.getIgnoredList().remove(target.getUniqueId());
        sendChat(p, "&aYou have successfully unignored " + target.getName() + ".");

        return false;
    }
}
