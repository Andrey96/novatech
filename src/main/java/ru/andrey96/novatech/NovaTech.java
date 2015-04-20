package ru.andrey96.novatech;

import java.io.IOException;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.Level;

import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.ClassPath;

import ru.andrey96.novatech.blocks.NTBlocks;
import ru.andrey96.novatech.client.ClientEventHandler;
import ru.andrey96.novatech.client.KeyBindings;
import ru.andrey96.novatech.integration.AbstractNTModule;
import ru.andrey96.novatech.items.NTItems;
import ru.andrey96.novatech.network.ChannelHanlderClient;
import ru.andrey96.novatech.network.ChannelHanlderServer;
import ru.andrey96.novatech.recipes.NTRecipes;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.FMLLog;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.relauncher.Side;

@Mod(modid=NovaTech.MODID, dependencies = "after:DynamicLights")
public class NovaTech {
	
	public static final String MODID = "novatech";
	
	@Instance(value = MODID)
	public static NovaTech instance;
	
	private NTItems items;
	private NTBlocks blocks;
	private final NTConfig config = new NTConfig();
	private final List<AbstractNTModule> modules = new ArrayList<>();
	
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
		NTUtils.preInit(isClient);
		config.init();
		blocks = new NTBlocks(isClient);
		items = new NTItems(isClient);
		try { //Loading integration modules (integrates NovaTech with other mods)
			ImmutableSet<ClassPath.ClassInfo> cmap = ClassPath.from(this.getClass().getClassLoader()).getTopLevelClasses("ru.andrey96.novatech.integration");
			for(ClassPath.ClassInfo ci : cmap){
				try{
					Class cl = ci.load();
					if(!Modifier.isAbstract(cl.getModifiers())){
						AbstractNTModule module = (AbstractNTModule)cl.getConstructor().newInstance();
						if(Loader.isModLoaded(module.getDependentModId())){
			                module.preInit(event);
			                modules.add(module);
		                }
					}
				}catch(Throwable th){
					FMLLog.log(Level.ERROR, "Failed to load module %s!", ci.getSimpleName());
				}
            }
			FMLLog.log(Level.INFO, "Succesfully loaded %d modules", modules.size());
		} catch (Exception ex) {
			FMLLog.log(Level.FATAL, "Failed to fetch module classes!");
			ex.printStackTrace();
		}
    }
	
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		NTUtils.init();
		blocks.init();
		items.init();
		NTRecipes.init(items, blocks);
		if(event.getSide()==Side.CLIENT){
			MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
			FMLCommonHandler.instance().bus().register(new KeyBindings());
		}
		NetworkRegistry.INSTANCE.newChannel("NovaTS", new ChannelHanlderClient());
		NetworkRegistry.INSTANCE.newChannel("NovaTC", new ChannelHanlderServer());
		for(AbstractNTModule module : modules)
			module.init(event);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		for(AbstractNTModule module : modules)
			module.postInit(event);
	}
	
}
