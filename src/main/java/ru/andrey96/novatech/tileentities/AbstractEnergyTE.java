package ru.andrey96.novatech.tileentities;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import ru.andrey96.novatech.api.IEnergyTE;
import ru.andrey96.novatech.energynet.EnergyNet;

public abstract class AbstractEnergyTE extends TileEntity implements IEnergyTE{
	
	public EnergyNet energyNet = null;
	private long charge = 0;

	@Override
	public void onConnect(EnergyNet net) {
		this.energyNet = net;
	}

	@Override
	public void onDisconnect() {
		this.energyNet = null;
	}

	@Override
	public long getCharge() {
		return charge;
	}

	@Override
	public void setCharge(long charge) {
		this.charge = charge;
	}

	@Override
	public long modifyCharge(long delta, boolean force) {
		if(delta==0) return 0;
		long newCharge = getCharge()+delta;
		if(newCharge<=0){
			if(force || newCharge==0)
				setCharge(0);
			return newCharge;
		}
		long max = getMaxCharge();
		if(newCharge>=max){
			if(force || newCharge==max)
				setCharge(max);
			return newCharge-max;
		}
		setCharge(newCharge);
		return 0;
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.charge = compound.getLong("charge");
	}
	
	@Override
	public void writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setLong("charge", this.charge);
	}
	
}
