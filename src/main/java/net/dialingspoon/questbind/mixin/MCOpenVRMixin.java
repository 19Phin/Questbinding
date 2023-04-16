package net.dialingspoon.questbind.mixin;

import net.dialingspoon.questbind.Questbind;
import net.fabricmc.loader.api.FabricLoader;
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
		// Define the source and destination file paths
		Path sourcePath = Paths.get(FabricLoader.getInstance().getConfigDir().toString(), "oculus_defaults.json");
		Path targetPath = Paths.get(FabricLoader.getInstance().getGameDir().toString(), "openvr", "input", "oculus_defaults.json");

		// Check if the source file exists
		if (Files.exists(sourcePath)) {
			try {
				// Copy the source file to the destination file
				Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);
				Questbind.LOGGER.info("New controls registered");
			} catch (Exception e) {
				Questbind.LOGGER.error("Failed to copy file: " + e.getMessage());
			}
		}
	}
}
