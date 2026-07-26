package cc.tianxun.changeclient.feature;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;

public class StringFeature extends Feature<String> {
	public StringFeature(String id, String defaultValue) {
		super(id, defaultValue);
	}

	@Override
	public void createCommands(LiteralArgumentBuilder<FabricClientCommandSource> command) {
		command.then(ClientCommands.literal(this.getId()).executes(this::getCommand)
			.then(ClientCommands.argument("value", StringArgumentType.string()).executes(this::setCommand)));
	}

	@Override
	protected int getCommand(CommandContext<FabricClientCommandSource> context) {
		context.getSource().sendFeedback(Component.translatable("command.change.get",this.getId(),this.getName(),this.getValue()));
		return 1;
	}

	@Override
	protected int setCommand(CommandContext<FabricClientCommandSource> context) {
		this.setValue(StringArgumentType.getString(context, "value"));
		context.getSource().sendFeedback(Component.translatable("command.change.set.sucess",this.getId(),this.getName(),this.getValue()));
		return 1;
	}
}
