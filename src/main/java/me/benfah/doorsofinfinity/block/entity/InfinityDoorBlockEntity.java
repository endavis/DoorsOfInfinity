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
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.world.World;
import qouteall.imm_ptl.core.portal.Portal;
import qouteall.imm_ptl.core.portal.PortalManipulation;

public class InfinityDoorBlockEntity extends AbstractInfinityDoorBlockEntity<InfinityDoorBlockEntity> {
	public PersonalDimension link;

	public InfinityDoorBlockEntity(BlockPos pos, BlockState state) {
		super(DOFBlockEntities.INFINITY_DOOR, pos, state);
	}

	public void syncWith(InfinityDoorBlockEntity entity) {
		super.syncWith(entity);
		if (this.link != null) {
			entity.link = this.link;
		}

		this.markDirty();
		entity.markDirty();
	}

	public void updateSyncDoor() {
		super.updateSyncDoor();
		if (this.isSyncPresent()
				&& this.world.getRegistryKey().equals(DOFDimensions.INFINITY_DIM)
				&& this.world.getEntitiesByClass(Portal.class, BoxUtils.getBoxInclusive(this.pos, this.pos.up()), portal -> true).isEmpty()) {
			this.deleteSyncPortal();
			PortalManipulation.completeBiWayPortal(this.getSyncEntity().localPortal, Portal.entityType);
		}

	}

	private void createSyncedPortals() {
		Direction direction = this.getCachedState().get(InfinityDoorBlock.FACING);
		Direction rightDirection = Direction.fromHorizontal(direction.getHorizontal() + 1);
		Vec3d portalPos = new Vec3d(this.pos.getX(), this.pos.getY(), this.pos.getZ()).add(0.5, 1.0, 0.5);
		Quaternion rot = new Quaternion(Vec3f.POSITIVE_Y, (float)(direction.getOpposite().getHorizontal() * 90), true);
		PersonalDimension personalDim = this.getOrCreateLinkedDimension();
		this.deleteSyncPortal();
		this.localPortal = PortalCreationHelper.spawn(
				this.world, portalPos, 1.0, 2.0, rightDirection, DOFDimensions.INFINITY_DIM, personalDim.getPlayerPosCentered().add(0.0, 1.0, 0.0), true, rot
		);
		this.updateSyncDoor();
	}

	public void placeSyncedDoor(World otherWorld, BlockPos otherPos) {
		BlockState state = this.getCachedState();
		otherWorld.setBlockState(otherPos, DOFBlocks.GENERATED_INFINITY_DOOR.getDefaultState().with(InfinityDoorBlock.HINGE,
				state.get(InfinityDoorBlock.HINGE)).with(InfinityDoorBlock.FACING, Direction.NORTH).with(InfinityDoorBlock.HALF, DoubleBlockHalf.LOWER)
		);

		otherWorld.setBlockState(otherPos.up(), DOFBlocks.GENERATED_INFINITY_DOOR.getDefaultState().with(InfinityDoorBlock.HINGE,
						state.get(InfinityDoorBlock.HINGE)).with(InfinityDoorBlock.FACING, Direction.NORTH).with(InfinityDoorBlock.HALF, DoubleBlockHalf.UPPER)
		);
		this.syncWith((InfinityDoorBlockEntity)otherWorld.getBlockEntity(otherPos));
		this.createSyncedPortals();
	}

	public PersonalDimension getOrCreateLinkedDimension() {
		if (this.link == null) {
			this.link = InfinityDimHelper.getEmptyPersonalDimension();
			this.link.generate();
		}

		return this.link;
	}

	public BlockEntityUpdateS2CPacket toUpdatePacket() {
		return BlockEntityUpdateS2CPacket.of(this);
	}

	public NbtCompound toInitialChunkDataNbt() {
		return this.toNbt();
	}

	public void markDirty() {
		if (this.hasWorld() && !this.getWorld().isClient()) {
			((ServerWorld)this.world).getChunkManager().markForUpdate(this.getPos());
		}

		super.markDirty();
	}

	public void readNbt(NbtCompound tag) {
		if (tag.contains("DimOffset")) {
			this.link = InfinityDimHelper.getPersonalDimension(tag.getInt("DimOffset"), tag.contains("Upgrades") ? tag.getInt("Upgrades") : 0, true);
		}

		if (tag.contains("PersonalDimension")) {
			this.link = InfinityDimHelper.fromNbt(tag.getCompound("PersonalDimension"), true);
		}

		super.readNbt(tag);
	}

	public void writeNbt(NbtCompound tag) {
		if (this.link != null) {
			tag.put("PersonalDimension", this.link.toNbt(new NbtCompound()));
		}

		super.writeNbt(tag);
	}
}
