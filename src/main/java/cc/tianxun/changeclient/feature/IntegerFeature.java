package cc.tianxun.changeclient.feature;

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
}
