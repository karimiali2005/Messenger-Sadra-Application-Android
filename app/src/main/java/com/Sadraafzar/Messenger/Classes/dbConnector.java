package com.Sadraafzar.Messenger.Classes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;


public class dbConnector extends SQLiteOpenHelper {

	Context context;
	public dbConnector(Context context, String name, CursorFactory factory,
                       int version) {
		super(context, name, factory, version);

		this.context=context;
		create_Tables() ;
	}

	public  void backupDatabase(String databaseName) {
		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();

			String packageName = context.getApplicationInfo().packageName;

			if (sd.canWrite()) {
				String currentDBPath = String.format("//data//%s//databases//%s",
						packageName, databaseName);
				String backupDBPath = String.format("debug_%s.sqlite", packageName);
				File currentDB = new File(data, currentDBPath);
				File backupDB = new File(sd, backupDBPath);

				if (currentDB.exists()) {
					FileChannel src = new FileInputStream(currentDB).getChannel();
					FileChannel dst = new FileOutputStream(backupDB).getChannel();
					dst.transferFrom(src, 0, src.size());
					src.close();
					dst.close();
				}
			}
		} catch (Exception e) {
			throw new Error(e);
		}
	}


	@Override
	public void onCreate(SQLiteDatabase arg0) {
	 
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		
	}
	
	
	
	public void create_Tables() {

		//create Table User
		String query =
				"CREATE TABLE IF NOT EXISTS User (" +
				"UserID       INTEGER   , " +
				"UserName    TEXT                            , " +
				"FirstName    TEXT                            , " +
				"LastName     TEXT                               ," +
				"UserType     INTEGER                               ," +
				"Address     TEXT                               ," +
				"UserPic     TEXT                              , " +
				"PublicKey     TEXT                              , " +
				"PrivateKey     TEXT                              , " +
				"PrivateKeyRefresh     TEXT                              , " +
				"PrivateKeyExpire     TEXT                              , " +
				"newUser     TEXT                              , " +
				"ActiveCode     INTEGER                           " +
				"); ";
		this.exec(query);
		//create Table Companey

		 query =
				"CREATE TABLE IF NOT EXISTS mesCompany (" +
						"pkfCompany        INTEGER   , " +
						"companyName        TEXT   , " +
						"number     TEXT                            , " +
						"CountAll     INTEGER                            , " +
						"CountSend      INTEGER                               ," +
						"CountRecive      INTEGER                               ," +
						"CountShow      INTEGER                               " +
						"); ";
		this.exec(query);
		 //.......................................................................
		query =
				"CREATE TABLE IF NOT EXISTS mesMessage (" +
						"pkfMessage        INTEGER   , " +
						"FkfUser      INTEGER                            , " +
						"message      TEXT                            , " +
						"FkfStatusMessage      INTEGER                               ," +
						"FkfTypeMessage       INTEGER                               ," +
						"FkfSource         INTEGER   , " +
						"FkfCompanyId     INTEGER                            , " +
						"sendDate      TEXT                            , " +
						"reciveDate       TEXT                               ," +
						"showDate       TEXT                               ," +
						"ansswerDate         TEXT   , " +
						"isDelete      INTEGER                            , " +
						"FkfDocument      INTEGER                            , " +
						"replay       INTEGER                               ," +
						"sendTime       TEXT                               ," +
						"reciveTime         TEXT   , " +
						"showTime      TEXT                            , " +
						"ansswerTime      TEXT                            , " +
						"sendDateEN       TEXT                               ," +
						"reciveDateEN       TEXT                            , " +
						"showDateEN       TEXT                               ," +
						"ansswerDateEN       TEXT                            , " +
						"replayDiscript       TEXT                               ," +
						"changeDateEN       TEXT                           " +

						"); ";
		this.exec(query);
		//....................................................
		query =
				"CREATE TABLE IF NOT EXISTS mesStatus (" +
						"pkfStatusMessage         INTEGER   , " +
						"statusMessage      TEXT                            , " +
						"statusMessageCode      TEXT                            , " +
						"memo      TEXT                               ," +
						"changeDateEN      TEXT                               " +

						"); ";
		this.exec(query);
		//.......................................................................
		query =
				"CREATE TABLE IF NOT EXISTS mesType (" +
						"pkfTypeMessage          INTEGER   , " +
						"typeMessage       TEXT                            , " +
						"typeMessageCode       TEXT                            , " +
						"memo      TEXT                               ," +
						"changeDateEN       TEXT                               " +
						"); ";
		this.exec(query);
		//.......................................................................
		query =
				"CREATE TABLE IF NOT EXISTS ChangeDate (" +
						"id          INTEGER   , " +
						"ChangeDate       TEXT          " +
						"); ";
		this.exec(query);
		//...........................................................
		query =
				"CREATE TABLE IF NOT EXISTS Changetype (" +
						"idmessage          INTEGER   , " +
						"type       INTEGER          " +
						"); ";
		this.exec(query);
		//............................................................
		query =
				"CREATE TABLE IF NOT EXISTS ErrorHandling  (" +
						"ErrorHandlingID       INTEGER PRIMARY KEY AUTOINCREMENT  , " +
						"ErrorType       INTEGER   , " +
						"FormName    TEXT                            , " +
						"FunctionName    TEXT                            , " +
						"MessageError     TEXT                              " +
						"); ";
		
		
		this.exec(query);


		query =
				"CREATE TABLE IF NOT EXISTS PictureCache (" +
						"UserID       INTEGER   , " +
						"UserPicName    TEXT                            " +
						"); ";


		this.exec(query);




		//Delete Table
		//query ="Delete From tblBoxDischargeItem";
		//this.exec(query);


	}

	public void  drop_Tables(String table)
	{
		//Drop Table
		String query ="Drop Table IF  EXISTS "+table;
		this.exec(query);
	}



	public void delete_Tables(String table){
		//Delete Table
		String query ="delete from "+table;
		this.exec(query);
	}

	public void exec(String query) {

		try {

			this.getWritableDatabase().execSQL(query);
		} catch (Exception e) {

		}

	}


	public Boolean insert(String table , ContentValues values) {


		try {
			this.getWritableDatabase().insert(table, null , values);
			return true;
		} catch (Exception e) {
			 return false;
		}
		 
	}


	public Boolean update(String table , ContentValues values, String strFilter) {


		try {
			this.getWritableDatabase().update(table,values,strFilter,null);
			return true;
		} catch (Exception e) {
			return false;
		}

	}

	public Boolean Delete(String table, String strFilter) {


		try {
			this.getWritableDatabase().delete(table,strFilter,null);
			return true;
		} catch (Exception e) {
			return false;
		}

	}


	public Cursor select (String query) {
		
		Cursor c = null;
		try {
			c =  this.getWritableDatabase().rawQuery(query, null);
		}catch (SQLiteException e)
		{

		}

		
		
		return c;
	}
	public Cursor select (String query, String param1, String param2) {

		Cursor c = null;
		c =  this.getWritableDatabase().rawQuery(query, new String[]{param1, param2});


		return c;
	}
	
	public boolean DropDatabase()
	{
		try {
			this.context.deleteDatabase(app.Database.dbName);
			return true;
		}catch (Exception ex)
		{
			return false;
		}

	}
	

}
