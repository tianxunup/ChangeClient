package cc.tianxun.changeclient;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;

public class ChangeFunctions {
	public static void boatFly(Minecraft client) {
		Entity vehicle = client.player.getVehicle();
		if (vehicle == null) {
			return;
		}
		Vec3 vec = vehicle.getDeltaMovement();
		double x = vec.x, y = 0, z = vec.z;
		if (client.options.keyJump.isDown()) {
			y += ChangeFeaturesConfig.BOAT_VERTICAL_SPEED.getValue();
		}
		if (ChangeClient.boatFlyDeclineKey.isDown()) {
			y -= ChangeFeaturesConfig.BOAT_VERTICAL_SPEED.getValue();
		}
		if (client.options.keyUp.isDown()) {
			double multiplier = calculateMultiplier(ChangeFeaturesConfig.BOAT_SPEED.getValue());
			x *= multiplier;
			z *= multiplier;
		}
		vehicle.setDeltaMovement(x, y, z);
	}
	private static double calculateMultiplier(double velocity) {
		if (velocity <= 0.0D)
			return 0.0D;
		double logInput = velocity - 8.0D + 11.9072D;
		if (logInput <= 0.0D)
			return 1.0D;
		double term1 = -5.33893D * Math.pow(Math.log(logInput), -3.31832D);
		double base = term1 + 1.26253D;
		if (base < 0.0D)
			return 1.0D;
		return Math.pow(base, 0.470998D);
	}
}
