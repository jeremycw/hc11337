package ca.hc11337.app.core.hardwaremodel.instructions;

import ca.hc11337.app.core.hardwaremodel.*;

public class JSR_9D implements Instruction 
{
	private CPU cpu;
	private Memory mem;
	
	public JSR_9D(CPU c, Memory m)
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
		UnsignedNumber sp = cpu.getReg(Reg.SP);
		sp.sub(2);
		mem.write(sp, pc);
		pc.setVal(mem.read(op).getVal());
	}

}