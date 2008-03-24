package ca.hc11337.app.core.hardwaremodel.instructions;

import ca.hc11337.app.core.hardwaremodel.*;

public class LDAA_B6 implements Instruction 
{
	private CPU cpu;
	private Memory mem;
	
	public LDAA_B6(CPU c, Memory m)
	{
		cpu = c;
		mem = m;
	}
	
	public void exec()
	{
		UnsignedNumber pc = cpu.getReg(Reg.PC);
		pc.inc();
		UnsignedNumber op = mem.read(pc).clone();
		op.setBytes(2);
		op.mul(0x100);
		pc.inc();
		op.add(mem.read(pc));
		cpu.setReg(Reg.A, mem.read(op).clone());
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