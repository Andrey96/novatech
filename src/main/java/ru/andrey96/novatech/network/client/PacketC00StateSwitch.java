package ru.andrey96.novatech.network.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import ru.andrey96.novatech.api.IStateableItem;
import ru.andrey96.novatech.network.AbstractNTPacket;

public class PacketC00StateSwitch extends AbstractNTPacket{

	@Override
	public byte getPacketId() {
		return 0x00;
	}
	
	@Override
	public void handle(EntityPlayer player) {
		ItemStack ist = player.getCurrentEquippedItem();
		if(ist!=null && ist.getItem() instanceof IStateableItem)
			((IStateableItem)ist.getItem()).onStateSwitch(ist, player);
	}
	
}
