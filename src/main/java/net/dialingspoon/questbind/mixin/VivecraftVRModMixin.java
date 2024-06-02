package net.dialingspoon.questbind.mixin;

import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.vivecraft.client.VivecraftVRMod;

import java.util.LinkedHashSet;
import java.util.Set;

@Mixin(VivecraftVRMod.class)
public abstract class VivecraftVRModMixin {

	@Shadow public Set<KeyBinding> getAllKeyBindings() {
        return null;
    }

	@Inject(at = @At("HEAD"), method = "getUserKeyBindings", remap = false, cancellable = true)
	private void returnAll(CallbackInfoReturnable<Set<KeyBinding>> cir) {
		cir.setReturnValue(getAllKeyBindings());
	}

	@Inject(at = @At("HEAD"), method = "getHiddenKeyBindings", remap = false, cancellable = true)
	private void clearHidden(CallbackInfoReturnable<Set<KeyBinding>> cir) {
		cir.setReturnValue(new LinkedHashSet());
	}
}
