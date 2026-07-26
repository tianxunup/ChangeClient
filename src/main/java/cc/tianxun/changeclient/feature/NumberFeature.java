package cc.tianxun.changeclient.feature;

import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.network.chat.Component;

public abstract class NumberFeature<T extends Number> extends Feature<T> {
	private final boolean limited;
	private final T minValue;
	private final T maxValue;
	public NumberFeature(String id, T defaultValue, T minValue, T maxValue) {
		super(id,defaultValue);
		this.limited = true;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}
	public NumberFeature(String id, T defaultValue) {
		super(id,defaultValue);
		this.limited = false;
		this.minValue = null;
		this.maxValue = null;
	}
	public T getMinValue() {
		return minValue;
	}
	public T getMaxValue() {
		return maxValue;
	}
	public boolean isLimited() {
		return limited;
	}

	public abstract T limitedValue(T value);
	
	public void setValue(T value){
		if (value == null) {
			this.setDefault();
		}
		else if (this.isLimited()) {
			this.value = this.limitedValue(value);
		}
		else {
			this.value = value;
		}
	}

	public abstract boolean isValid(String value);

	@Override
	protected int getCommand(CommandContext<FabricClientCommandSource> context) {
		context.getSource().sendFeedback(Component.translatable("command.change.get",this.getId(),this.getName(),this.getValue()));
		return 1;
	}
}
