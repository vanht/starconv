package org.sdconvertor;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;

import org.sdconvertor.db.DictionaryManager;
import org.sdconvertor.db.InfoManager;
import org.sdconvertor.loader.DictLoader;
import org.sdconvertor.loader.InfoLoader;
import org.sdconvertor.structure.Constant;
import org.sdconvertor.utility.Utility;


public class testMain{

	public static void main(String[] args) throws IOException{
		String dbName = "langdao-ec-big5.db";
		HashMap<String, File> fileMap = Utility.prepareDictionaryFiles("dict");
		
		InfoLoader infoLoader = new InfoLoader(fileMap.get("ifo"), Constant.UTF8);
		InfoManager infoManager = new InfoManager(dbName);
		
		infoLoader.loadData();
		Iterator<String> keys = infoLoader.getKeys();
		String key, value;
		
		while (keys.hasNext()){
			key = keys.next();
			value = key + Constant.INSERT_FIELD_SEPERATOR + infoLoader.getValue(key);
			infoManager.addBatch(value);
		}
		
		infoManager.commit();
		
		File[] dictFiles = {fileMap.get("idx"), fileMap.get("dict")};
		DictionaryManager dictManager = new DictionaryManager(dbName);
		DictLoader indexLoader = new DictLoader(dictManager, dictFiles, Constant.UTF8);
		indexLoader.loadData();
	}

}