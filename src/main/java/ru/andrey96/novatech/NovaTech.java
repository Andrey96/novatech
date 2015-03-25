package ru.andrey96.novatech;

import org.apache.commons.lang3.StringEscapeUtils;

import ru.andrey96.novatech.blocks.NTBlocks;
import ru.andrey96.novatech.items.NTItems;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid=NovaTech.MODID)
public class NovaTech {
	
	public static final String MODID = "novatech";
	
	@Instance(value = MODID)
	public static NovaTech instance;
	
	public static final CreativeTabs creativeTab = new CreativeTabs(MODID){
		@Override
		public Item getTabIconItem() {
			return NTItems.screwdr;
		}
	};
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		NTConfig.init();
    }
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		NTBlocks.init(event.getSide()==Side.CLIENT);
		NTItems.init(event.getSide()==Side.CLIENT);
	}
	
}
