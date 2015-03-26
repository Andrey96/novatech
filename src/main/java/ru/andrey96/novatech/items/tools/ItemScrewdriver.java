package ru.andrey96.novatech.items.tools;

import ru.andrey96.novatech.api.IScrewable;
import ru.andrey96.novatech.items.NTItem;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public class ItemScrewdriver extends NTItem {

	public ItemScrewdriver(String name) {
		super(name);
		this.setMaxStackSize(1);
	}

	@Override
	public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		IBlockState state = world.getBlockState(pos);
		Block block = world.getBlockState(pos).getBlock();
		if(block instanceof IScrewable){
			return ((IScrewable)block).onScrewed(stack, player, world, pos, side, hitX, hitY, hitZ);
		}else if(block.hasTileEntity(state)){
			TileEntity te = world.getTileEntity(pos);
			if(te instanceof IScrewable)
				return ((IScrewable)te).onScrewed(stack, player, world, pos, side, hitX, hitY, hitZ);
		}
		return false;
	}
	
}
