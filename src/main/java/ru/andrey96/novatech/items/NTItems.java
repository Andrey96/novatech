package ru.andrey96.novatech.items;

import ru.andrey96.novatech.NovaTech;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class NTItems {
	
	private static boolean isClient;
	public static Item battery, batpack, screwdr;
	
	public static void init(boolean argIsClient){
		isClient = argIsClient;
		battery = register(new ItemBattery(), "battery", 4);
		batpack = register(new ItemBatpack(), "batpack", 4);
		screwdr = register(new ItemScrewdriver(), "screwdr", 1);
	}
	
	private static Item register(Item it, String name, int variants){
		GameRegistry.registerItem(it, name, NovaTech.MODID);
		it.setUnlocalizedName(name);
		if(isClient){
			it.setCreativeTab(NovaTech.creativeTab);
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(it, 0, new ModelResourceLocation(NovaTech.MODID+":"+name, "inventory"));
			if(variants>1){
				String[] varr = new String[variants];
				for(int i=0; i<variants; i++){
					varr[i] = NovaTech.MODID+":"+name+i;
				}
				ModelBakery.addVariantName(it, varr);
			}
		}
		return it;
	}
	
}
