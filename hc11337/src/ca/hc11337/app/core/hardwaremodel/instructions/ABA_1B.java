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