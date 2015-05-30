package ru.andrey96.novatech.blocks;

import ru.andrey96.novatech.blocks.machines.BlockCrusher;
import ru.andrey96.novatech.blocks.machines.BlockEFurnace;
import ru.andrey96.novatech.blocks.tubes.BlockTube;
import ru.andrey96.novatech.tileentities.tubes.EnergyTubeTE;

public class NTBlocks {
	
	public final NTBlock efurnace, crusher, energyTube;
	public final INTBlock[] blocks;
	
	private final boolean isClient;
	
	public NTBlocks(boolean isClient){
		this.isClient = isClient;
		blocks = new INTBlock[3];
		blocks[0] = efurnace = new BlockEFurnace("efurnace");
		blocks[1] = crusher = new BlockCrusher("crusher");
		blocks[2] = energyTube = new BlockTube("energy_tube", EnergyTubeTE.class);
	}
	
	public void init(){
		if(isClient){
			for(INTBlock block : blocks)
				block.registerModels();
		}
	}
	
}
