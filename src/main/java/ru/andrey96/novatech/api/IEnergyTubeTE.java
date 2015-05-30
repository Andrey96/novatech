package ru.andrey96.novatech.api;

import ru.andrey96.novatech.energynet.EnergyNet;

public interface IEnergyTubeTE {
	
	public void setNet(EnergyNet net);
	public long getConductivity();
	
}
