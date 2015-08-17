package willy.server.sql;

public class DBParam {
	
	//Xstream: llicencia BSD per pasar clase a XML
	//http://xstream.codehaus.org/license.html
	
	protected String user = null;
    protected String password = null;
    protected String host = null;
    protected String port = "3306";
    
    protected boolean fromFile = false;
    public boolean isFile() { return this.fromFile; }
    
    protected boolean recording =false;
    public boolean isRecording() { return this.recording; }
    public void enableRecording() { this.recording=true; }
    public void disableRecording() { this.recording=false; }
    
    
    public DBParam(String user, String password, String host)
    {
    	this.user=user;
    	this.password=password;
    	this.host=host;
    }
    
    public DBParam(String user, String password, String host, String port)
    {
    	this.user=user;
    	this.password=password;
    	this.host=host;
    	this.port=port;
    }
    
	public String getUrl() {
		return "jdbc:mysql://"+host+":"+port+"/";
	}
    
    public String getHostname()
    {
    	return host;
    }
    
    public void setHostname(String host)
    {
    	this.host=host;
    }
    
    public String getUser() {
		return user;
	}
	public void setUser(String user) {
		this.user = user;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
		
}
