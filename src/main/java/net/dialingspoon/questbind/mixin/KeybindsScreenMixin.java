package net.dialingspoon.questbind.mixin;

import net.dialingspoon.questbind.interfaces.MinecraftClientInterface;
import net.dialingspoon.questbind.util.KeyBindUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.KeybindsScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.DirectionalLayoutWidget;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(KeybindsScreen.class)
public class KeybindsScreenMixin extends GameOptionsScreen {

	public KeybindsScreenMixin(Screen parent, GameOptions gameOptions, Text title) {
		super(parent, gameOptions, title);
	}

	@Override
	public void addOptions() {
	}

	@Redirect(method = "initFooter", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/widget/DirectionalLayoutWidget;add(Lnet/minecraft/client/gui/widget/Widget;)Lnet/minecraft/client/gui/widget/Widget;"))
	protected Widget addDrawableChildRedirect(DirectionalLayoutWidget instance, Widget element) {
		if (element instanceof ButtonWidget buttonWidget) {
			//custom done button
            if (buttonWidget.getMessage() == ScreenTexts.DONE) {
				return instance.add(ButtonWidget.builder(ScreenTexts.DONE, (button) -> {
					//update the binds when screen is closed
					((MinecraftClientInterface) client).getKeyBindUtil().actualSave();
					this.client.setScreen(this.parent);
				}).dimensions(buttonWidget.getX(), buttonWidget.getY(), buttonWidget.getWidth(), buttonWidget.getHeight()).build());
			} else if (buttonWidget.getMessage().equals(Text.translatable("controls.resetAll"))) {
				//custom reset
				return instance.add(ButtonWidget.builder(Text.translatable("controls.resetAll"), (button) -> {
					KeyBindUtil keyBindUtil = ((MinecraftClientInterface)MinecraftClient.getInstance()).getKeyBindUtil();
					keyBindUtil.binds = keyBindUtil.DEFAULTBINDS;
					keyBindUtil.read = 1;
					KeyBinding.updateKeysByCode();
					keyBindUtil.read = 0;
				}).dimensions(buttonWidget.getX(), buttonWidget.getY(), buttonWidget.getWidth(), buttonWidget.getHeight()).build());
			}
			return instance.add(buttonWidget);
		} else {
			return instance.add((ControlsListWidget)element);
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
