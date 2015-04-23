package ru.andrey96.novatech.network.client;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import ru.andrey96.novatech.NTUtils;
import ru.andrey96.novatech.items.armor.ItemPoweredArmor;
import ru.andrey96.novatech.network.AbstractNTPacket;

public class PacketC01BootsSprint extends AbstractNTPacket{

	private boolean start;
	
	public PacketC01BootsSprint(){}
	
	public PacketC01BootsSprint(boolean start) {
		this.start = start;
	}
	
	@Override
	public byte getPacketId() {
		return 0x01;
	}

	@Override
	public void readData(ByteBuf payload) {
		start = payload.readBoolean();
	}
	
	@Override
	public ByteBuf writeData() {
		return super.writeData().writeBoolean(start);
	}
	
	@Override
	public void handle(EntityPlayer player) {
		IAttributeInstance attrib = player.getEntityAttribute(SharedMonsterAttributes.movementSpeed);
		if (attrib.getModifier(NTUtils.bootsSprintModUUID)!=null)
            attrib.removeModifier(NTUtils.bootsSprintMod);
		if(start){
			ItemStack ist = player.getCurrentArmor(1); //Leggings
			if(ist!=null && ist.hasTagCompound() && ist.getItem() instanceof ItemPoweredArmor){
				if(ist.getTagCompound().getBoolean("leggings_p")){
					attrib.applyModifier(NTUtils.bootsSprintMod);
				}
			}
		}
	}

}
