package ru.andrey96.novatech.client;

import org.lwjgl.input.Keyboard;

import ru.andrey96.novatech.NTUtils;
import ru.andrey96.novatech.api.IStateableItem;
import ru.andrey96.novatech.network.ChannelHanlderClient;
import ru.andrey96.novatech.network.client.PacketC00StateSwitch;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class KeyBindings {
	
	public static KeyBindings instance = null;
	public final KeyBinding stateswitch;
	private final Minecraft mc;
	private boolean stateswitchDown = false, stateswitchClear = false;
	
	public KeyBindings() {
		instance = this;
		mc = Minecraft.getMinecraft();
		ClientRegistry.registerKeyBinding(stateswitch = new KeyBinding("nt.key.stateswitch", Keyboard.KEY_P, "key.categories.novat"));
	}
	
	@SubscribeEvent
	public void onKeyboardInput(KeyInputEvent event){
		if(mc.thePlayer!=null && mc.currentScreen==null){
			if(stateswitch.isKeyDown()){
				if(!stateswitchDown){
					stateswitchDown=true;
					stateswitchClear=true;
				}
				KeyBinding[] hotbar = mc.gameSettings.keyBindsHotbar;
				for(byte i=0; i<4; ++i){
					if(hotbar[i].isPressed()){
						switchArmorState(i);
						stateswitchClear=false;
					}
				}
			}else if(stateswitchDown){
				stateswitchDown=false;
				if(stateswitchClear){
					ItemStack ist = mc.thePlayer.getCurrentEquippedItem();
					if(ist!=null && ist.getItem() instanceof IStateableItem){
						IStateableItem it = (IStateableItem)ist.getItem();
						if(it.canSwitchState(ist, mc.thePlayer)){
							it.onStateSwitch(ist, mc.thePlayer);
							String state = it.getStateName(ist, mc.thePlayer, false);
							ChannelHanlderClient.sendPacket(new PacketC00StateSwitch((byte)-1));
							if(state!=null)
								NTUtils.chatMessage(I18n.format("message.state", I18n.format(state)));
						}else{
							NTUtils.chatMessage(I18n.format("message.state_not_switched"));
						}
					}
				}
			}
		}
	}
	
	private void switchArmorState(byte id){
		byte armorSlot = (byte)(3-id);
		ItemStack ist = mc.thePlayer.getCurrentArmor(armorSlot);
		if(ist!=null && ist.getItem() instanceof IStateableItem){
			IStateableItem it = (IStateableItem)ist.getItem();
			if(it.canSwitchState(ist, mc.thePlayer)){
				it.onStateSwitch(ist, mc.thePlayer);
				String state = it.getStateName(ist, mc.thePlayer, false);
				ChannelHanlderClient.sendPacket(new PacketC00StateSwitch(armorSlot));
				if(state!=null)
					NTUtils.chatMessage(I18n.format("message.state"+id, I18n.format(state)));
			}else{
				NTUtils.chatMessage(I18n.format("message.state_not_switched"));
			}
		}
	}
	
}
