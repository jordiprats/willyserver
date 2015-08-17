package willy.server.gui.tabs;

import org.eclipse.swt.widgets.Composite;

public abstract class GenericGUIObject {

	protected Composite composite=null;
	
	public Composite getComposite()
	{
		return this.composite;
	}
}
