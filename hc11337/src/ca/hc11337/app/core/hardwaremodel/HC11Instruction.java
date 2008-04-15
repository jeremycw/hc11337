package ca.hc11337.app.core.hardwaremodel;

public class HC11Instruction {
	protected CPU cpu;
	protected Memory mem;
	
	public HC11Instruction(CPU cpu, Memory mem)
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
	
	protected UnsignedNumber read(UnsignedNumber address, int bytes)
	{
		UnsignedNumber[] byteArray = new UnsignedNumber[bytes];
		for(int i = 0; i < bytes; i++)
		{
			byteArray[i] = mem.read(address);
			address.inc();
		}
		return new UnsignedNumber(byteArray);
	}
}
