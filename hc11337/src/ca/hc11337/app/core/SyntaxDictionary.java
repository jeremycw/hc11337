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
import java.io.*;

public class SyntaxDictionary {
	private Vector<String> tokens = new Vector<String>();
	
	public SyntaxDictionary() throws FileNotFoundException
	{
		File file = new File("hc11.syn");
		Scanner scanner = new Scanner(file);
		while(scanner.hasNext())
			tokens.add(scanner.next());
		
		Collections.sort(tokens);
	}
	
	public SyntaxDictionary(String filePath) throws FileNotFoundException
	{
		File file = new File(filePath);
		Scanner scanner = new Scanner(file);
		while(scanner.hasNext())
			tokens.add(scanner.next());
		
		Collections.sort(tokens);
	}
	
	public boolean checkFor(String word)
	{
		if(Collections.binarySearch(tokens, word) >= 0)
			return true;
		else
			return false;
	}
	
	public Vector<String> getVector()
	{
		return tokens;
	}
}
