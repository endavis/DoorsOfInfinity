package me.benfah.doorsofinfinity.block.entity;

import me.benfah.doorsofinfinity.block.InfinityDoorBlock;
import me.benfah.doorsofinfinity.utils.BoxUtils;
import me.benfah.doorsofinfinity.utils.MCUtils;
import me.benfah.doorsofinfinity.utils.PortalCreationHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.DoorHinge;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import qouteall.imm_ptl.core.portal.Portal;

public abstract class AbstractInfinityDoorBlockEntity<S extends AbstractInfinityDoorBlockEntity<S>> extends BlockEntity {
	public BlockPos syncDoorPos;
	public World syncDoorWorld;
	public Portal localPortal;

	public AbstractInfinityDoorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	private boolean isUpper() {
		return this.getCachedState().get(InfinityDoorBlock.HALF) == DoubleBlockHalf.UPPER;
	}

	public void syncWith(S entity) {
		entity.syncDoorPos = this.pos;
		entity.syncDoorWorld = this.world;
		this.syncDoorPos = entity.pos;
		this.syncDoorWorld = entity.world;
		if (this.localPortal != null && this.localPortal.isAlive()) {
			this.createSyncedPortals();
		}
	}

	public void updateSyncDoor() {
		if (this.isSyncPresent()) {
			this.syncDoorWorld.setBlockState(
							this.syncDoorPos,
							this.getSyncEntity().getWorld().getBlockState(
									this.getSyncEntity().getPos()).with(InfinityDoorBlock.HINGE,
									this.getCachedState().get(InfinityDoorBlock.HINGE)).with(InfinityDoorBlock.OPEN,
									this.getCachedState().get(InfinityDoorBlock.OPEN)),
							10
					);
		}

	}

	public boolean isSyncPresent() {
		return this.syncDoorPos != null && this.syncDoorWorld != null && !this.syncDoorWorld.getBlockState(this.syncDoorPos).isAir();
	}

	public void deleteLocalPortal() {
		this.deletePortals(this.world, this.pos);
	}

	public void deleteSyncPortal() {
		this.deletePortals(this.syncDoorWorld, this.syncDoorPos);
	}

	private void deletePortals(World world, BlockPos pos) {
		world.getEntitiesByClass(Portal.class, BoxUtils.getBoxInclusive(pos, pos.up()), portal -> true)
				.forEach(portal -> portal.setRemoved(RemovalReason.DISCARDED));
	}

	private void createSyncedPortals() {
		Direction direction = this.getCachedState().get(InfinityDoorBlock.FACING);
		Direction rightDirection = Direction.fromHorizontal(direction.getHorizontal() + 1);
		Vec3d portalPos = new Vec3d(this.pos.getX(), this.pos.getY(), this.pos.getZ()).add(0.5, 1.0, 0.5);
		Quaternion rot = new Quaternion(Vec3f.POSITIVE_Y, (float)(direction.getOpposite().getHorizontal() * 90), true);
		this.deleteSyncPortal();
		this.localPortal = PortalCreationHelper.spawn(
				this.world, portalPos, 1.0, 2.0, rightDirection, this.syncDoorWorld.getRegistryKey(), Vec3d.of(this.syncDoorPos), true, rot
		);
		this.updateSyncDoor();
	}

	public void syncWithDoor() {
		this.syncWith(this.getSyncEntity());
	}

	public S getSyncEntity() {
		return (S)(this.syncDoorWorld == null ? null : this.syncDoorWorld.getBlockEntity(this.syncDoorPos));
	}

	public void readNbt(NbtCompound tag) {
		if (tag.contains("SyncDoorDimName")) {
			this.syncDoorWorld = MCUtils.getServer().getWorld(RegistryKey.of(Registry.WORLD_KEY, new Identifier(tag.getString("SyncDoorDimName"))));
			this.syncDoorPos = new BlockPos(tag.getInt("SyncDoorX"), tag.getInt("SyncDoorY"), tag.getInt("SyncDoorZ"));
		}

		super.readNbt(tag);
	}

	public void writeNbt(NbtCompound tag) {
		if (this.syncDoorWorld != null) {
			tag.putString("SyncDoorDimName", this.syncDoorWorld.getRegistryKey().getValue().toString());
			tag.putInt("SyncDoorX", this.syncDoorPos.getX());
			tag.putInt("SyncDoorY", this.syncDoorPos.getY());
			tag.putInt("SyncDoorZ", this.syncDoorPos.getZ());
		}

		super.writeNbt(tag);
	}
}
