package ru.andrey96.novatech.blocks.tubes;

import java.util.HashMap;

import org.apache.logging.log4j.Level;

import net.minecraft.block.Block;
import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLLog;
import ru.andrey96.novatech.NTUtils;
import ru.andrey96.novatech.api.IEnergyTE;
import ru.andrey96.novatech.blocks.NTBlock;

public class BlockTube extends NTBlock implements ITileEntityProvider{

	public static final PropertyBool DOWN = PropertyBool.create("down"), UP = PropertyBool.create("up"), NORTH = PropertyBool.create("north"), SOUTH = PropertyBool.create("south"), WEST = PropertyBool.create("west"), EAST = PropertyBool.create("east");
	private static final PropertyBool[] props = new PropertyBool[]{ DOWN, UP, NORTH, SOUTH, WEST, EAST };
	
	Class<? extends TileEntity> tileEntityClass;
	
	public BlockTube(String name, Class<? extends TileEntity> tileEntityClass) {
		super(name, Material.circuits);
		this.tileEntityClass = tileEntityClass;
		IBlockState state = this.blockState.getBaseState();
		for(PropertyBool p : props)
			state.withProperty(p, false);
		this.setDefaultState(state);
	}
	
	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		try {
			return tileEntityClass.newInstance();
		} catch (Exception ex) {
			FMLLog.log(Level.FATAL, ex, "Can't initialize tube's TileEntity (%s)", tileEntityClass.getSimpleName());
			return null;
		}
	}
	
	@Override
	protected BlockState createBlockState()
    {
        return new BlockState(this, props);
    }
	
	private void updateSides(World worldIn, BlockPos pos, IBlockState state) {
		for(int i=0; i<6; ++i) {
			System.out.printf("updating: %s (%s)\n", props[i].getName(), EnumFacing.VALUES[i].getName());
			EnumFacing f = EnumFacing.VALUES[i];
			BlockPos npos = pos.offset(f);
			IBlockState s = worldIn.getBlockState(npos);
			if(s.getBlock().hasTileEntity(s)){
				TileEntity te = worldIn.getTileEntity(npos);
				if(te instanceof IEnergyTE || te.getClass().isAssignableFrom(tileEntityClass))
					state.withProperty(props[i], true);
				else
					state.withProperty(props[i], false);
			}else
				state.withProperty(props[i], false);
		}
	}
	
	@Override
	public void onBlockAdded(World worldIn, BlockPos pos, IBlockState state) {
		updateSides(worldIn, pos, state);
	}
	
	@Override
	public void onNeighborBlockChange(World worldIn, BlockPos pos, IBlockState state, Block neighborBlock) {
		updateSides(worldIn, pos, state);
	}
	
	/*@Override
	public IBlockState onBlockPlaced(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
		IBlockState state = super.onBlockPlaced(worldIn, pos, facing, hitX, hitY, hitZ, meta, placer);
		for(int i=0; i<6; ++i) {
			EnumFacing f = EnumFacing.VALUES[i];
			BlockPos npos = pos.offset(f);
			IBlockState s = worldIn.getBlockState(npos);
			if(s.getBlock().hasTileEntity(s)){
				TileEntity te = worldIn.getTileEntity(npos);
				if(te instanceof IEnergyTE || te.getClass().isAssignableFrom(tileEntityClass))
					state.withProperty(props[i], true);
			}
		}
		return state;
	}*/
	
	@Override
	public IBlockState getStateFromMeta(int meta)
    {
		IBlockState state = this.getDefaultState();
		for(int i=0; i<6; ++i)
			state.withProperty(props[i], NTUtils.getBool32(meta, i));
		return state;
    }
	
	@Override
	public int getMetaFromState(IBlockState state)
    {
		int meta = 0;
		for(int i=0; i<6; ++i)
			meta = NTUtils.setBool32(meta, i, (boolean)state.getValue(props[i]));
		return meta;
    }
	
}
