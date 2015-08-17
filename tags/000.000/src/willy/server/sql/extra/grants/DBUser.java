package willy.server.sql.extra.grants;

//potser massa MySQL
public class DBUser
{
	protected String user;
	protected String password;
	protected String host;
	protected String grants;
	
	public DBUser(String user, String password, String host, String grants)
	{
		this.user=user;
		this.password=password;
		this.host=host;
		this.grants=grants;
	}
	
	public String toString()
	{
		return user+":"+password+"@"+host;
	}

	public String getUser() {
		return user;
	}

	public String getPassword() {
		return password;
	}

	public String getHost() {
		return host;
	}

	public String getGrants() {
		return grants;
	}
	
	//TODO: obtindre grants
}
