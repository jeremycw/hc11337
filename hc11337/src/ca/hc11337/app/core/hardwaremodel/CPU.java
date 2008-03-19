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
