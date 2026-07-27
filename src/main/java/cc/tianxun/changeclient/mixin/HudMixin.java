package cc.tianxun.changeclient.mixin;

import cc.tianxun.changeclient.ChangeFeaturesConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(Hud.class)
public class HudMixin {
	@Unique
	private final Identifier pumpkinBlur = Identifier.withDefaultNamespace("textures/misc/pumpkinblur.png");

	@Inject(method = "extractTextureOverlay", at = @At("HEAD"), cancellable = true)
	void extractTextureOverlay(GuiGraphicsExtractor graphics, Identifier texture, float alpha, CallbackInfo ci) {
		if (ChangeFeaturesConfig.DISABLE_PUMPKIN_BLUR.getValue() && texture.equals(pumpkinBlur)) {
			ci.cancel();
		}
	}
}
