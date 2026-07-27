package cc.tianxun.changeclient.mixin;

import cc.tianxun.changeclient.ChangeClient;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin{
	@Unique
	private static long last = -1;

	@Inject(method = "handleSetTime", at = @At("RETURN"))
	private void handleSetTime(CallbackInfo ci){
		if (Minecraft.getInstance().isLocalServer()){
			return;
		}
		if (last == -1){
			last = System.currentTimeMillis();
		}
		else {
			long now = System.currentTimeMillis();
			long delta = now - last;
			ChangeClient.setServerTps(20 * 1000.0f / delta);
			last = now;
		}
	}

}
