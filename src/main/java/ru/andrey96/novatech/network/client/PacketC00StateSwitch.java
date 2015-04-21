package ru.andrey96.novatech.network.client;

import org.apache.logging.log4j.Level;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLLog;
import ru.andrey96.novatech.api.IStateableItem;
import ru.andrey96.novatech.network.AbstractNTPacket;

public class PacketC00StateSwitch extends AbstractNTPacket{

	private byte slot;
	
	public PacketC00StateSwitch(){}
	
	public PacketC00StateSwitch(byte slot){
		this.slot = slot;
	}
	
	@Override
	public byte getPacketId() {
		return 0x00;
	}
	
	@Override
	public void readData(ByteBuf payload) {
		slot = payload.readByte();
	}
	
	@Override
	public ByteBuf writeData() {
		return super.writeData().writeByte(slot);
	}
	
	@Override
	public void handle(EntityPlayer player) {
		ItemStack ist = null;
		if(slot==-1)
			ist = player.getCurrentEquippedItem();
		else if(slot>-1 && slot<4)
			ist = player.getCurrentArmor(slot);
		if(ist!=null && ist.getItem() instanceof IStateableItem){
			IStateableItem it = (IStateableItem)ist.getItem();
			if(it.canSwitchState(ist, player))
				it.onStateSwitch(ist, player);
		}
		else
			FMLLog.log(Level.WARN, "[NovaTech] Wrong StateSwitch packet!");
	}
	
}
