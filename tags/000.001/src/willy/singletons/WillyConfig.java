package willy.singletons;


public class WillyConfig {
	private static WillyConfig instance = null;


	/**
	 *  soc protected, fuck you! (soc un singleton)
	 */
	protected WillyConfig() 
	{
		// fuck you!
		//llegir config desde fitxer?
	}

	public static WillyConfig getInstance() 
	{
		if(instance == null) instance = new WillyConfig();

		return instance;
	}

}