package cc.tianxun.changeclient;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.phys.Vec3;

public class ChangeFunctions {
	private static final EntityType<?>[] boats = {
		EntityTypes.ACACIA_BOAT,EntityTypes.BIRCH_BOAT,EntityTypes.DARK_OAK_BOAT,
		EntityTypes.JUNGLE_BOAT,EntityTypes.SPRUCE_BOAT,
		EntityTypes.OAK_BOAT,EntityTypes.CHERRY_BOAT,EntityTypes.PALE_OAK_BOAT,
	};

	public static void boatFly(Minecraft client) {
		if (client.player == null) {
			return;
		}
		Entity vehicle = client.player.getVehicle();
		if (vehicle == null || !isBoat(vehicle.getType())) {
			return;
		}
		float tps = ChangeClient.getServerTps();
		Vec3 vec = vehicle.getDeltaMovement();
		double x = vec.x, y = 0, z = vec.z;
		if (client.options.keyJump.isDown()) {
			y += ChangeFeaturesConfig.BOAT_VERTICAL_SPEED.getValue()/tps;
		}
		if (ChangeClient.boatFlyDeclineKey.isDown()) {
			y -= ChangeFeaturesConfig.BOAT_VERTICAL_SPEED.getValue()/tps;
		}
		if (client.options.keyUp.isDown()) {
			double multiplier = ChangeFeaturesConfig.BOAT_SPEED.getValue()/tps/Math.sqrt(x*x+z*z);
			x *= multiplier;
			z *= multiplier;
		}
		vehicle.setDeltaMovement(x, y, z);
	}

	private static boolean isBoat(EntityType<?> type) {
		for (EntityType<?> boat : boats) {
			if (boat.equals(type)) {
				return true;
			}
		}
		return false;
	}
}
