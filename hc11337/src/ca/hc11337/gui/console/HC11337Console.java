package ca.hc11337.gui.console;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.widgets.*;

public class HC11337Console {
	private StyledText consoleText;
	public HC11337Console(Composite parent, int style)
	{
		consoleText = new StyledText (parent, style);
		consoleText.setEditable(false);
	}
	
	public Composite getConsole()
	{
		return consoleText;
	}
	
	public String getText()
	{
		return consoleText.getText();
	}
	
	public void setText(String text)
	{
		consoleText.setText(text);
	}
}
