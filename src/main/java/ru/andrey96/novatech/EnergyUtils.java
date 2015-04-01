package ru.andrey96.novatech;

import net.minecraft.item.ItemStack;
import ru.andrey96.novatech.api.IEnergyItem;

/**
 * A bunch of utility methods related to energy
 */
public class EnergyUtils {
	
	/**
	 * Transfers all power from one ItemStack to another
	 * @param from ItemStack instance to take power from
	 * @param to ItemStack instance to put power into
	 * @return true if success
	 */
	public static boolean transferPower(ItemStack from, ItemStack to) {
		if(from==null || from.getItem()==null || !(from.getItem() instanceof IEnergyItem))
			return false;
		return transferPower(from, to, ((IEnergyItem)from.getItem()).getCharge(from));
	}
	
	/**
	 * Transfers given power amount from one ItemStack to another
	 * @param from ItemStack instance to take power from
	 * @param to ItemStack instance to put power into
	 * @param charge amount to transfer
	 * @return true if success
	 */
	public static boolean transferPower(ItemStack from, ItemStack to, long charge) {
		if(from==null || to==null)
			return false;
		IEnergyItem source = null, dest = null;
		if(from.getItem()!=null && from.getItem() instanceof IEnergyItem)
			source = (IEnergyItem)from.getItem();
		if(to.getItem()!=null && to.getItem() instanceof IEnergyItem)
			dest = (IEnergyItem)to.getItem();
		if(source==null || dest==null)
			return false;
		if(charge==0 || charge<source.getCharge(from))
			return false;
		source.setCharge(from, dest.modifyCharge(to, charge, true));
		return true;
	}
	
}
