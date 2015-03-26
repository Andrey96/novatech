package ru.andrey96.novatech.items.tools;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.andrey96.novatech.api.IEnergyItem;
import ru.andrey96.novatech.items.NTItem;

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
				stack.setItemDamage(100);
			}
			return charge;
		}
		long max = this.getMaxCharge(stack);
		if(charge>=max){
			if(force){
				stack.getTagCompound().setLong("charge", max);
				stack.setItemDamage(1);
			}
			return charge-max;
		}
		stack.getTagCompound().setLong("charge", charge);
		stack.setItemDamage(100-(int)Math.round((((double)charge/max)*99)));
		return 0;
	}
	
	@Override
	public boolean canOutput() {
		return false;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		tooltip.add(I18n.format("tooltip.charge", this.getCurrentCharge(stack), this.getMaxCharge(stack)));
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems)
    {
        subItems.add(new ItemStack(itemIn, 1, 100));
        ItemStack charged;
        subItems.add(charged = new ItemStack(itemIn, 1, 1));
        charged.setTagCompound(new NBTTagCompound());
        charged.getTagCompound().setLong("charge", this.getMaxCharge(charged));
    }
	
}
