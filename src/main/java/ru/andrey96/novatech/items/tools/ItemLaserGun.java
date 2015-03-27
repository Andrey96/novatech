package ru.andrey96.novatech.items.tools;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import ru.andrey96.novatech.NovaTech;
import ru.andrey96.novatech.items.NTItem;

public class ItemLaserGun extends ItemEnergyTool{

	private final ModelResourceLocation[] usageModels = new ModelResourceLocation[5];
	private static final int[] usageDelays = new int[]{ 0, 15, 35, 60 };
	private static final int[] powerConsumption = new int[]{ 800, 1600, 2400, 10000 };
	
	public ItemLaserGun(String name) {
		super(name);
		String[] varr = new String[6];
		varr[0] = NovaTech.MODID+":"+name;
		for(int i=0; i<5; i++){
			varr[i+1] = NovaTech.MODID+":"+name+"_power"+i;
		}
		varr[5] = NovaTech.MODID+":"+name+"_empty";
		ModelBakery.addVariantName(this, varr);
	}

	@Override
	public long getMaxCharge(ItemStack stack) {
		return 100000;
	}
	
	@Override
	public ModelResourceLocation getModel(ItemStack stack, EntityPlayer player, int useRemaining) {
		long charge = this.getCurrentCharge(stack);
		if(charge<powerConsumption[0])
			return usageModels[4];
		if(player.getItemInUse() != null){
			int using = stack.getMaxItemUseDuration() - useRemaining;
			for(int i=3; i>=0; i--)
				if(using>usageDelays[i] && charge>=powerConsumption[i])
					return usageModels[i];
		}
		return super.getModel(stack, player, useRemaining);
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack stack) {
		return 72000;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer playerIn) {
		if(this.getCurrentCharge(itemStackIn)>=powerConsumption[0])
			playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
		return itemStackIn;
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer playerIn, int timeLeft)
    {
		long charge = this.getCurrentCharge(stack);
		if(charge<powerConsumption[0]) return;
		int using = stack.getMaxItemUseDuration() - timeLeft;
		for(int i=3; i>=0; i--)
			if(using>usageDelays[i] && charge>=powerConsumption[i])
				this.shoot(stack, worldIn, playerIn, i);
    }
	
	public void shoot(ItemStack stack, World world, EntityPlayer player, int tier){
		if(this.modifyCharge(stack, -powerConsumption[tier], false)==0){
			//TODO: implement shooting
		}
	}
	
	@Override
	public void registerModels() {
		super.registerModels();
		for(int i=0; i<4; i++)
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(this, i+1000, usageModels[i] = new ModelResourceLocation(NovaTech.MODID+":"+this.getName()+"_power"+i, "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(this, 1004, usageModels[4] = new ModelResourceLocation(NovaTech.MODID+":"+this.getName()+"_empty", "inventory"));
	}
	
}
