package cc.tianxun.changeclient.gui.screens;

import cc.tianxun.changeclient.ChangeClient;
import cc.tianxun.changeclient.ChangeFeaturesConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

import java.io.IOException;

@Environment(EnvType.CLIENT)
public class ChangeGui extends Screen {
	protected HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
	public ChangeGui() {
		super(Component.translatable("gui.changeclient.main"));
	}
	@Override
	public void init() {
		super.init();
		this.layout.addTitleHeader(this.title,this.font);
		this.layout.addToContents(new FeaturesList(Minecraft.getInstance(),this));
		this.layout.addToFooter(Button.builder(Component.translatable("gui.changeclient.close"), button -> {
			Minecraft.getInstance().gui.setScreen(null);
		}).bounds(0,0,160,20).build());
		this.layout.visitWidgets(this::addRenderableWidget);
		this.layout.arrangeElements();
	}

	@Override
	public void extractRenderState(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, float delta) {
		super.extractRenderState(graphics,mouseX,mouseY,delta);
	}

	@Override
	public void onClose() {
		super.onClose();
		try {
			ChangeFeaturesConfig.saveConfig();
		} catch (IOException e) {
			ChangeClient.LOGGER.error("Couldn't save config file!",e);
		}
	}
}
