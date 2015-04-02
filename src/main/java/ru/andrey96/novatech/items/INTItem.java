package ru.andrey96.novatech.items;

import net.minecraft.item.Item;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface INTItem {
	
	/**
	 * @return unlocalized item name without domain
	 */
	public String getName();
	/**
	 * Registers item's models in game on initialization state. Called only on client
	 */
	@SideOnly(Side.CLIENT)
	public void registerModels();
	
}
