package ru.andrey96.novatech.items;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBattery extends Item implements IChargeable{
	
	public ItemBattery(){
		this.setHasSubtypes(true);
		this.setMaxDamage(0);
		this.setMaxStackSize(32);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return super.getUnlocalizedName(stack)+stack.getItemDamage();
	}

	@Override
	public long getMaxCharge(ItemStack stack) {
		return 10000;
	}

	@Override
	public long getCurrentCharge(ItemStack stack) {
		return stack.hasTagCompound()?stack.getTagCompound().getLong("charge"):0;
	}
	
	@Override
	public long modifyCharge(ItemStack stack, long delta) {
		if(delta==0) return 0;
		if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		long charge = stack.getTagCompound().getLong("charge")+delta;
		if(charge<=0){
			stack.setTagCompound(null);
			stack.setItemDamage(0);
			return charge;
		}
		long max = this.getMaxCharge(stack);
		if(charge>=max){
			stack.setTagCompound(null);
			stack.setItemDamage(3);
			return charge-max;
		}
		stack.getTagCompound().setLong("charge", charge);
		stack.setItemDamage((int)Math.round((((double)charge/max)*2)));
		return 0;
	}
	
	@SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems)
    {
        subItems.add(new ItemStack(itemIn, 1, 0));
        subItems.add(new ItemStack(itemIn, 1, 3));
    }
	
}
