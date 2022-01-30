package me.benfah.doorsofinfinity.block.entity;

import me.benfah.doorsofinfinity.block.InfinityDoorBlock;
import me.benfah.doorsofinfinity.dimension.InfinityDimHelper;
import me.benfah.doorsofinfinity.dimension.InfinityDimHelper.PersonalDimension;
import me.benfah.doorsofinfinity.init.DOFBlockEntities;
import me.benfah.doorsofinfinity.init.DOFBlocks;
import me.benfah.doorsofinfinity.init.DOFDimensions;
import me.benfah.doorsofinfinity.utils.BoxUtils;
import me.benfah.doorsofinfinity.utils.PortalCreationHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import qouteall.imm_ptl.core.portal.Portal;
import qouteall.imm_ptl.core.portal.PortalManipulation;

public class InfinityDoorBlockEntity extends AbstractInfinityDoorBlockEntity<InfinityDoorBlockEntity> {
	public PersonalDimension link;
	public InfinityDoorBlockEntity(BlockPos pos, BlockState state) {
		super(DOFBlockEntities.INFINITY_DOOR, pos, state);
	}

	@Override
	public void syncWith(InfinityDoorBlockEntity entity) {
		super.syncWith(entity);
		if(link != null) {
			entity.link = link;
		}

		this.markDirty();
		entity.markDirty();
	}
	
	public void updateSyncDoor() {
		super.updateSyncDoor();
		if (isSyncPresent()) {
			if (world.getRegistryKey().equals(DOFDimensions.INFINITY_DIM) && world.getEntitiesByClass(Portal.class, BoxUtils.getBoxInclusive(pos, pos.up()), portal -> true).isEmpty()) {
				deleteSyncPortal();
				PortalManipulation.completeBiWayPortal(getSyncEntity().localPortal, Portal.entityType);
			}
		}
	}

	private void createSyncedPortals() {
		Direction direction = getCachedState().get(InfinityDoorBlock.FACING);
		Direction rightDirection = Direction.fromHorizontal(direction.getHorizontal() + 1);
		Vec3d portalPos = new Vec3d(pos.getX(), pos.getY(), pos.getZ()).add(0.5, 1, 0.5);
		Quaternion rot = new Quaternion(Vec3f.POSITIVE_Y, direction.getOpposite().getHorizontal() * 90, true);

		PersonalDimension personalDim = getOrCreateLinkedDimension();
		deleteSyncPortal();
		localPortal = PortalCreationHelper.spawn(world, portalPos, 1, 2, rightDirection, DOFDimensions.INFINITY_DIM,
				personalDim.getPlayerPosCentered().add(0, 1, 0), true, rot);

		updateSyncDoor();
	}

	public void placeSyncedDoor(World otherWorld, BlockPos otherPos) {
		BlockState state = getCachedState();
		otherWorld.setBlockState(otherPos, DOFBlocks.GENERATED_INFINITY_DOOR.getDefaultState().with(InfinityDoorBlock.HINGE, state.get(InfinityDoorBlock.HINGE)).with(InfinityDoorBlock.FACING, Direction.NORTH).with(InfinityDoorBlock.HALF, DoubleBlockHalf.LOWER));
		otherWorld.setBlockState(otherPos.up(), DOFBlocks.GENERATED_INFINITY_DOOR.getDefaultState().with(InfinityDoorBlock.HINGE, state.get(InfinityDoorBlock.HINGE)).with(InfinityDoorBlock.FACING, Direction.NORTH).with(InfinityDoorBlock.HALF, DoubleBlockHalf.UPPER));

		syncWith((InfinityDoorBlockEntity) otherWorld.getBlockEntity(otherPos));
		createSyncedPortals();
	}

	public PersonalDimension getOrCreateLinkedDimension() {
		if (link == null) {
			link = InfinityDimHelper.getEmptyPersonalDimension();
			link.generate();
		}

		return link;
	}

	@Override
	public BlockEntityUpdateS2CPacket toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.create(this);
	}

	@Override
	public NbtCompound toInitialChunkDataNbt() {
		return this.createNbt();
	}

	@Override
	public void markDirty() {
		if (this.hasWorld() && !this.getWorld().isClient()) ((ServerWorld) world).getChunkManager().markForUpdate(getPos());

		super.markDirty();
	}

	@Override
	public void readNbt(NbtCompound tag) {
		if (tag.contains("DimOffset")) link = InfinityDimHelper.getPersonalDimension(tag.getInt("DimOffset"), tag.contains("Upgrades") ? tag.getInt("Upgrades") : 0, true);
		if (tag.contains("PersonalDimension")) link = InfinityDimHelper.fromNbt(tag.getCompound("PersonalDimension"), true);

		super.readNbt(tag);
	}

	@Override
	public void writeNbt(NbtCompound tag) {
		if(link != null) {
			tag.put("PersonalDimension", link.toNbt(new NbtCompound()));
		}

		super.writeNbt(tag);
	}
}
