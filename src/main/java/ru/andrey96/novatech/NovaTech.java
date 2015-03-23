package ru.andrey96.novatech;

import ru.andrey96.novatech.blocks.NTBlocks;
import ru.andrey96.novatech.items.NTItems;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid="novatech")
public class NovaTech {
	
	@Instance(value = "novatech")
	public static NovaTech instance;
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		NTConfig.init();
		NTBlocks.init();
		NTItems.init();
    }
	
}
