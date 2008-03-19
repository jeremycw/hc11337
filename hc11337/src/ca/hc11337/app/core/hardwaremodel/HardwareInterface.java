package ca.hc11337.app.core.hardwaremodel;

public class HardwareInterface
{
	private Memory mem = new Memory();
	private CPU cpu = new CPU(mem);
	
	public void setReg(int reg, int val)
	{
		UnsignedNumber a;
		if(reg < 2)
			a = new UnsignedNumber(val,1);
		else
			a = new UnsignedNumber(val,2);
		cpu.setReg(reg, a);
	}
	
	public int getReg(int reg)
	{
		return cpu.getReg(reg).getVal();
	}
	
	public void setCC(int code, boolean val)
	{
		cpu.setCC(code, val);
	}
	
	public boolean getCC(int code)
	{
		return getCC(code);
	}
	
	public void execute()
	{
		cpu.executeInstruction();
	}
}
