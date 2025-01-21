package net.dialingspoon.questbind.mixin;

import net.dialingspoon.questbind.interfaces.KeyBindingInterface;
import net.dialingspoon.questbind.interfaces.MinecraftClientInterface;
import net.dialingspoon.questbind.util.ButtonBind;
import net.dialingspoon.questbind.util.KeyBindUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;
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
	private boolean duplicate = false;
	@Shadow @Final private Text bindingName;

    @Shadow protected void update() {}

    final ArrayList<String> BUTTONPATH = new ArrayList<>(java.util.Arrays.asList("null","right/input/a", "right/input/b", "left/input/x", "left/input/y", "left/input/trigger", "right/input/trigger", "left/input/grip", "right/input/grip", "left/input/joystick", "right/input/joystick", "left/input/application_menu"));
	final ArrayList<String> BUTTONS = new ArrayList<>(java.util.Arrays.asList("null","a", "b", "x", "y", "Ltrigger", "Rtrigger", "Lgrip", "Rgrip", "LstickPress", "RstickPress", "MenuButton"));

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/screen/option/ControlsListWidget$KeyBindingEntry;update()V"), method = "<init>")
	private void redirectReset(CallbackInfo ci) {
		resetButton = ButtonWidget.builder(Text.translatable("controls.reset"), button -> {
			String bindIt = "null";
			for (ButtonBind bind : KeyBindUtil.DEFAULTBINDS) {
				if (bind.output.equals(binding.getTranslationKey())) {
					bindIt = bind.button;
				}
			}
			((KeyBindingInterface) binding).setBindIt(bindIt);
			update();
		}).dimensions(0, 0, 50, 20).narrationSupplier(textSupplier -> Text.translatable("narrator.controls.reset", bindingName)).build();
	}

	@Inject(at = @At("HEAD"), method = "update", cancellable = true)
	private void update(CallbackInfo ci) {
		//flip-through buttons when bind clicked
		if (((ControlsListWidgetAccessor) field_2742).getParent().selectedKeyBinding != null) {
			int i = BUTTONPATH.indexOf(((KeyBindingInterface) ((ControlsListWidgetAccessor) field_2742).getParent().selectedKeyBinding).getBindIt()) +1;
			if (i == 12) i = 0;
			((KeyBindingInterface) ((ControlsListWidgetAccessor) field_2742).getParent().selectedKeyBinding).setBindIt(BUTTONPATH.get(i));
		}

		KeyBindUtil keyBindUtil = ((MinecraftClientInterface) MinecraftClient.getInstance()).getKeyBindUtil();
		//show button name
		this.editButton.setMessage(Text.literal(BUTTONS.get(BUTTONPATH.indexOf(((KeyBindingInterface)this.binding).getBindIt()))));
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
		this.duplicate = false;
		((ControlsListWidgetAccessor) field_2742).getParent().selectedKeyBinding = null;
		ci.cancel();
	}
}
