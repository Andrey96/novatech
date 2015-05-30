package ru.andrey96.novatech.blocks.machines;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import ru.andrey96.novatech.api.IScrewable;
import ru.andrey96.novatech.blocks.NTBlock;

public abstract class BlockMachine extends NTBlock implements IScrewable {

	public static final PropertyDirection FACING = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL);
	public static final PropertyBool WORKING = PropertyBool.create("working");
	
	public BlockMachine(String name) {
		super(name, Material.iron);
	}
	
	@Override
	public boolean onScrewed(ItemStack stack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
		world.setBlockState(pos, world.getBlockState(pos).withProperty(FACING, player.getHorizontalFacing().rotateY()));
		return true;
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
        return ((EnumFacing)state.getValue(FACING)).getHorizontalIndex()
        |(((boolean)state.getValue(WORKING)?1:0) << 2);
    }
	
	@Override
	protected BlockState createBlockState()
    {
        return new BlockState(this, new IProperty[] {FACING, WORKING});
    }
	
	@Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, BlockPos pos, IBlockState state, Random rand) {
		if((boolean)state.getValue(WORKING)){
			double x = pos.getX();
            double y = pos.getY()+1+rand.nextFloat()/3;
            double z = pos.getZ();
            EnumFacing f = (EnumFacing)state.getValue(FACING);
            if(f.getAxis()==Axis.X){
            	x+=rand.nextFloat();
            	if(f.getAxisDirection()==AxisDirection.POSITIVE)
            		z+=rand.nextFloat()/3;
            	else
            		z+=1-rand.nextFloat()/3;
            }else{
            	z+=rand.nextFloat();
            	if(f.getAxisDirection()==AxisDirection.POSITIVE)
            		x+=1-rand.nextFloat()/3;
            	else
            		x+=rand.nextFloat()/3;
            }
            world.spawnParticle(getMachineParticle(), x, y, z, 0, 0, 0);
		}
	}
	
	@SideOnly(Side.CLIENT)
	abstract EnumParticleTypes getMachineParticle();
	
}
