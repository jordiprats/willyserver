package willy.server.remote;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

//public domain
//http://iharder.sourceforge.net/current/java/base64/

public class RSWriter {
	
	protected ZipOutputStream zip=null;
	HashMap<String,String> index=null;
	int i=0;
	
	public RSWriter(String zipfile) throws IOException
	{
		File yourFile = new File(zipfile);
		if(!yourFile.exists()) {
		    yourFile.createNewFile();
		} 
		zip = new ZipOutputStream(new FileOutputStream(zipfile));
		
		index=new HashMap<String,String>();
	}
	
	public void write(ResultSet rs, String name) throws SQLException, IOException
	{
		String entryname="penis_enlarger_"+i++;
		index.put(entryname, name);
		zip.putNextEntry(new ZipEntry(entryname));
		
		ResultSetMetaData rsm=rs.getMetaData();
		int colcount=rsm.getColumnCount();

		String linea=new String();
		
		//metadades
		for(int i=1;i<=colcount;i++)
		{
			System.out.print("\""+Base64.encodeObject(rsm.getColumnName(i))+"\"");
			linea+="\""+Base64.encodeObject(rsm.getColumnName(i))+"\"";
			
			if(i+1<=colcount)
			{
				System.out.print(",");
				linea+=",";
			}
			else
			{
				System.out.println();
				linea+="\n";
			}

		}
		zip.write(linea.getBytes());
		
		//dades
		String tmp=null;
		
		while(rs.next())
		{
			linea="";
			for(int i=1;i<=colcount;i++)
			{
				tmp=rs.getString(i);
				if(tmp!=null)
				{
					System.out.print("\""+Base64.encodeObject(tmp)+"\"");
					linea+="\""+Base64.encodeObject(tmp)+"\"";
				}
				if(i+1<=colcount)
				{
					System.out.print(",");
					linea+=",";
				}
				else
				{
					System.out.println();
					linea+="\n";
				}
			}
			zip.write(linea.getBytes());
		}
		
		
		zip.closeEntry();
	}

	public void close() throws IOException
	{
		//afegir fitxer d'index
		zip.putNextEntry(new ZipEntry("enlargeyourpenis"));
		
		Iterator<String> it=index.keySet().iterator();
		String linea=null;
		
		while(it.hasNext())
		{
			linea="";
			String key=it.next();
			linea+="\""+key+"\",\""+Base64.encodeObject(index.get(key))+"\"\n";
			
			zip.write(linea.getBytes());
		}
		
		zip.closeEntry();
		zip.close();
	}
	
//	try {
//	    // Create temp file.
//	    
//
//	    // Delete temp file when program exits.
//	    temp.deleteOnExit();
//
//	    // Write to temp file
//	    BufferedWriter out = new BufferedWriter(new FileWriter(temp));
//	    out.write("aString");
//	    out.close();
//	} catch (IOException e) {
//	}
	
//	
//	// These are the files to include in the ZIP file
//	String[] filenames = new String[]{"filename1", "filename2"};
//
//	// Create a buffer for reading the files
//	byte[] buf = new byte[1024];
//
//	try {
//	    // Create the ZIP file
//	    String outFilename = "outfile.zip";
//	    ZipOutputStream out = new ZipOutputStream(new FileOutputStream(outFilename));
//
//	    // Compress the files
//	    for (int i=0; i<filenames.length; i++) {
//	        FileInputStream in = new FileInputStream(filenames[i]);
//
//	        // Add ZIP entry to output stream.
//	        out.putNextEntry(new ZipEntry(filenames[i]));
//
//	        // Transfer bytes from the file to the ZIP file
//	        int len;
//	        while ((len = in.read(buf)) > 0) {
//	            out.write(buf, 0, len);
//	        }
//
//	        // Complete the entry
//	        out.closeEntry();
//	        in.close();
//	    }
//
//	    // Complete the ZIP file
//	    out.close();
//	} catch (IOException e) {
//	}
	
}
