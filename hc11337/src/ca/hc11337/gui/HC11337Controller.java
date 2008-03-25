package ca.hc11337.gui;

import java.util.*;
import java.io.*;
import java.awt.Point;

import ca.hc11337.app.core.*;

public class HC11337Controller implements Observer {

	private final HC11337GUI view;
	private final HC11337Core model;
	private int newFileCount = 0;
	
	public HC11337Controller(HC11337GUI gui, HC11337Core core)
	{
		view = gui;
		model = core;
		model.addObserver(view);
		model.addObserver(this);
	}
	
	public void newFile()
	{
		try{
			view.newEditorTab("Untitled-"+newFileCount+".asm");
			model.newEditor("Untitled-"+newFileCount+".asm");
			view.switchEditor(view.getNumberOfTabs()-1);
			newFileCount++;
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void closeFile()
	{
		int index = view.indexOfCurrentEditor();
		if(index >= 0)
		{
			view.removeEditorTab(index); 
			model.removeEditor(index);
		}
	}
	
	public void closeAll()
	{
		int numberOfTabs = view.getNumberOfTabs();
		for(int i = 0; i < numberOfTabs; i++)
		{
			view.removeEditorTab(0);
			model.removeEditor(0);
		}
	}
	
	public void loadBinary(File file)
	{
		
	}
	
	public void openFile(File file)
	{
		try{
			int tabNum = view.checkIfOpen(file.getName());
			if(tabNum >= 0)
				view.switchEditor(tabNum);
			else{
				view.newEditorTab(file.getName());
				view.switchEditor(view.getNumberOfTabs()-1);
				model.newEditor(file);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void openFile()
	{
		
	}
	
	public void saveFile()
	{
		try{
			if(view.getNumberOfTabs() > 0){
				File file = model.getEditorFile(view.indexOfCurrentEditor());
				BufferedWriter writer = new BufferedWriter(new FileWriter(file));
				writer.write(view.getCurrentEditor().getText());
				writer.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	public void run()
	{
		
	}
	
	public void step()
	{
		
	}
	
	public void stop()
	{
		
	}
	
	public void delete()
	{
		
	}
	
	public void cut()
	{
		if(view.getNumberOfTabs() > 0)
			if(view.getCurrentEditor().hasSelection())
			{
				view.getCurrentEditor().cut();
				model.setText(view.getCurrentEditor().getText(), view.indexOfCurrentEditor());
				highlightAtCaret();
			}
	}
	
	public void copy()
	{
		if(view.getNumberOfTabs() > 0)
			view.getCurrentEditor().copy();
	}
	
	public void paste()
	{
		if(view.getNumberOfTabs() > 0){
			view.getCurrentEditor().paste();
			model.setText(view.getCurrentEditor().getText(), view.indexOfCurrentEditor());
			highlightSyntax();
		}
	}
	
	public void reset()
	{
		
	}
	
	public void deepReset()
	{
		
	}
	
	public void undo()
	{
		
	}
	
	public void redo()
	{
		
	}
	
	public void build()
	{
		saveFile();
		view.switchCurrentConsole(0);
		try{
			if(view.getNumberOfTabs() > 0)
			{
				Process asm = Runtime.getRuntime().exec("asm11.exe -l " + '"' + model.getEditorFile(view.indexOfCurrentEditor()).getAbsolutePath() + '"');
				Scanner console = new Scanner(asm.getInputStream());
				while(console.hasNext())
				{
					view.setConsoleText(view.getConsoleText()+console.nextLine()+'\n');
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	
	public void highlightAtCaret()
	{
		model.setText(view.getCurrentEditor().getText(), view.indexOfCurrentEditor());
		if(model.getText(view.indexOfCurrentEditor()).length() != 0){
			int[] array1 = model.getHighlightAt(view.indexOfCurrentEditor(), view.getCurrentEditor().getCaretPosition());
			int[] array2;
			Vector<int[]> v = new Vector<int[]>();
			if(view.getCurrentEditor().getCaretPosition()-2 >= 0){
				array2 = model.getHighlightAt(view.indexOfCurrentEditor(), view.getCurrentEditor().getCaretPosition()-2);
				v.add(array2);
			}
			v.add(array1);
			view.getCurrentEditor().highlightSyntax(v);
		}
	}
	
	public void highlightSyntax()
	{
		//view.getCurrentEditor().setText(model.getText(view.indexOfCurrentEditor()));
		view.getCurrentEditor().highlightSyntax(model.getHighlightRanges(view.indexOfCurrentEditor()));
	}
	
	public void selectAll()
	{
		view.getCurrentEditor().selectAll();
	}
	
	public void update(Observable o, Object arg) {
		// TODO Auto-generated method stub

	}

}
