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
	
	/**
	 * 
	 * @param value Value to give the unsigned number
	 * @param bytes The number of bytes to use to hold this value
	 */
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
	
	public UnsignedNumber(UnsignedNumber... bytes)
	{
		this.bytes = bytes.length;
		byteArray = new int[bytes.length];
		String val = "";
		for(int i = 0; i < bytes.length; i++){
			byteArray[i] = bytes[i].getVal();
			String byteString = Integer.toHexString(bytes[i].getVal());
			if(byteString.length() == 1)
				val += "0"+byteString;
			else
				val += byteString;
		}

		int value = Integer.parseInt(val, 16);
		
		int power = this.bytes * 2;
		maxVal = (int)(Math.pow(16.0, (double)power) - 1.0);
		
		if(value > maxVal)
			this.val = maxVal;
		else
			this.val = value;
	}
	
	/**
	 * Increment unsigned number
	 */
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
		if(ival < maxVal+1/2 && val >= maxVal+1/2)
			tcOverflow = true;
		
		constructByteArray();
	}
	
	/**
	 * Decrement unsigned number
	 */
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
		if(ival < maxVal+1/2 && val >= maxVal+1/2)
			tcOverflow = true;
		
		constructByteArray();
	}
	
	/**
	 * Subtract from unsigned number
	 * 
	 * @param a value to subtract
	 */
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
		if(ival < maxVal+1/2 && val >= maxVal+1/2)
			tcOverflow = true;
		
		constructByteArray();
	}
	
	/**
	 * Subtract from unsigned number
	 * 
	 * @param a value to subtract
	 */
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
		if(ival < maxVal+1/2 && val >= maxVal+1/2)
			tcOverflow = true;
		
		constructByteArray();
	}
	
	/**
	 * Add to unsigned number
	 * 
	 * @param a Value to add
	 */
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
		if(ival < maxVal+1/2 && val >= maxVal+1/2)
			tcOverflow = true;
		
		constructByteArray();
	}
	
	/**
	 * Add to unsigned number
	 * 
	 * @param a Value to add
	 */
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
		if(ival < maxVal+1/2 && val >= maxVal+1/2)
			tcOverflow = true;
		
		constructByteArray();
	}
	
	/**
	 * Multiply unsigned number
	 * 
	 * @param a Value to multiply by
	 */
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
		if(ival < maxVal+1/2 && val >= maxVal+1/2)
			tcOverflow = true;
		
		constructByteArray();
	}
	
	/**
	 * Multiply unsigned number
	 * 
	 * @param a Value to multiply by
	 */
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
		if(ival < maxVal+1/2 && val >= maxVal+1/2)
			tcOverflow = true;
		
		constructByteArray();
	}
	
	/**
	 * Set the value of the unsigned number
	 * 
	 * @param val
	 */
	public void setVal(int val)
	{
		if(val <= maxVal)
			this.val = val;
		else
			this.val = maxVal;
		
		constructByteArray();
	}
	
	/**
	 * Get the value of the unsigned number
	 * 
	 * @return value
	 */
	public int getVal()
	{
		return val;
	}
	
	/**
	 * Check if there has been overflow
	 * 
	 * @return
	 */
	public boolean overflow()
	{
		return tcOverflow;
	}
	
	/**
	 * Returns the carry bit
	 * 
	 * @return Carry bit
	 */
	public boolean carry()
	{
		return overflow;
	}
	
	/**
	 * Duplicate you unsigned number
	 */
	public UnsignedNumber clone()
	{
		return new UnsignedNumber(this.val, this.bytes);
	}
	
	/**
	 * Set the size of the unsigned number in bytes
	 * 
	 * @param bytes
	 */
	public void setBytes(int bytes)
	{
		this.bytes = bytes;
		int power = bytes * 2;
		maxVal = (int)(Math.pow(16.0, (double)power) - 1.0);
		byteArray = new int[bytes];
		constructByteArray();
	}
	
	/**
	 * Get the amount of bytes
	 * 
	 * @return Number of bytes
	 */
	public int getBytes()
	{
		return bytes;
	}
	
	/**
	 * Get the byte at the given index
	 * 
	 * @param index
	 * @return Byte
	 */
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
