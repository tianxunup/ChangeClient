package cc.tianxun.changeclient.gui.screens;

import cc.tianxun.changeclient.ChangeFeaturesConfig;
import cc.tianxun.changeclient.feature.*;
import cc.tianxun.changeclient.util.RGBAColors;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.ContainerObjectSelectionList;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.network.chat.Component;
import org.jspecify.annotations.NonNull;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class FeaturesList extends ContainerObjectSelectionList<FeaturesList.Entry> {
	public static Minecraft client;

	private static final int itemWidth = 170;
	private static final int itemHeight = 28;

	public FeaturesList(Minecraft minecraft, ChangeGui guiScreen) {
		super(minecraft, guiScreen.width, guiScreen.layout.getContentHeight(), guiScreen.layout.getHeaderHeight(),itemHeight);
		client = minecraft;
		for (Feature<?> feature : ChangeFeaturesConfig.features) {
			switch (feature) {
				case BooleanFeature item: addEntry(new BooleanEntry(item));break;
				case NumberFeature<?> item: addEntry(new NumberEntry(item));break;
				case EnumFeature item: addEntry(new EnumEntry(item));break;
				case StringFeature item: addEntry(new StringEntry(item));break;
				default:break;
			}
		}
	}

	@Environment(EnvType.CLIENT)
	public abstract static class Entry extends ContainerObjectSelectionList.Entry<Entry> {
		protected Component name;
		protected final List<AbstractWidget> children = new ArrayList<>();

		public Entry(Component name) {
			this.name = name;
		}

		@Override
		public @NonNull List<? extends NarratableEntry> narratables() {
			return this.children;
		}

		@Override
		public @NonNull List<? extends GuiEventListener> children() {
			return this.children;
		}

	}

	@Environment(EnvType.CLIENT)
	public abstract static class FeatureEntry extends Entry {
		protected final Feature<?> feature;

		public FeatureEntry(Feature<?> feature) {
			super(feature.getName());
			this.feature = feature;
		}
		@Override
		public void extractContent(@NonNull GuiGraphicsExtractor graphics, int mouseX, int mouseY, boolean hovered, float a) {
			graphics.text(client.font, this.name, this.getContentX(), this.getContentYMiddle()-4, RGBAColors.WHITE);
			if (!this.children.isEmpty()) {
				this.children.getFirst().setX(this.getContentRight() - 80);
				this.children.getFirst().setY(this.getContentY() + 4);
				this.children.getFirst().extractRenderState(graphics, mouseX, mouseY, a);
			}
		}

	}

	@Environment(EnvType.CLIENT)
	public static class BooleanEntry extends FeatureEntry{
		private final CycleButton<Boolean> checkbox;

		public BooleanEntry(BooleanFeature feature) {
			super(feature);
			checkbox = CycleButton.onOffBuilder(feature.getValue()).displayOnlyValue().create(10,5,itemWidth,itemHeight-10,feature.getName(),
				(_, value) -> feature.setValue(value));
			this.children.add(checkbox);
		}
	}
	@Environment(EnvType.CLIENT)
	public static class NumberEntry extends FeatureEntry{
		private final EditBox input;

		public NumberEntry(NumberFeature<?> feature) {
			super(feature);
			this.input = new EditBox(client.font,itemWidth,itemHeight-8,feature.getName());
			this.input.setValue(feature.getValue().toString());
			this.input.setResponder(value -> {
				if (feature.isValid(value)) {
					this.input.setTextColor(-2039584);
					if (feature instanceof IntegerFeature) {
						((IntegerFeature) feature).setValue(Integer.parseInt(value));
					}
					else {
						((FloatFeature) feature).setValue(Float.parseFloat(value));
					}
				}
				else {
					this.input.setTextColor(-65536);
				}
			});
			this.children.add(input);
		}
	}

	@Environment(EnvType.CLIENT)
	public static class StringEntry extends FeatureEntry{
		private final EditBox input;

		public StringEntry(StringFeature feature) {
			super(feature);
			this.input = new EditBox(client.font,itemWidth,itemHeight-8,feature.getName());
			this.input.setValue(feature.getValue());
			this.input.setResponder(feature::setValue);
			this.children.add(input);
		}
	}
	@Environment(EnvType.CLIENT)
	public static class EnumEntry extends FeatureEntry{
		private final CycleButton<String> checkbox;

		public EnumEntry(EnumFeature feature) {
			super(feature);
			CycleButton.Builder<String> builder = CycleButton.builder(feature::getTranslatableValue,feature.getDefaultValue()).displayOnlyValue();
			builder.withValues(feature.getEnumValues(),feature.getEnumValues());
			this.checkbox = builder.create(0,4,itemWidth,itemHeight-8,feature.getName(),(_,value) -> feature.setValue(value));
			this.checkbox.setValue(feature.getValue());
			this.children.add(checkbox);
		}
	}
}
