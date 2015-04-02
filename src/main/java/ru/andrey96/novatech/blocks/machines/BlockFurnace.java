package ru.andrey96.novatech.blocks.machines;

import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockFurnace extends BlockMachine{

	public BlockFurnace(String name) {
		super(name);
	}

	@Override
	@SideOnly(Side.CLIENT)
	EnumParticleTypes getMachineParticle() {
		return EnumParticleTypes.FLAME;
	}
	
}
