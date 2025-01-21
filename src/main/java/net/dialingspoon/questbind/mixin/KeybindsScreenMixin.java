package net.dialingspoon.questbind.mixin;

import net.dialingspoon.questbind.interfaces.MinecraftClientInterface;
import net.dialingspoon.questbind.util.KeyBindUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.KeybindsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeybindsScreen.class)
public class KeybindsScreenMixin extends GameOptionsScreen {

	@Shadow private ControlsListWidget controlsList;

	public KeybindsScreenMixin(Screen parent, GameOptions gameOptions, Text title) {
		super(parent, gameOptions, title);
	}

	@Redirect(method = "init", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/option/KeybindsScreen;addDrawableChild(Lnet/minecraft/client/gui/Element;)Lnet/minecraft/client/gui/Element;"))
	private Element addDrawableChildRedirect(KeybindsScreen instance, Element element) {
		if (element instanceof ButtonWidget buttonWidget) {
			//custom done button
            if (buttonWidget.getMessage() == ScreenTexts.DONE) {
				return this.addDrawableChild(ButtonWidget.builder(ScreenTexts.DONE, (button) -> {
					//update the binds when screen is closed
					((MinecraftClientInterface) client).getKeyBindUtil().actualSave();
					this.client.setScreen(this.parent);
				}).dimensions(buttonWidget.getX(), buttonWidget.getY(), buttonWidget.getWidth(), buttonWidget.getHeight()).build());
			} else if (buttonWidget.getMessage().equals(Text.translatable("controls.resetAll"))) {
				//custom reset
				return this.addDrawableChild(ButtonWidget.builder(Text.translatable("controls.resetAll"), (button) -> {
					KeyBindUtil keyBindUtil = ((MinecraftClientInterface) MinecraftClient.getInstance()).getKeyBindUtil();
					keyBindUtil.binds = keyBindUtil.DEFAULTBINDS;
					keyBindUtil.read = 2;
					controlsList.update();
					keyBindUtil.read = 0;
				}).dimensions(buttonWidget.getX(), buttonWidget.getY(), buttonWidget.getWidth(), buttonWidget.getHeight()).build());
			}
			return this.addDrawableChild(buttonWidget);
		} else {
			return this.addDrawableChild((ControlsListWidget)element);
		}
	}

	@Inject(method = "render", at = @At(value = "TAIL"))
	protected void renderWarning(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
		TextRenderer textRenderer = MinecraftClient.getInstance().textRenderer;

		//draw the warning on the screen (centered)
		String message = "Must restart Questcraft for changes to take place!";
		context.drawCenteredTextWithShadow(textRenderer, message, (this.width) / 2, 22, 0xFF0000);
	}
}
