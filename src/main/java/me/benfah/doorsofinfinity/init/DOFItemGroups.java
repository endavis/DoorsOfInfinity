package me.benfah.doorsofinfinity.init;

import me.benfah.doorsofinfinity.DOFMod;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;

public class DOFItemGroups {
    public static final ItemGroup DOF_GROUP = FabricItemGroupBuilder.build(new Identifier(DOFMod.MOD_ID, "items"), () -> new ItemStack(DOFItems.INFINITY_DOOR));
}
