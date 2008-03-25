package ca.hc11337.app.core;

import java.util.*;

public class CPUManager {
	private int[] registerValues;
	private String[]registerNames;
	
	public void setRegisterValues(int[] values)
	{
		registerValues = values;
	}
	
	public int[] getRegisterValues()
	{
		return registerValues;
	}
	
	
	public void setRegisterNames(String[] names)
	{
		registerNames = names;
	}
	
	public String[] getRegisterNames()
	{
		return registerNames;
	}
	
	
	public void setRegisterValue(int index, int value)
	{
		registerValues[index] = value;
	}
	
	public int getRegisterValue(int index)
	{
		return registerValues[index];
	}

}
