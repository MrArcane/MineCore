package me.arkallic.mineCore.utils;

import me.arkallic.mineCore.MineCore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerUtils {

    //Default messages
    public static String ONLYPLAYERSCOMMAND = "Only players are allowed to use this command.";

    public static void log(MineCore mineCore, Level level, String message) {
        mineCore.getLogger().log(level, message);
    }

    public static String color(String s) {
        // Convert hex color codes from #RRGGBB to the correct format
        Pattern hexPattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = hexPattern.matcher(s);

        while (matcher.find()) {
            String hexColor = matcher.group();
            s = s.replace(hexColor, net.md_5.bungee.api.ChatColor.of(hexColor) + "");
        }

        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static void sendChat(CommandSender sender, String message) {
        sender.sendMessage(color(message));
    }

    public static void announceToServer(String message) {
        String formattedMessage = "&6☑ &l&bAnnouncement: &f" + message;
        for (Player p : Bukkit.getOnlinePlayers()) {
            sendChat(p, formattedMessage);
        }
    }
}
