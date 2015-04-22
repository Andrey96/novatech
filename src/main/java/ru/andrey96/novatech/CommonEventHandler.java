package ru.andrey96.novatech;

import ru.andrey96.novatech.api.IUpdatingArmor;
import ru.andrey96.novatech.items.armor.ItemPoweredArmor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

public class CommonEventHandler {
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event) {
		if(event.phase==Phase.END){
			for(int i=0; i<4; ++i){
				ItemStack ist = event.player.getCurrentArmor(i);
				if(ist!=null && ist.getItem() instanceof IUpdatingArmor)
					((IUpdatingArmor)ist.getItem()).onArmorUpdateTick(event.player, ist, i);
			}
		}
	}
	
	@SubscribeEvent
	public void onLivingHurt(LivingHurtEvent event) {
		if(event.entityLiving instanceof EntityPlayer && event.source!=null){
			System.out.println("living hurt");
			EntityPlayer player = (EntityPlayer)event.entityLiving;
			ItemStack helmet = player.getCurrentArmor(3);
			boolean h = helmet!=null && helmet.getItem() instanceof ItemPoweredArmor;
			ItemStack chestplate = player.getCurrentArmor(2);
			boolean c = chestplate!=null && chestplate.getItem() instanceof ItemPoweredArmor;
			ItemStack leggings = player.getCurrentArmor(1);
			boolean l = leggings!=null && leggings.getItem() instanceof ItemPoweredArmor;
			ItemStack boots = player.getCurrentArmor(0);
			boolean b = boots!=null && boots.getItem() instanceof ItemPoweredArmor;
			if(event.source == DamageSource.onFire || event.source == DamageSource.inFire || event.source == DamageSource.lava){
				if(h && c && l && b && chestplate.hasTagCompound()){
					ItemPoweredArmor armor = (ItemPoweredArmor)chestplate.getItem();
					if(armor.modifyCharge(chestplate, (long)(event.ammount*100), true)==0){
						player.extinguish();
						event.ammount=0;
						event.setCanceled(true);
					}
				}
			}else if(event.source == DamageSource.fall && b && boots.hasTagCompound()){
				ItemPoweredArmor armor = (ItemPoweredArmor)chestplate.getItem();
				if(armor.modifyCharge(boots, (long)(event.ammount*80), true)==0){
					event.ammount=0;
					event.setCanceled(true);
				}
			}else if(!event.source.isUnblockable() || (event.source.isMagicDamage() || event.source == DamageSource.wither) && h && l && b){
				if(c && chestplate.hasTagCompound()){
					ItemPoweredArmor armor = (ItemPoweredArmor)chestplate.getItem();
					if(chestplate.getTagCompound().getBoolean("chestplate_p")){
						if(armor.modifyCharge(chestplate, (long)(event.ammount*250), true)==0){
							event.ammount=0;
							event.setCanceled(true);
						}
					}else{
						if(armor.modifyCharge(chestplate, (long)(event.ammount*100), true)==0){
							event.ammount=(int)(event.ammount*.25);
							if(event.ammount==0)
								event.setCanceled(true);
						}
					}
				}
			}
		}
	}
	
}
