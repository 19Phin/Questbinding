package net.dialingspoon.questbind;

import net.dialingspoon.questbind.interfaces.MinecraftClientInterface;
import net.fabricmc.api.ModInitializer;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Questbind implements ModInitializer {

	public static final String MOD_ID = "questbind";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	@Override
	public void onInitialize() {
		//*!((MinecraftClientInterface) MinecraftClient.getInstance()).getKeyBindUtil().loadKeybinds();
	}
}
