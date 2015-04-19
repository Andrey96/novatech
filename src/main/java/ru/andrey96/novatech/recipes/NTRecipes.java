package ru.andrey96.novatech.recipes;

import java.util.ArrayList;
import java.util.List;

import ru.andrey96.novatech.NovaTech;
import ru.andrey96.novatech.blocks.NTBlocks;
import ru.andrey96.novatech.items.NTItems;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;

public class NTRecipes {
	
	public static void addChargeRecipe(Item item) {
		NTItems items = NovaTech.instance.getItems();
		CraftingManager.getInstance().addRecipe(new ChargingRecipe(item, items.battery));
		CraftingManager.getInstance().addRecipe(new ChargingRecipe(item, items.batpack));
	}
	
	public static void init(NTItems items, NTBlocks blocks) {
		addChargeRecipe(items.lasergun);
		addChargeRecipe(items.flashl);
	}
	
}
