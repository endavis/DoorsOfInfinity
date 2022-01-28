package me.benfah.doorsofinfinity.plugin;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.benfah.doorsofinfinity.DOFMod;
import me.benfah.doorsofinfinity.config.DOFConfig;
import me.shedaniel.autoconfig.AutoConfig;

public class ModMenuPlugin implements ModMenuApi {
    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> AutoConfig.getConfigScreen(DOFConfig.class, parent).get();
    }
}
