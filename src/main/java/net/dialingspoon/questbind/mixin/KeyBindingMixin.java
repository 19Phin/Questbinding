package net.dialingspoon.questbind.mixin;

import com.google.common.collect.Maps;
import net.dialingspoon.questbind.interfaces.KeyBindingInterface;
import net.dialingspoon.questbind.interfaces.MinecraftClientInterface;
import net.dialingspoon.questbind.util.ButtonBind;
import net.dialingspoon.questbind.util.KeyBindUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Map;

@Mixin(KeyBinding.class)
public class KeyBindingMixin implements KeyBindingInterface {
    @Shadow
    private static @Final Map<String, KeyBinding> KEYS_BY_ID = Maps.newHashMap();

    public String bindIt = "null";

    @Override
    public String getBindIt() {
        return bindIt;
    }
    @Override
    public void setBindIt(String bindIt) {
        this.bindIt = bindIt;
    }

    @Inject(at = @At("HEAD"), method = "updateKeysByCode")
    private static void updateKeysByCode(CallbackInfo ci) {
        KeyBindUtil keyBindUtil = ((MinecraftClientInterface)MinecraftClient.getInstance()).getKeyBindUtil();
        if (keyBindUtil.read == 2) {
            //set all keybind ui buttons from save
            ArrayList<ButtonBind> binds = keyBindUtil.binds;
            for (KeyBinding keyBinding : KEYS_BY_ID.values()) {
                for (ButtonBind bind : binds) {
                    if (bind.output.equals(keyBinding.getTranslationKey()))
                        ((KeyBindingInterface) keyBinding).setBindIt(bind.button);
                }
            }
        } else if (keyBindUtil.read == 1) {
            //load all keybind ui buttons to save
            ArrayList<ButtonBind> binds = new ArrayList<>();
            for (KeyBinding keyBinding : KEYS_BY_ID.values()) {
                String bindIt = ((KeyBindingInterface) keyBinding).getBindIt();
                //if it is not null
                if (!bindIt.equals("null")) {
                    // if button exists already, keep strangeness
                    int exists = 0;
                    for (ButtonBind buttonBind : keyBindUtil.binds) {
                        if (buttonBind.output.equals(keyBinding.getTranslationKey())) {
                            break;
                        }
                        exists++;
                    }
                    if (exists != keyBindUtil.binds.size()) {
                        ButtonBind bind = keyBindUtil.binds.get(exists);
                        bind.button = ((KeyBindingInterface) keyBinding).getBindIt();
                        binds.add(bind);
                    } else {
                        exists = 0;
                        // if not, check if button exists as default
                        for (ButtonBind buttonBind : KeyBindUtil.DEFAULTBINDS) {
                            if (buttonBind.output.equals(keyBinding.getTranslationKey())) {
                                break;
                            }
                            exists++;
                        }
                        if (exists != KeyBindUtil.DEFAULTBINDS.size())
                        {
                            ButtonBind bind = KeyBindUtil.DEFAULTBINDS.get(exists);
                            bind.button = ((KeyBindingInterface) keyBinding).getBindIt();
                            binds.add(bind);
                        } else {
                            // if not, add it
                            binds.add(new ButtonBind(keyBinding.getTranslationKey(), bindIt));
                        }
                    }
                }
            }
            //if not in the gui, keep anyway
            for (ButtonBind buttonBinding : keyBindUtil.binds) {
                if (!KEYS_BY_ID.containsKey(buttonBinding.output)) {
                    binds.add(buttonBinding);
                }
            }
            keyBindUtil.binds = binds;
        }
    }
}
