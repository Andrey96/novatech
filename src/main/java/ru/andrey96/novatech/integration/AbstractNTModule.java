package ru.andrey96.novatech.integration;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;


public abstract class AbstractNTModule {
	
	public abstract String getDependentModId();
	public void preInit(FMLPreInitializationEvent event){}
	public void init(FMLInitializationEvent event){}
	public void postInit(FMLPostInitializationEvent event){}
	
}
