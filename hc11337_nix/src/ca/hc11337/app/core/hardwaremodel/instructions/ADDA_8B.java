package ca.hc11337.app.core.hardwaremodel.instructions;

import ca.hc11337.app.core.hardwaremodel.*;


public class ADDA_8B implements Instruction 
{
	private CPU cpu;
	private Memory mem;
	
	public ADDA_8B(CPU c, Memory m)
	{
		cpu = c;
		mem = m;
	}
	
	public void exec()
	{
		UnsignedNumber pc = cpu.getReg(Reg.PC);
		pc.inc();
		UnsignedNumber op = mem.read(pc);
		pc.inc();
		cpu.getReg(Reg.A).add(op);
		int val = cpu.getReg(Reg.A).getVal();
		
		//set ccr
		if(cpu.getReg(Reg.A).overflow())
			cpu.setCC(CCR.V, true);
		else
			cpu.setCC(CCR.V, false);
		if(val > 127)
			cpu.setCC(CCR.N, true);
		else
			cpu.setCC(CCR.N, false);
		if(val == 0)
			cpu.setCC(CCR.Z, true);
		else
			cpu.setCC(CCR.Z, false);
	}

}