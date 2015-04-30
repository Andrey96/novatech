package ru.andrey96.novatech.tileentities;

import java.util.HashSet;

import ru.andrey96.novatech.api.IEnergyNode;
import ru.andrey96.novatech.energynet.EnergyNet;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;

public abstract class AbstractEnergyTE extends TileEntity implements IEnergyNode{

	private EnergyNet energyNet = null;
	private final HashSet<IEnergyNode> receivers = new HashSet<>(), providers = new HashSet<>();
	private long charge = 0;
	
	@Override
	public void onConnect(EnergyNet net) {
		energyNet = net;
	}

	@Override
	public void onDisconnect() {
		energyNet = null;
		receivers.clear();
		providers.clear();
	}

	@Override
	public void addNeighbor(IEnergyNode neighbor) {
		if(neighbor.getNodeType()==NodeType.PROVIDER){
			providers.add(neighbor);
		}else if(neighbor.getNodeType()==NodeType.RECEIVER){
			receivers.add(neighbor);
		}else{
			providers.add(neighbor);
			receivers.add(neighbor);
		}
	}

	@Override
	public void removeNeighbor(IEnergyNode neighbor) {
		if(neighbor.getNodeType()==NodeType.PROVIDER){
			providers.remove(neighbor);
		}else if(neighbor.getNodeType()==NodeType.RECEIVER){
			receivers.remove(neighbor);
		}else{
			providers.remove(neighbor);
			receivers.remove(neighbor);
		}
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
