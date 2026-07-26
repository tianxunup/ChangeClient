package cc.tianxun.changeclient.feature;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class IntegerFeature extends NumberFeature<Integer> {
	public IntegerFeature(String id, Integer defaultValue, Integer minValue, Integer maxValue) {
		super(id, defaultValue, minValue, maxValue);
	}

	public IntegerFeature(String id, Integer defaultValue) {
		super(id, defaultValue);
	}

	@Override
	public Integer limitedValue(@NotNull Integer value) {
		if (value < this.getMinValue())  {
			return this.getMinValue();
		}
		else {
			return Math.min(value, this.getMaxValue());
		}
	}

	@Override
	public boolean isValid(String value) {
		try {
			int iv = Integer.parseInt(value);
			return iv >= this.getMinValue() && iv <= this.getMaxValue();
		}
		catch (NumberFormatException e) {
			return false;
		}
	}

	@Override
	public void createCommands(LiteralArgumentBuilder<FabricClientCommandSource> command) {
		LiteralArgumentBuilder<FabricClientCommandSource> sub = ClientCommands.literal(this.getId()).executes(this::getCommand);
		if (this.isLimited()) {
			sub.then(ClientCommands.argument("value", IntegerArgumentType.integer(this.getMinValue(),this.getMaxValue())).executes(this::setCommand));
		}
		else {
			sub.then(ClientCommands.argument("value", IntegerArgumentType.integer()).executes(this::setCommand));
		}
		command.then(sub);
	}

	@Override
	protected int setCommand(CommandContext<FabricClientCommandSource> context) {
		this.setValue(IntegerArgumentType.getInteger(context, "value"));
		context.getSource().sendFeedback(Component.translatable("command.change.get",this.getId(),this.getName(),this.getValue()));
		return 1;
	}
}
