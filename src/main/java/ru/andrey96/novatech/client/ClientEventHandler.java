package ru.andrey96.novatech.client;

import java.util.HashMap;

import org.lwjgl.opengl.GL11;

import com.ibm.icu.impl.duration.impl.Utils;

import ru.andrey96.novatech.NTUtils;
import ru.andrey96.novatech.NovaTech;
import ru.andrey96.novatech.items.tools.ItemLaserGun;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class ClientEventHandler {
	
	private static final HashMap<EntityPlayer, ItemLaserGun.LaserShot> laserShots = new HashMap<>();
	private static final ResourceLocation textureLaserRay = new ResourceLocation(NovaTech.MODID, "textures/misc/laser_ray.png");
	
	private final Minecraft mc;
	
	public ClientEventHandler() {
		this.mc = Minecraft.getMinecraft();
	}
	
	public static void addLaserShot(ItemLaserGun.LaserShot shot) {
		laserShots.put(shot.player, shot);
	}
	
	private static void addRayVertexesWithUV(WorldRenderer wr, double len, double thic)
	{
		double texRepeat = len/thic;
        
        //| /
        //| 
        wr.addVertexWithUV(-thic,     0,     0, 0, 0);//1           start
        wr.addVertexWithUV(    0,  thic,     0, 1, 0);//2           start
        wr.addVertexWithUV(    0,  thic,   len, 1, texRepeat);//2     end
        wr.addVertexWithUV(-thic,     0,   len, 0, texRepeat);//1     end
        
        //| /\
        //| 
        wr.addVertexWithUV(    0,  thic,     0, 0, 0);//1
        wr.addVertexWithUV( thic,     0,     0, 1, 0);//2
        wr.addVertexWithUV( thic,     0,   len, 1, texRepeat);//2
        wr.addVertexWithUV(    0,  thic,   len, 0, texRepeat);//1
        
        //| /\
        //|  /
        wr.addVertexWithUV( thic,     0,     0, 0, 0);//1
        wr.addVertexWithUV(    0, -thic,     0, 1, 0);//2
        wr.addVertexWithUV(    0, -thic,   len, 1, texRepeat);//2
        wr.addVertexWithUV( thic,     0,   len, 0, texRepeat);//1
        
        //| /\
        //| \/
        wr.addVertexWithUV(    0, -thic,     0, 0, 0);//1
        wr.addVertexWithUV(-thic,     0,     0, 1, 0);//2
        wr.addVertexWithUV(-thic,     0,   len, 1, texRepeat);//2
        wr.addVertexWithUV(    0, -thic,   len, 0, texRepeat);//1
	}
	
	@SubscribeEvent
	public void onPreRenderEntity(RenderLivingEvent.Pre event) {
		if(event.entity!=null && event.entity instanceof EntityPlayer){
			EntityPlayer player = (EntityPlayer)event.entity;
			if(player!=null){
				ItemLaserGun.LaserShot shot = laserShots.get(player);
				if(shot!=null){
					if(--shot.renderFramesLeft==0)
						laserShots.remove(player);
					
					Tessellator tes = Tessellator.getInstance();
		            WorldRenderer wr = tes.getWorldRenderer();
		            
		            this.mc.getTextureManager().bindTexture(textureLaserRay);
		            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
	                GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
	                
	                GlStateManager.disableLighting();
	                GlStateManager.disableCull();
	                GlStateManager.disableBlend();
	                GlStateManager.pushMatrix();
	                wr.startDrawingQuads();
	                
	                addRayVertexesWithUV(wr, shot.rayLength, .02);
	                Vec3 start = player.getPositionEyes(NTUtils.getPartialTicks()).subtract(EntityFX.interpPosX, EntityFX.interpPosY+.8, EntityFX.interpPosZ);
	                GL11.glTranslated(start.xCoord, start.yCoord, start.zCoord);
	                GL11.glRotatef(360-player.rotationYaw, 0, 1, 0);
	                GL11.glTranslatef(-.5f, 0, .5f);
	                GL11.glRotatef(player.rotationPitch, 1, 0, 0);
	                
	                tes.draw();
	                GlStateManager.popMatrix();
	                GlStateManager.enableLighting();
	                GlStateManager.enableCull();
	                GlStateManager.enableBlend();
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onRenderHand(RenderHandEvent event) {
		if(mc.gameSettings.thirdPersonView!=0) return;
		EntityPlayer player = mc.thePlayer;
		ItemLaserGun.LaserShot shot = laserShots.get(player);
		if(shot!=null) {
			if(--shot.renderFramesLeft==0)
				laserShots.remove(player);
			
			Tessellator tes = Tessellator.getInstance();
            WorldRenderer wr = tes.getWorldRenderer();
            
            this.mc.getTextureManager().bindTexture(textureLaserRay);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
            
            GlStateManager.disableLighting();
            GlStateManager.disableCull();
            GlStateManager.disableBlend();
            GlStateManager.pushMatrix();
            wr.startDrawingQuads();
            
            addRayVertexesWithUV(wr, shot.rayLength+.25, .02);
            Vec3 start = player.getPositionEyes(event.partialTicks).subtract(EntityFX.interpPosX, EntityFX.interpPosY+.2, EntityFX.interpPosZ);
            GL11.glTranslated(start.xCoord, start.yCoord, start.zCoord);
            GL11.glRotatef(360-player.rotationYaw, 0, 1, 0);
            GL11.glTranslatef(-.2f, 0, -.2f);
            GL11.glRotatef(player.rotationPitch, 1, 0, 0);
            
            tes.draw();
            GlStateManager.popMatrix();
            GlStateManager.enableLighting();
            GlStateManager.enableCull();
            GlStateManager.enableBlend();
		}
	}
	
}
