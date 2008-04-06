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

public class Memory {
	private Vector<UnsignedNumber> mem = new Vector<UnsignedNumber>();
	private Vector<Integer> changedAddresses = new Vector<Integer>();
	
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
			changedAddresses.add(address.getVal());
			address.inc();
		}
	}
	
	public void write(int address, UnsignedNumber val)
	{
		for(int i = 0; i < val.getBytes(); i++)
		{
			UnsignedNumber piece = new UnsignedNumber(val.getByte(i), 1);
			mem.set(address, piece);
			changedAddresses.add(address);
			address++;
		}
	}
	
	public void clearUpdatedAddresses()
	{
		changedAddresses.clear();
	}
	
	public int[] getUpdatedAddresses()
	{
		int[] addr = new int[changedAddresses.size()];
		for(int i = 0; i < changedAddresses.size(); i++)
			addr[i] = changedAddresses.get(i);
		return addr;
	}
}
