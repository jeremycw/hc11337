/*This file is part of HC11337.

    HC11337 is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    HC11337 is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with HC11337.  If not, see <http://www.gnu.org/licenses/>.
*/

package ca.hc11337.app.core.hardwaremodel;

import java.util.*;

public class UnsignedNumber implements Cloneable {
	protected int val;
	protected int bytes;
	protected int maxVal;
	protected int[] byteArray;
	protected boolean overflow = false;
	protected boolean tcOverflow = false;
	
	public UnsignedNumber(int value, int bytes)
	{
		this.bytes = bytes;
		byteArray = new int[bytes];
		
		int power = bytes * 2;
		maxVal = (int)(Math.pow(16.0, (double)power) - 1.0);
		
		if(value > maxVal)
			val = maxVal;
		else
			val = value;
		
		constructByteArray();
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
		
		constructByteArray();
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
		
		constructByteArray();
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
		
		constructByteArray();
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
		
		constructByteArray();
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
		
		constructByteArray();
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
		
		constructByteArray();
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
		
		constructByteArray();
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
		
		constructByteArray();
	}
	
	public void setVal(int val)
	{
		if(val <= maxVal)
			this.val = val;
		else
			this.val = maxVal;
		
		constructByteArray();
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
		return byteArray[index];
	}
	
	private void constructByteArray()
	{
		String hexString = Integer.toHexString(val);
		if(hexString.length() % 2 == 1)
			hexString = "0"+hexString;
		for(int index = 0; index < hexString.length(); index +=2)
		{
			byteArray[index/2] = Integer.parseInt(hexString.substring(index, index+2), 16);
		}
	}
}
