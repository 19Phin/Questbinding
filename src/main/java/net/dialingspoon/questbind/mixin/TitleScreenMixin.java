package net.dialingspoon.questbind.mixin;

import net.dialingspoon.questbind.interfaces.MinecraftClientInterface;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.TitleScreen;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class TitleScreenMixin {
    boolean done = false;
    @Inject(at = @At("HEAD"), method = "init()V")
    private void init(CallbackInfo info) {
        //add the values to display on the keybind gui upon load
        /*!if (!done) {
            ((MinecraftClientInterface) MinecraftClient.getInstance()).getKeyBindUtil().read = 2;
            KeyBinding.updateKeysByCode();
            ((MinecraftClientInterface) MinecraftClient.getInstance()).getKeyBindUtil().read = 0;
            done = true;
        }*/
    }
}
