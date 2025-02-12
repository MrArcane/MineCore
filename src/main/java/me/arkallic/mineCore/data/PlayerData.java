package me.arkallic.mineCore.data;

import me.arkallic.mineCore.MineCore;
import me.arkallic.mineCore.objects.Home;
import me.arkallic.mineCore.objects.Mail;
import me.arkallic.mineCore.wrappers.YMLFileWrapper;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;
import java.util.stream.Collectors;

public class PlayerData extends YMLFileWrapper {

    private final UUID uuid;
    private final List<Home> homeList = new ArrayList<>();
    private final List<Mail> mail = new ArrayList<>();
    private final List<UUID> ignoredList = new ArrayList<>();
    private int homeLimit = 1;


    public PlayerData(UUID uuid, MineCore mineCore) {
        super("Players", uuid.toString(), mineCore);
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return uuid;
    }

    public List<Home> getHomes() {
        return homeList;
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

    public void modifyHomeLimit(int amount) {
        this.homeLimit += amount;
        if (this.homeLimit <= 1) {
            this.homeLimit = 1; // Prevents negative home limits
        }
    }


    public void loadData() {

        if (this.getConfig().isConfigurationSection("Settings")) {
            homeLimit = this.getConfig().getInt("Settings.HomeLimit");
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
                this.getHomes().add(home);
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

        removeDeletedHomes();
        this.getConfig().set("Settings.HomeLimit", this.homeLimit);
        this.getConfig().set("IgnoredPlayers", this.ignoredList);

        this.save();
    }

    private void removeDeletedHomes() {

        if (this.homeList == null || this.homeList.isEmpty()) {
            return;
        }

        Set<String> currentHomeNames = this.homeList.stream()
                .map(Home::name)
                .collect(Collectors.toSet());

        // Save homes in the map
        for (Home home : this.homeList) {
            this.getConfig().set("Homes." + home.name(), home.location());
        }

        // Check for deleted homes
        ConfigurationSection homesConfig = this.getConfig().getConfigurationSection("Homes");
        if (homesConfig != null) {
            Set<String> savedHomeNames = homesConfig.getKeys(false);

            for (String savedHomeName : savedHomeNames) {
                if (!currentHomeNames.contains(savedHomeName)) {
                    // Home was deleted remove from config
                    this.getConfig().set("Homes." + savedHomeName, null);
                }
            }
        }
    }

}
