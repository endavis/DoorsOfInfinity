package me.benfah.doorsofinfinity;

import me.benfah.doorsofinfinity.config.DOFConfig;
import me.benfah.doorsofinfinity.init.*;
import me.benfah.doorsofinfinity.utils.MCUtils;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.JanksonConfigSerializer;
import net.fabricmc.api.ModInitializer;


public class DOFMod implements ModInitializer {
	public static final String MOD_ID = "doorsofinfinity";
	
	@Override
	public void onInitialize() {
		MCUtils.init();
		DOFBlocks.init();
		DOFItems.init();
		DOFBlockEntities.init();
		DOFDimensions.init();
		DOFEntities.init();
		AutoConfig.register(DOFConfig.class, JanksonConfigSerializer::new);
	}
}
