package me.benfah.doorsofinfinity;

import me.benfah.doorsofinfinity.init.DOFBlocks;
import me.benfah.doorsofinfinity.init.DOFEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import qouteall.imm_ptl.core.render.PortalEntityRenderer;


public class DOFModClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.INSTANCE.putBlock(DOFBlocks.GENERATED_INFINITY_DOOR, RenderLayer.getCutout());
		BlockRenderLayerMap.INSTANCE.putBlock(DOFBlocks.INFINITY_DOOR, RenderLayer.getCutout());
		EntityRendererRegistry.register(DOFEntities.BREAKABLE_PORTAL, PortalEntityRenderer::new);

		//BlockEntityRendererRegistry.register(DOFBlockEntities.INFINITY_DOOR, InfinityDoorBlockEntityRenderer::new);
	}
}
