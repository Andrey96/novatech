package ru.andrey96.novatech.blocks.machines;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import ru.andrey96.novatech.blocks.NTBlock;

public class BlockMachine extends NTBlock {

	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool WORKING = PropertyBool.create("working");
	
	public BlockMachine(String name) {
		super(name, Material.iron);
	}
	
	@Override
	public boolean onBlockActivated(World worldIn, BlockPos pos, IBlockState state, EntityPlayer playerIn, EnumFacing side, float hitX, float hitY, float hitZ)
    {
		//Just for BlockStates test
        worldIn.setBlockState(pos, state.withProperty(WORKING, !((boolean)state.getValue(WORKING))));
        return true;
    }
	
	@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
    {
        return super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer).withProperty(FACING, placer.getHorizontalFacing().rotateY()).withProperty(WORKING, false);
    }
	
	@Override
	public IBlockState getStateFromMeta(int meta)
    {
        return this.getDefaultState().withProperty(FACING, EnumFacing.getHorizontal(meta & 3)).withProperty(WORKING, ((meta & 4) >> 2) == 1);
    }
	
	@Override
	public int getMetaFromState(IBlockState state)
    {
        byte b0 = 0;
        int i = b0 | ((EnumFacing)state.getValue(FACING)).getHorizontalIndex();
        i |= ((boolean)state.getValue(WORKING)?1:0) << 2;
        return i;
    }
	
	@Override
	protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {FACING, WORKING});
    }
	
}
