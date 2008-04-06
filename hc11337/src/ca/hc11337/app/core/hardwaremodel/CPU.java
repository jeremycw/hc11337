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
import java.util.Vector;

import ca.hc11337.app.core.hardwaremodel.instructions.*;


public class CPU
{
	private Memory mem;
	private boolean ccr[] = new boolean[7];
	private Vector<UnsignedNumber> registers = new Vector<UnsignedNumber>();
	private Hashtable<Integer, Instruction> instructionSet = new Hashtable<Integer, Instruction>();
	
	public CPU(Memory m)
	{
		mem = m;
		//initialize registers
		registers.add(new UnsignedNumber(0,1));
		registers.add(new UnsignedNumber(0,1));
		registers.add(new UnsignedNumber(0,2));
		registers.add(new UnsignedNumber(0,2));
		registers.add(new UnsignedNumber(0,2));
		registers.add(new UnsignedNumber(0xC000,2));
		registers.add(new UnsignedNumber(0,2));
		//TODO initialize instructionSet
		instructionSet.put(0x1B, new ABA_1B(this, mem));
		instructionSet.put(0x3A, new ABX_3A(this, mem));
		instructionSet.put(0x183A, new ABY_183A(this, mem));
		instructionSet.put(0x18AB, new ADDA_18AB(this, mem));
		instructionSet.put(0x9B, new ADDA_9B(this, mem));
		instructionSet.put(0x8B, new ADDA_8B(this, mem));
		instructionSet.put(0xAB, new ADDA_AB(this, mem));
		instructionSet.put(0xBB, new ADDA_BB(this, mem));
		instructionSet.put(0x27, new BEQ_27(this, mem));
		instructionSet.put(0x25, new BLO_25(this, mem));
		instructionSet.put(0x23, new BLS_23(this, mem));
		instructionSet.put(0x20, new BRA_20(this, mem));
		instructionSet.put(0x188C, new CPY_188C(this, mem));
		instructionSet.put(0x189C, new CPY_189C(this, mem));
		instructionSet.put(0x18AC, new CPY_18AC(this, mem));
		instructionSet.put(0x18BC, new CPY_18BC(this, mem));
		instructionSet.put(0x1AAC, new CPY_1AAC(this, mem));
		instructionSet.put(0x4A, new DECA_4A(this, mem));
		instructionSet.put(0x9D, new JSR_9D(this, mem));
		instructionSet.put(0xBD, new JSR_BD(this, mem));
		instructionSet.put(0x18A6, new LDAA_18A6(this, mem));
		instructionSet.put(0x86, new LDAA_86(this, mem));
		instructionSet.put(0x96, new LDAA_96(this, mem));
		instructionSet.put(0xA6, new LDAA_A6(this, mem));
		instructionSet.put(0xB6, new LDAA_B6(this, mem));
		instructionSet.put(0xCDEE, new LDX_CDEE(this, mem));
		instructionSet.put(0xDE, new LDX_DE(this, mem));
		instructionSet.put(0xCE, new LDX_CE(this, mem));
		instructionSet.put(0xEE, new LDX_EE(this, mem));
		instructionSet.put(0xFE, new LDX_FE(this, mem));
		instructionSet.put(0x18CE, new LDY_18CE(this, mem));
		instructionSet.put(0x18DE, new LDY_18DE(this, mem));
		instructionSet.put(0x18EE, new LDY_18EE(this, mem));
		instructionSet.put(0x18FE, new LDY_18FE(this, mem));
		instructionSet.put(0x1AEE, new LDY_1AEE(this, mem));
		instructionSet.put(0x3C, new PSHX_3C(this, mem));
		instructionSet.put(0x183C, new PSHY_183C(this, mem));
		instructionSet.put(0x38, new PULX_38(this, mem));
		instructionSet.put(0x39, new RTS_39(this, mem));
		instructionSet.put(0x30, new TSX_30(this, mem));
		instructionSet.put(0x1830, new TSY_1830(this, mem));
		instructionSet.put(0xB7, new STAA_B7(this, mem));
		instructionSet.put(0x18A7, new STAA_18A7(this, mem));
		instructionSet.put(0x97, new STAA_97(this, mem));
		instructionSet.put(0xA7, new STAA_A7(this, mem));
		instructionSet.put(0x97, new STAA_97(this, mem));
		
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
			opcode.setBytes(2);
			opcode.mul(0x100);
			opcode.add(mem.read(pc));
			break;
		case 0x1A:
			pc.inc();
			opcode.setBytes(2);
			opcode.mul(0x100);
			opcode.add(mem.read(pc));
			break;
		case 0xCD:
			pc.inc();
			opcode.setBytes(2);
			opcode.mul(0x100);
			opcode.add(mem.read(pc));
			break;
		default:
			
		}
		currentInstruction = instructionSet.get(opcode.getVal());
		currentInstruction.exec();
	}
}
