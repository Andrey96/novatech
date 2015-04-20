package ru.andrey96.novatech.network;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;

public abstract class AbstractNTPacket {

	public abstract byte getPacketId();
	public void readData(ByteBuf payload) {}
	public ByteBuf writeData() {
		return  Unpooled.buffer().writeByte(this.getPacketId());
	}
	public abstract void handle(EntityPlayer player);
	
}
