package me.benfah.doorsofinfinity.init;

import me.benfah.doorsofinfinity.DOFMod;
import me.benfah.doorsofinfinity.entity.PhotonPortal;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;

public class DOFEntities {
    public static EntityType<PhotonPortal> BREAKABLE_PORTAL = Registry.register(Registry.ENTITY_TYPE, new Identifier(DOFMod.MOD_ID, "breakable_portal"), FabricEntityTypeBuilder.create(SpawnGroup.MISC, PhotonPortal::new).dimensions(EntityDimensions.fixed(1, 1)).fireImmune().build());

    public static void init() {}
}
