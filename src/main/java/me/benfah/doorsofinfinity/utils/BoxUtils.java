package me.benfah.doorsofinfinity.utils;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.property.Property;
import net.minecraft.util.Pair;
import net.minecraft.util.math.*;

import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.Optional;

import qouteall.q_misc_util.my_util.IntBox;

public class BoxUtils {
	public static Box getBoxInclusive(BlockPos pos1, BlockPos pos2) {
		return new Box(new Vec3d(pos1.getX(), pos1.getY(), pos1.getZ()), new Vec3d(pos2.getX() + 1, pos2.getY() + 1, pos2.getZ() + 1));
	}

	public static Pair<BlockPos, BlockPos> getAnchors(IntBox box) {
		BlockPos lowAnchorPos = box.stream().min(Comparator.comparingInt(Vec3i::getX).thenComparingInt(Vec3i::getY).thenComparingInt(Vec3i::getZ)).get();
		BlockPos highAnchorPos = box.stream().min(Comparator.comparingInt(Vec3i::getX).thenComparingInt(Vec3i::getY).thenComparingInt(Vec3i::getZ)).get();
		return new Pair<>(lowAnchorPos, highAnchorPos);
	}

	public static NbtCompound vecToNbt(Vec3i vec3i) {
		NbtCompound tag = new NbtCompound();

		tag.putInt("PosX" , vec3i.getX());
		tag.putInt("PosY" , vec3i.getY());
		tag.putInt("PosZ" , vec3i.getZ());

		return tag;
	}

	public static Vec3i vecFromNbt(NbtCompound vec3i) {
		int x = vec3i.getInt("PosX");
		int y = vec3i.getInt("PosY");
		int z = vec3i.getInt("PosZ");

		return new Vec3i(x, y, z);
	}

	public static PlaneInfo getPlaneFromIntBox(IntBox box, Direction direction) {
		Direction rightDirection = Direction.fromHorizontal(getAbsoluteHorizontal(direction.getHorizontal() - 1));

		Vec3i axisH = Direction.UP.getVector();
		Vec3i axisW = rightDirection.getVector();
		Vec3i axisL = direction.getVector();

		int height = box.stream().mapToInt(BlockPos::getY).max().getAsInt() - box.stream().mapToInt(BlockPos::getY).min().getAsInt() + 1;

		IntSummaryStatistics widthStream = box.stream().mapToInt(pos -> rightDirection.getAxis().choose(pos.getX(), pos.getY(), pos.getZ())).summaryStatistics();

		int width = widthStream.getMax() - widthStream.getMin() + 1;



		int x = box.stream().map(BlockPos::getX).min((i1, i2) -> axisW.getX() == 0 ? Integer.compare(i1 * axisL.getX(), i2 * axisL.getX()) : Integer.compare(i1 * axisW.getX(), i2 * axisW.getX())).get();
		int y = box.stream().mapToInt(BlockPos::getY).min().getAsInt();
		int z = box.stream().map(BlockPos::getZ).min((i1, i2) -> axisW.getZ() == 0 ? Integer.compare(i1 * axisL.getZ(), i2 * axisL.getZ()) : Integer.compare(i1 * axisW.getZ(), i2 * axisW.getZ())).get();

		return new PlaneInfo(axisW, axisH, width, height, new BlockPos(x, y, z));
	}

	public static <T extends Comparable<T>> boolean hasSamePropertyValue(Property<T> property, BlockState toCompare, T value, Block isBlock) {
		Optional<Property<?>> optional = toCompare.getProperties().stream().filter(compProp -> compProp.equals(property)).findAny();

		if(optional.isPresent() && toCompare.isOf(isBlock)) {
			return optional.get().equals(property) && toCompare.get(property).equals(value);
		}
		return false;
	}


	public static int getAbsoluteHorizontal(int horizontal) {
		return horizontal < 0 ? horizontal + 4 : horizontal;
	}

	public static class PlaneInfo {
		public Vec3i axisW;
		public Vec3i axisH;

		public int width;
		public int height;

		public BlockPos pos;

		public PlaneInfo(Vec3i axisW, Vec3i axisH, int width, int height, BlockPos pos) {
			this.axisW = axisW;
			this.axisH = axisH;
			this.width = width;
			this.height = height;
			this.pos = pos;
		}

		public PlaneInfo(NbtCompound tag) {
			this.axisW = BoxUtils.vecFromNbt(tag.getCompound("AxisW"));
			this.axisH = BoxUtils.vecFromNbt(tag.getCompound("AxisH"));

			this.width = tag.getInt("Width");
			this.height = tag.getInt("Height");

			this.pos = new BlockPos(BoxUtils.vecFromNbt(tag.getCompound("StartPos")));
		}

		public NbtCompound toNbt(NbtCompound tag) {
			tag.put("AxisW", BoxUtils.vecToNbt(axisW));
			tag.put("AxisH", BoxUtils.vecToNbt(axisH));
			tag.putInt("Width", width);
			tag.putInt("Height", height);
			tag.put("StartPos", BoxUtils.vecToNbt(pos));

			return tag;
		}

		public boolean equals(Object obj) {
			if (obj instanceof PlaneInfo) {
				PlaneInfo toCompare = (PlaneInfo) obj;
				return toCompare.height == this.height && toCompare.width == this.width;
			}
			return false;
		}
	}
}
