package ru.andrey96.novatech.tileentities;

import ru.andrey96.novatech.tileentities.machines.FurnaceTE;
import net.minecraft.tileentity.TileEntity;

public class NTTileEntities {
	
	public static void init(){
		TileEntity.addMapping(FurnaceTE.class, "EFurnace");
	}
	
}
