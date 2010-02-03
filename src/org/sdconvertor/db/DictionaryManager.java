package org.sdconvertor.db;

import java.sql.*;

/**
 * Created:	January 31, 2010
 * License:	GPLv3
 * @author	I-Fan Chen
 * @version	1
 *
 */
public class DictionaryManager extends DatabaseManager{

	public DictionaryManager(String databaseName){
		super(databaseName, "dict", "Word,Meaning");
	}
	
	@Override
	protected void prepareTable() throws SQLException{
		getStatement().executeUpdate("DROP TABLE IF EXISTS dict;");
		getStatement().executeUpdate("CREATE TABLE dict (Word, Meaning);");
	}

}