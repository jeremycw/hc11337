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
		}else
			byteCountRead = true;
		return Integer.parseInt(currentLine.substring(2, 4));
	}
	
	public boolean hasNextLine()
	{
		System.out.println(nextLine.charAt(1));
		return nextLine.charAt(1) == '9'? false : true;
	}

}
