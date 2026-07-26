package cc.tianxun.changeclient.feature;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;

public class BooleanFeature extends Feature<Boolean> {

	public BooleanFeature(String id, boolean defaultValue) {
		super(id,defaultValue);
	}

	public void setOn() {
		this.setValue(true);
	}
	public void setOff() {
		this.setValue(false);
	}

	@Override
	public void createCommands(LiteralArgumentBuilder<FabricClientCommandSource> command) {
		command.then(ClientCommands.literal(this.getId()).executes(this::getCommand)
			.then(ClientCommands.argument("value", BoolArgumentType.bool()).executes(this::setCommand)));
	}

	@Override
	protected int getCommand(CommandContext<FabricClientCommandSource> context) {
		if (this.getValue()) {
			context.getSource().sendFeedback(Component.translatable("command.change.get.true",this.getId(),this.getName()));
		}
		else {
			context.getSource().sendFeedback(Component.translatable("command.change.get.false",this.getId(),this.getName()));
		}
		return 1;
	}

	@Override
	protected int setCommand(CommandContext<FabricClientCommandSource> context) {
		this.setValue(BoolArgumentType.getBool(context, "value"));
		if (this.getValue()) {
			context.getSource().sendFeedback(Component.translatable("command.change.set.sucess.true",this.getId(),this.getName()));
		}
		else {
			context.getSource().sendFeedback(Component.translatable("command.change.set.sucess.false",this.getId(),this.getName()));
		}
		return 1;
	}
}
