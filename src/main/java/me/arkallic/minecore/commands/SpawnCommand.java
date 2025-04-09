package me.arkallic.minecore.commands;

import me.arkallic.minecore.MineCore;
import me.arkallic.minecore.utils.ConfigData;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

import static me.arkallic.minecore.utils.ChatUtils.*;

public class SpawnCommand implements CommandExecutor {

    private final MineCore mineCore;

    public SpawnCommand(MineCore mineCore) {
        this.mineCore = mineCore;
    }


    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String s, String[] args) {
        if (!(sender instanceof Player p)) {
            log(Level.INFO, PLAYERSONLYCOMMAND);
            return true;
        }
        ConfigData configData = mineCore.getConfigData();
        if (configData.getSpawnLocation() == null) {
            sendChat(p, "&cThere is no spawn!");
            return true;
        }

        p.teleport(configData.getSpawnLocation());
        sendChat(sender, "&aWelcome to the server spawn!");
        return false;
    }
}
