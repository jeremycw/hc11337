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

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map.Entry;

import ca.hc11337.app.core.HardwareAPI;

public class HardwareInterface implements HardwareAPI
{
	private Memory mem = new Memory();
	private CPU cpu = new CPU(mem);
	private String[] regNames = new String[]{"A", "B", "D", "X", "Y", "PC", "SP"};
	
	public void setRegister(int reg, int val)
	{
		UnsignedNumber a;
		if(reg < 2)
			a = new UnsignedNumber(val,1);
		else
			a = new UnsignedNumber(val,2);
		cpu.setReg(reg, a);
	}
	
	public int[] getRegisterValues()
	{
		int[] values = new int[regNames.length];
		for(int i = 0; i < regNames.length; i++)
			values[i] = cpu.getReg(i).getVal();
		return values;
	}
	
	public int getReg(int reg)
	{
		return cpu.getReg(reg).getVal();
	}
	
	public void setCC(int code, boolean val)
	{
		cpu.setCC(code, val);
	}
	
	public boolean getCC(int code)
	{
		return getCC(code);
	}
	
	public void execute()
	{
		mem.clearUpdatedAddresses();
		cpu.executeInstruction();
	}
	
	public String[] getRegisterNames()
	{
		return regNames;
	}
	
	public void setMemoryAt(int index, int value)
	{
		mem.write(index, new UnsignedNumber(value, 1));
	}
	
	public int getMemoryAt(int index)
	{
		return mem.read(index).getVal();
	}
	
	public int[] getChangedMemoryValues()
	{
		return mem.getUpdatedAddresses();
	}
	
	public Hashtable<Integer, Integer> getUsedMemory()
	{
		Hashtable<Integer, UnsignedNumber> dump = mem.getMemTable();
		Hashtable<Integer, Integer> intDump = new Hashtable<Integer, Integer>();
		Iterator<Entry<Integer, UnsignedNumber>> itr = dump.entrySet().iterator();
		while(itr.hasNext())
		{
			Entry<Integer, UnsignedNumber> keyVal = itr.next();
			intDump.put(keyVal.getKey(), keyVal.getValue().getVal());
		}
		return intDump;
	}

	public int[] getStack() {
		int sp = cpu.getReg(Reg.SP).getVal();
		int[] stack = new int[(short)Math.abs(sp-65535)+1];
		int count = 0;
		if((short)Math.abs(sp-65535)+1 > 0)
		{
			while(sp <= 65535)
			{
				stack[count] = mem.read(sp).getVal();
				count++;
				sp++;
			}
		}
		return stack;
	}
}
