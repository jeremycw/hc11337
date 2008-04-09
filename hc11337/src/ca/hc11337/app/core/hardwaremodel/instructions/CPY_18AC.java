/*This file is part of HC11337.

    HC11337 is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    HC11337 is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with HC11337.  If not, see <http://www.gnu.org/licenses/>.
*/

package ca.hc11337.app.core.hardwaremodel.instructions;

import ca.hc11337.app.core.hardwaremodel.*;

public class CPY_18AC implements Instruction 
{
	private CPU cpu;
	private Memory mem;
	
	public CPY_18AC(CPU c, Memory m)
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
		op.setBytes(2);
		op.add(cpu.getReg(Reg.Y));
		UnsignedNumber y = cpu.getReg(Reg.Y).clone();
		y.sub(op);
		int val = y.getVal();
		
		//TODO set C ccr
		if(y.overflow())
			cpu.setCC(CCR.V, true);
		else
			cpu.setCC(CCR.V, false);
		if(val > 32767)
			cpu.setCC(CCR.N, true);
		else
			cpu.setCC(CCR.N, false);
		if(val == 0)
			cpu.setCC(CCR.Z, true);
		else
			cpu.setCC(CCR.Z, false);
	}

}