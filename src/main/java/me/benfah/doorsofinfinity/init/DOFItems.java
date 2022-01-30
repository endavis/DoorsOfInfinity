package me.benfah.doorsofinfinity.init;

import me.benfah.doorsofinfinity.DOFMod;
import me.benfah.doorsofinfinity.item.DimensionalShardItem;
import me.benfah.doorsofinfinity.item.InfinityDoorItem;
import me.benfah.doorsofinfinity.item.LoreBlockItem;
import me.benfah.doorsofinfinity.item.PhotonLinkItem;
import me.benfah.doorsofinfinity.utils.MCUtils;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.item.Item.Settings;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.minecraft.util.registry.Registry;

import java.util.Arrays;
import java.util.function.BiFunction;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class DOFItems
{


	public static final String ANCIENT_MADE = Util.createTranslationKey("lore",
			new Identifier(DOFMod.MOD_ID, "ancient_made"));

	public static Item INFINITY_DOOR;
	public static Item SIMULATED_INFINITY_DOOR;

	public static Item BLOCK_OF_INFINITY;
	public static Item SIMULATED_BLOCK_OF_INFINITY;

	public static Item PHOTON_TRANSMITTER;
	public static Item PHOTON_LINK;

	public static Item DIMENSIONAL_SHARD;

	public static final ItemGroup DOF_GROUP = FabricItemGroupBuilder.build(new Identifier(DOFMod.MOD_ID, "items"), () -> new ItemStack(INFINITY_DOOR));

	public static void init()
	{
		INFINITY_DOOR = registerBlockItem(DOFBlocks.GENERATED_INFINITY_DOOR, new Settings(), InfinityDoorItem::new);
		SIMULATED_INFINITY_DOOR = registerBlockItem(DOFBlocks.INFINITY_DOOR, new Settings().group(DOF_GROUP).maxCount(1), InfinityDoorItem::new);

		BLOCK_OF_INFINITY = registerBlockItem(DOFBlocks.GENERATED_INFINITY_BLOCK, new Settings(), BlockItem::new);
		SIMULATED_BLOCK_OF_INFINITY = registerBlockItem(DOFBlocks.INFINITY_BLOCK, new Settings().group(DOF_GROUP), BlockItem::new);

		PHOTON_TRANSMITTER = registerBlockItem(DOFBlocks.PHOTON_TRANSMITTER, new Settings().group(DOF_GROUP), BlockItem::new, MCUtils::isIPPresent);

		PHOTON_LINK = registerItem("photon_link", new PhotonLinkItem(new Settings().group(DOF_GROUP).maxCount(1)), MCUtils::isIPPresent);

		DIMENSIONAL_SHARD = registerItem("dimensional_shard", new DimensionalShardItem(new Settings().group(DOF_GROUP).maxCount(16)));
	}

	public static <T extends Item> T registerItem(String name, T item, BooleanSupplier... conditions)
	{
		if(conditions.length == 0 || Arrays.stream(conditions).parallel().allMatch(s -> s.getAsBoolean()))
			return Registry.register(Registry.ITEM, new Identifier(DOFMod.MOD_ID, name), item);
		return null;
	}

	public static <T extends Item> T registerBlockItem(Block block, Settings itemSettings, BiFunction<Block, Settings, T> itemFactory, BooleanSupplier... conditions)
	{
		if(conditions.length == 0 || Arrays.stream(conditions).parallel().allMatch(s -> s.getAsBoolean()))
		{
			T item = itemFactory.apply(block, itemSettings);
			return registerItem(Registry.BLOCK.getId(block).getPath(), item);
		}
		return null;
	}

}
