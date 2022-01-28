package me.benfah.doorsofinfinity.config;

import me.benfah.doorsofinfinity.DOFMod;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = DOFMod.MOD_ID)
public class DOFConfig implements ConfigData {
    public boolean requireDoorBorder = true;
    public int dimensionSize = 11;
    public int maxUpgrades = 8;

    public static DOFConfig getInstance() {
        return AutoConfig.getConfigHolder(DOFConfig.class).getConfig();
    }
}
