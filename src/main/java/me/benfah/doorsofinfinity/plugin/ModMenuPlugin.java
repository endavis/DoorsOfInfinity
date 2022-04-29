package me.benfah.doorsofinfinity.plugin;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import eu.midnightdust.lib.config.MidnightConfig;
import me.benfah.doorsofinfinity.DOFMod;
import me.benfah.doorsofinfinity.config.DOFConfig;

public class ModMenuPlugin implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> MidnightConfig.getScreen(parent, DOFMod.MOD_ID);
    }
}
