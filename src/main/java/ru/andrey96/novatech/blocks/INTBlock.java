package ru.andrey96.novatech.blocks;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface INTBlock {
	
	/**
	 * @return unlocalized block name without domain
	 */
	public String getName();
	/**
	 * Registers block's models in game on initialization state. Called only on client
	 */
	@SideOnly(Side.CLIENT)
	public void registerModels();
	
	
}
