package net.dialingspoon.questbind;

import net.dialingspoon.questbind.interfaces.MinecraftClientInterface;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.MinecraftClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class QuestbindClient implements ClientModInitializer {

	public static final String MOD_ID = "questbind";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitializeClient() {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			// This code will be executed when the JVM is shutting down
			((MinecraftClientInterface)MinecraftClient.getInstance()).getKeyBindUtil().actualSave();
		}));
	}
}
