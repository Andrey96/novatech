package ru.andrey96.novatech.items;

import ru.andrey96.novatech.NovaTech;
import ru.andrey96.novatech.items.armor.ItemPoweredArmor;
import ru.andrey96.novatech.items.armor.NTItemArmor;
import ru.andrey96.novatech.items.energy.ItemBatpack;
import ru.andrey96.novatech.items.energy.ItemBattery;
import ru.andrey96.novatech.items.tools.ItemFlashLight;
import ru.andrey96.novatech.items.tools.ItemLaserGun;
import ru.andrey96.novatech.items.tools.ItemScrewdriver;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor.ArmorMaterial;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class NTItems {
	
	public final ArmorMaterial poweredArmorMaterial;
	public final NTItem screwdr, battery, batpack, lasergun, flashl;
	public final NTItemArmor powerHelmet, powerChestplate, powerLeggings, powerBoots;
	public final INTItem[] items;
	
	private final boolean isClient;
	
	public NTItems(boolean isClient) {
		this.isClient = isClient;
		poweredArmorMaterial = EnumHelper.addArmorMaterial("POWERED", "NovaTech:power_armor", -1, new int[]{ 2, 4, 3, 2 }, 0);
		items = new INTItem[9];
		items[0] = screwdr = new ItemScrewdriver("screwdr");
		items[1] = battery = new ItemBattery("battery");
		items[2] = batpack = new ItemBatpack("batpack");
		items[3] = lasergun = new ItemLaserGun("lasergun");
		items[4] = flashl = new ItemFlashLight("flashl");
		items[5] = powerHelmet = new ItemPoweredArmor("power_helmet", poweredArmorMaterial, 0);
		items[6] = powerChestplate = new ItemPoweredArmor("power_chestplate", poweredArmorMaterial, 1);
		items[7] = powerLeggings = new ItemPoweredArmor("power_leggings", poweredArmorMaterial, 2);
		items[8] = powerBoots = new ItemPoweredArmor("power_boots", poweredArmorMaterial, 3);
	}
	
	public void init() {
		if(isClient){
			for(INTItem item : items)
				item.registerModels();
		}
	}
	
}
