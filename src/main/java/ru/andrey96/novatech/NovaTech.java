package ru.andrey96.novatech;

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
	
	private NTItems items;
	private NTBlocks blocks;
	private final NTConfig config = new NTConfig();
	
	public static final CreativeTabs creativeTab = new CreativeTabs(MODID){
		@Override
		public Item getTabIconItem() {
			return NovaTech.instance.items.screwdr;
		}
	};
	
	public NTItems getItems(){
		return items;
	}
	
	public NTBlocks getBlocks(){
		return blocks;
	}
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		boolean isClient = event.getSide()==Side.CLIENT; //If true, render is registered
		config.init();
		blocks = new NTBlocks(isClient);
		items = new NTItems(isClient);
    }
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		blocks.init();
		items.init();
	}
	
}
