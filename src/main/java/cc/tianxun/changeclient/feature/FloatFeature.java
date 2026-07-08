package cc.tianxun.changeclient.feature;

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
}
