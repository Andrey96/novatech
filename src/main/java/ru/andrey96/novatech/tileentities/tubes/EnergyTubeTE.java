package ru.andrey96.novatech.tileentities.tubes;

import ru.andrey96.novatech.api.IEnergyTubeTE;
import ru.andrey96.novatech.energynet.EnergyNet;

public class EnergyTubeTE extends TubeTE implements IEnergyTubeTE{
	
	public EnergyNet energyNet = null;

	@Override
	public void setNet(EnergyNet net) {
		this.energyNet = net;
	}

	@Override
	public long getConductivity() {
		return 100;
	}
	
}
