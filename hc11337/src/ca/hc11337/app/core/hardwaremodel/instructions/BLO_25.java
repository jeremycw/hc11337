package ca.hc11337.app.core.hardwaremodel.instructions;

import ca.hc11337.app.core.hardwaremodel.*;

public class BLO_25 implements Instruction 
{
	private CPU cpu;
	private Memory mem;
	
	public BLO_25(CPU c, Memory m)
	{
		cpu = c;
		mem = m;
	}
	
	public void exec()
	{
		UnsignedNumber pc = cpu.getReg(Reg.PC);
		pc.inc();
		byte op = (byte)mem.read(pc).getVal();
		if(cpu.getCC(CCR.C))
		{
			pc.dec();
			pc.add((int)op);
		}
		else
			pc.inc();
	}

}
