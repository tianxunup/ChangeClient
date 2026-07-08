package cc.tianxun.changeclient.feature;

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
	public Component getTranslatableName() {
		return Component.translatable("changeclient.feature." + id);
	}
	public T getValue() {
		return value;
	}
	public T getDefaultValue() {
		return defaultValue;
	}

	public boolean isDefault() {
		return value == defaultValue;
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
}
