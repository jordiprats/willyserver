package willy.server.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import willy.server.gui.tabs.GenericGUIObject;
import willy.singletons.GUIImages;
import willy.singletons.GUIMessages;

//TODO: taula de processlist
public class ProcessListTable extends GenericGUIObject {

	protected GUIMessages rb=null;
	protected GUIImages rm=null;
	
	protected Table table=null;
	//TODO: afegir totes les columnes
	protected TableColumn querycol=null;
	protected TableColumn maxtimecol=null;
	
	public ProcessListTable(Composite comp)
	{
		rb=GUIMessages.getInstance();
		rm=GUIImages.getInstance(comp.getShell());

		composite = new Composite(comp, SWT.NONE);
		composite.setLayout(new GridLayout(2, false));
	}
}
