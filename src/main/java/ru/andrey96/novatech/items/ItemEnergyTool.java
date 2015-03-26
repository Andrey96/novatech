package ru.andrey96.novatech.items;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import ru.andrey96.novatech.api.IEnergyItem;

public abstract class ItemEnergyTool extends NTItem implements IEnergyItem{

	public ItemEnergyTool(String name) {
		super(name);
		this.setMaxStackSize(1);
		this.setMaxDamage(100);
	}
	
	@Override
	public long getCurrentCharge(ItemStack stack) {
		return stack.hasTagCompound()?stack.getTagCompound().getLong("charge"):0;
	}
	
	@Override
	public long modifyCharge(ItemStack stack, long delta, boolean force) {
		if(delta==0) return 0;
		if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		long charge = stack.getTagCompound().getLong("charge")+delta;
		if(charge<=0){
			if(force){
				stack.getTagCompound().setLong("charge", 0);
				stack.setItemDamage(0);
			}
			return charge;
		}
		long max = this.getMaxCharge(stack);
		if(charge>=max){
			if(force){
				stack.getTagCompound().setLong("charge", max);
				stack.setItemDamage(100);
			}
			return charge-max;
		}
		stack.getTagCompound().setLong("charge", charge);
		stack.setItemDamage((int)Math.round((((double)charge/max)*100)));
		return 0;
	}
	
	@Override
	public boolean canOutput() {
		return false;
	}
	
}
