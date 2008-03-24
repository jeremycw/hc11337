package ca.hc11337.gui.editor;

import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;

import ca.hc11337.gui.HC11337Controller;

public class HC11337Editor {
	private TabItem editorTab;
	private StyledText fileEditor;
	private final HC11337Controller controller;
	
	public HC11337Editor(TabFolder parent, String name, HC11337Controller controller, int style)
	{
		this.controller = controller;
		editorTab = new TabItem (parent, SWT.NONE);
		editorTab.setText (name);
		fileEditor = new StyledText (parent, SWT.BORDER | SWT.H_SCROLL| SWT.V_SCROLL);
		editorTab.setControl(fileEditor);
		fileEditor.setTabs(8);
		fileEditor.setFont(new Font(Display.getCurrent(), "Fixedsys", 10, SWT.NORMAL));
		fileEditor.addKeyListener(new MyKeyListener(controller));
		parent.pack();
	}
	
	class MyKeyListener implements KeyListener{
		private HC11337Controller controller;
		
		MyKeyListener(HC11337Controller controller)
		{
			this.controller = controller;
		}
		
		public void keyPressed(KeyEvent e)
		{
			controller.highlightAtCaret();
		}
		
		public void keyReleased(KeyEvent e)
		{
			
		}
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
	
	public void selectAll()
	{
		fileEditor.selectAll();
	}
}
