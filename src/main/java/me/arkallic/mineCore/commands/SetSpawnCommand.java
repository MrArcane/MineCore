package me.arkallic.mineCore.commands;

import me.arkallic.mineCore.MineCore;
import me.arkallic.mineCore.data.ConfigData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

import static me.arkallic.mineCore.utils.ServerUtils.*;

public class SetSpawnCommand implements CommandExecutor {

    private final MineCore mineCore;

    public SetSpawnCommand(MineCore mineCore) {
        this.mineCore = mineCore;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        if (!(sender instanceof Player p)) {
            log(mineCore, Level.INFO, ONLYPLAYERSCOMMAND);
            return true;
        }

        ConfigData configData = mineCore.getConfigData();

        configData.setSpawnLocation(p.getLocation());
        sendChat(sender, "&aSpawn set to your location successfully!");
        return true;
    }
}
