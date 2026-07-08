package cc.tianxun.changeclient;

import cc.tianxun.changeclient.feature.BooleanFeature;
import cc.tianxun.changeclient.feature.Feature;
import cc.tianxun.changeclient.feature.FloatFeature;
import cc.tianxun.changeclient.feature.IntegerFeature;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ChangeFeaturesConfig {
	public static final List<Feature<?>> features = new ArrayList<>();
	public static final BooleanFeature BOAT_FLY = new BooleanFeature("boat_fly", false);
	public static final FloatFeature BOAT_SPEED = new FloatFeature("boat_speed", 12.0f, 0.0f, 1000.0f);
	public static final FloatFeature BOAT_VERTICAL_SPEED = new FloatFeature("boat_vertical_speed", 0.4f, 0.0f, 10.0f);

	static {
		features.add(BOAT_FLY);
		features.add(BOAT_SPEED);
		features.add(BOAT_VERTICAL_SPEED);
	}

	public static Feature<?> getFeatureByName(String name) {
		for (Feature<?> feature : features) {
			if (feature.getId().equals(name)) {
				return feature;
			}
		}
		return null;
	}

	public static void loadConfig() throws IOException {
		File file = new File(String.format("config/%s/features_config.json", ChangeClient.MOD_ID));
		if (!file.exists()) {
			return;
		}
		JsonReader reader = new JsonReader(new InputStreamReader(new FileInputStream(file), StandardCharsets.UTF_8));
		reader.beginObject();
		while (reader.hasNext()) {
			String name = reader.nextName();
			Feature<?> feature = getFeatureByName(name);
			if (feature == null) {
				reader.skipValue();
				continue;
			}
			switch (feature) {
				case BooleanFeature booleanFeature -> booleanFeature.setValue(reader.nextBoolean());
				case FloatFeature floatFeature -> floatFeature.setValue((float) reader.nextDouble());
				case IntegerFeature integerFeature -> integerFeature.setValue(reader.nextInt());
				default -> {}
			}

		}
	}
	public static void saveConfig() throws IOException {
		File dir = new File(String.format("config/%s", ChangeClient.MOD_ID));
		File file = new File(String.format("config/%s/features_config.json", ChangeClient.MOD_ID));
		if (!file.exists()) {
			dir.mkdirs();
			file.createNewFile();
		}
		OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(file), StandardCharsets.UTF_8);
		writer.write("{\n");
		int index = 1;
		for (Feature<?> feature : features) {
			writer.write(String.format("\t\"%s\": %s", feature.getId(),feature.getValue().toString()));
			if (index < features.size()) {
				writer.write(',');
			}
			writer.write("\n");
			index++;
		}
		writer.write("}");
		writer.close();
	}
}
