package net.dialingspoon.questbind.mixin;

import net.dialingspoon.questbind.interfaces.MinecraftClientInterface;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.KeybindsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(KeybindsScreen.class)
public class KeybindsScreenMixin extends GameOptionsScreen {

	public KeybindsScreenMixin(Screen parent, GameOptions gameOptions, Text title) {
		super(parent, gameOptions, title);
	}

	@Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/option/KeybindsScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;"))
	private Element addDrawableChildRedirect(KeybindsScreen instance, Element element) {
		ButtonWidget buttonWidget = (ButtonWidget) element;
		// Replace the "DONE" button with a custom one
		if (buttonWidget.getMessage() == ScreenTexts.DONE) {
			return this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, button -> {
				((MinecraftClientInterface)client).getKeyBindUtil().actualSave();
				this.client.setScreen(this.parent);
			}).dimensions(buttonWidget.getX(), buttonWidget.getY(), buttonWidget.getWidth(), buttonWidget.getHeight()).build());
		}

		// For all other buttons, return them as-is
		return this.addDrawableChild(buttonWidget);
	}
}
