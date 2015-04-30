package ru.andrey96.novatech.energynet;

import java.util.concurrent.ConcurrentHashMap;

import net.minecraft.util.BlockPos;

public class ENetFactory extends Thread{
	
	private ConcurrentHashMap<BlockPos, EnergyNet> queue = new ConcurrentHashMap<>();
	
	public ENetFactory(){
		super("ENetFactory");
	}
	
	@Override
	public void run() {
		
	}
	
}
