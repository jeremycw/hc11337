package ca.hc11337.app.core.hardwaremodel.instructions;

import ca.hc11337.app.core.hardwaremodel.*;

public class JSR_BD implements Instruction 
{
	private CPU cpu;
	private Memory mem;
	
	public JSR_BD(CPU c, Memory m)
	{
		cpu = c;
		mem = m;
	}
	
	public void exec()
	{
		UnsignedNumber pc = cpu.getReg(Reg.PC);
		pc.inc();
		UnsignedNumber op = mem.read(pc).clone();
		pc.inc();
		UnsignedNumber sp = cpu.getReg(Reg.SP);
		sp.sub(2);
		pc.inc();
		mem.write(sp, pc);
		op.mul(0x100);
		op.add(mem.read(pc));
		pc.setVal(mem.read(op).getVal());
	}

}