package org.sdconvertor.db;

import java.sql.*;

/**
 * Created:	January 31, 2010
 * License:	GPLv3
 * @author	I-Fan Chen
 * @version	1
 *
 */
public class InfoManager extends DatabaseManager{

	public InfoManager(String databaseName){
		super(databaseName, "ifo", "Option,Value");
	}
	
	@Override
	protected void prepareTable() throws SQLException{
		getStatement().executeUpdate("DROP TABLE IF EXISTS ifo;");
		getStatement().executeUpdate("CREATE TABLE ifo (Option, Value);");
	}

}