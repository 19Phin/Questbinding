package net.dialingspoon.questbind.mixin;

import net.dialingspoon.questbind.interfaces.KeyBindingInterface;
import net.dialingspoon.questbind.util.KeyBindUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ControlsListWidget.KeyBindingEntry.class)
public class KeyBindingEntryMixin {
	@Shadow
	private @Final KeyBinding binding;
	@Shadow
	private @Final Text bindingName;
	@Shadow
	private @Final ButtonWidget editButton;
	@Shadow
	private @Final ButtonWidget resetButton;
	@Shadow
	@Final ControlsListWidget field_2742;


	@Inject(at = @At("HEAD"), method = "render", cancellable = true)
	private void init(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta, CallbackInfo ci) {
		boolean bl = ((ControlsListWidgetAccessor)field_2742).getParent().selectedKeyBinding == this.binding;
		if (bl && ((ControlsListWidgetAccessor) field_2742).getParent().selectedKeyBinding != null) {
			int i = ((KeyBindingInterface) ((ControlsListWidgetAccessor) field_2742).getParent().selectedKeyBinding).getBindIt() + 1;
			if (i == 12) i = 0;
			((KeyBindingInterface) ((ControlsListWidgetAccessor) field_2742).getParent().selectedKeyBinding).setBindIt(i);
			((ControlsListWidgetAccessor) field_2742).getParent().selectedKeyBinding = null;
		}
		TextRenderer var10000 = MinecraftClient.getInstance().textRenderer;
		Text var10002 = this.bindingName;
		float var10003 = (float)(x + 90 - ((ControlsListWidgetAccessor)field_2742).getMaxKeyNameLength());
		int var10004 = y + entryHeight / 2;
		Objects.requireNonNull(MinecraftClient.getInstance().textRenderer);
		var10000.draw(matrices, var10002, var10003, (float)(var10004 - 9 / 2), 16777215);
		this.resetButton.x = x + 190;
		this.resetButton.y = y;
		this.resetButton.active = !this.binding.isDefault();
		this.resetButton.render(matrices, mouseX, mouseY, tickDelta);
		this.editButton.x = x + 105;
		this.editButton.y = y;
		this.editButton.setMessage(Text.literal(KeyBindUtil.BUTTONS[((KeyBindingInterface)this.binding).getBindIt()]));

		this.editButton.render(matrices, mouseX, mouseY, tickDelta);
		ci.cancel();
	}
}
