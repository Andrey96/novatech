package ru.andrey96.novatech.items.tools;

import ru.andrey96.novatech.NTUtils;
import ru.andrey96.novatech.NovaTech;
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

public class ItemFlashLight extends ItemEnergyTool{

	public static final byte lightRange = 32;
	private static final byte powerConsumption = 1;
	private static ModelResourceLocation modelOn;
	private static byte updateCounter = 0;
	
	public ItemFlashLight(String name) {
		super(name);
		if(NTUtils.isClient())
			ModelBakery.addVariantName(this, new String[]{ NovaTech.MODID+":"+name, NovaTech.MODID+":"+name+"_on" });
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining) {
		if(stack.hasTagCompound() && stack.getTagCompound().getBoolean("flashl_on"))
			return modelOn;
		return super.getModel(stack, player, useRemaining);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() {
		super.registerModels();
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(this, 1001, modelOn = new ModelResourceLocation(NovaTech.MODID+":"+this.getName()+"_on", "inventory"));
	}
	
	@Override
	public long getMaxCharge(ItemStack stack) {
		return 20000;
	}

	@Override
	public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
		if(worldIn.isRemote){
			
		}else{
			if(updateCounter++==20){
				updateCounter=0;
				if(isSelected && entityIn instanceof EntityPlayer && stack.hasTagCompound() && stack.getTagCompound().getBoolean("flashl_on") && modifyCharge(stack, -20, true)!=0){
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
	
}
