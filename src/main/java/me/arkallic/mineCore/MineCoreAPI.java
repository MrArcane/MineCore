package me.arkallic.mineCore;

import me.arkallic.mineCore.managers.PlayerDataManager;

public class MineCoreAPI {
    private MineCore mineCore;

    public MineCoreAPI(MineCore mineCore) {
        this.mineCore = mineCore;
    }

    public PlayerDataManager getPlayerDataManager() {
        return mineCore.getPlayerDataManager();
    }
}
