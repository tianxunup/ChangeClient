package cc.tianxun.changeclient.feature;

import cc.tianxun.changeclient.ChangeClient;

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
