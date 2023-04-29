package net.dialingspoon.questbind.mixin;

import net.dialingspoon.questbind.Questbind;
import net.dialingspoon.questbind.interfaces.MinecraftClientInterface;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.vivecraft.provider.openvr_jna.MCOpenVR;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Mixin(MCOpenVR.class)
public class MCOpenVRMixin {
	@Inject(at = @At("TAIL"), method = "generateActionManifest()V", remap = false)
	private void init(CallbackInfo info) {
		((MinecraftClientInterface) MinecraftClient.getInstance()).getKeyBindUtil().loadKeybinds();
	}
}
