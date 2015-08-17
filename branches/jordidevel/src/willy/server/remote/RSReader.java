package willy.server.remote;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.zip.ZipFile;

import willy.server.regex.Base64;

//per tests:
//http://mockrunner.sourceforge.net/
//http://stackoverflow.com/questions/878848/easy-way-to-fill-up-resultset-with-data
//Mockrunner is released under the terms of an Apache style license, i.e. it's free for commercial and non-commercial use

public class RSReader {
	
	ZipFile zf=null;
	HashMap<String,ResultSet> index=null;
	
	//TODO: delayed constructor?
	public RSReader(String file) throws IOException, ClassNotFoundException
	{
		zf = new ZipFile(file);
		index=new HashMap<String,ResultSet>();
		
		BufferedReader in = new BufferedReader(new InputStreamReader(zf.getInputStream(zf.getEntry("enlargeyourpenis"))));
		String line = null;
		while((line = in.readLine()) != null) {
			
			String[] values=line.split(",");
			index.put(
						((String)Base64.decodeToObject(values[1].substring(1,values[1].length()-1))), 
						this.getEntry(values[0])
						);
		}
		
		System.out.println(index.toString());
	}

	private ResultSet getEntry(String name) throws IOException, ClassNotFoundException
	{
		BufferedReader in = new BufferedReader(new InputStreamReader(zf.getInputStream(zf.getEntry(name))));
		
		FakeResultSet rs = new FakeResultSet(in);
		
		return rs;
	}
	
	public void close()
	{
		if(zf!=null)
			try {
				zf.close();
			} catch (IOException e) {
				; //e.printStackTrace();
			}
	}
	
//	ZipFile zf = new ZipFile(file);
//	try {
//	  InputStream in = zf.getInputStream("file.txt");
//	  // ... read from 'in' as normal
//	} finally {
//	  zf.close();
//	}


	
//	MockResultSet rs = new MockResultSet("myMock");
//
//	rs.addColumn("columnA", new Integer[]{1});
//	rs.addColumn("columnB", new String[]{"Column B Value"});
//	rs.addColumn("columnC", new Double[]{2});
//
//	// make sure to move the cursor to the first row
//	try
//	{
//	  rs.next();
//	}
//	catch (SQLException sqle)
//	{
//	  fail("unable to move resultSet");
//	}
//
//	// process the result set
//	MyObject obj = processor.processResultSet(rs);
//
//	// run your tests using the ResultSet like you normally would
//	assertEquals(1, obj.getColumnAValue());
//	assertEquals("Column B Value", obj.getColumnBValue());
//	assertEquals(2.0d, obj.getColumnCValue());

	
}
