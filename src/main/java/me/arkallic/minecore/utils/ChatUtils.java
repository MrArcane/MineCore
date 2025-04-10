package me.arkallic.minecore.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChatUtils {

    //Default messages
    public final static String PLAYERSONLYCOMMAND = "Only players are allowed to use this command.";

    public static void log(Level level, String message) {
        Bukkit.getPluginManager().getPlugin("MineCore").getLogger().log(level, message);
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

    public static void broadcast(String message) {
        String formattedMessage = "&6☑ &l&bBroadcast: &f" + message;
        for (Player p : Bukkit.getOnlinePlayers()) {
            sendChat(p, formattedMessage);
        }
    }
}
