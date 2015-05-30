package ru.andrey96.novatech.tileentities.machines;

import ru.andrey96.novatech.tileentities.AbstractEnergyTE;

public class EFurnaceTE extends AbstractEnergyTE{

	@Override
	public NodeType getNodeType() {
		return NodeType.RECEIVER;
	}

	@Override
	public long getMaxCharge() {
		return 10000;
	}
	
	
}
