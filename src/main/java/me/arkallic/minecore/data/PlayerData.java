package me.arkallic.minecore.data;

import me.arkallic.minecore.MineCore;
import me.arkallic.minecore.models.Home;
import me.arkallic.minecore.models.Mail;
import me.arkallic.minecore.wrappers.YMLFileWrapper;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class PlayerData extends YMLFileWrapper {

    private final LinkedHashMap<String, Home> homeMap = new LinkedHashMap<>();
    private final List<Mail> mail = new ArrayList<>();
    private final List<UUID> ignoredList = new ArrayList<>();
    private int homeLimit = 1;
    private boolean pvp = true;
    private UUID guildUUID;


    public PlayerData(UUID uuid, MineCore mineCore) {
        super("Players", uuid.toString(), mineCore);
    }

    public UUID getGuildUUID() {
        return guildUUID;
    }

    public void setGuildUUID(UUID guildUUID) {
        this.guildUUID = guildUUID;
    }

    public LinkedHashMap<String, Home> getHomes() {
        return homeMap;
    }

    public List<Mail> getMail() {
        return mail;
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

    public void loadData() {

        if (this.getConfig().isConfigurationSection("Settings")) {
            homeLimit = this.getConfig().getInt("Settings.HomeLimit");
            pvp = this.getConfig().getBoolean("Settings.PVP");
            guildUUID = UUID.fromString(this.getConfig().getString("Settings.Guild"));
        }

        if (this.getConfig().isConfigurationSection("IgnoredPlayers")) {
            for (String ignored : this.getConfig().getConfigurationSection( "IgnoredPlayers").getKeys(false)) {
                UUID uuid = UUID.fromString(ignored);
                ignoredList.add(uuid);
            }
        }

        if (this.getConfig().isConfigurationSection("Homes")) {
            for (String homeName : this.getConfig().getConfigurationSection("Homes").getKeys(false)) {
                Home home = new Home(homeName, this.getConfig().getLocation("Homes." + homeName));
                this.getHomes().putIfAbsent(homeName, home);
            }

            if (this.getConfig().isConfigurationSection("Mail")) {
                for (String mail : this.getConfig().getConfigurationSection("Mail").getKeys(false)) {
                    ConfigurationSection messageSection = this.getConfig().getConfigurationSection(mail);
                    UUID author = (UUID) messageSection.get("Author");
                    List<String> messages = messageSection.getStringList(author + ".Messages");
                    Mail playerMail = new Mail(author, messages);
                    this.getMail().add(playerMail);
                }
            }
        }
    }

    public void saveData() {
        for (Mail mail : this.getMail()) {
            String path = "Mail." + mail.getAuthor().toString();
            this.getConfig().set(path + ".Messages", mail.getMessages());
        }

        for (Home home : getHomes().values()) {
            this.getConfig().set("Homes." + home.name(), home.location());
        }
        this.getConfig().set("Settings.HomeLimit", this.homeLimit);
        this.getConfig().set("Settings.PVP", this.pvp);
        this.getConfig().set("IgnoredPlayers", this.ignoredList);
        this.getConfig().set("Settings.Guild", this.guildUUID);

        this.save();
    }

}
