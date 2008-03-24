package ca.hc11337.app.core.hardwaremodel.instructions;

import ca.hc11337.app.core.hardwaremodel.*;

public class BRA_20 implements Instruction 
{
	private CPU cpu;
	private Memory mem;
	
	public BRA_20(CPU c, Memory m)
	{
		cpu = c;
		mem = m;
	}
	
	public void exec()
	{
		UnsignedNumber pc = cpu.getReg(Reg.PC);
		byte op = (byte)mem.read(pc).getVal();
		pc.add((int)op);
	}

}
