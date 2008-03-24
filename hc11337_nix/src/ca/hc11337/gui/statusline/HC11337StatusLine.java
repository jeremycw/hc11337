package ca.hc11337.gui.statusline;

import org.eclipse.jface.action.*;

import ca.hc11337.gui.Managed;


public class HC11337StatusLine implements Managed {

	private StatusLineManager status = new StatusLineManager();
	
	public HC11337StatusLine()
	{
			status.setMessage("Hello!");
	}
	
	public ContributionManager getManager() {
		return status;
	}

}
