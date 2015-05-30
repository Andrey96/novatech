package ru.andrey96.novatech.api;

import ru.andrey96.novatech.energynet.EnergyNet;

public interface IEnergyTE {
	
	public static enum NodeType{
		RECEIVER, PROVIDER, BOTH;
	}
	
	/**
	 * @return this node's type. Is it a receiver, provider or both
	 */
	public NodeType getNodeType();
	/**
	 * Called when this tile being connected to the new energy net
	 * @param net is a EnergyNet this tile just connected to
	 */
	public void onConnect(EnergyNet net);
	/**
	 * Called when this tile being disconnected from energy net. Used to clear neighbor lists
	 */
	public void onDisconnect();
	
	public long getMaxCharge();
	public long getCharge();
	public void setCharge(long charge);
	public long modifyCharge(long delta, boolean force);
	
}
