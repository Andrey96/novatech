package ru.andrey96.novatech.items;

import net.minecraft.item.Item;

public interface INTItem {
	
	/**
	 * @return unlocalized item name without domain
	 */
	public String getName();
	/**
	 * Registers item's models in game on initialization state
	 */
	public void registerModels();
	
}
