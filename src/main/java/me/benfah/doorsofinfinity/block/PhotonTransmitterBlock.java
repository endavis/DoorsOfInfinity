package me.benfah.doorsofinfinity.block;

import me.benfah.doorsofinfinity.init.DOFItems;
import me.benfah.doorsofinfinity.utils.BoxUtils;
import me.benfah.doorsofinfinity.utils.MCUtils;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import qouteall.q_misc_util.Helper;
import qouteall.q_misc_util.my_util.IntBox;

public class PhotonTransmitterBlock extends GlassBlock {
    public static DirectionProperty FACING = HorizontalFacingBlock.FACING;

    public PhotonTransmitterBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if(hand == Hand.MAIN_HAND && !world.isClient && !player.getMainHandStack().isEmpty()) {
            if(player.getMainHandStack().getItem() == DOFItems.PHOTON_LINK) {
                Direction direction = state.get(PhotonTransmitterBlock.FACING);

                IntBox box = Helper.expandRectangle(pos,
                        testPos -> BoxUtils.hasSamePropertyValue(PhotonTransmitterBlock.FACING, world.getBlockState(testPos), state.get(PhotonTransmitterBlock.FACING)),
                        direction.getAxis());

                NbtCompound tag = player.getMainHandStack().getOrCreateSubNbt("PhotonLink");
                BoxUtils.PlaneInfo planeInfo = BoxUtils.getPlaneFromIntBox(box, direction);
                planeInfo.toCompound(tag);
                tag.putInt("Direction", direction.getHorizontal());
                tag.putString("WorldName", world.getRegistryKey().getValue().toString());

                //Todo: Make this into some sort of block state thing.
                player.sendMessage(new TranslatableText("lore.doorsofinfinity.linked"), false);

                return ActionResult.SUCCESS;
            }
        }
        return ActionResult.PASS;
    }


    public BlockState getPlacementState(ItemPlacementContext ctx) {
        return this.getDefaultState().with(FACING, ctx.getPlayerFacing().getOpposite());
    }
}
