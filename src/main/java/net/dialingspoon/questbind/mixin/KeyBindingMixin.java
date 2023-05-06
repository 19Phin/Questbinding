package net.dialingspoon.questbind.mixin;

import com.google.common.collect.Maps;
import net.dialingspoon.questbind.interfaces.KeyBindingInterface;
import net.dialingspoon.questbind.interfaces.MinecraftClientInterface;
import net.dialingspoon.questbind.util.KeyBindUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;
import java.util.Map;

@Mixin(KeyBinding.class)
public class KeyBindingMixin implements KeyBindingInterface {
    @Shadow
    private static @Final Map<String, KeyBinding> KEYS_BY_ID = Maps.newHashMap();

    public int bindIt = 0;

    @Override
    public int getBindIt() {
        return bindIt;
    }
    @Override
    public void setBindIt(int bindIt) {
        this.bindIt = bindIt;
    }

    @Inject(at = @At("HEAD"), method = "updateKeysByCode", cancellable = true)
    private static void updateKeysByCode(CallbackInfo ci) {
        KeyBindUtil keyBindUtil = ((MinecraftClientInterface)MinecraftClient.getInstance()).getKeyBindUtil();
        if (keyBindUtil.read) {
            Map<String, Integer> binds = keyBindUtil.binds;
            for (KeyBinding keyBinding : KEYS_BY_ID.values()) {
                if (binds.containsKey(keyBinding.getTranslationKey())) ((KeyBindingInterface) keyBinding).setBindIt(binds.get(keyBinding.getTranslationKey()));
            }
        } else {
            Map<String, Integer> map = new HashMap<>();
            for (KeyBinding keyBinding : KEYS_BY_ID.values()) {
                int bindIt = ((KeyBindingInterface) keyBinding).getBindIt();
                if (bindIt != 0) map.put(keyBinding.getTranslationKey(), bindIt);
            }
            keyBindUtil.binds = map;
        }
    }
}
