package me.arkallic.minecore;

import me.arkallic.minecore.managers.HomeManager;
import me.arkallic.minecore.managers.PlayerDataManager;

public class MineCoreAPI {
    private MineCore mineCore;

    public MineCoreAPI(MineCore mineCore) {
        this.mineCore = mineCore;
    }

    public PlayerDataManager getPlayerDataManager() {
        return mineCore.getPlayerDataManager();
    }

    public HomeManager getHomeManager() {
        return mineCore.getHomeManager();
    }

}
