package net.dialingspoon.questbind.mixin;

import net.dialingspoon.questbind.interfaces.KeyBindingInterface;
import net.dialingspoon.questbind.util.KeyBindUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.option.ControlsListWidget;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
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
	@Shadow
	private boolean duplicate = false;


	@Inject(at = @At("HEAD"), method = "render", cancellable = true)
	private void render(MatrixStack matrices, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean hovered, float tickDelta, CallbackInfo ci) {
		TextRenderer var10000 = MinecraftClient.getInstance().textRenderer;
		Text var10002 = this.bindingName;
		float var10003 = (float)(x + 90 - ((ControlsListWidgetAccessor)field_2742).getMaxKeyNameLength());
		int var10004 = y + entryHeight / 2;
		Objects.requireNonNull(MinecraftClient.getInstance().textRenderer);
		var10000.draw(matrices, var10002, var10003, (float)(var10004 - 9 / 2), 16777215);
		this.resetButton.setX(x + 190);
		this.resetButton.setY(y);
		this.resetButton.render(matrices, mouseX, mouseY, tickDelta);
		this.editButton.setX(x + 105);
		this.editButton.setY(y);
		if (this.duplicate) {
			int j = this.editButton.getX() - 6;
			DrawableHelper.fill(matrices, j, y + 2, j + 3, y + entryHeight + 2, Formatting.RED.getColorValue() | -16777216);
		}

		this.editButton.render(matrices, mouseX, mouseY, tickDelta);
		ci.cancel();
	}

	@Inject(at = @At("HEAD"), method = "update", cancellable = true)
	private void update(CallbackInfo ci) {
		if (!(((ControlsListWidgetAccessor) field_2742).getParent().selectedKeyBinding == null)) {
			int i = ((KeyBindingInterface) ((ControlsListWidgetAccessor) field_2742).getParent().selectedKeyBinding).getBindIt() + 1;
			if (i == 12) i = 0;
			((KeyBindingInterface) ((ControlsListWidgetAccessor) field_2742).getParent().selectedKeyBinding).setBindIt(i);
		}
		this.editButton.setMessage(Text.literal(KeyBindUtil.BUTTONS[((KeyBindingInterface)this.binding).getBindIt()]));
		this.resetButton.active = false;
		this.duplicate = false;
		((ControlsListWidgetAccessor) field_2742).getParent().selectedKeyBinding = null;
		ci.cancel();
	}
}
