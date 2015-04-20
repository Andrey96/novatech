package ru.andrey96.novatech.network.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;

import java.io.IOException;

import ru.andrey96.novatech.client.ClientEventHandler;
import ru.andrey96.novatech.items.tools.ItemLaserGun.LaserShot;
import ru.andrey96.novatech.network.AbstractNTPacket;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.INetHandler;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;

public class PacketS00LaserShot extends AbstractNTPacket{

	private EntityPlayer shooter;
	private byte tier;
	
	public PacketS00LaserShot(){}
	
	public PacketS00LaserShot(EntityPlayer shooter, byte tier) {
		this.shooter = shooter;
		this.tier = tier;
	}
	
	@Override
	public byte getPacketId() {
		return 0x00;
	}
	
	@Override
	public void readData(ByteBuf payload) {
		shooter = (EntityPlayer)Minecraft.getMinecraft().theWorld.getEntityByID(payload.readInt());
		tier = payload.readByte();
	}

	@Override
	public ByteBuf writeData() {
		return super.writeData().writeInt(shooter.getEntityId()).writeByte(tier);
	}

	@Override
	public void handle(EntityPlayer player) {
		new LaserShot(shooter, tier).process();
	}

}
