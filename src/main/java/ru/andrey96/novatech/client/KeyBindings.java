package ru.andrey96.novatech.client;

import org.lwjgl.input.Keyboard;

import ru.andrey96.novatech.api.IStateableItem;
import ru.andrey96.novatech.network.ChannelHanlderClient;
import ru.andrey96.novatech.network.client.PacketC00StateSwitch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;

public class KeyBindings {
	
	public static KeyBindings instance = null;
	public final KeyBinding stateswitch;
	private final Minecraft mc;
	
	public KeyBindings() {
		instance = this;
		mc = Minecraft.getMinecraft();
		ClientRegistry.registerKeyBinding(stateswitch = new KeyBinding("nt.key.stateswitch", Keyboard.KEY_P, "key.categories.novat"));
	}
	
	@SubscribeEvent
    public void onClientTick(ClientTickEvent event) {
		if(stateswitch.isPressed()){
			if(mc.thePlayer!=null && mc.currentScreen==null){
				ItemStack ist = mc.thePlayer.getCurrentEquippedItem();
				if(ist!=null && ist.getItem() instanceof IStateableItem){
					((IStateableItem)ist.getItem()).onStateSwitch(ist, mc.thePlayer);
					ChannelHanlderClient.sendPacket(new PacketC00StateSwitch());
				}
			}
		}
	}
	
}
