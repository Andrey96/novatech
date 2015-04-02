package ru.andrey96.novatech.items;

import ru.andrey96.novatech.NTUtils;
import ru.andrey96.novatech.NovaTech;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class NTItem extends Item implements INTItem{

	private final String name;
	private final int variants;
	
	public NTItem(String name, int variants){
		this.name = name;
		this.variants = variants;
		if(variants>1){
			this.setHasSubtypes(true);
			this.setMaxDamage(0);
			if(NTUtils.isClient()){
				String[] varr = new String[variants];
				for(int i=0; i<variants; i++){
					varr[i] = NovaTech.MODID+":"+name+i;
				}
				ModelBakery.addVariantName(this, varr);
			}
		}
		this.setUnlocalizedName(name);
		this.setCreativeTab(NovaTech.creativeTab);
		GameRegistry.registerItem(this, name);
	}
	
	public NTItem(String name) {
		this(name, 1);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerModels() {
		if(variants>1){
			for(int i=0; i<variants; i++){
				Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(this, i, new ModelResourceLocation(NovaTech.MODID+":"+this.getName()+i, "inventory"));
			}
		}else{
			Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(this, 0, new ModelResourceLocation(NovaTech.MODID+":"+this.getName(), "inventory"));
		}
	}
	
}
