package ru.andrey96.novatech.items.tools;

import ru.andrey96.novatech.NTUtils;
import ru.andrey96.novatech.NovaTech;
import ru.andrey96.novatech.api.IStateableItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemFlashLight extends ItemEnergyTool implements IStateableItem{

	public static final byte lightRange = 32;
	private static ModelResourceLocation modelOn, modelP, modelPOn;
	private byte updateCounter = 0;
	
	public ItemFlashLight(String name) {
		super(name);
		if(NTUtils.isClient()){
			ModelBakery.addVariantName(this, new String[]{ NovaTech.MODID+":"+name, NovaTech.MODID+":"+name+"_on" });
			ModelBakery.addVariantName(this, new String[]{ NovaTech.MODID+":"+name, NovaTech.MODID+":"+name+"_p" });
			ModelBakery.addVariantName(this, new String[]{ NovaTech.MODID+":"+name, NovaTech.MODID+":"+name+"_p_on" });
		}
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining) {
		NBTTagCompound nbt = stack.getTagCompound();
		if(nbt!=null){
			if(nbt.getBoolean("flashl_p")){
				if(nbt.getBoolean("flashl_on"))
					return modelPOn;
				else
					return modelP;
			}else if(nbt.getBoolean("flashl_on")){
				return modelOn;
			}
		}
		return super.getModel(stack, player, useRemaining);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() {
		super.registerModels();
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(this, 1001, modelOn = new ModelResourceLocation(NovaTech.MODID+":"+this.getName()+"_on", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(this, 1002, modelP = new ModelResourceLocation(NovaTech.MODID+":"+this.getName()+"_p", "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(this, 1003, modelPOn = new ModelResourceLocation(NovaTech.MODID+":"+this.getName()+"_p_on", "inventory"));
	}
	
	/**
	 * @return power consumption per 20 ticks (negative number) based on state
	 */
	private static int getPowerConsumption(boolean state){
		return state?-60:-20;
	}
	
	/**
	 * @return light level based on state
	 */
	public static int getLightLevel(boolean state){
		return state?15:10;
	}
	
	@Override
	public long getMaxCharge(ItemStack stack) {
		return 20000;
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if(!worldIn.isRemote && updateCounter++==20){
			updateCounter=0;
			if(isSelected && entityIn instanceof EntityPlayer){
				NBTTagCompound nbt = stack.getTagCompound();
				if(nbt!=null && nbt.getBoolean("flashl_on") && modifyCharge(stack, getPowerConsumption(nbt.getBoolean("flashl_p")), true)!=0){
					stack.getTagCompound().setBoolean("flashl_on", false);
				}
			}
		}
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		if(!itemStackIn.hasTagCompound())
			itemStackIn.setTagCompound(new NBTTagCompound());
		itemStackIn.getTagCompound().setBoolean("flashl_on", !itemStackIn.getTagCompound().getBoolean("flashl_on"));
		return super.onItemRightClick(itemStackIn, worldIn, playerIn);
	}

	@Override
	public void onStateSwitch(ItemStack ist, EntityPlayer player) {
		NBTTagCompound nbt = ist.getTagCompound();
		if(nbt==null)
			ist.setTagCompound(nbt = new NBTTagCompound());
		nbt.setBoolean("flashl_p", !nbt.getBoolean("flashl_p"));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getStateName(ItemStack ist, EntityPlayer player, boolean advanced) {
		if(ist.hasTagCompound() && ist.getTagCompound().getBoolean("flashl_p"))
			return "item.flashl.state1";
		return "item.flashl.state0";
	}

	@Override
	public boolean canSwitchState(ItemStack ist, EntityPlayer player) {
		return true;
	}
	
}
