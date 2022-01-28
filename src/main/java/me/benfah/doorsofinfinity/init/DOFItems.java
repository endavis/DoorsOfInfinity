package me.benfah.doorsofinfinity.init;

import me.benfah.doorsofinfinity.DOFMod;
import me.benfah.doorsofinfinity.item.DimensionalShardItem;
import me.benfah.doorsofinfinity.item.InfinityDoorItem;
import me.benfah.doorsofinfinity.item.LoreBlockItem;
import me.benfah.doorsofinfinity.item.PhotonLinkItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Settings;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;

import static me.benfah.doorsofinfinity.init.DOFItemGroups.DOF_GROUP;

public class DOFItems {
	public static final String ANCIENT_MADE = Util.createTranslationKey("lore", new Identifier(DOFMod.MOD_ID, "ancient_made"));

	public static Item INFINITY_DOOR = register("infinity_door", new InfinityDoorItem(DOFBlocks.GENERATED_INFINITY_DOOR, new Settings().maxCount(1)));
	public static Item SIMULATED_INFINITY_DOOR = register("simulated_infinity_door", new InfinityDoorItem(DOFBlocks.INFINITY_DOOR, new Settings().group(DOF_GROUP).maxCount(1)));

	public static Item BLOCK_OF_INFINITY = register("block_of_infinity", new BlockItem(DOFBlocks.GENERATED_INFINITY_BLOCK, new Settings()));
	public static Item SIMULATED_BLOCK_OF_INFINITY = register("simulated_block_of_infinity", new BlockItem(DOFBlocks.INFINITY_BLOCK, new Settings().group(DOF_GROUP)));

	public static Item PHOTON_TRANSMITTER = register("photon_transmitter", new LoreBlockItem(DOFBlocks.PHOTON_TRANSMITTER, new Settings().group(DOF_GROUP)));
	public static Item PHOTON_LINK = register("photon_link", new PhotonLinkItem(new Settings().group(DOF_GROUP).maxCount(1)));
	public static Item DIMENSIONAL_SHARD = register("dimensional_shard", new DimensionalShardItem(new Settings().group(DOF_GROUP).maxCount(16)));

	private static Item register(String name, Item item) {
		return Registry.register(Registry.ITEM, new Identifier(DOFMod.MOD_ID, name), item);
	}

	public static void init() {}
}
