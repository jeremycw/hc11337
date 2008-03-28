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

public class CPU
{
	private Memory mem;
	private boolean ccr[] = new boolean[7];
	private Vector<UnsignedNumber> registers = new Vector<UnsignedNumber>();
	private Hashtable<Byte, Instruction> instructionSet = new Hashtable<Byte, Instruction>();
	
	public CPU(Memory m)
	{
		mem = m;
		//initialize registers
		registers.add(new UnsignedNumber(0,1));
		registers.add(new UnsignedNumber(0,1));
		registers.add(new UnsignedNumber(0,2));
		registers.add(new UnsignedNumber(0,2));
		registers.add(new UnsignedNumber(0,2));
		registers.add(new UnsignedNumber(0,2));
		registers.add(new UnsignedNumber(0,2));
		//initialize instructionSet
	}
	
	public void setReg(int reg, UnsignedNumber val)
	{
		registers.set(reg, val);
	}
	
	public UnsignedNumber getReg(int reg)
	{
		return registers.get(reg);
	}
	
	public void setCC(int code, boolean val)
	{
		ccr[code] = val;
	}
	
	public boolean getCC(int code)
	{
		return ccr[code];
	}
	
	public void executeInstruction()
	{
		UnsignedNumber pc = registers.get(Reg.PC);
		UnsignedNumber opcode = mem.read(pc).clone();
		Instruction currentInstruction;
		switch(opcode.getVal())
		{
		case 0x18:
			pc.inc();
			opcode.mul(0x100);
			opcode.add(mem.read(pc));
			break;
		case 0x1A:
			pc.inc();
			opcode.mul(0x100);
			opcode.add(mem.read(pc));
			break;
		case 0xCD:
			pc.inc();
			opcode.mul(0x100);
			opcode.add(mem.read(pc));
			break;
		default:
			
		}
		currentInstruction = instructionSet.get(opcode);
		currentInstruction.exec();
	}
}
