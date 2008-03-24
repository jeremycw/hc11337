package ca.hc11337.app.core.hardwaremodel.instructions;

import ca.hc11337.app.core.hardwaremodel.*;

public class ABA_1B implements Instruction 
{
	private CPU cpu;
	private Memory mem;
	
	public ABA_1B(CPU c, Memory m)
	{
		cpu = c;
		mem = m;
	}
	
	public void exec()
	{
		cpu.getReg(Reg.PC).inc();
		cpu.getReg(Reg.A).add(cpu.getReg(Reg.B));
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