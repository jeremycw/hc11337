package ca.hc11337.app.core.hardwaremodel.instructions;

import ca.hc11337.app.core.hardwaremodel.*;

public class LDAA_86 implements Instruction 
{
	private CPU cpu;
	private Memory mem;
	
	public LDAA_86(CPU c, Memory m)
	{
		cpu = c;
		mem = m;
	}
	
	public void exec()
	{
		UnsignedNumber pc = cpu.getReg(Reg.PC);
		pc.inc();
		cpu.setReg(Reg.A, mem.read(pc).clone());
		pc.inc();
		int val = cpu.getReg(Reg.A).getVal();
		
		//set ccr
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