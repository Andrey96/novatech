package ru.andrey96.novatech.items.armor;

import ru.andrey96.novatech.NovaTech;
import ru.andrey96.novatech.items.INTItem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class NTItemArmor extends ItemArmor implements INTItem{

	private final String name;
	public final int armorType;
	
	public NTItemArmor(String name, ArmorMaterial material, int renderIndex, int armorType) {
		super(material, renderIndex, armorType);
		this.armorType = armorType;
		this.name = name;
		this.setUnlocalizedName(name);
		this.setCreativeTab(NovaTech.creativeTab);
		GameRegistry.registerItem(this, name);
	}

	@Override
	public String getName() {
		return name;
	}
	
	@Override
	public void registerModels() {
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(this, 0, new ModelResourceLocation(NovaTech.MODID+":"+this.getName(), "inventory"));
	}

}
