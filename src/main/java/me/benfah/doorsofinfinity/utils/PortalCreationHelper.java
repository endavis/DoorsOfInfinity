package me.benfah.doorsofinfinity.utils;

import me.benfah.doorsofinfinity.entity.PhotonPortal;
import me.benfah.doorsofinfinity.init.DOFEntities;
import qouteall.q_misc_util.my_util.IntBox;
import qouteall.imm_ptl.core.portal.Portal;
import qouteall.imm_ptl.core.portal.PortalManipulation;

import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Quaternion;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class PortalCreationHelper {
	public static Portal spawn(World world, Vec3d pos, double width, double height, Vec3i axisW, Vec3i axisH, RegistryKey<World> dimensionTo, Vec3d dest, boolean teleportable, Quaternion rot, boolean biWay) {
		Portal portal = new Portal(Portal.entityType, world);
		portal.width = width;
		portal.height = height;
		portal.axisH = new Vec3d(axisH.getX(), axisH.getY(), axisH.getZ());
		portal.axisW = new Vec3d(axisW.getX(), axisW.getY(), axisW.getZ());
		portal.dimensionTo = dimensionTo;
		portal.destination = dest;
		portal.teleportable = teleportable;
		portal.cullableXEnd = 0;
		portal.cullableYEnd = 0;
		portal.cullableXStart = 0;
		portal.cullableYStart = 0;
		if(rot != null) portal.rotation = rot;
		portal.setPos(pos.getX(), pos.getY(), pos.getZ());
		world.spawnEntity(portal);
		if(biWay) PortalManipulation.completeBiWayPortal(portal, Portal.entityType);
		return portal;
	}

	public static PhotonPortal spawnBreakable(World world, Vec3d pos, double width, double height, Vec3i axisW, Vec3i axisH, RegistryKey<World> dimensionTo, Vec3d dest, boolean teleportable, Quaternion rot, boolean biWay, IntBox transmitterBox, IntBox glassBox, World transmitterWorld) {
		PhotonPortal portal = new PhotonPortal(DOFEntities.BREAKABLE_PORTAL, world);
		portal.width = width;
		portal.height = height;
		portal.axisH = new Vec3d(axisH.getX(), axisH.getY(), axisH.getZ());
		portal.axisW = new Vec3d(axisW.getX(), axisW.getY(), axisW.getZ());
		portal.dimensionTo = dimensionTo;
		portal.destination = dest;
		portal.teleportable = teleportable;
		portal.cullableXEnd = 0;
		portal.cullableYEnd = 0;
		portal.cullableXStart = 0;
		portal.cullableYStart = 0;
		portal.transmitterArea = transmitterBox;
		portal.transmitterWorld = transmitterWorld;
		portal.glassArea = glassBox;

		if(rot != null) portal.rotation = rot;
		portal.setPos(pos.getX(), pos.getY(), pos.getZ());
		world.spawnEntity(portal);
		if(biWay) PortalManipulation.completeBiWayPortal(portal, Portal.entityType);
		return portal;
	}

	public static Portal spawn(World world, Vec3d pos, double width, double height, Direction axisW, RegistryKey<World> dimensionTo, Vec3d dest, boolean teleportable, Quaternion rot, boolean biWay) {
		return spawn(world, pos, width, height, axisW.getVector(), Direction.UP.getVector(), dimensionTo, dest, teleportable, rot, biWay);
	}

	public static Portal spawn(World world, Vec3d pos, double width, double height, Direction axisW, RegistryKey<World> dimensionTo, Vec3d dest, boolean teleportable, Quaternion rot) {
		return spawn(world, pos, width, height, axisW.getVector(), Direction.UP.getVector(), dimensionTo, dest, teleportable, rot, true);
	}

}
