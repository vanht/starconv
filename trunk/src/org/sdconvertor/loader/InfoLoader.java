package org.sdconvertor.loader;

import java.io.*;
import java.util.*;

import org.sdconvertor.utility.*;

/**
 * Created:	January 31, 2010
 * License:	GPLv3
 * @author	I-Fan Chen
 * @version	1
 *
 */
public class InfoLoader{
	private HashMap<String, String> dataMap;
	private File file;
	private String encoding;

	public InfoLoader(File file, String encoding){
		dataMap = new HashMap<String, String>();
		this.file = file;
		this.encoding = encoding;
	}
	
	public void loadData(){
		String line = null;
		
		try{
			BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(file), encoding));
			
			while ((line = in.readLine()) != null){
				if (line.equals("StarDict's dict ifo file")){
					continue;
				}
				
				if (line.contains("=")){
					dataMap.put(line.split("=")[0], line.split("=")[1]);
				}
				else{
					dataMap.put(line, "");
				}
			}
			
			in.close();
		}catch (UnsupportedEncodingException e){
			Utility.log(e.getMessage());
		}catch (FileNotFoundException e){
			Utility.log(e.getMessage());
		}catch (IOException e){
			Utility.log(e.getMessage());
		}
	}

///////////////////////////////////////////////////////////////////////////////
// Getters and Setters
	
	public Iterator<String> getKeys(){
		return dataMap.keySet().iterator();
	}
	
	public String getValue(String key){
		return dataMap.get(key);
	}

}