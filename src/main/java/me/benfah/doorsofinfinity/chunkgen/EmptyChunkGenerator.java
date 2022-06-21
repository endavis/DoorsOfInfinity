package me.benfah.doorsofinfinity.chunkgen;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.block.BlockState;
import net.minecraft.structure.StructureManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.world.*;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.RandomState;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;

public class EmptyChunkGenerator extends ChunkGenerator {
	public static final Codec<EmptyChunkGenerator> CODEC = RecordCodecBuilder.create((instance) -> instance
			.group(BiomeSource.CODEC.fieldOf("biome_source").forGetter(ChunkGenerator::getBiomeSource))
			.apply(instance, instance.stable(EmptyChunkGenerator::new)));


	public EmptyChunkGenerator(BiomeSource biomeSource) {
		super(BuiltinRegistries.STRUCTURE_SET, Optional.empty(), biomeSource);
	}

	@Override
	protected Codec<? extends ChunkGenerator> getCodec() {
		return CODEC;
	}

	@Override
	public void carve(ChunkRegion chunkRegion, long seed, RandomState randomState, BiomeAccess biomeAccess, StructureManager structureManager, Chunk chunk, GenerationStep.Carver generationStep) {}

	@Override
	public void buildSurface(ChunkRegion region, StructureManager structureManager, RandomState randomState, Chunk chunk) {}

	@Override
	public void populateEntities(ChunkRegion region) {}

	@Override
	public int getWorldHeight() {
		return 256;
	}

	@Override
	public CompletableFuture<Chunk> populateNoise(Executor executor, Blender blender, RandomState randomState, StructureManager structureManager, Chunk chunk) {
		return CompletableFuture.completedFuture(chunk);
	}

	@Override
	public int getSeaLevel() {
		return 0;
	}

	@Override
	public int getMinimumY() {
		return 0;
	}

	@Override
	public int getHeight(int x, int z, Type heightmap, HeightLimitView world, RandomState randomState) {
		return 256;
	}

	@Override
	public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world, RandomState randomState) {
		return new VerticalBlockSample(0, new BlockState[0]);
	}

	@Override
	public void method_40450(List<String> list, RandomState randomState, BlockPos blockPos) {}
}
