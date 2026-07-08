package cc.tianxun.changeclient;

import cc.tianxun.changeclient.feature.BooleanFeature;
import cc.tianxun.changeclient.feature.Feature;
import cc.tianxun.changeclient.feature.FloatFeature;
import cc.tianxun.changeclient.feature.IntegerFeature;
import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Environment(EnvType.CLIENT)
public class ChangeClient implements ClientModInitializer {
	public static final String MOD_ID = "changeclient";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

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
		ClientCommandRegistrationCallback.EVENT.register((dispatcher, _) -> {
			LiteralArgumentBuilder<FabricClientCommandSource> command = ClientCommands.literal("change");
			for (Feature<?> feature : ChangeFeaturesConfig.features) {
				RequiredArgumentBuilder<FabricClientCommandSource, ?> arg = ClientCommands.argument("value", StringArgumentType.string());
				switch (feature) {
					case BooleanFeature booleanFeature ->
						arg = ClientCommands.argument("value", BoolArgumentType.bool());
					case IntegerFeature integerFeature -> {
						if (integerFeature.isLimited()) {
							arg = ClientCommands.argument("value", IntegerArgumentType.integer(integerFeature.getMinValue(), integerFeature.getMaxValue()));
						}
						else {
							arg = ClientCommands.argument("value", IntegerArgumentType.integer());
						}
					}
					case FloatFeature floatFeature -> {
						if (floatFeature.isLimited()) {
							arg =ClientCommands.argument("value", FloatArgumentType.floatArg(floatFeature.getMinValue(), floatFeature.getMaxValue()));
						}
						else {
							arg = ClientCommands.argument("value", FloatArgumentType.floatArg());
						}
					}
					default -> {}
				}
				command.then(ClientCommands.literal(feature.getId()).executes(context -> {
					context.getSource().sendFeedback(Component.translatable("command.change.get",
						feature.getId(),feature.getTranslatableName(),feature.getValue().toString()));
					return 1;
				}).then(arg.executes(this::setCommandExecute)));
			}
			dispatcher.register(command);
		});
	}
	public int setCommandExecute(CommandContext<FabricClientCommandSource> context) {
		String name = context.getLastChild().getInput().split(" ",3)[1];
		Feature<?> feature = ChangeFeaturesConfig.getFeatureByName(name);
		switch (feature) {
			case null -> {
				context.getSource().sendFeedback(Component.translatable("command.change.fail.invalid_key", name));
				return 0;
			}
			case BooleanFeature feature1 -> feature1.setValue(BoolArgumentType.getBool(context, "value"));
			case IntegerFeature feature1 -> feature1.setValue(IntegerArgumentType.getInteger(context, "value"));
			case FloatFeature feature1 -> feature1.setValue(FloatArgumentType.getFloat(context, "value"));
			default -> {}
		}
		try {
			ChangeFeaturesConfig.saveConfig();
		}
		catch (IOException e) {
			LOGGER.error(e.getMessage());
		}
		context.getSource().sendFeedback(Component.translatable("command.change.sucess", name,feature.getTranslatableName(),feature.getValue().toString()));
		return 1;
	}

	private void tick(Minecraft client) {
		while (changeGuiKey.consumeClick()) {
			// pass
		}
		if (client.player == null) {
			return;
		}
		if (ChangeFeaturesConfig.BOAT_FLY.getValue()) {
			ChangeFunctions.boatFly(client);
		}
	}

	public static Identifier id(String path) {
		return Identifier.fromNamespaceAndPath(MOD_ID, path);
	}
}
