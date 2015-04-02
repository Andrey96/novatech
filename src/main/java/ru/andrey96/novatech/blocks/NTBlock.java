package ru.andrey96.novatech.blocks;

import ru.andrey96.novatech.NovaTech;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NTBlock extends Block implements INTBlock{

	private final String name;
	
	public NTBlock(String name, Material materialIn) {
		super(materialIn);
		this.name = name;
		this.setUnlocalizedName(name);
		this.setCreativeTab(NovaTech.creativeTab);
		GameRegistry.registerBlock(this, name);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() {
		Item item = GameRegistry.findItem(NovaTech.MODID, this.getName());
		Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation(NovaTech.MODID+":"+this.getName(), "inventory"));
	}

}
