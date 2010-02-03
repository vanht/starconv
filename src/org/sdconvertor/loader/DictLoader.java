package org.sdconvertor.loader;

import java.io.*;
import java.nio.*;
import java.util.*;
import java.util.zip.*;

import org.sdconvertor.db.*;
import org.sdconvertor.structure.*;
import org.sdconvertor.utility.*;



/**
 * Created:	January 31, 2010
 * License:	GPLv3
 * @author	I-Fan Chen
 * @version	1
 *
 */
public class DictLoader{
	private ArrayList<StarDictWord> dataList;
	private DictionaryManager dictManager;
	private File[] files;
	private String encoding;

	public DictLoader(DictionaryManager dictManager, File[] files, String encoding){
		dataList = new ArrayList<StarDictWord>();
		this.dictManager = dictManager;
		this.files = files;
		this.encoding = encoding;
	}

	public void loadData(){
		InputStream input;
		
		try {
			// load index data
			input = new BufferedInputStream(new FileInputStream(files[0]));
			byte[] indexBytes = loadBytesFromFile(input);
			input.close();
			processIndexBytes(indexBytes);
			
			//load dict data
			input = new GZIPInputStream(new BufferedInputStream(new FileInputStream(files[1])));
			byte[] dictBytes = loadBytesFromFile(input);
			input.close();
			processDictBytes(dictBytes);
		}catch (FileNotFoundException e){
			Utility.log(e.getMessage());
		}catch (IOException e){
			Utility.log(e.getMessage());
		}
	}
	
	private byte[] loadBytesFromFile(InputStream in) throws IOException{
		ByteArrayOutputStream streamBuffer = new ByteArrayOutputStream();
		byte[] readBuffer = new byte[1024];
		int numRead = 0;
		
		while ((numRead = in.read(readBuffer)) != -1){
			streamBuffer.write(readBuffer, 0, numRead);
		}
		
		return streamBuffer.toByteArray();
	}
	
	private void processIndexBytes(byte[] data) throws UnsupportedEncodingException{
		int currentIndex = 0, dataIndex = 0, dataSize;
		byte[] wordBytes, dataOffsetBytes, dataSizeBytes;
		
		while (currentIndex < data.length){
			if (data[currentIndex] == Constant.INDEX_FILE_SPLIT_BYTE){
				if (currentIndex + 8 < data.length){
					dataSize = currentIndex - dataIndex;
					wordBytes = new byte[dataSize];
					
					for (int i = 0; i < dataSize; i++){
						wordBytes[i] = data[i + dataIndex];
					}
					
					dataOffsetBytes = new byte[4];
					dataSizeBytes = new byte[4];
					
					for (int i = 1; i < 4; i++){
						dataOffsetBytes[i] = data[currentIndex + i + 1];
						dataSizeBytes[i] = data[currentIndex + i + 5];
					}
					
					dataList.add(new StarDictWord(
						new String(wordBytes, encoding),
						ByteBuffer.wrap(dataOffsetBytes).getInt(),
						ByteBuffer.wrap(dataSizeBytes).getInt()
					));
				}
				
				currentIndex += 8;
				dataIndex = currentIndex + 1;
			}
			
			currentIndex += 1;
		}
	}
	
	private void processDictBytes(byte[] dictBytes) throws UnsupportedEncodingException{
		if (dataList.size() == 0){
			return;
		}
		
		byte[] definition = null;
		int offset = 0, size = 0;
		String value = "";
		
		for (StarDictWord word : dataList){
			offset = word.getOffset();
			size = word.getSize();
			
			if (offset + size - 1 < dictBytes.length){
				definition = new byte[size];
				
				for (int i = 0; i < size; i++){
					definition[i] = dictBytes[offset + i];
				}
				
				value = word.getName() + Constant.INSERT_FIELD_SEPERATOR + new String(definition, encoding);
				dictManager.addBatch(value);
				dictManager.commit();
			}
		}
	}

///////////////////////////////////////////////////////////////////////////////
// Getters and Setters
	
	public int getWordCount(){
		return dataList.size();
	}

}