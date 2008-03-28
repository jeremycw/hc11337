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

package ca.hc11337.app.core;

import java.util.*;

public class CPUManager {
	private int[] registerValues;
	private String[]registerNames;
	
	public void setRegisterValues(int[] values)
	{
		registerValues = values;
	}
	
	public int[] getRegisterValues()
	{
		return registerValues;
	}
	
	
	public void setRegisterNames(String[] names)
	{
		registerNames = names;
	}
	
	public String[] getRegisterNames()
	{
		return registerNames;
	}
	
	
	public void setRegisterValue(int index, int value)
	{
		registerValues[index] = value;
	}
	
	public int getRegisterValue(int index)
	{
		return registerValues[index];
	}

}
