package net.dialingspoon.questbind.mixin;

import net.dialingspoon.questbind.interfaces.MinecraftClientInterface;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.vivecraft.provider.openvr_jna.MCOpenVR;

@Mixin(MCOpenVR.class)
public class MCOpenVRMixin {

	@Inject(at = @At("TAIL"), method = "generateActionManifest()V", remap = false)
	private void init(CallbackInfo info) {
		// load keybinds upon starting, after they won't be overridden
		((MinecraftClientInterface) MinecraftClient.getInstance()).getKeyBindUtil().loadKeybinds();
	}
}
