package willy.server.gui;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

public class WillyAbout implements Runnable{

//	Shell parent = ....
//	Shell popup = new Shell(parent, SWT.NO_TRIM);
	
	protected volatile boolean open=false;
	protected Shell shell = null;
	protected Display display=null;
	
	/**
	 * @wbp.parser.entryPoint
	 */
	public void run()
	{
		shell=new Shell ();
		shell.setSize(745, 471);
		shell.setMinimumSize(645, 471);
		
		display = Display.getDefault();

		shell.setMaximized(true);
		shell.setLayout(new FillLayout(SWT.VERTICAL));
		shell.open ();
		
		/*
		 * AQUI LES COSES BONES
		 * 
		 * https://developers.google.com/image-search/v1/jsondevguide#json_snippets_java
		 * https://ajax.googleapis.com/ajax/services/search/images?v=1.0&q=barack%20obama
		 */
		
		//shell.pack (); //tamany mínim
		this.open=true;
		while (!shell.isDisposed())
		{
			//mentre no tanqui la finestre
			if (!display.readAndDispatch ()) display.sleep ();
		}
		this.open=false;
	}

	/**
	 * @return esta arrancada linterficie gràfica?
	 */
	public boolean isOpen() {
		return open;
	}
	
}
