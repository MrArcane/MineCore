package me.arkallic.minecore.commands;

import me.arkallic.minecore.MineCore;
import me.arkallic.minecore.utils.ConfigData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

import static me.arkallic.minecore.utils.ChatUtils.*;

public class SetSpawnCommand implements CommandExecutor {

    private final MineCore mineCore;

    public SetSpawnCommand(MineCore mineCore) {
        this.mineCore = mineCore;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player p)) {
            log(Level.INFO, PLAYERSONLYCOMMAND);
            return true;
        }

        ConfigData configData = mineCore.getConfigData();

        configData.setSpawnLocation(p.getLocation());
        configData.saveSpawnLocation();
        configData.reloadConfig();
        sendChat(sender, "&aSpawn set to your location successfully!");
        return true;
    }
}
