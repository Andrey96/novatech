package ru.andrey96.novatech.items;

import net.minecraft.item.ItemStack;

public class ItemBatpack extends ItemBattery {
	
	public ItemBatpack(){
		super();
		setMaxStackSize(16);
	}
	
	@Override
	public long getMaxCharge(ItemStack stack) {
		return 100000;
	}
	
}