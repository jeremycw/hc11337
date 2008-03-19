package ca.hc11337.app.core.hardwaremodel;

import java.util.*;

public class UnsignedNumber implements Cloneable {
	protected int val;
	protected int bytes;
	protected int maxVal;
	protected Vector<Integer> byteArray = new Vector<Integer>();
	protected boolean overflow = false;
	protected boolean tcOverflow = false;
	
	public UnsignedNumber(int value, int bytes)
	{
		this.bytes = bytes;
		
		int maxMul = 0;
		for(int i = 0; i <= bytes; i++)
		{
			maxMul += 0x10^i;
		}
		maxVal = maxMul * 0xF;
		
		if(value > maxVal)
			this.val = maxVal;
		
		int power = 2 * bytes;
		for(int i = 0; i < bytes; i++)
		{
			byteArray.add(val/(0x10^power));
			power -= 2;
		}
		byteArray.add(val%0x100);
	}
	
	public void inc()
	{
		overflow = false;
		tcOverflow = false;
		
		int ival = val;
		val++;
		if(val > maxVal)
		{
			val = 0;
			overflow = true;
		}
		if(ival < 128 && val >= 128)
			tcOverflow = true;
	}
	
	public void dec()
	{
		overflow = false;
		tcOverflow = false;
		
		int ival = val;
		val--;
		if(val < 0)
		{
			val = maxVal;
			overflow = true;
		}
		if(ival < 128 && val >= 128)
			tcOverflow = true;
	}
	
	public void sub(int a)
	{
		overflow = false;
		tcOverflow = false;
		
		int ival = val;
		val -= a;
		if(val < 0)
		{
			val += maxVal + 1;
			overflow = true;
		}
		if(ival < 128 && val >= 128)
			tcOverflow = true;
	}
	
	public void sub(UnsignedNumber a)
	{
		overflow = false;
		tcOverflow = false;
		
		int ival = val;
		val -= a.getVal();
		if(val < 0)
		{
			val += maxVal + 1;
			overflow = true;
		}
		if(ival < 128 && val >= 128)
			tcOverflow = true;
	}
	
	public void add(int a)
	{
		overflow = false;
		tcOverflow = false;
		
		int ival = val;
		val += a;
		int overFlow = val - maxVal;
		if(overFlow > 0)
		{
			val = overFlow - 1;
			overflow = true;
		}
		if(ival < 128 && val >= 128)
			tcOverflow = true;
	}
	
	public void add(UnsignedNumber a)
	{
		overflow = false;
		tcOverflow = false;
		
		int ival = val;
		val += a.getVal();
		int overFlow = val - maxVal;
		if(overFlow > 0)
		{
			val = overFlow - 1;
			overflow = true;
		}
		if(ival < 128 && val >= 128)
			tcOverflow = true;
	}
	
	public void mul(UnsignedNumber a)
	{
		overflow = false;
		tcOverflow = false;
		
		int ival = val;
		val *= a.getVal();
		if(val > maxVal)
		{
			val = maxVal;
			overflow = true;
		}
		if(ival < 128 && val >= 128)
			tcOverflow = true;
	}
	
	public void mul(int a)
	{
		overflow = false;
		tcOverflow = false;
		
		int ival = val;
		val *= a;
		if(val > maxVal)
		{
			val = maxVal;
			overflow = true;
		}
		if(ival < 128 && val >= 128)
			tcOverflow = true;
	}
	
	public void setVal(int val)
	{
		if(val <= maxVal)
			this.val = val;
		else
			this.val = maxVal;
	}
	
	public int getVal()
	{
		return val;
	}
	
	public boolean overflow()
	{
		return tcOverflow;
	}
	
	public UnsignedNumber clone()
	{
		return new UnsignedNumber(this.val, this.bytes);
	}
	
	public void setBytes(int bytes)
	{
		this.bytes = bytes;
	}
	
	public int getBytes()
	{
		return bytes;
	}
	
	public int getByte(int index)
	{
		return byteArray.get(index);
	}
}
