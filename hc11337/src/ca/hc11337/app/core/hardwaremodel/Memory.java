package ca.hc11337.app.core.hardwaremodel;

import java.util.*;

public class Memory {
	private Vector<UnsignedNumber> mem = new Vector<UnsignedNumber>();
	
	public Memory()
	{
		for(int i = 0; i <= 65535; i++)
		{
			mem.add(new UnsignedNumber(0,1));
		}
	}
	
	public UnsignedNumber read(int address)
	{
		return (UnsignedNumber)mem.get(address);
	}
	
	
	public UnsignedNumber read(UnsignedNumber address)
	{
		return mem.get(address.getVal());
	}
	
	public void write(UnsignedNumber address, UnsignedNumber val)
	{
		for(int i = 0; i < val.getBytes(); i++)
		{
			UnsignedNumber piece = new UnsignedNumber(val.getByte(i), 1);
			mem.set(address.getVal(), piece);
			address.inc();
		}
	}
	
	public void write(int address, UnsignedNumber val)
	{
		for(int i = 0; i < val.getBytes(); i++)
		{
			UnsignedNumber piece = new UnsignedNumber(val.getByte(i), 1);
			mem.set(address, piece);
			address++;
		}
	}
}
