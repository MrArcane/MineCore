package me.arkallic.mineCore.managers;

import me.arkallic.mineCore.MineCore;
import me.arkallic.mineCore.data.PlayerData;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

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
