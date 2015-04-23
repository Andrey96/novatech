package ru.andrey96.novatech.items.tools;

import javax.vecmath.Point3d;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.andrey96.novatech.NTUtils;
import ru.andrey96.novatech.NovaTech;
import ru.andrey96.novatech.client.ClientEventHandler;
import ru.andrey96.novatech.items.NTItem;
import ru.andrey96.novatech.network.ChannelHandlerServer;
import ru.andrey96.novatech.network.server.PacketS00LaserShot;
import ru.andrey96.novatech.recipes.NTRecipes;

public class ItemLaserGun extends ItemEnergyTool{

	@SideOnly(Side.CLIENT)
	private ModelResourceLocation[] usageModels;
	private static final int[] usageDelays = new int[]{ 0, 15, 35, 60 };
	private static final int[] powerConsumption = new int[]{ 400, 600, 1600, 10000 };
	
	public ItemLaserGun(String name) {
		super(name);
		if(NTUtils.isClient()){
			usageModels = new ModelResourceLocation[5];
			String[] varr = new String[6];
			varr[0] = NovaTech.MODID+":"+name;
			for(int i=0; i<5; i++){
				varr[i+1] = NovaTech.MODID+":"+name+"_power"+i;
			}
			varr[5] = NovaTech.MODID+":"+name+"_empty";
			ModelBakery.addVariantName(this, varr);
		}
	}

	@Override
	public long getMaxCharge(ItemStack stack) {
		return 100000;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
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
	@SideOnly(Side.CLIENT)
	public void registerModels() {
		super.registerModels();
		for(int i=0; i<4; i++)
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(this, i+1000, usageModels[i] = new ModelResourceLocation(NovaTech.MODID+":"+this.getName()+"_power"+i, "inventory"));
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(this, 1004, usageModels[4] = new ModelResourceLocation(NovaTech.MODID+":"+this.getName()+"_empty", "inventory"));
	}
	
	public static class LaserShot {
		
		private static final float[] laserReachDistance = new float[]{ 8f, 16f, 32f, 48f };
		private static final float[] laserDamage = new float[]{ 4f, 7f, 10f };
		
		public EntityPlayer player;
		public byte tier;
		@SideOnly(Side.CLIENT)
		public short renderFramesLeft;
		public double rayLength;
		
		public LaserShot(EntityPlayer player, byte tier) {
			this.player = player;
			this.tier = tier;
			if(player.worldObj.isRemote)
				this.renderFramesLeft = (short)(Minecraft.getDebugFPS()/4);
		}
		
		public void process() {
	        MovingObjectPosition obj = NTUtils.rayTrace(player, laserReachDistance[tier], true);
			Vec3 point = obj.hitVec;
			if(player.worldObj.isRemote){
				rayLength = point.subtract(player.getPositionEyes(1f)).lengthVector();
				ClientEventHandler.addLaserShot(this);
			}else{
				ChannelHandlerServer.broadcastPacketRange(new PacketS00LaserShot(player, tier), player, 128, player);
			}
			if(tier==3){
				player.worldObj.createExplosion(player, point.xCoord, point.yCoord, point.zCoord, 8f, true);
			}else if(obj.typeOfHit==MovingObjectType.ENTITY && obj.entityHit!=null){
				obj.entityHit.attackEntityFrom(DamageSource.lightningBolt, laserDamage[tier]);
			}
		}
	}
	
}
