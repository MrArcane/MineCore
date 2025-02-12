package me.arkallic.mineCore.commands.home;

import me.arkallic.mineCore.data.PlayerData;
import me.arkallic.mineCore.managers.PlayerDataManager;
import me.arkallic.mineCore.utils.MiscUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import static me.arkallic.mineCore.utils.ServerUtils.sendChat;

public class HomeLimitCommand implements CommandExecutor {

    private PlayerDataManager playerDataManager;

    public HomeLimitCommand(PlayerDataManager playerDataManager) {
        this.playerDataManager = playerDataManager;
    }
    /**
     * @param sender
     * @param cmd
     * @param s
     * @param args
     * @return
     */
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {

        if (args.length != 2 || !MiscUtils.isInt(args[1])) {
            sendChat(sender, "&cINVALID USAGE: /homelimit <PLAYER> <AMOUNT>");
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(args[0]);
        PlayerData pd = playerDataManager.get(target.getUniqueId());
        int amount = Integer.parseInt(args[1]);

        pd.modifyHomeLimit(amount);

        if (!target.isOnline()) {
            playerDataManager.unregister(target.getUniqueId());
        }
        sendChat(sender, "&aYou have successfully updated &7" + target.getName() + "&a's home limit to &7" + pd.getHomeLimit() + "&a.");
        return true;
    }
}
