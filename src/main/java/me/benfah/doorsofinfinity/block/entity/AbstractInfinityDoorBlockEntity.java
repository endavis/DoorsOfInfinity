package me.benfah.doorsofinfinity.block.entity;

import net.minecraft.entity.Entity;
import qouteall.imm_ptl.core.portal.Portal;
import me.benfah.doorsofinfinity.block.InfinityDoorBlock;
import me.benfah.doorsofinfinity.utils.BoxUtils;
import me.benfah.doorsofinfinity.utils.MCUtils;
import me.benfah.doorsofinfinity.utils.PortalCreationHelper;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public abstract class AbstractInfinityDoorBlockEntity<S extends AbstractInfinityDoorBlockEntity<S>> extends BlockEntity {
	public BlockPos syncDoorPos;
	public World syncDoorWorld;
	public Portal localPortal;

	public AbstractInfinityDoorBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	private boolean isUpper() {
		return getCachedState().get(InfinityDoorBlock.HALF) == DoubleBlockHalf.UPPER;
	}

	public void syncWith(S entity) {
		entity.syncDoorPos = this.pos;
		entity.syncDoorWorld = this.world;
		this.syncDoorPos = entity.pos;
		this.syncDoorWorld = entity.world;
		if(localPortal != null && localPortal.isAlive()) {
			createSyncedPortals();
		}
	}

	public void updateSyncDoor() {
		if (isSyncPresent()) {
			syncDoorWorld.setBlockState(syncDoorPos, getSyncEntity().getWorld().getBlockState(getSyncEntity().getPos()).with(InfinityDoorBlock.HINGE, getCachedState().get(InfinityDoorBlock.HINGE)).with(InfinityDoorBlock.OPEN, getCachedState().get(InfinityDoorBlock.OPEN)), 10);
		}
	}

	public boolean isSyncPresent() {
		return syncDoorPos != null && syncDoorWorld != null && !syncDoorWorld.getBlockState(syncDoorPos).isAir();
	}
	
	public void deleteLocalPortal() {
		deletePortals(world, pos);
	}
	
	public void deleteSyncPortal() {
		deletePortals(syncDoorWorld, syncDoorPos);
	}
	
	private void deletePortals(World world, BlockPos pos) {
		world.getEntitiesByClass(Portal.class, BoxUtils.getBoxInclusive(pos, pos.up()), portal -> true).forEach(portal -> portal.remove(Entity.RemovalReason.DISCARDED));
	}

	private void createSyncedPortals() {
		Direction direction = getCachedState().get(InfinityDoorBlock.FACING);
		Direction rightDirection = Direction.fromHorizontal(direction.getHorizontal() + 1);
		Vec3d portalPos = new Vec3d(pos.getX(), pos.getY(), pos.getZ()).add(0.5, 1, 0.5);
		Quaternion rot = new Quaternion(Vec3f.POSITIVE_Y, direction.getOpposite().getHorizontal() * 90, true);

		deleteSyncPortal();
		localPortal = PortalCreationHelper.spawn(world, portalPos, 1, 2, rightDirection, syncDoorWorld.getRegistryKey(), Vec3d.of(syncDoorPos), true, rot);

		updateSyncDoor();
	}


	public void syncWithDoor() {
		syncWith(getSyncEntity());
	}

	public S getSyncEntity() {
		if (syncDoorWorld == null) return null;

		return (S) syncDoorWorld.getBlockEntity(syncDoorPos);
	}

	@Override
	public void readNbt(NbtCompound tag) {
		if (tag.contains("SyncDoorDimName")) {
			syncDoorWorld = MCUtils.getServer().getWorld(RegistryKey.of(Registry.WORLD_KEY, new Identifier(tag.getString("SyncDoorDimName"))));
			syncDoorPos = new BlockPos(tag.getInt("SyncDoorX"), tag.getInt("SyncDoorY"), tag.getInt("SyncDoorZ"));
		}

		super.readNbt(tag);
	}

	@Override
	public void writeNbt(NbtCompound tag) {
		if (syncDoorWorld != null) {
			tag.putString("SyncDoorDimName", syncDoorWorld.getRegistryKey().getValue().toString());
			tag.putInt("SyncDoorX", syncDoorPos.getX());
			tag.putInt("SyncDoorY", syncDoorPos.getY());
			tag.putInt("SyncDoorZ", syncDoorPos.getZ());
		}
		super.writeNbt(tag);
	}
	
}
