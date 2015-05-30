package ru.andrey96.novatech.energynet;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

import ru.andrey96.novatech.api.IEnergyTE;
import ru.andrey96.novatech.api.IEnergyTubeTE;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;

public class ENetFactory{
	
	public static final ArrayList<EnergyNet> nets = new ArrayList<>();
	
	public static void onWireAdded(BlockPos pos, IEnergyTubeTE tile) {
		
	}
	
	public static void onTileAdded(BlockPos pos, IEnergyTE tile) {
		
	}
	
}
