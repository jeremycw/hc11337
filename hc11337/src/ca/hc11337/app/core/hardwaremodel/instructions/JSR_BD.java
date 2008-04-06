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
		UnsignedNumber op1 = mem.read(pc).clone();
		pc.inc();
		UnsignedNumber op2 = mem.read(pc).clone();
		pc.inc();
		UnsignedNumber op3 = new UnsignedNumber(op1, op2);
		UnsignedNumber sp = cpu.getReg(Reg.SP);
		sp.sub(2);
		mem.write(sp, pc);
		pc.setVal((op3).getVal());
	}

}