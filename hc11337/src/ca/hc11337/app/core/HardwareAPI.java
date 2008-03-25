package ca.hc11337.app.core;

import ca.hc11337.app.core.hardwaremodel.*;

public class HardwareAPI {
	private HardwareInterface hardware = new HardwareInterface();
	
	public String[] getRegisterNames()
	{
		return hardware.getRegNames();
	}
}
