package ru.andrey96.novatech.network;

import java.util.List;

import ru.andrey96.novatech.network.client.*;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.ChannelHandler.Sharable;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.NetHandlerPlayServer;
import net.minecraft.network.Packet;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S3FPacketCustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.fml.common.network.internal.FMLProxyPacket;

@Sharable
public class ChannelHanlderServer extends SimpleChannelInboundHandler<FMLProxyPacket> {

	@Override
	protected void channelRead0(ChannelHandlerContext ctx, FMLProxyPacket msg) throws Exception {
		ByteBuf payload = msg.payload();
		AbstractNTPacket packet = getClientPacket(payload.readByte());
		packet.readData(new PacketBuffer(payload));
		packet.handle(((NetHandlerPlayServer)msg.getOrigin().getNetHandler()).playerEntity);
	}

	private AbstractNTPacket getClientPacket(byte id) throws Exception{
		switch(id){
			
			default:
				throw new Exception("Server packet "+Integer.toHexString(Byte.toUnsignedInt(id))+" not found");
		}
	}
	
	public static void sendPacket(EntityPlayerMP playerTo, AbstractNTPacket packet){
		playerTo.playerNetServerHandler.sendPacket(new S3FPacketCustomPayload("NovaTS", new PacketBuffer(packet.writeData())));
	}
	
	public static void broadcastPacket(AbstractNTPacket packet){
		for(EntityPlayerMP player : (List<EntityPlayerMP>)MinecraftServer.getServer().getConfigurationManager().playerEntityList)
			sendPacket(player, packet);
	}
	
	public static void broadcastPacket(AbstractNTPacket packet, EntityPlayer except){
		for(EntityPlayerMP player : (List<EntityPlayerMP>)MinecraftServer.getServer().getConfigurationManager().playerEntityList)
			if(player!=except)
				sendPacket(player, packet);
	}
	
	public static void broadcastPacket(AbstractNTPacket packet, int dimension){
		for(EntityPlayerMP player : (List<EntityPlayerMP>)MinecraftServer.getServer().getConfigurationManager().playerEntityList)
			if(player.dimension==dimension)
				sendPacket(player, packet);
	}
	
	public static void broadcastPacket(AbstractNTPacket packet, int dimension, EntityPlayer except){
		for(EntityPlayerMP player : (List<EntityPlayerMP>)MinecraftServer.getServer().getConfigurationManager().playerEntityList)
			if(player!=except && player.dimension==dimension)
				sendPacket(player, packet);
	}
	
	public static void broadcastPacketRange(AbstractNTPacket packet, int dimension, double x, double y, double z, double radius){
		for(EntityPlayerMP player : (List<EntityPlayerMP>)MinecraftServer.getServer().getConfigurationManager().playerEntityList){
			if(player.dimension==dimension){
				double d1 = x - player.posX;
                double d2 = y - player.posY;
                double d3 = z - player.posZ;
                if (d1 * d1 + d2 * d2 + d3 * d3 < radius * radius)
                    sendPacket(player, packet);
			}
		}
	}
	
	public static void broadcastPacketRange(AbstractNTPacket packet, int dimension, double x, double y, double z, double radius, EntityPlayer except){
		for(EntityPlayerMP player : (List<EntityPlayerMP>)MinecraftServer.getServer().getConfigurationManager().playerEntityList){
			if(player!=except && player.dimension==dimension){
				double d1 = x - player.posX;
                double d2 = y - player.posY;
                double d3 = z - player.posZ;
                if (d1 * d1 + d2 * d2 + d3 * d3 < radius * radius)
                    sendPacket(player, packet);
			}
		}
	}
	
	public static void broadcastPacketRange(AbstractNTPacket packet, Entity near, double radius){
		broadcastPacketRange(packet, near.worldObj.provider.getDimensionId(), near.posX, near.posY, near.posZ, radius);
	}
	
	public static void broadcastPacketRange(AbstractNTPacket packet, Entity near, double radius, EntityPlayer except){
		broadcastPacketRange(packet, near.worldObj.provider.getDimensionId(), near.posX, near.posY, near.posZ, radius, except);
	}
	
}
