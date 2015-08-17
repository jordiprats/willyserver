package willy.server.gui.tabs;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;


import willy.singletons.*;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;

public class EnginesTab extends GenericTab{

	
	protected GUIMessages rb=null;
	protected GUIImages rm=null;
	
	protected Button goAhead=null;
	protected Label enginestabdescription=null;
	
	public EnginesTab(Composite comp)
	{
		rb=GUIMessages.getInstance();
		rm=GUIImages.getInstance(comp.getShell());
		
		composite = new Composite(comp, SWT.NONE);
		composite.setLayout(new FormLayout());
		
		goAhead = new Button(composite, SWT.NONE);
		goAhead.addMouseListener(new MouseAdapter() {
			public void mouseUp(MouseEvent e) {
				//borrem pantalla d'inici
				goAhead.dispose();
				enginestabdescription.dispose();
			}
		});
		
		FormData fd_goAhead = new FormData();
		fd_goAhead.right = new FormAttachment(100, -191);
		fd_goAhead.bottom = new FormAttachment(100, -143);
		fd_goAhead.top = new FormAttachment(0, 128);
		fd_goAhead.left = new FormAttachment(0, 180);
		goAhead.setLayoutData(fd_goAhead);
		goAhead.setText(rb.getString("pestanyes.global.engines.botogoahead"));
		
		enginestabdescription = new Label(composite, SWT.NONE);
		FormData descripciosuperior = new FormData();
		descripciosuperior.top = new FormAttachment(0, 10);
		descripciosuperior.left = new FormAttachment(0, 10);
		enginestabdescription.setLayoutData(descripciosuperior);
		enginestabdescription.setText(rb.getString("pestanyes.global.engines.description"));
		
	}
	
}
