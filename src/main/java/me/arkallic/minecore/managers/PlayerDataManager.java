package me.arkallic.minecore.managers;

import me.arkallic.minecore.MineCore;
import me.arkallic.minecore.data.PlayerData;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

import static me.arkallic.minecore.utils.ServerUtils.log;

public class PlayerDataManager {

    private final Map<UUID, PlayerData> playerMap = new HashMap<>();
    private final MineCore mineCore;

    public PlayerDataManager(MineCore mineCore) {
        this.mineCore = mineCore;
    }

    public void register(UUID uuid) {
        this.playerMap.computeIfAbsent(uuid, _ -> new PlayerData(uuid, mineCore));
        PlayerData pd = playerMap.get(uuid);
        pd.loadData();
    }

    public void unregister(UUID uuid) {
        PlayerData pd = this.playerMap.get(uuid);
        pd.saveData();
        pd.getHomes().clear();
        this.playerMap.remove(uuid);
    }

    public PlayerData get(UUID uuid) {
        if (this.playerMap.containsKey(uuid)) {
            return this.playerMap.get(uuid);
        }

        PlayerData pd = new PlayerData(uuid, mineCore);
        pd.loadData();
        this.playerMap.put(uuid, pd);

        return pd;
    }

}
