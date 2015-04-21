package ru.andrey96.novatech;

import java.lang.reflect.Field;
import java.util.List;

import ru.andrey96.novatech.api.IEnergyItem;
import ru.andrey96.novatech.api.IStateableItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Timer;
import net.minecraft.util.Vec3;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A bunch of different utility methods
 */
public class NTUtils {
	
	@SideOnly(Side.CLIENT)
	private static Timer timer;
	@SideOnly(Side.CLIENT)
	private static Field equippedProgress;
	@SideOnly(Side.CLIENT)
	private static ItemRenderer itemRenderer;
	private static boolean client;
	final protected static char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	static void preInit(boolean isClient) {
		client = isClient;
		if(isClient){
			timer = (Timer)getFieldVal(Minecraft.getMinecraft(), "field_71428_T", "timer");
		}
	}
	
	static void init() {
		if(client){
			itemRenderer = Minecraft.getMinecraft().getItemRenderer();
			equippedProgress = getPrivateField(ItemRenderer.class, "field_78454_c", "equippedProgress");
		}
	}
	
	/**
	 * @return is mod running on client (true) or on dedicated server (false)
	 */
	public static boolean isClient() {
		return client;
	}
	
	/**
	 * Gets private field from given class via reflection
	 * @param c Class to get field from
	 * @param obfName obfuscated field name
	 * @param deobfName development field name
	 */
	public static Field getPrivateField(Class c, String obfName, String deobfName) {
		Field f;
		try{
			f = c.getDeclaredField(obfName);
		}catch(Exception ex){
			try{
				f = c.getDeclaredField(deobfName);
			}catch(Exception ex2){
				FMLLog.severe("Can't find %s(%s) field in %s class! Stopping the game.", deobfName, obfName, c.getSimpleName());
				ex2.printStackTrace();
				FMLCommonHandler.instance().exitJava(0, false);
				return null;
			}
		}
		try{
			f.setAccessible(true);
			return f;
		}catch(Exception ex){
			FMLLog.severe("Can't access %s(%s) field in %s class! Stopping the game.", deobfName, obfName, c.getSimpleName());
			ex.printStackTrace();
			FMLCommonHandler.instance().exitJava(0, false);
			return null;
		}
	}
	
	/**
	 * Gets field value from obj's class via reflection
	 * @param obj object to get field from
	 * @param obfName obfuscated field name
	 * @param deobfName development field name
	 */
	public static Object getFieldVal(Object obj, String obfName, String deobfName) {
		Field f;
		Class c = obj.getClass();
		try{
			f = c.getDeclaredField(obfName);
		}catch(Exception ex){
			try{
				f = Minecraft.class.getDeclaredField(deobfName);
			}catch(Exception ex2){
				FMLLog.severe("Can't find %s(%s) field in %s class! Stopping the game.", deobfName, obfName, c.getSimpleName());
				ex2.printStackTrace();
				FMLCommonHandler.instance().exitJava(0, false);
				return null;
			}
		}
		try{
			f.setAccessible(true);
			return f.get(obj);
		}catch(Exception ex){
			FMLLog.severe("Can't access %s(%s) field in %s class! Stopping the game.", deobfName, obfName, c.getSimpleName());
			ex.printStackTrace();
			FMLCommonHandler.instance().exitJava(0, false);
			return null;
		}
	}
	
	/**
	 * @return render partial ticks
	 */
	@SideOnly(Side.CLIENT)
	public static float getPartialTicks() {
		return timer.renderPartialTicks;
	}
	
	@SideOnly(Side.CLIENT)
	public static void resetEquipAnimation() {
		try {
			equippedProgress.set(itemRenderer, 1f);
		} catch (Exception ex) {
			FMLLog.severe("Can't set equippedProgress field's value in ItemRenderer class.");
			ex.printStackTrace();
			FMLCommonHandler.instance().exitJava(0, false);
		}
	}
	
	/**
	 * Performs a ray cast over blocks and entities
	 * @param entity entity that points
	 * @param distance scan distance
	 * @param air should function return air's block pos intstead of null if nothing found
	 * @return Object, entity is pointing to or null (air block's pos if air is true) if entity is pointing to air
	 */
	public static MovingObjectPosition rayTrace(Entity entity, double distance, boolean air) {
		//Tracing blocks
		Vec3 vec3 = getEyesPos(entity);
        Vec3 look = entity.getLook(1f);
        Vec3 trace = vec3.addVector(look.xCoord * distance, look.yCoord * distance, look.zCoord * distance);
        MovingObjectPosition objectMouseOver = entity.worldObj.rayTraceBlocks(vec3, trace, false, true, true);
        
        //Tracing entities
        double d1 = distance;
        if (objectMouseOver != null)
            d1 = objectMouseOver.hitVec.distanceTo(vec3);
        Vec3 vec31 = entity.getLook(1f);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * distance, vec31.yCoord * distance, vec31.zCoord * distance);
        Entity pointedEntity = null;
        Vec3 vec33 = null;
        float f1 = 1.0F;
        List list = entity.worldObj.getEntitiesWithinAABBExcludingEntity(entity, entity.getEntityBoundingBox().addCoord(vec31.xCoord * distance, vec31.yCoord * distance, vec31.zCoord * distance).expand((double)f1, (double)f1, (double)f1));
        double d2 = d1;
        for (int i = 0; i < list.size(); ++i){
            Entity entity1 = (Entity)list.get(i);
            if (entity1.canBeCollidedWith()){
                float f2 = entity1.getCollisionBorderSize();
                AxisAlignedBB axisalignedbb = entity1.getEntityBoundingBox().expand((double)f2, (double)f2, (double)f2);
                MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);
                if (axisalignedbb.isVecInside(vec3)){
                    if (0.0D < d2 || d2 == 0.0D){
                        pointedEntity = entity1;
                        vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
                        d2 = 0.0D;
                    }
                }else if (movingobjectposition != null){
                    double d3 = vec3.distanceTo(movingobjectposition.hitVec);
                    if (d3 < d2 || d2 == 0.0D){
                        if (entity1 == entity.ridingEntity && !entity.canRiderInteract()){
                            if (d2 == 0.0D){
                                pointedEntity = entity1;
                                vec33 = movingobjectposition.hitVec;
                            }
                        }else{
                            pointedEntity = entity1;
                            vec33 = movingobjectposition.hitVec;
                            d2 = d3;
                        }
                    }
                }
            }
        }
        
        if (pointedEntity != null && (d2 < d1 || objectMouseOver == null))
            return new MovingObjectPosition(pointedEntity, vec33);
        if(objectMouseOver == null && air)
        	return new MovingObjectPosition(trace, null, new BlockPos(trace));
        return objectMouseOver;
	}
    
	/**
	 * Returns entities eye's position vector
	 * Method is here because Entity.getEyesPosition() is client-side
	 * @param ent entity to calculate position for
	 * @return Vec3 with eye's coordinates
	 */
	public static Vec3 getEyesPos(Entity ent) {
		return new Vec3(ent.posX, ent.posY + ent.getEyeHeight(), ent.posZ);
	}
	
	public static String byteToHexString(byte b) {
		int v = b & 0xFF;
		return new String(new char[]{ hexArray[v >>> 4], hexArray[v & 0x0F] });
	}
	
	@SideOnly(Side.CLIENT)
	public static void chatMessage(String text){
		Minecraft.getMinecraft().ingameGUI.getChatGUI().printChatMessage(new ChatComponentText(text));
	}
	
}
