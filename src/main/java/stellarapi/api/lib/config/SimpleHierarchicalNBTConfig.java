package stellarapi.api.lib.config;

import java.util.Map;

import com.google.common.collect.Maps;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.config.Configuration;

public abstract class SimpleHierarchicalNBTConfig extends SimpleNBTConfig {

	private Map<String, INBTConfig> subConfigs = Maps.newHashMap();

	public void putSubConfig(String key, INBTConfig config) {
		subConfigs.put(key, config);
	}

	public INBTConfig getSubConfig(String key) {
		return subConfigs.get(key);
	}

	@Override
	public void deserializeNBT(NBTTagCompound compound) {
		super.deserializeNBT(compound);
		for (Map.Entry<String, INBTConfig> entry : subConfigs.entrySet())
			entry.getValue().deserializeNBT(compound.getCompoundTag(entry.getKey()));
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		for (Map.Entry<String, INBTConfig> entry : subConfigs.entrySet())
			compound.setTag(entry.getKey(), entry.getValue().serializeNBT());

		return compound;
	}

	@Override
	public void setupConfig(Configuration config, String category) {
		super.setupConfig(config, category);
		for (Map.Entry<String, INBTConfig> entry : subConfigs.entrySet())
			entry.getValue().setupConfig(config, category + Configuration.CATEGORY_SPLITTER + entry.getKey());
	}

	@Override
	public void loadFromConfig(Configuration config, String category) {
		super.loadFromConfig(config, category);
		for (Map.Entry<String, INBTConfig> entry : subConfigs.entrySet())
			entry.getValue().loadFromConfig(config, category + Configuration.CATEGORY_SPLITTER + entry.getKey());
	}

	@Override
	public void saveToConfig(Configuration config, String category) {
		super.saveToConfig(config, category);
		for (Map.Entry<String, INBTConfig> entry : subConfigs.entrySet())
			entry.getValue().saveToConfig(config, category + Configuration.CATEGORY_SPLITTER + entry.getKey());
	}

	@Override
	public abstract INBTConfig copy();

	protected void applyCopy(SimpleHierarchicalNBTConfig config) {
		super.applyCopy(config);
		for (Map.Entry<String, INBTConfig> entry : subConfigs.entrySet())
			config.putSubConfig(entry.getKey(), entry.getValue().copy());
	}

}
