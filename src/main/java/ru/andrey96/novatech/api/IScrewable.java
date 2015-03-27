package ru.andrey96.novatech.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;

public interface IScrewable {
	
	/**
	 * Method that called when player uses Screwdriver item on Block/TileEntity
	 * To use it, just implement IScrewable in Block/TileEntity class
	 * @param stack stack player used to screw
	 * @param player player who screwed
	 * @param world world where screwing happened
	 * @param pos screwed block's position
	 * @param side screwed side
	 * @param hitX X coordinate of hit vector
	 * @param hitY Y coordinate of hit vector
	 * @param hitZ Z coordinate of hit vector
	 * @return true if screwing has been handled and game must stop any further interaction with block like opening it's GUI
	 */
	public boolean onScrewed(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ);
	
}
