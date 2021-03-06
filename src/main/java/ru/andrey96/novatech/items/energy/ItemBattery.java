package ru.andrey96.novatech.items.energy;

import java.util.List;

import ru.andrey96.novatech.api.IEnergyItem;
import ru.andrey96.novatech.items.NTItem;
import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemBattery extends NTItem implements IEnergyItem{
	
	public ItemBattery(String name){
		super(name, 4);
		this.setMaxStackSize(32);
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return stack.getItemDamage()==3?super.getUnlocalizedName(stack)+"_full":super.getUnlocalizedName(stack);
	}

	@Override
	public long getMaxCharge(ItemStack stack) {
		return 10000;
	}

	@Override
	public long getCharge(ItemStack stack) {
		return stack.hasTagCompound()?stack.getTagCompound().getLong("charge"):stack.getItemDamage()==3?this.getMaxCharge(stack):0;
	}
	
	@Override
	public long modifyCharge(ItemStack stack, long delta, boolean force) {
		if(delta==0) return 0;
		if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		long actual = stack.getTagCompound().getLong("charge");
		long charge = actual+delta;
		if(charge<=0){
			if(force || charge==0 || actual==0){
				stack.setTagCompound(null);
				stack.setItemDamage(0);
			}
			return charge;
		}
		long max = this.getMaxCharge(stack);
		if(charge>=max){
			if(force || charge==max || actual==max){
				stack.setTagCompound(null);
				stack.setItemDamage(3);
			}
			return charge-max;
		}
		stack.getTagCompound().setLong("charge", charge);
		stack.setItemDamage((int)Math.round((((double)charge/max)*3)));
		return 0;
	}
	
	@Override
	public void setCharge(ItemStack stack, long charge) {
		long max = this.getMaxCharge(stack);
		if(charge<=0){
			stack.setTagCompound(null);
			stack.setItemDamage(0);
		}else if(charge>=max){
			stack.setTagCompound(null);
			stack.setItemDamage(3);
		}else{
			if(!stack.hasTagCompound())
				stack.setTagCompound(new NBTTagCompound());
			stack.getTagCompound().setLong("charge", charge);
			stack.setItemDamage((int)Math.round((((double)charge/max)*3)));
		}
	}
	
	@Override
	public boolean canOutput() {
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		tooltip.add(I18n.format("tooltip.charge", this.getCharge(stack), this.getMaxCharge(stack)));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems)
    {
        subItems.add(new ItemStack(itemIn, 1, 0));
        subItems.add(new ItemStack(itemIn, 1, 3));
    }
	
}
