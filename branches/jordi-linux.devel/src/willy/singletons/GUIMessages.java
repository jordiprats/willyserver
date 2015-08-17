package willy.singletons;

import java.util.Locale;
import java.util.ResourceBundle;

public class GUIMessages {
	private static GUIMessages instance = null;

	protected ResourceBundle rb=null;

	private final Locale[] supportedLocales = {
		    Locale.ENGLISH
		};
	
	protected Locale currentLocale=null;
	
	/**
	 *  soc protected, fuck you! (soc un singleton)
	 */
	protected GUIMessages() 
	{
		// fuck you!
		//rb=new ResourceBundle();
		currentLocale=supportedLocales[0];
		
		rb=ResourceBundle.getBundle("messages", currentLocale);
		
		//System.out.print("LOCALE: "+rb.getString("test"));

	}
	
	public String getString(String key)
	{
		return this.rb.getString(key);
	}

	public static GUIMessages getInstance() 
	{
		if(instance == null) instance = new GUIMessages();

		return instance;
	}

}