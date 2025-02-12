package me.arkallic.mineCore.loggers;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class PMLogger {

    private static File logFile;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    public static void logMessage(UUID senderUUID, UUID recipientUUID, String message) {
        Player sender = Bukkit.getPlayer(senderUUID);
        Player recipient = Bukkit.getPlayer(recipientUUID);

        String senderName = (sender != null) ? sender.getName() : "Unknown";
        String recipientName = (recipient != null) ? recipient.getName() : "Unknown";
        String timestamp = LocalDateTime.now().format(timeFormatter);

        String logEntry = "[" + timestamp + "] " + senderName + " -> " + recipientName + ": " + message;

        // Create a new log file every day
        File logFile = new File("plugins/MineCore/logs", LocalDate.now().format(dateFormatter) + ".log");

        // Ensure the directory exists
        logFile.getParentFile().mkdirs();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFile, true))) {
            writer.write(logEntry);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void cleanOldLogs(int daysToKeep) {
        File logDir = new File("plugins/MineCore/logs");
        if (!logDir.exists()) return;

        File[] logFiles = logDir.listFiles();
        if (logFiles == null) return;

        LocalDate cutoffDate = LocalDate.now().minusDays(daysToKeep);
        for (File file : logFiles) {
            if (file.getName().endsWith(".log")) {
                String fileName = file.getName().replace(".log", "");
                try {
                    LocalDate fileDate = LocalDate.parse(fileName, dateFormatter);
                    if (fileDate.isBefore(cutoffDate)) {
                        file.delete(); // Delete old logs
                    }
                } catch (Exception ignored) {} // Ignore parsing errors
            }
        }
    }
}
