package me.benfah.doorsofinfinity.mixin;

import com.mojang.authlib.GameProfileRepository;
import com.mojang.authlib.minecraft.MinecraftSessionService;
import com.mojang.datafixers.DataFixer;
import me.benfah.doorsofinfinity.utils.MCUtils;
import net.minecraft.resource.ResourcePackManager;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.SaveLoader;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.util.UserCache;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraft.util.ApiServices;
import net.minecraft.world.SaveProperties;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.lang.ref.WeakReference;
import java.net.Proxy;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(method = "<init>", at = @At(value = "RETURN"))
    public void doorsOfInfinity$onInit(Thread thread, LevelStorage.Session session, 
        ResourcePackManager resourcePackManager, SaveLoader saveLoader, Proxy proxy, 
        DataFixer dataFixer, ApiServices apiServices,
        WorldGenerationProgressListenerFactory worldGenerationProgressListenerFactory, 
        CallbackInfo ci) {
        MCUtils.mcServerReference = new WeakReference<>((MinecraftServer) ((Object) this));
    }
}
