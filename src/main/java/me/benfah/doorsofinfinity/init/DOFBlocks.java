package me.benfah.doorsofinfinity.init;

import me.benfah.doorsofinfinity.DOFMod;
import me.benfah.doorsofinfinity.block.InfinityBlock;
import me.benfah.doorsofinfinity.block.InfinityDoorBlock;
import me.benfah.doorsofinfinity.block.PhotonTransmitterBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class DOFBlocks {
	public static Block GENERATED_INFINITY_BLOCK = register("block_of_infinity", new InfinityBlock(FabricBlockSettings.copy(Blocks.BEDROCK)));
	public static Block INFINITY_BLOCK =  register("simulated_block_of_infinity", new InfinityBlock(FabricBlockSettings.copy(Blocks.STONE)));
	public static Block GENERATED_INFINITY_DOOR = register("infinity_door", new InfinityDoorBlock(FabricBlockSettings.copy(Blocks.IRON_DOOR).strength(-1.0F, 3600000.0F).sounds(BlockSoundGroup.STONE)));
	public static Block INFINITY_DOOR = register("simulated_infinity_door", new InfinityDoorBlock(FabricBlockSettings.copy(Blocks.IRON_DOOR).sounds(BlockSoundGroup.STONE)));
	public static Block PHOTON_TRANSMITTER = register( "photon_transmitter", new PhotonTransmitterBlock(FabricBlockSettings.of(Material.GLASS).nonOpaque()));

	private static Block register(String name, Block block) {
		return Registry.register(Registry.BLOCK, new Identifier(DOFMod.MOD_ID, name), block);
	}

	public static void init() {}
	
}
