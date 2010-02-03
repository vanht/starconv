package org.sdconvertor.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;

import org.sdconvertor.structure.*;
import org.sdconvertor.utility.*;

/**
 * Created:	January 31, 2010
 * License:	GPLv3
 * @author	I-Fan Chen
 * @version	1
 *
 */
abstract public class DatabaseManager{
	private PreparedStatement preparedStatement;
	private Connection connection;
	private Statement statement;
	private String tableName, columns;

	public DatabaseManager(String databaseName, String tableName, String columns){
		setTableName(tableName);
		setColumns(columns);
		
		try{
			Class.forName(Constant.DRIVER_NAME);
			setConnection(DriverManager.getConnection("jdbc:sqlite:" + databaseName));
			setStatement(getConnection().createStatement());
			prepareTable();
			prepareInsertStatement();
		}catch (ClassNotFoundException e){
			Utility.log("Database driver not found: " + e.getMessage());
			return;
		}catch (SQLException e){
			Utility.log("SQL error: " + e.getMessage());
			return;
		}
	}
	
	private void prepareInsertStatement() throws SQLException{
		String placeHolder = "";
		
		for (int i = 0; i < getColumns().split(",").length; i++){
			placeHolder += "?,";
		}
		
		placeHolder = placeHolder.substring(0, placeHolder.length() - 1);
		setPreparedStatement(getConnection().prepareStatement("INSERT INTO " + getTableName() + " VALUES (" + placeHolder + ");"));
	}
	
	public void addBatch(String record){
		String[] fields;
		
		if (record == null || (fields = record.split(Constant.INSERT_FIELD_SEPERATOR)).length != getColumns().split(",").length){
			Utility.log("Error: record value is not compatible with table " + getTableName() + ".");
			return;
		}
		
		try{
			for (int i = 1; i <= fields.length; i++){
				getPreparedStatement().setString(i, fields[i - 1]);
			}
			
			getPreparedStatement().addBatch();
		}catch (SQLException e){
			Utility.log("SQL error: " + e.getMessage());
		}
	}
	
	public void commit(){
		try{
			getConnection().setAutoCommit(false);
			getPreparedStatement().executeBatch();
			getConnection().setAutoCommit(true);
		}catch (SQLException e){
			Utility.log("SQL error: " + e.getMessage());
		}
	}
	
	@Override
	protected void finalize() throws Throwable{
		super.finalize();
		getConnection().close();
	}
	
	abstract protected void prepareTable() throws SQLException;

///////////////////////////////////////////////////////////////////////////////
// Getters and Setters
	
	protected Connection getConnection(){return connection;}
	protected void setConnection(Connection connection){this.connection = connection;}
	
	protected Statement getStatement(){return statement;}
	protected void setStatement(Statement statement){this.statement = statement;}
	
	protected PreparedStatement getPreparedStatement(){return preparedStatement;}
	protected void setPreparedStatement(PreparedStatement preparedStatement){this.preparedStatement = preparedStatement;}
	
	public String getTableName(){return tableName;}
	public void setTableName(String tableName){this.tableName = tableName;}
	
	public String getColumns(){return columns;}
	public void setColumns(String columns){this.columns = columns;}

}