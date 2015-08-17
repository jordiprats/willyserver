package willy.server.gui;

import org.eclipse.swt.*;
import org.eclipse.swt.graphics.*;
import org.eclipse.swt.widgets.*;

public class WillyTray {
	//TODO: falta afegir
	
	//exemple de funcionament
	public static void main(String[] args) {
		Display display = new Display ();
		Shell shell = new Shell (display);
		Image image = new Image (display, 16, 16);
		Image image2 = new Image (display, 16, 16);
		GC gc = new GC(image2);
		gc.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
		gc.fillRectangle(image2.getBounds());
		gc.dispose();
		final Tray tray = display.getSystemTray ();
		if (tray == null) {
			System.out.println ("The system tray is not available");
		} else {
			final TrayItem item = new TrayItem (tray, SWT.NONE);
			item.setToolTipText("SWT TrayItem");
			item.addListener (SWT.Show, new Listener () {
				public void handleEvent (Event event) {
					System.out.println("show");
				}
			});
			item.addListener (SWT.Hide, new Listener () {
				public void handleEvent (Event event) {
					System.out.println("hide");
				}
			});
			item.addListener (SWT.Selection, new Listener () {
				public void handleEvent (Event event) {
					System.out.println("selection");
				}
			});
			item.addListener (SWT.DefaultSelection, new Listener () {
				public void handleEvent (Event event) {
					System.out.println("default selection");
				}
			});
			final Menu menu = new Menu (shell, SWT.POP_UP);
			for (int i = 0; i < 8; i++) {
				MenuItem mi = new MenuItem (menu, SWT.PUSH);
				mi.setText ("Item" + i);
				mi.addListener (SWT.Selection, new Listener () {
					public void handleEvent (Event event) {
						System.out.println("selection " + event.widget);
					}
				});
				if (i == 0) menu.setDefaultItem(mi);
			}
			item.addListener (SWT.MenuDetect, new Listener () {
				public void handleEvent (Event event) {
					menu.setVisible (true);
				}
			});
			item.setImage (image2);
			//item.set (image);
		}
		shell.setBounds(50, 50, 300, 200);
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		image.dispose ();
		image2.dispose ();
		display.dispose ();
	}
}
