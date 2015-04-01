package ru.andrey96.novatech.items.tools;

import javax.vecmath.Point3d;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import ru.andrey96.novatech.NTUtils;
import ru.andrey96.novatech.NovaTech;
import ru.andrey96.novatech.client.ClientEventHandler;
import ru.andrey96.novatech.items.NTItem;

public class ItemLaserGun extends ItemEnergyTool{

	private final ModelResourceLocation[] usageModels = new ModelResourceLocation[5];
	private static final int[] usageDelays = new int[]{ 0, 15, 35, 60 };
	private static final int[] powerConsumption = new int[]{ 800, 1800, 4000, 10000 };
	
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
		long charge = this.getCharge(stack);
		if(charge<powerConsumption[0])
			return usageModels[4];
		if(player.getItemInUse() != null){
			int using = stack.getMaxItemUseDuration() - useRemaining;
			for(byte i=3; i>=0; i--)
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
		if(this.getCharge(itemStackIn)>=powerConsumption[0])
			playerIn.setItemInUse(itemStackIn, this.getMaxItemUseDuration(itemStackIn));
		return itemStackIn;
	}
	
	@Override
	public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityPlayer playerIn, int timeLeft)
    {
		long charge = this.getCharge(stack);
		if(charge<powerConsumption[0]) return;
		int using = stack.getMaxItemUseDuration() - timeLeft;
		for(byte i=3; i>=0; i--)
			if(using>usageDelays[i] && charge>=powerConsumption[i])
				this.shoot(stack, worldIn, playerIn, i);
    }
	
	public void shoot(ItemStack stack, World world, EntityPlayer player, byte tier){
		if(this.modifyCharge(stack, -powerConsumption[tier], false)==0){
			new LaserShot(player, tier).process();
		}
	}
	
	@Override
	public void registerModels() {
		super.registerModels();
		for(int i=0; i<4; i++)
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(this, i+1000, usageModels[i] = new ModelResourceLocation(NovaTech.MODID+":"+this.getName()+"_power"+i, "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(this, 1004, usageModels[4] = new ModelResourceLocation(NovaTech.MODID+":"+this.getName()+"_empty", "inventory"));
	}
	
	public static class LaserShot {
		
		private static final float[] laserReachDistance = new float[]{ 8f, 16f, 32f, 64f };
		private static final float[] laserDamage = new float[]{ 4f, 7f, 10f };
		
		public EntityPlayer player;
		public byte tier;
		public short renderFramesLeft;
		//public Vec3 point;
		public double rayLength;
		
		public LaserShot(EntityPlayer player, byte tier) {
			this.player = player;
			this.tier = tier;
			this.renderFramesLeft = (short)(Minecraft.getDebugFPS()/4);
		}
		
		public void process() {
	        MovingObjectPosition obj = NTUtils.rayTrace(player, laserReachDistance[tier]);
			Vec3 point;
	        if(obj!=null && obj.typeOfHit!=MovingObjectType.MISS){
				point = obj.hitVec;
			}else{
				Vec3 look = player.getLookVec();
				double reach = laserReachDistance[tier];
				point = NTUtils.getEyesPos(player).addVector(look.xCoord*reach, look.yCoord*reach, look.zCoord*reach);
			}
			if(player.worldObj.isRemote){
				rayLength = point.subtract(player.getPositionEyes(1f)).lengthVector();
				ClientEventHandler.addLaserShot(this);
			}else{
				if(obj==null || obj.typeOfHit==MovingObjectType.BLOCK || tier==3){
					if(tier==3)
						player.worldObj.createExplosion(player, point.xCoord, point.yCoord, point.zCoord, 8f, true);
				}else if(obj.typeOfHit==MovingObjectType.ENTITY && obj.entityHit!=null){
					obj.entityHit.attackEntityFrom(DamageSource.lightningBolt, laserDamage[tier]);
				}
			}
		}
	}
	
}
