package net.dialingspoon.questbind.mixin;

import net.dialingspoon.questbind.interfaces.KeyBindingInterface;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.KeybindsScreen;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeybindsScreen.class)
public class KeybindsScreenMixin extends GameOptionsScreen {
	@Shadow
	public KeyBinding selectedKeyBinding;

	public KeybindsScreenMixin(Screen parent, GameOptions gameOptions, Text title) {
		super(parent, gameOptions, title);
	}

	@Inject(at = @At("HEAD"), method = "mouseClicked", cancellable = true)
	private void mouseClicked(double mouseX, double mouseY, int button, CallbackInfoReturnable<Boolean> cir) {
		if (!super.mouseClicked(mouseX, mouseY, button)) this.selectedKeyBinding = null;
		if (this.selectedKeyBinding != null) {
            int i = ((KeyBindingInterface)selectedKeyBinding).getBindIt()+1;
            if (i == 12) i = 0;
            ((KeyBindingInterface)selectedKeyBinding).setBindIt(i);
			cir.setReturnValue(true);
		} else {
			cir.setReturnValue(super.mouseClicked(mouseX, mouseY, button));
		}
		cir.cancel();
	}
}
