package ru.andrey96.novatech.network;

import ru.andrey96.novatech.NTUtils;
import ru.andrey96.novatech.network.server.*;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.client.C17PacketCustomPayload;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;

@Sharable
public class ChannelHanlderClient extends SimpleChannelInboundHandler<FMLProxyPacket> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FMLProxyPacket msg) throws Exception {
		ByteBuf payload = msg.payload();
		AbstractNTPacket packet = getServerPacket(payload.readByte());
		packet.readData(payload);
		packet.handle(Minecraft.getMinecraft().thePlayer);
	}
	
	private AbstractNTPacket getServerPacket(byte id) throws Exception{
		switch(id){
			case 0x00:
				return new PacketS00LaserShot();
			default:
				throw new Exception("Server packet "+NTUtils.byteToHexString(id)+" not found");
		}
	}
	
	public static void sendPacket(AbstractNTPacket packet){
		Minecraft.getMinecraft().getNetHandler().addToSendQueue(new C17PacketCustomPayload("NovaTC", new PacketBuffer(packet.writeData())));
	}
}
