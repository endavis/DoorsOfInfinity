package me.benfah.doorsofinfinity.chunkgen;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import me.benfah.doorsofinfinity.config.DOFConfig;
import net.minecraft.block.BlockState;
import net.minecraft.world.*;
import net.minecraft.world.Heightmap.Type;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.biome.source.BiomeSource;
import net.minecraft.world.biome.source.util.MultiNoiseUtil;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.Blender;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.StructuresConfig;
import net.minecraft.world.gen.chunk.VerticalBlockSample;

public class EmptyChunkGenerator extends ChunkGenerator {
	public static final Codec<EmptyChunkGenerator> CODEC = RecordCodecBuilder.create((instance) -> instance
			.group(BiomeSource.CODEC.fieldOf("biome_source").forGetter((generator) -> generator.biomeSource))
			.apply(instance, instance.stable(EmptyChunkGenerator::new)));


	public EmptyChunkGenerator(BiomeSource biomeSource) {
		super(biomeSource, new StructuresConfig(Optional.empty(), new HashMap<>()));
	}

	@Override
	protected Codec<? extends ChunkGenerator> getCodec() {
		return CODEC;
	}

	@Override
	public ChunkGenerator withSeed(long seed) {
		return this;
	}

	@Override
	public MultiNoiseUtil.MultiNoiseSampler getMultiNoiseSampler() {
		return null;
	}

	@Override
	public void carve(ChunkRegion chunkRegion, long seed, BiomeAccess biomeAccess, StructureAccessor structureAccessor, Chunk chunk, GenerationStep.Carver generationStep) {}

	@Override
	public void buildSurface(ChunkRegion region, StructureAccessor structures, Chunk chunk) {}

	@Override
	public void populateEntities(ChunkRegion region) {}

	@Override
	public int getWorldHeight() {
		return 256;
	}

	@Override
	public CompletableFuture<Chunk> populateNoise(Executor executor, Blender blender, StructureAccessor structureAccessor, Chunk chunk) {
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
	public int getHeight(int x, int z, Type heightmap, HeightLimitView world) {
		return 256;
	}

	@Override
	public VerticalBlockSample getColumnSample(int x, int z, HeightLimitView world) {
		return new VerticalBlockSample(world.getBottomY(), new BlockState[0]);
	}

}
