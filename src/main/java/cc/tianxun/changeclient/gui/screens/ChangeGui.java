package cc.tianxun.changeclient.gui.screens;

import cc.tianxun.changeclient.ChangeFeaturesConfig;
import cc.tianxun.changeclient.feature.Feature;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.PlainTextButton;
import net.minecraft.client.gui.components.StringWidget;
import net.minecraft.client.gui.layouts.GridLayout;
import net.minecraft.client.gui.layouts.HeaderAndFooterLayout;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

@Environment(EnvType.CLIENT)
public class ChangeGui extends Screen {
	private final HeaderAndFooterLayout layout = new HeaderAndFooterLayout(this);
	private final GridLayout grid = new GridLayout();
	public ChangeGui() {
		super(Component.translatable("gui.changeclient.main"));
	}
	@Override
	public void init() {
		super.init();
		this.layout.addTitleHeader(this.title,this.font);
		int index = 0;
		for (Feature<?> feature : ChangeFeaturesConfig.features) {
			PlainTextButton button = new PlainTextButton(0,0,200,40,
				Component.literal(feature.getValue().toString()),
				current -> this.buttonClicked(current,feature),
				this.font
			);
			this.grid.addChild(new StringWidget(feature.getTranslatableName(),this.font),0,index);
			this.grid.addChild(button,1,index);
			index++;
		}
		this.layout.addToContents(this.grid);
	}
	private void buttonClicked(Button button, Feature<?> feature) {

	}
}
