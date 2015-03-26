package ru.andrey96.novatech.items.tools;

import net.minecraft.item.ItemStack;
import ru.andrey96.novatech.items.ItemEnergyTool;
import ru.andrey96.novatech.items.NTItem;

public class ItemLaserGun extends ItemEnergyTool{

	public ItemLaserGun(String name) {
		super(name);
	}

	@Override
	public long getMaxCharge(ItemStack stack) {
		return 100000;
	}
	
}
