package ru.andrey96.novatech.client;

import java.util.HashMap;

import ru.andrey96.novatech.items.tools.ItemLaserGun;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MovingObjectPosition;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientEventHandler {
	
	private static final HashMap<EntityPlayer, ItemLaserGun.LaserShot> laserShooters = new HashMap<>();
	
	private final Minecraft mc;
	
	public ClientEventHandler() {
		this.mc = Minecraft.getMinecraft();
	}
	
	public static void addLaserShot(ItemLaserGun.LaserShot shot) {
		laserShooters.put(shot.player, shot);
	}
	
	@SubscribeEvent
	public void onPostRenderPlayer(RenderPlayerEvent.Post event) {
		EntityPlayer player = event.entityPlayer;
		if(player!=null){
			ItemLaserGun.LaserShot shot = laserShooters.get(player);
			if(shot!=null){
				if(--shot.renderFramesLeft==0)
					laserShooters.remove(player);
				
			}
		}
	}
	
	/*@SubscribeEvent
	public void onClientTick(ClientTickEvent event) {
		if(event.phase==Phase.START && mc.thePlayer!=null && mc.theWorld!=null){
			
		}
	}*/
	
}
