package ru.andrey96.novatech.items;

import net.minecraft.item.ItemStack;

public interface IChargeable {
	
	public long getMaxCharge(ItemStack stack);
	public long getCurrentCharge(ItemStack stack);
	public long modifyCharge(ItemStack stack, long delta);
	
}
