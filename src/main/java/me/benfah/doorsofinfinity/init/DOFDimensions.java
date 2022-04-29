package me.benfah.doorsofinfinity.init;

import me.benfah.doorsofinfinity.DOFMod;
import me.benfah.doorsofinfinity.chunkgen.EmptyChunkGenerator;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

public class DOFDimensions {
	public static final RegistryKey<World> INFINITY_DIM = RegistryKey.of(Registry.WORLD_KEY, new Identifier(DOFMod.MOD_ID, "infinity_dimension"));
    
	public static void init() {
		Registry.register(Registry.CHUNK_GENERATOR, new Identifier(DOFMod.MOD_ID, "empty"), EmptyChunkGenerator.CODEC);
	}
	
}
