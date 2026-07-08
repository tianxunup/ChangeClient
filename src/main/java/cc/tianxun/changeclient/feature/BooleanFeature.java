package cc.tianxun.changeclient.feature;

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
}
