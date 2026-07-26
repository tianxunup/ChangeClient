package cc.tianxun.changeclient.feature;

import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class FloatFeature extends NumberFeature<Float> {
	public FloatFeature(String id, Float defaultValue, Float minValue, Float maxValue) {
		super(id, defaultValue, minValue, maxValue);
	}

	public FloatFeature(String id, Float defaultValue) {
		super(id, defaultValue);
	}

	@Override
	public Float limitedValue(@NotNull Float value) {
		if (value < this.getMinValue())  {
			return this.getMinValue();
		}
		else {
			return Math.min(value, this.getMaxValue());
		}
	}

	@Override
	public void createCommands(LiteralArgumentBuilder<FabricClientCommandSource> command) {
		LiteralArgumentBuilder<FabricClientCommandSource> sub = ClientCommands.literal(this.getId()).executes(this::getCommand);
		if (this.isLimited()) {
			sub.then(ClientCommands.argument("value", FloatArgumentType.floatArg(this.getMinValue(),this.getMaxValue())).executes(this::setCommand));
		}
		else {
			sub.then(ClientCommands.argument("value", FloatArgumentType.floatArg()).executes(this::setCommand));
		}
		command.then(sub);
	}

	@Override
	protected int setCommand(CommandContext<FabricClientCommandSource> context) {
		this.setValue(FloatArgumentType.getFloat(context, "value"));
		context.getSource().sendFeedback(Component.translatable("command.change.get",this.getId(),this.getName(),this.getValue()));
		return 1;
	}
}
