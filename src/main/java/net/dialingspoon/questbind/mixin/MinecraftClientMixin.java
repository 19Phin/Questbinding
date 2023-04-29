package net.dialingspoon.questbind.mixin;

import net.dialingspoon.questbind.interfaces.MinecraftClientInterface;
import net.dialingspoon.questbind.util.KeyBindUtil;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin implements MinecraftClientInterface {

    public final KeyBindUtil keyBindUtil = new KeyBindUtil();


    @Override
    public KeyBindUtil getKeyBindUtil() {
        return keyBindUtil;
    }
}
