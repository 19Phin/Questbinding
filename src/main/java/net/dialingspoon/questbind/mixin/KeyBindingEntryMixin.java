package net.dialingspoon.questbind.mixin;

import net.dialingspoon.questbind.interfaces.KeyBindingInterface;
import net.dialingspoon.questbind.interfaces.MinecraftClientInterface;
import net.dialingspoon.questbind.util.ButtonBind;
import net.dialingspoon.questbind.util.KeyBindUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(ControlsListWidget.KeyBindingEntry.class)
public class KeyBindingEntryMixin {
	@Shadow
	private @Final KeyBinding binding;
	@Shadow
	private @Final ButtonWidget editButton;
	@Mutable
	@Shadow
	private @Final ButtonWidget resetButton;
	@Shadow
	@Final ControlsListWidget field_2742;
	@Shadow
	private @Final Text bindingName;

	@Shadow protected void update() {}

	final ArrayList<String> BUTTONPATH = new ArrayList<>(java.util.Arrays.asList("null","right/input/a", "right/input/b", "left/input/x", "left/input/y", "left/input/trigger", "right/input/trigger", "left/input/grip", "right/input/grip", "left/input/joystick", "right/input/joystick", "left/input/application_menu"));
	final ArrayList<String> BUTTONS = new ArrayList<>(java.util.Arrays.asList("null","a", "b", "x", "y", "Ltrigger", "Rtrigger", "Lgrip", "Rgrip", "LstickPress", "RstickPress", "MenuButton"));

	@Inject(at = @At(value = "TAIL"), method = "<init>")
	private void redirectReset(CallbackInfo ci) {
		resetButton = new ButtonWidget(0, 0, 50, 20, Text.translatable("controls.reset"), button -> {
			String bindIt = "null";
			for (ButtonBind bind : KeyBindUtil.DEFAULTBINDS) {
				if (bind.output.equals(binding.getTranslationKey())) {
					bindIt = bind.button;
				}
			}
			((KeyBindingInterface) binding).setBindIt(bindIt);
			update();
		});
	}

	@Inject(at = @At("HEAD"), method = "render", cancellable = true)
	private void init(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta, CallbackInfo ci) {
		boolean bl = ((ControlsListWidgetAccessor)field_2742).getParent().selectedKeyBinding == this.binding;
		if (bl && ((ControlsListWidgetAccessor) field_2742).getParent().selectedKeyBinding != null) {
			int i = BUTTONPATH.indexOf(((KeyBindingInterface) ((ControlsListWidgetAccessor) field_2742).getParent().selectedKeyBinding).getBindIt()) +1;
			if (i == 12) i = 0;
			((KeyBindingInterface) ((ControlsListWidgetAccessor) field_2742).getParent().selectedKeyBinding).setBindIt(BUTTONPATH.get(i));
			((ControlsListWidgetAccessor) field_2742).getParent().selectedKeyBinding = null;
		}

		KeyBindUtil keyBindUtil = ((MinecraftClientInterface) MinecraftClient.getInstance()).getKeyBindUtil();

		MinecraftClient.getInstance().textRenderer.draw(matrices, this.bindingName, (float)(x + 90 - ((ControlsListWidgetAccessor)field_2742).getMaxKeyNameLength()), (float)(y + entryHeight / 2 - MinecraftClient.getInstance().textRenderer.fontHeight / 2), 16777215);
		this.resetButton.x = x + 190;
		this.resetButton.y = y;
		this.resetButton.active = false;
		boolean found = false;
		for (ButtonBind bind : keyBindUtil.DEFAULTBINDS) {
			if (bind.output.equals(binding.getTranslationKey())) {
				this.resetButton.active = !((KeyBindingInterface) binding).getBindIt().equals(bind.button);
				found = true;
			}
		}
		if (!found) {
			this.resetButton.active = !((KeyBindingInterface) binding).getBindIt().equals("null");
		}
		this.resetButton.render(matrices, mouseX, mouseY, tickDelta);
		this.editButton.x = x + 105;
		this.editButton.y = y;
		this.editButton.setMessage(Text.literal(BUTTONS.get(BUTTONPATH.indexOf(((KeyBindingInterface)this.binding).getBindIt()))));

		this.editButton.render(matrices, mouseX, mouseY, tickDelta);
		ci.cancel();
	}
}
