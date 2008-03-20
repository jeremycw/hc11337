package ca.hc11337.gui.editor;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.*;
import org.eclipse.swt.widgets.*;
import org.eclipse.swt.graphics.*;

import java.util.*;
import java.awt.Point;

public class HC11337Editor {
	private TabItem editorTab;
	private StyledText fileEditor;
	
	public HC11337Editor(TabFolder parent, String name, int style)
	{
		editorTab = new TabItem (parent, SWT.NONE);
		editorTab.setText (name);
		fileEditor = new StyledText (parent, SWT.BORDER | SWT.H_SCROLL| SWT.V_SCROLL);
		editorTab.setControl(fileEditor);
		fileEditor.setTabs(8);
		fileEditor.setFont(new Font(Display.getCurrent(), "Fixedsys", 10, SWT.NORMAL));
		parent.pack();
	}
	
	public boolean hasSelection()
	{
		if(fileEditor.getSelectionText().length() > 0)
			return true;
		else
			return false;
	}
	
	public void highlightSyntax(Vector<int[]> positions)
	{
		for(int i = 0; i < positions.size(); i++)
		{
			StyleRange highlights = new StyleRange();
			highlights.start = positions.get(i)[1];
			highlights.length = positions.get(i)[2];
			switch(positions.get(i)[0])
			{
			case 0:
				highlights.foreground = null;
				break;
			case 1:
				highlights.foreground = Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);
				break;
			}
			fileEditor.setStyleRange(highlights);
		}
	}
	
	public void setText(String text)
	{
		fileEditor.setText(text);
	}
	
	public String getText()
	{
		return fileEditor.getText();
	}
	
	public void cut()
	{
		fileEditor.cut();
	}
	
	public void copy()
	{
		fileEditor.copy();
	}
	
	public void paste()
	{
		fileEditor.paste();
	}
	
	public void dispose()
	{
		editorTab.dispose();
		fileEditor.dispose();
	}
	
	public String getName()
	{
		return editorTab.getText();
	}
	
	public int getCaretPosition()
	{
		return fileEditor.getCaretOffset();
	}
	
	public void setCaretPosition(int position)
	{
		fileEditor.setCaretOffset(position);
	}
	
	public int getVerticalScrollPosition()
	{
		return fileEditor.getTopPixel();
	}
	
	public void setVerticalScrollPosition(int position)
	{
		fileEditor.setTopPixel(position);
	}
}
