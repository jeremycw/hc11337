package ca.hc11337.app.core.hardwaremodel.instructions;

import ca.hc11337.app.core.hardwaremodel.*;

public class BLS_23 implements Instruction 
{
	private CPU cpu;
	private Memory mem;
	
	public BLS_23(CPU c, Memory m)
	{
		cpu = c;
		mem = m;
	}
	
	public void exec()
	{
		UnsignedNumber pc = cpu.getReg(Reg.PC);
		pc.inc();
		byte op = (byte)mem.read(pc).getVal();
		if(cpu.getCC(CCR.C) | cpu.getCC(CCR.Z))
		{
			pc.dec();
			pc.add((int)op);
		}
		else
			pc.inc();
	}

}