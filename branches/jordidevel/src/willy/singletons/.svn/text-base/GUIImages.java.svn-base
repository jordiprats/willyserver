package willy.singletons;

import java.util.HashMap;

import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Shell;


//	final Image small = new Image(shell.getDisplay(),
//    "resources/images/icon_16.png");
//final Image large = new Image(shell.getDisplay(),
//    "resources/images/icon_32.png");

/**
 * conjunt de singletons (un per shell), yep... WTF
 */
public class GUIImages {
	/**
	 * tinc un hashmap per pode tindre varies finestres. sino es fa servir caldrà
	 * fer refactor a una sola instancia
	 */
	private static HashMap<Shell,GUIImages> instances = null;
	
	protected HashMap<String, Image> hmimg=null;
	
	/**
	 *  soc protected, fuck you! (soc un singleton)
	 */
	protected GUIImages(Shell shell) 
	{
		//TODO: mirar si SWTResourceManager ens ajuda
		
		this.hmimg=new HashMap<String, Image>();
		
		this.hmimg.put("check_ok",new Image(shell.getDisplay(),"res/img/checkicon_ok.png"));
		this.hmimg.put("check_fail",new Image(shell.getDisplay(),"res/img/checkicon_fail.png"));
		this.hmimg.put("check_warn",new Image(shell.getDisplay(),"res/img/checkicon_warn.png"));
	}

	public Image getImage(String key)
	{
		return this.hmimg.get(key);
	}

	public static GUIImages getInstance(Shell shell) 
	{
		if(instances == null) instances = new HashMap<Shell,GUIImages>();
		
		GUIImages instance= instances.get(shell);
		
		if(instance==null) instance = new GUIImages(shell);
		
		instances.put(shell, instance);

		return instance;
	}

}