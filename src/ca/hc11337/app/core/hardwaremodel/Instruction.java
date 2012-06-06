package ca.hc11337.app.core.hardwaremodel;

public class Instruction {
	protected CPU cpu;
	protected Memory mem;
	
	public Instruction(CPU cpu, Memory mem)
	{
		this.cpu = cpu;
		this.mem = mem;
	}
	
	private UnsignedNumber decodeOperator(int bytes)
	{
		UnsignedNumber pc = cpu.getReg(Reg.PC);
		pc.inc();
		UnsignedNumber[] byteArray = new UnsignedNumber[bytes];
		for(int i = 0; i < bytes; i++)
		{
			byteArray[i] = mem.read(pc);
			pc.inc();
		}
		return new UnsignedNumber(byteArray);
	}
	
	protected UnsignedNumber direct()
	{
		return decodeOperator(1);
	}
	
	protected UnsignedNumber immediate(int bytes)
	{
		return decodeOperator(bytes);
	}
	
	protected UnsignedNumber extended()
	{
		return decodeOperator(2);
	}
	
	protected UnsignedNumber indirectX()
	{
		UnsignedNumber addr = decodeOperator(1);
		addr.setBytes(2);
		addr.add(cpu.getReg(Reg.X));
		return addr;
	}
	
	protected UnsignedNumber indirectY()
	{
		UnsignedNumber addr = decodeOperator(1);
		addr.setBytes(2);
		addr.add(cpu.getReg(Reg.Y));
		return addr;
	}
	
	protected void calcConditionCodes(UnsignedNumber val, int... codes)
	{
		for(int i = 0; i < codes.length; i++)
		{
			switch(codes[i])
			{
			case CCR.S:
				break;
			case CCR.X:
				break;
			case CCR.H:
				break;
			case CCR.I:
				break;
			case CCR.N:
				if(val.getVal() > Math.pow(2.0, val.getBytes()*8)/2 - 1)
					cpu.setCC(CCR.N, true);
				else
					cpu.setCC(CCR.N, false);
				break;
			case CCR.Z:
				if(val.getVal() == 0)
					cpu.setCC(CCR.Z, true);
				else
					cpu.setCC(CCR.Z, false);
				break;
			case CCR.V:
				if(val.overflow())
					cpu.setCC(CCR.V, true);
				else
					cpu.setCC(CCR.V, false);
				break;
			case CCR.C:
				if(val.carry())
					cpu.setCC(CCR.C, true);
				else
					cpu.setCC(CCR.C, false);
				break;
			default:
				System.out.println("Fuck!");
			}
		}
	}
}
