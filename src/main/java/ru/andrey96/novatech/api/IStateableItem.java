package ru.andrey96.novatech.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public interface IStateableItem {
	
	/**
	 * Executed on client and server when state switch key pressed (default P)
	 * @param ist ItemStack action performed on
	 * @param player Player that performed action
	 */
	public void onStateSwitch(ItemStack ist, EntityPlayer player);
	
	public boolean canSwitchState(ItemStack ist, EntityPlayer player);
	
	@SideOnly(Side.CLIENT)
	public String getStateName(ItemStack ist, EntityPlayer player, boolean advanced);
	
}
