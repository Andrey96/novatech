package ru.andrey96.novatech.tileentities;

import ru.andrey96.novatech.tileentities.machines.EFurnaceTE;
import ru.andrey96.novatech.tileentities.tubes.EnergyTubeTE;
import net.minecraft.tileentity.TileEntity;

public class NTTileEntities {
	
	public static void init(){
		TileEntity.addMapping(EFurnaceTE.class, "EFurnace");
		TileEntity.addMapping(EnergyTubeTE.class, "EnergyTube");
	}
	
}
