package ru.andrey96.novatech.integration;

import java.util.HashMap;

import ru.andrey96.novatech.NTUtils;
import ru.andrey96.novatech.items.armor.ItemPoweredArmor;
import ru.andrey96.novatech.items.tools.ItemFlashLight;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import atomicstryker.dynamiclights.client.DynamicLights;
import atomicstryker.dynamiclights.client.IDynamicLightSource;

@SideOnly(Side.CLIENT)
public class NTDynamicLights extends AbstractNTModule{
	
	public static class DummyEntity extends Entity{

		public DummyEntity(World worldIn) {
			super(worldIn);
		}

		@Override
		protected void entityInit() {}

		@Override
		protected void readEntityFromNBT(NBTTagCompound tagCompund) {}

		@Override
		protected void writeEntityToNBT(NBTTagCompound tagCompound) {}
		
	}
	
	public static class HelmetAdapter implements IDynamicLightSource{

		public final Entity dummy;
		public Entity point;
		
		public HelmetAdapter(World world){
			point = dummy = new DummyEntity(world);
		}
		
		@Override
		public Entity getAttachmentEntity() {
			return point;
		}

		@Override
		public int getLightLevel() {
			return 15;
		}
		
	}
	
	public static class FlashLightAdapter implements IDynamicLightSource{
		
		public final Entity dummy;
		public Entity point;
		public boolean state;
		
		public FlashLightAdapter(World world){
			point = dummy = new DummyEntity(world);
		}
		
		@Override
		public Entity getAttachmentEntity() {
			return point;
		}

		@Override
		public int getLightLevel() {
			return ItemFlashLight.getLightLevel(state);
		}
		
	}
	
	private Minecraft mc;
	private HashMap<EntityPlayer, FlashLightAdapter> map;
	private HashMap<EntityPlayer, HelmetAdapter> map2;
	
	@Override
	public String getDependentModId() {
		return "DynamicLights";
	}
	
	@Override
	public void init(FMLInitializationEvent event) {
		MinecraftForge.EVENT_BUS.register(this);
		mc = Minecraft.getMinecraft();
		map = new HashMap<EntityPlayer, FlashLightAdapter>();
		map2 = new HashMap<EntityPlayer, HelmetAdapter>();
	}
	
	@SubscribeEvent
	public void onPreRenderEntity(RenderLivingEvent.Pre event) {
		if(event.entity!=null && event.entity instanceof EntityPlayer)
			handleFlashlight((EntityPlayer)event.entity);
	}
	
	@SubscribeEvent
	public void onRenderHand(RenderHandEvent event) {
		if(mc.gameSettings.thirdPersonView==0)
			handleFlashlight((EntityPlayer)mc.thePlayer);
	}
	
	private void handleFlashlight(EntityPlayer player){
		//Equipped tool
		ItemStack ist = player.getCurrentEquippedItem();
		if(ist!=null && ist.getItem() instanceof ItemFlashLight && ist.hasTagCompound() && ist.getTagCompound().getBoolean("flashl_on")){
			FlashLightAdapter adapter = map.get(player);
			if(adapter == null){
				adapter = new FlashLightAdapter(player.worldObj);
				DynamicLights.addLightSource(adapter);
				map.put(player, adapter);
			}
			adapter.state = ist.getTagCompound().getBoolean("flashl_p");
			MovingObjectPosition mop = NTUtils.rayTrace(player, ItemFlashLight.lightRange, true);
			if(mop.typeOfHit == MovingObjectType.BLOCK){
				adapter.point = adapter.dummy;
				BlockPos pos = mop.getBlockPos();
				adapter.dummy.setPosition(pos.getX()+.5, pos.getY()+.5, pos.getZ()+.5);
			}else{
				adapter.point = mop.entityHit;
			}
		}else if(map.containsKey(player)){
			DynamicLights.removeLightSource(map.remove(player));
		}
		//Helmet
		ist = player.getCurrentArmor(3);
		if(ist!=null && ist.getItem() instanceof ItemPoweredArmor && ist.hasTagCompound() && ((ItemPoweredArmor)ist.getItem()).armorType==0){
			byte mode = ist.getTagCompound().getByte("flashl_mode");
			if(mode!=0){
				HelmetAdapter adapter = map2.get(player);
				if(adapter == null){
					adapter = new HelmetAdapter(player.worldObj);
					DynamicLights.addLightSource(adapter);
					map2.put(player, adapter);
				}
				if(mode==1){ //Floodlight
					adapter.point = player;
				}else{ //Spotlight
					MovingObjectPosition mop = NTUtils.rayTrace(player, ItemFlashLight.lightRange, true);
					if(mop.typeOfHit == MovingObjectType.BLOCK){
						adapter.point = adapter.dummy;
						BlockPos pos = mop.getBlockPos();
						adapter.dummy.setPosition(pos.getX()+.5, pos.getY()+.5, pos.getZ()+.5);
					}else{
						adapter.point = mop.entityHit;
					}
				}
			}else if(map2.containsKey(player)){
				DynamicLights.removeLightSource(map2.remove(player));
			}
		}else if(map2.containsKey(player)){
			DynamicLights.removeLightSource(map2.remove(player));
		}
	}
	
}
