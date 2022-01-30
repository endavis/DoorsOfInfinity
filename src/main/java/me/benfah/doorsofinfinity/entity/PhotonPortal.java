package me.benfah.doorsofinfinity.entity;

import me.benfah.doorsofinfinity.init.DOFBlocks;
import me.benfah.doorsofinfinity.utils.BoxUtils;
import net.minecraft.block.AbstractGlassBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import qouteall.imm_ptl.core.portal.Portal;
import qouteall.q_misc_util.my_util.IntBox;

//Todo: Give this a better name
public class PhotonPortal extends Portal {
    public IntBox transmitterArea;
    public IntBox glassArea;
    public World transmitterWorld;

    public PhotonPortal(EntityType<? extends PhotonPortal> entityType, World world_1) {
        super(entityType, world_1);
        setInteractable(false);
    }
    
    @Override
    protected void readCustomDataFromNbt(NbtCompound compoundTag) {
        super.readCustomDataFromNbt(compoundTag);
        if(!world.isClient) {
	        transmitterArea = new IntBox(new BlockPos(BoxUtils.vecFromNbt(compoundTag.getCompound("PhotonTransmitterL"))), new BlockPos(BoxUtils.vecFromNbt(compoundTag.getCompound("PhotonTransmitterH"))));
	        glassArea = new IntBox(new BlockPos(BoxUtils.vecFromNbt(compoundTag.getCompound("GlassAreaL"))), new BlockPos(BoxUtils.vecFromNbt(compoundTag.getCompound("GlassAreaH"))));

	        if(compoundTag.contains("WorldName")) {
	        	transmitterWorld = world.getServer().getWorld(RegistryKey.of(Registry.WORLD_KEY, new Identifier(compoundTag.getString("WorldName"))));
	        } else this.remove(RemovalReason.DISCARDED);
	    }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound compoundTag) {
        super.writeCustomDataToNbt(compoundTag);
        compoundTag.put("PhotonTransmitterL", BoxUtils.vecToNbt(transmitterArea.l));
        compoundTag.put("PhotonTransmitterH", BoxUtils.vecToNbt(transmitterArea.h));
        compoundTag.put("GlassAreaL", BoxUtils.vecToNbt(glassArea.l));
        compoundTag.put("GlassAreaH", BoxUtils.vecToNbt(glassArea.h));
        compoundTag.putString("WorldName", transmitterWorld.getRegistryKey().getValue().toString());

    }

    private void checkIntegrity() {
        boolean transmittersValid = transmitterArea.fastStream().allMatch(blockPos -> transmitterWorld.getBlockState(blockPos).getBlock() == DOFBlocks.PHOTON_TRANSMITTER);
        boolean glassValid = glassArea.fastStream().allMatch(blockPos -> world.getBlockState(blockPos).getBlock() instanceof AbstractGlassBlock);

        if (!transmittersValid || !glassValid) setRemoved(RemovalReason.DISCARDED);
    }

    @Override
    public void tick() {
        super.tick();
        if (!world.isClient) {
            if (world.getTime() % 10 == getId() % 10) checkIntegrity();
        }
    }
}
