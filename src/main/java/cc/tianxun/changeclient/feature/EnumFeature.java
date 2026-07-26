package cc.tianxun.changeclient.feature;

import cc.tianxun.changeclient.ChangeClient;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommands;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;

import java.util.ArrayList;
import java.util.List;

public class EnumFeature extends StringFeature {
	private final List<String> enumValues;
	private final int defaultIndex;
	private int currentIndex;
	private EnumFeature(String id, List<String> enumValues, int defaultIndex) {
		super(id, enumValues.get(defaultIndex));
		this.enumValues = enumValues;
		this.currentIndex = this.defaultIndex = defaultIndex;
	}

	public int getDefaultIndex() {
		return defaultIndex;
	}
	public String getValue(int index) {
		return enumValues.get(index);
	}
	public int getCurrentIndex() {
		return currentIndex;
	}
	public List<String> getEnumValues() {
		return enumValues;
	}
	public Component getTranslatableValue() {
		return getTranslatableValue(this.currentIndex);
	}
	public Component getTranslatableValue(int index) {
		return Component.translatable("feature.change_client.feature." + enumValues.get(index));
	}
	public Component getTranslatableValue(String id) {
		return Component.translatable("feature.change_client.feature." + id);
	}

	@Override
	public void setDefault() {
		this.setCurrentIndex(this.defaultIndex);
	}

	public void setCurrentIndex(int currentIndex) {
		if (currentIndex < this.enumValues.size()) {
			this.currentIndex = currentIndex;
		}
		this.value = this.enumValues.get(currentIndex);
	}
	@Override
	public void setValue(String value) {
		int index = this.enumValues.indexOf(value);
		if (index >= 0) {
			this.setCurrentIndex(index);
		}
		else {
			ChangeClient.LOGGER.warn("[{}] Invalid value: {}", this.getId(), value);
		}

	}

	@Override
	public void createCommands(LiteralArgumentBuilder<FabricClientCommandSource> command) {
		LiteralArgumentBuilder<FabricClientCommandSource> sub = ClientCommands.literal(this.getId()).executes(this::getCommand);
		int index = 0;
		for (String value : this.enumValues) {
			int finalIndex = index;
			sub.then(ClientCommands.literal(value).executes(context -> {
				this.setCurrentIndex(finalIndex);
				context.getSource().sendFeedback(Component.translatable("command.change.set.sucess.enum",this.getId(),this.getName(),this.getValue(),this.getTranslatableValue()));
				return 1;
			}));
			index++;
		}
		command.then(sub);
	}

	@Override
	protected int getCommand(CommandContext<FabricClientCommandSource> context) {
		context.getSource().sendFeedback(Component.translatable("command.change.get.enum",this.getId(),this.getName(),this.getValue(),this.getTranslatableValue()));
		return 1;
	}

	public static class Builder {
		private final String id;
		private int defaultIndex = 0;
		private final List<String> enumValues = new ArrayList<>();
		public Builder(String id) {
			this.id = id;
		}
		public void addEnumValue(String value) {
			this.enumValues.add(value);
		}

		public void setDefaultIndex(int defaultIndex) {
			this.defaultIndex = defaultIndex;
		}

		public EnumFeature build() {
			return new EnumFeature(id,this.enumValues,this.defaultIndex);
		}
	}
}
