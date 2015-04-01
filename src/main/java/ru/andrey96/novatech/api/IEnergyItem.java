package ru.andrey96.novatech.api;

import net.minecraft.item.ItemStack;

public interface IEnergyItem {
	
	/**
	 * @param stack ItemStack instance to get from
	 * @return maximum amount of energy stored
	 */
	public long getMaxCharge(ItemStack stack);
	/**
	 * @param stack ItemStack instance to get from
	 * @return amount of energy stored
	 */
	public long getCharge(ItemStack stack);
	/**
	 * Modifies item's charge
	 * @param stack ItemStack instance to perform action on
	 * @param delta amount (can be negative) for modification
	 * @param force if <code>true</code> and there is overflow/lack of energy, it will be put/taken and set to 0/max
	 * @return 0 if success, otherwise negative (extra amount needed) if there's lack of energy or positive (extra amount inserted) if supplied too much
	 */
	public long modifyCharge(ItemStack stack, long delta, boolean force);
	/**
	 * Sets item's charge
	 * @param stack ItemStack instance to perform action on
	 * @param charge to set
	 */
	public void setCharge(ItemStack stack, long charge);
	/**
	 * @return can item output energy or not
	 */
	public boolean canOutput();
	
}
