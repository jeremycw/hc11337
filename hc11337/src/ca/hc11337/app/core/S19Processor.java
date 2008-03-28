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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class S19Processor {
	private Scanner scanner;
	private String currentLine;
	private String nextLine;
	private int byteCount = 0;
	private boolean addressRead = false;
	private int byteReads = 0;
	private boolean byteCountRead = false;
	
	public S19Processor(File file) throws FileNotFoundException
	{
		scanner = new Scanner(file);
		scanner.nextLine();
		currentLine = scanner.nextLine();
		nextLine = scanner.nextLine();
	}
	
	public int nextAddress()
	{
		if(addressRead){
			currentLine = nextLine;
			nextLine = scanner.nextLine();
			addressRead = false;
			byteCountRead = false;
			byteReads = 0;
		}
		addressRead = true;
		return Integer.parseInt(currentLine.substring(4, 8), 16);
	}
	
	public int nextByte()
	{
		if(byteCount == 0)
			byteCount = Integer.parseInt(currentLine.substring(2, 4));
		if(byteReads == byteCount-3){
			currentLine = nextLine;
			nextLine = scanner.nextLine();
			byteReads = 0;
			byteCount = 0;
			byteCountRead = false;
			addressRead = false;
		}
		int num = Integer.parseInt(currentLine.substring(8+(byteReads*2), 10+(byteReads*2)), 16);
		byteReads++;
		return num;
	}
	
	public int nextByteCount()
	{
		if(byteCountRead){
			currentLine = nextLine;
			nextLine = scanner.nextLine();
			byteCountRead = false;
			addressRead = false;
			byteReads = 0;
		}
		byteCountRead = true;
		return Integer.parseInt(currentLine.substring(2, 4));
	}
	
	public boolean hasNextLine()
	{
		System.out.println(nextLine.charAt(1));
		return nextLine.charAt(1) == '9'? false : true;
	}

}
