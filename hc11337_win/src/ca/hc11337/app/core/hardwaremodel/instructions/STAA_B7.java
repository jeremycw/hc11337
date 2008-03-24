package ca.hc11337.app.core.hardwaremodel.instructions;

import ca.hc11337.app.core.hardwaremodel.CCR;
import ca.hc11337.app.core.hardwaremodel.CPU;
import ca.hc11337.app.core.hardwaremodel.Instruction;
import ca.hc11337.app.core.hardwaremodel.Memory;
import ca.hc11337.app.core.hardwaremodel.Reg;
import ca.hc11337.app.core.hardwaremodel.UnsignedNumber;

public class STAA_B7 implements Instruction 
{
	private CPU cpu;
	private Memory mem;
	
	public STAA_B7(CPU c, Memory m)
	{
		cpu = c;
		mem = m;
	}
	
	public void exec()
	{
		UnsignedNumber pc = cpu.getReg(Reg.PC);
		pc.inc();
		UnsignedNumber op = mem.read(pc).clone();
		op.mul(0x100);
		op.add(mem.read(pc));
		mem.write(op, cpu.getReg(Reg.A).clone());
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