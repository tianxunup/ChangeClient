package cc.tianxun.changeclient.feature;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;

public abstract class Feature<T> {
	private final String id;
	protected T value;
	private final T defaultValue;

	public Feature(String id, T defaultValue) {
		this.id = id;
		this.value = this.defaultValue = defaultValue;
	}
	public String getId() {
		return id;
	}
	public Component getName() {
		return Component.translatable("changeclient.feature." + id);
	}
	public T getValue() {
		return value;
	}
	public T getDefaultValue() {
		return defaultValue;
	}

	public boolean isDefault() {
		return value.equals(defaultValue);
	}
	public void setDefault() {
		this.value = this.defaultValue;
	}

	public void setValue(T value) {
		if (value == null) {
			this.value = this.defaultValue;
		}
		else {
			this.value = value;
		}
	}

	public abstract void createCommands(LiteralArgumentBuilder<FabricClientCommandSource> command);

	protected abstract int getCommand(CommandContext<FabricClientCommandSource> context);

	protected abstract int setCommand(CommandContext<FabricClientCommandSource> context);
}
