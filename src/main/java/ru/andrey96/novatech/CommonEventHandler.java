package ru.andrey96.novatech;

import ru.andrey96.novatech.api.IUpdatingArmor;
import net.minecraft.item.ItemStack;
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
	
}
