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

package ca.hc11337.app.core;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

import ca.hc11337.app.core.hardwaremodel.HardwareInterface;
import ca.hc11337.app.core.hardwaremodel.UnsignedNumber;

public class HardwareAPI {
	private HardwareInterface hardware = new HardwareInterface();
	
	public String[] getRegisterNames()
	{
		return hardware.getRegNames();
	}
	
	public int[] getRegisterValues()
	{
		return hardware.getRegisterValues();
	}
	
	public int[] getChangedMemoryValues()
	{
		return hardware.getMemoryUpdates();
	}
	
	public void setMemoryAt(int index, int value)
	{
		hardware.setMemory(index, value);
	}
	
	public int getMemoryAt(int index)
	{
		return hardware.getMemory(index);
	}
	
	public void setRegister(int index, int value)
	{
		hardware.setReg(index, value);
	}
	
	public void execute()
	{
		hardware.execute();
	}
	
	public Hashtable<Integer, Integer> getUsedMemory()
	{
		return hardware.dumpUsedMem();
	}
}
