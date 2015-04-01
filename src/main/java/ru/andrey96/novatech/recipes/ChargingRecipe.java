package ru.andrey96.novatech.recipes;

import java.util.ArrayList;
import java.util.List;

import ru.andrey96.novatech.EnergyUtils;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ShapelessRecipes;

public class ChargingRecipe extends ShapelessRecipes{

	private static List getInputList(Item it, Item source){
		List lst = new ArrayList();
		lst.add(new ItemStack(source, 1, 32767));
		lst.add(new ItemStack(it, 1, 32767));
		return lst;
	}
	
	private final Item item;
	
	public ChargingRecipe(Item item, Item energySource) {
		super(new ItemStack(item), getInputList(item, energySource));
		this.item = item;
	}

	@Override
	public ItemStack[] getRemainingItems(InventoryCrafting inv)
    {
		ItemStack from = null, to = null;
        ItemStack[] items = new ItemStack[inv.getSizeInventory()];
        for (int i = 0; i < items.length; ++i)
        {
            ItemStack ist = inv.getStackInSlot(i);
            if(ist!=null){
            	if(ist.getItem()==item){
            		to = ist;
        		}else{
        			from = ist;
        			items[i] = ist;
        		}
        	}
        }
		EnergyUtils.transferPower(from, to);
        return items;
    }
	
	@Override
	public ItemStack getCraftingResult(InventoryCrafting inv)
    {
		for (int i = 0; i < inv.getSizeInventory(); ++i)
        {
			ItemStack ist = inv.getStackInSlot(i);
			if(ist!=null && ist.getItem()==item)
				return ist;
        }
		return null;
    }
	
}
