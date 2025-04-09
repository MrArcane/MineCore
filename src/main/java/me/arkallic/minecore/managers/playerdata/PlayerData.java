package me.arkallic.minecore.managers.playerdata;

import me.arkallic.minecore.MineCore;
import me.arkallic.minecore.models.Home;
import me.arkallic.minecore.models.Mail;
import me.arkallic.minecore.managers.YMLFileManager;
import me.arkallic.minecore.utils.ConfigData;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class PlayerData extends YMLFileManager {

    private final LinkedHashMap<String, Home> homeMap = new LinkedHashMap<>();
    private final List<Mail> inbox = new ArrayList<>();
    private final List<UUID> ignoredList = new ArrayList<>();
    private int homeLimit;
    private boolean pvp;
    private UUID guild;
    private boolean muted = false;


    public PlayerData(UUID uuid, MineCore mineCore) {
        super("Players", uuid.toString(), mineCore);

        pvp = mineCore.getConfigData().isDefaultPVPEnabled();
        homeLimit = mineCore.getConfigData().getPlayerHomeLimit();
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public UUID getGuild() {
        return guild;
    }

    public void setGuild(UUID guild) {
        this.guild = guild;
    }

    public LinkedHashMap<String, Home> getHomes() {
        return homeMap;
    }

    public List<Mail> getMail() {
        return inbox;
    }

    public List<UUID> getIgnoredList() {
        return ignoredList;
    }

    public int getHomeLimit() {
        return homeLimit;
    }

    public boolean pvpEnabled() {
        return pvp;
    }

    public void setPVP(boolean pvp) {
        this.pvp = pvp;
    }

    public void modifyHomeLimit(int amount) {
        this.homeLimit += amount;
        if (this.homeLimit <= 1) {
            this.homeLimit = 1; // Prevents negative home limits
        }
    }

    public void addHome(Home home) {
        this.homeMap.putIfAbsent(home.name(), home);
    }

    public void deleteHome(String homeName) {
        this.homeMap.remove(homeName);
    }

    public void loadPlayerData() {
        loadSettings();
        loadHomes();
        loadMail();
        loadIgnoredPlayers();
    }
    private void loadIgnoredPlayers() {
        if (this.getConfig().isConfigurationSection("IgnoredPlayers")) {
            for (String ignored : this.getConfig().getConfigurationSection("IgnoredPlayers").getKeys(false)) {
                UUID uuid = UUID.fromString(ignored);
                ignoredList.add(uuid);
            }
        }
    }

    private void loadSettings() {
        if (this.getConfig().isConfigurationSection("Settings")) {
            homeLimit = this.getConfig().getInt("Settings.HomeLimit");
            pvp = this.getConfig().getBoolean("Settings.PVP");
            muted = this.getConfig().getBoolean("Settings.Muted");

            if (this.getConfig().getString("Settings.Guild") != null) {
                UUID guildUUID = UUID.fromString(this.getConfig().getString("Settings.Guild"));
                guild = guildUUID;
            }
        }
    }

    private void loadMail() {
        if (!this.getConfig().isConfigurationSection("Mail")) {
            return;
        }

        for (String key : this.getConfig().getConfigurationSection("Mail").getKeys(false)) {
            ConfigurationSection mailSection = this.getConfig().getConfigurationSection("Mail." + key);
            String author = mailSection.getString("Author");
            String message = mailSection.getString("Message");

            this.inbox.add(new Mail(author, message));
        }
    }

    private void loadHomes() {
        if (this.getConfig().isConfigurationSection("Homes")) {
            for (String homeName : this.getConfig().getConfigurationSection("Homes").getKeys(false)) {
                Home home = new Home(homeName, this.getConfig().getLocation("Homes." + homeName));
                this.getHomes().putIfAbsent(homeName, home);
            }
        }
    }


    public void saveData() {
        saveMail();
        saveHomes();
        saveSettings();
        saveIgnored();
        this.save();
    }

    private void saveSettings() {
        this.getConfig().set("Settings.HomeLimit", this.homeLimit);
        this.getConfig().set("Settings.PVP", this.pvp);
        this.getConfig().set("Settings.Guild", (guild != null) ? guild.toString() : null);
        this.getConfig().set("Settings.Muted", this.muted);
    }

    private void saveIgnored() {
        for (UUID uuid : this.getIgnoredList()) {
            this.getConfig().set("IgnoredPlayers." + uuid.toString(), uuid.toString());
        }
    }

    private void saveHomes() {
        for (Home home : getHomes().values()) {
            this.getConfig().set("Homes." + home.name(), home.location());
        }
    }

    private void saveMail() {
        String basePath = "Mail";
        getConfig().set(basePath, null); // Clear old mail

        for (int i = 0; i < inbox.size(); i++) {
            Mail mail = this.inbox.get(i);
            String path = basePath + "." + i;
            getConfig().set(path + ".Author", mail.getAuthor().toString());
            getConfig().set(path + ".Message", mail.getMessage());
        }

    }

}
