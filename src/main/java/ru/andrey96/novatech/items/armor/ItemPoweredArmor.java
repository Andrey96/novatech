package ru.andrey96.novatech.items.armor;

import java.util.List;

import net.minecraft.client.resources.I18n;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.andrey96.novatech.NTUtils;
import ru.andrey96.novatech.NovaTech;
import ru.andrey96.novatech.api.IEnergyItem;
import ru.andrey96.novatech.api.IStateableItem;
import ru.andrey96.novatech.api.IUpdatingArmor;

public class ItemPoweredArmor extends NTItemArmor implements IEnergyItem, IStateableItem, IUpdatingArmor{

	private byte updateCounter = 0;
	
	public ItemPoweredArmor(String name, ArmorMaterial material, int armorType) {
		super(name, material, 0, armorType);
	}

	@Override
	public long getMaxCharge(ItemStack stack) {
		return 200000;
	}

	@Override
	public long getCharge(ItemStack stack) {
		return stack.hasTagCompound()?stack.getTagCompound().getLong("charge"):0;
	}
	
	@Override
	public long modifyCharge(ItemStack stack, long delta, boolean force) {
		if(delta==0) return 0;
		if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		long charge = stack.getTagCompound().getLong("charge")+delta;
		if(charge<=0){
			if(force || charge==0){
				stack.getTagCompound().setLong("charge", 0);
			}
			return charge;
		}
		long max = this.getMaxCharge(stack);
		if(charge>=max){
			if(force || charge==max){
				stack.getTagCompound().setLong("charge", max);
			}
			return charge-max;
		}
		stack.getTagCompound().setLong("charge", charge);
		return 0;
	}
	
	@Override
	public void setCharge(ItemStack stack, long charge) {
		long max = this.getMaxCharge(stack);
		if(charge<0)
			charge=0;
		else if(charge>max)
			charge=max;
		if(!stack.hasTagCompound())
			stack.setTagCompound(new NBTTagCompound());
		stack.getTagCompound().setLong("charge", charge);
	}
	
	@Override
	public boolean canOutput() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, EntityPlayer playerIn, List tooltip, boolean advanced) {
		tooltip.add(I18n.format("tooltip.charge", this.getCharge(stack), this.getMaxCharge(stack)));
		String state = this.getStateName(stack, playerIn, advanced);
		if(state!=null)
			tooltip.add(I18n.format("tooltip.state", I18n.format(state)));
	}
	
	@Override
	public void onStateSwitch(ItemStack ist, EntityPlayer player) {
		if(!ist.hasTagCompound())
			ist.setTagCompound(new NBTTagCompound());
		switch(this.armorType){
			case 0:
				byte mode = ist.getTagCompound().getByte("flashl_mode");
				if(++mode==3) mode = 0;
				ist.getTagCompound().setByte("flashl_mode", mode);
				break;
			case 1:
				ist.getTagCompound().setBoolean("chestplate_p", !ist.getTagCompound().getBoolean("chestplate_p"));
				break;
			case 2:
				boolean state;
				ist.getTagCompound().setBoolean("leggings_p",  state = !ist.getTagCompound().getBoolean("leggings_p"));
				if(player.isSprinting()){
					IAttributeInstance attrib = player.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
					if (attrib.getModifier(NTUtils.bootsSprintModUUID)!=null)
			            attrib.removeModifier(NTUtils.bootsSprintMod);
					if(state)
						attrib.applyModifier(NTUtils.bootsSprintMod);
				}
				break;
			case 3:
				ist.getTagCompound().setBoolean("boots_p", !ist.getTagCompound().getBoolean("boots_p"));
				break;
		}
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public String getStateName(ItemStack ist, EntityPlayer player, boolean advanced) {
		switch(this.armorType){
			case 0:
				if(ist.hasTagCompound())
					return "item.power_helmet.state"+ist.getTagCompound().getByte("flashl_mode");
				return "item.power_helmet.state0";
			case 1:
				if(ist.hasTagCompound())
					return "item.power_chestplate.state"+(ist.getTagCompound().getBoolean("chestplate_p")?1:0);
				return "item.power_chestplate.state0";
			case 2:
				if(ist.hasTagCompound())
					return "item.power_leggings.state"+(ist.getTagCompound().getBoolean("leggings_p")?1:0);
				return "item.power_leggings.state0";
			case 3:
				if(ist.hasTagCompound())
					return "item.power_boots.state"+(ist.getTagCompound().getBoolean("boots_p")?1:0);
				return "item.power_boots.state0";
		}
		return null;
	}

	@Override
	public void onArmorUpdateTick(EntityPlayer player, ItemStack stack, int armorSlot) {
		if(!player.worldObj.isRemote && updateCounter++==20){
			updateCounter = 0;
			if(3-this.armorType==armorSlot){ //Checking just in case, we don't want armor to work if it's in a wrong slot
				switch(this.armorType){
					case 0: //Helmet
						if(stack.hasTagCompound()){
							byte mode = stack.getTagCompound().getByte("flashl_mode");
							if(mode!=0 && modifyCharge(stack, mode==1?-20:-40, true)!=0)
								stack.getTagCompound().setByte("flashl_mode", (byte)0);
							if(player.getAir()<=0 && modifyCharge(stack, -2000, true)==0)
								player.setAir(300);
						}
						break;
					case 2: //Leggings
						if(stack.hasTagCompound() && player.isSprinting()){
							if(stack.getTagCompound().getBoolean("leggings_p") && modifyCharge(stack, -160, true)!=0)
								stack.getTagCompound().setBoolean("leggings_p", false);
						}
						break;
				}
			}
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
    public void getSubItems(Item itemIn, CreativeTabs tab, List subItems)
    {
        subItems.add(new ItemStack(itemIn, 1));
        ItemStack charged;
        subItems.add(charged = new ItemStack(itemIn, 1));
        charged.setTagCompound(new NBTTagCompound());
        charged.getTagCompound().setLong("charge", this.getMaxCharge(charged));
    }

	@Override
	public boolean canSwitchState(ItemStack ist, EntityPlayer player) {
		return this.armorType==1 || this.getCharge(ist)!=0;
	}

	@Override
	public boolean showDurabilityBar(ItemStack stack) {
		return true;
	}
	
	@Override
	public double getDurabilityForDisplay(ItemStack stack) {
		return 1-this.getCharge(stack)/(double)this.getMaxCharge(stack);
	}
	
}
