package me.benfah.doorsofinfinity;

import eu.midnightdust.lib.config.MidnightConfig;
import me.benfah.doorsofinfinity.config.DOFConfig;
import me.benfah.doorsofinfinity.init.*;
import me.benfah.doorsofinfinity.utils.MCUtils;
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
		MidnightConfig.init(MOD_ID, DOFConfig.class);
	}
}
