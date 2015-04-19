package ru.andrey96.novatech.items;

import ru.andrey96.novatech.NovaTech;
import ru.andrey96.novatech.items.energy.ItemBatpack;
import ru.andrey96.novatech.items.energy.ItemBattery;
import ru.andrey96.novatech.items.tools.ItemFlashLight;
import ru.andrey96.novatech.items.tools.ItemLaserGun;
import ru.andrey96.novatech.items.tools.ItemScrewdriver;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class NTItems {
	
	public final NTItem screwdr, battery, batpack, lasergun, flashl;
	public final INTItem[] items;
	
	private final boolean isClient;
	
	public NTItems(boolean isClient) {
		this.isClient = isClient;
		items = new INTItem[5];
		items[0] = screwdr = new ItemScrewdriver("screwdr");
		items[1] = battery = new ItemBattery("battery");
		items[2] = batpack = new ItemBatpack("batpack");
		items[3] = lasergun = new ItemLaserGun("lasergun");
		items[4] = flashl = new ItemFlashLight("flashl");
	}
	
	public void init() {
		if(isClient){
			for(INTItem item : items)
				item.registerModels();
		}
	}
	
}
