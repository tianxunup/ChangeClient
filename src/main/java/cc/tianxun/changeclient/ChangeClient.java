package cc.tianxun.changeclient;

import cc.tianxun.changeclient.feature.Feature;
import cc.tianxun.changeclient.gui.screens.ChangeGui;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientLifecycleEvents;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Environment(EnvType.CLIENT)
public class ChangeClient implements ClientModInitializer {
	public static final String MOD_ID = "changeclient";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static float serverTps;

	public static KeyMapping changeGuiKey;
	public static KeyMapping boatFlyDeclineKey;
	private static final KeyMapping.Category KEY_CATEGORY = KeyMapping.Category.register(id("main"));

	@Override
	public void onInitializeClient() {
		changeGuiKey = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.change.gui",
			InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_BACKSPACE, KEY_CATEGORY));
		boatFlyDeclineKey = KeyMappingHelper.registerKeyMapping(new KeyMapping("key.change.boat_fly_decline",
			InputConstants.Type.KEYSYM, GLFW.GLFW_KEY_DOWN, KEY_CATEGORY));
		ClientTickEvents.END_CLIENT_TICK.register(this::tick);
		ClientLifecycleEvents.CLIENT_STOPPING.register(_ -> {
			try {
				ChangeFeaturesConfig.saveConfig();
			} catch (IOException e) {
				ChangeClient.LOGGER.error("Couldn't save config file!",e);
			}
		});
		try {
			ChangeFeaturesConfig.loadConfig();
		}
		catch (IOException e) {
			LOGGER.error(e.getMessage());
		}
		this.registerCommands();
		LOGGER.info("Initialized!");
	}

	private void registerCommands() {
		ClientCommandRegistrationCallback.EVENT.register((dispatcher,_) -> {
			LiteralArgumentBuilder<FabricClientCommandSource> command = ClientCommands.literal("change");
			for (Feature<?> feature : ChangeFeaturesConfig.features){
				feature.createCommands(command);
			}
			dispatcher.register(command);
		});
	}

	private void tick(Minecraft client) {
		while (changeGuiKey.consumeClick()) {
			client.setScreenAndShow(new ChangeGui());
		}
		if (client.player == null || client.player.gameMode() == null) {
			return;
		}
		if (ChangeFeaturesConfig.BOAT_FLY.getValue()) {
			ChangeFunctions.boatFly(client);
		}
		if (ChangeFeaturesConfig.AIR_FLY.getValue()) {
			client.player.getAbilities().mayfly = true;
		}
		else if (client.player.gameMode().isSurvival()) {
			client.player.getAbilities().mayfly = false;
		}
	}

	public static float getServerTps() {
		if (Minecraft.getInstance().isLocalServer() && Minecraft.getInstance().getSingleplayerServer() != null) {
			return Minecraft.getInstance().getSingleplayerServer().tickRateManager().tickrate();
		}
		return serverTps;
	}

	public static void setServerTps(float tps) {
		 serverTps = tps;
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
