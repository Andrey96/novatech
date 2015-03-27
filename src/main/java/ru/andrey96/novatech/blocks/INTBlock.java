package ru.andrey96.novatech.blocks;

public interface INTBlock {
	
	/**
	 * @return unlocalized block name without domain
	 */
	public String getName();
	/**
	 * Registers block's models in game on initialization state. Called only on client
	 */
	public void registerModels();
	
	
}
