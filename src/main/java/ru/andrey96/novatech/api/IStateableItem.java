package ru.andrey96.novatech.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IStateableItem {
	
	/**
	 * Executed on client and server when state switch key pressed (default P)
	 * @param ist ItemStack action performed on
	 * @param player Player that performed action
	 */
	public void onStateSwitch(ItemStack ist, EntityPlayer player);
	
}
