package willy.server.regex;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexParser {
	
	protected Pattern r=null;
	protected Matcher m=null;
	
	protected boolean found=false;
	
	public RegexParser(String str, String pattern)
	{
		this.r = Pattern.compile(pattern);
		this.m = r.matcher(str);
		this.found=m.find();
	}

	
	public String getMatch(int i)
	{
		if (this.found) 
		{
			//System.out.println("Found value: " + m.group(1) );
			return m.group(i);
		}
		else
			return null;
	}

}
