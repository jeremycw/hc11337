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

public class HardwareInterface
{
	private Memory mem = new Memory();
	private CPU cpu = new CPU(mem);
	private String[] regNames = new String[]{"A", "B", "D", "X", "Y", "PC", "SP"};
	
	public void setReg(int reg, int val)
	{
		UnsignedNumber a;
		if(reg < 2)
			a = new UnsignedNumber(val,1);
		else
			a = new UnsignedNumber(val,2);
		cpu.setReg(reg, a);
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
		cpu.executeInstruction();
	}
	
	public String[] getRegNames()
	{
		return regNames;
	}
}
