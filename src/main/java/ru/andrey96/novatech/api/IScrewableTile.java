package ru.andrey96.novatech.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;

public interface IScrewableTile {
	
	public void onScrewed(EntityPlayer player, BlockPos pos);
	
}
