package me.benfah.doorsofinfinity.item;

import me.benfah.doorsofinfinity.utils.BoxUtils;
import me.benfah.doorsofinfinity.utils.MCUtils;
import me.benfah.doorsofinfinity.utils.PortalCreationHelper;
import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.text.Text;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import qouteall.q_misc_util.Helper;
import qouteall.q_misc_util.my_util.IntBox;

import java.util.List;

public class PhotonLinkItem extends Item {
    public PhotonLinkItem(Settings settings)
    {
        super(settings);
    }

    @Override
    public ActionResult useOnBlock(ItemUsageContext context) {
        if(!context.getWorld().isClient) {
        	if(context.getStack().getSubNbt("PhotonLink") != null || context.getStack().getSubNbt("DoorLink") != null) {
        		NbtCompound tag = context.getStack().getSubNbt("PhotonLink");
                Direction savedFacing = Direction.fromHorizontal(tag.getInt("Direction")).getOpposite();
                Direction currentFacing = context.getSide();
                BoxUtils.PlaneInfo savedPlane = new BoxUtils.PlaneInfo(tag);

                IntBox savedIntBox = new IntBox(savedPlane.pos, savedPlane.pos.add((savedPlane.width - 1) * savedPlane.axisW.getX(), savedPlane.height - 1, (savedPlane.width - 1) * savedPlane.axisW.getZ()));
                IntBox currentIntBox = Helper.expandRectangle(context.getBlockPos(), (pos) -> context.getWorld().getBlockState(pos).getBlock() instanceof AbstractGlassBlock, context.getSide().getAxis());

                BoxUtils.PlaneInfo currentPlane = BoxUtils.getPlaneFromIntBox(currentIntBox, context.getSide());

                Vec3d entityVec = currentIntBox.getCenterVec().add(0.495 * currentFacing.getOffsetX(), 0, 0.495 * currentFacing.getOffsetZ());
                Vec3d vecToRender = savedIntBox.getCenterVec().subtract(savedFacing.getOffsetX() != 0 ? 0.5 * savedFacing.getOffsetX() : 0, 0, savedFacing.getOffsetZ() != 0 ? 0.5 * savedFacing.getOffsetZ() : 0);
                int difference = BoxUtils.getAbsoluteHorizontal(currentFacing.getHorizontal() - savedFacing.getHorizontal());
                RegistryKey<World> worldKey = RegistryKey.of(Registry.WORLD_KEY, new Identifier(tag.getString("WorldName")));

                if(savedPlane.equals(currentPlane)) {
                    PortalCreationHelper.spawnBreakable(context.getWorld(), entityVec, currentPlane.width, currentPlane.height, currentPlane.axisW, currentPlane.axisH, worldKey, vecToRender, false, Vec3f.POSITIVE_Y.getDegreesQuaternion(difference * 90), false, savedIntBox, currentIntBox, context.getWorld().getServer().getWorld(worldKey));
                    context.getStack().removeSubNbt("PhotonLink");
                    return ActionResult.SUCCESS;
                }
            }
        }

        return ActionResult.CONSUME;
    }

    @Override
    public void appendTooltip(ItemStack stack, World world, List<Text> tooltip, TooltipContext context) {
        NbtCompound photonTag = stack.getSubNbt("PhotonLink");
        if(photonTag != null) {
            tooltip.add(Text.translatable("lore.doorsofinfinity.photo_link_size", photonTag.getInt("Width"), photonTag.getInt("Height")).formatted(Formatting.GRAY));
        }
        
        super.appendTooltip(stack, world, tooltip, context);
    }
}
