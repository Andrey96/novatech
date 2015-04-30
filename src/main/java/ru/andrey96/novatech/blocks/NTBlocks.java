package ru.andrey96.novatech.blocks;

import ru.andrey96.novatech.blocks.machines.BlockCrusher;
import ru.andrey96.novatech.blocks.machines.BlockFurnace;

public class NTBlocks {
	
	public final NTBlock furnace, crusher;
	public final INTBlock[] blocks;
	
	private final boolean isClient;
	
	public NTBlocks(boolean isClient){
		this.isClient = isClient;
		blocks = new INTBlock[2];
		blocks[0] = furnace = new BlockFurnace("efurnace");
		blocks[1] = crusher = new BlockCrusher("crusher");
	}
	
	public void init(){
		if(isClient){
			for(INTBlock block : blocks)
				block.registerModels();
		}
	}
	
}
