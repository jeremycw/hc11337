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
