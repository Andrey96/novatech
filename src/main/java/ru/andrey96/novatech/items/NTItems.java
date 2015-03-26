package ru.andrey96.novatech.items;

import ru.andrey96.novatech.NovaTech;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class NTItems {
	
	public final NTItem screwdr, battery, batpack;
	public final INTItem[] items;
	
	private boolean isClient;
	
	public NTItems(boolean isClient) {
		this.isClient = isClient;
		items = new INTItem[3];
		items[0] = screwdr = new ItemScrewdriver("screwdr");
		items[1] = battery = new ItemBattery("battery");
		items[2] = batpack = new ItemBatpack("batpack");
	}
	
	public void init() {
		if(isClient){
			for(INTItem item : items)
				item.registerModels();
		}
	}
	
}
