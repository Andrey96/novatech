package ru.andrey96.novatech.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.PacketBuffer;

public abstract class AbstractNTPacket {

	public abstract void readData(ByteBuf payload);
	public abstract ByteBuf writeData();
	public abstract void handle(EntityPlayer player);
	
}
