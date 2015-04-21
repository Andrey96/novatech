package ru.andrey96.novatech.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public interface IUpdatingArmor {
	
	/**
	 * Triggered every tick on client and server for items in armor slots that're implementing IUpdatingArmor interface
	 * @param player EntityPlayer armor updating on
	 * @param stack ItemStack that is updating
	 * @param armorSlot armor slot id where item is
	 */
	public void onArmorUpdateTick(EntityPlayer player, ItemStack stack, int armorSlot);
	
}
