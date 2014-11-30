package com.pocketools.currency;

import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class ConverterContentProvider extends ContentProvider{
	

	 private DatabaseHelper dbHelper;
	  
	 public static final int UPDATE_SET_DEFAULT_BASE_CURRENCY_LIST = 3;
	 public static final int UPDATE_SET_DEFAULT_BASE_CURRENCY_LIST_AMOUNT = 4;
	 private static final int INSERT_ADD_DEFAULT_NEWS_CURRENCY = 5;
	 private static final int UPDATE_DEFAULT_NEWS_CURRENCY = 6;
	 private static final int UPDATE_SET_USER_AGREEMENT = 8;
	 public static final int UPDATE_EXCHANGE_RATE = 9;
	 private static final int UPDATE_UNSELECT_ALL_CURRENCIES = 	11;
	 private static final int UPDATE_RATE_DATE = 12;
	 public static final int QUERY_GET_EXCHANGE_RATES = 13;
	 public static final int QUERY_GET_EXCHANGE_RATE_DATES = 14;
	 public static final int QUERY_GET_CURRENCIES = 15;
	 public static final int QUERY_GET_SELECTED_CURRENCIES = 16;
	 private static final int QUERY_GET_CURRENCY_DETAILS = 17;
	 public static final int INSERT_ADD_CURRENCY = 18;
	 public static final int QUERY_GET_DEFAULT_VALUES = 19;
	 private static final int UPDATE_DEFAULT_BASE_CURRENCY = 20;
	 private static final int UPDATE_DEFAULT_RESULT_CURRENCY = 21;

 
	 
	 // authority is the symbolic name of your provider
	 // To avoid conflicts with other providers, you should use 
	 // Internet domain ownership (in reverse) as the basis of your provider authority. 
	 private static final String AUTHORITY = "com.pocketools.currency";
	 
	 // create content URIs from the authority by appending path to database table
	 public static final Uri INSERT_ADD_CURRENCY_URI = 
	  Uri.parse("content://" + AUTHORITY + "/addCurrency");
	 
	 
	 public static final Uri QUERY_GET_CURRENCIES_URI = 
			  Uri.parse("content://" + AUTHORITY + "/getCurrencies");
	 
	 public static final Uri QUERY_GET_DEFAULT_VALUES_URI = 
			  Uri.parse("content://" + AUTHORITY + "/getDefaultValues");
	  
	 public static final Uri QUERY_GET_SELECTED_CURRENCIES_URI = 
			  Uri.parse("content://" + AUTHORITY + "/getSelectedCurrencies");
	 
	 public static final Uri UPDATE_EXCHANGE_RATE_URI = 
			  Uri.parse("content://" + AUTHORITY + "/updateExchangeRate");
	 
	 public static final Uri UPDATE_DEFAULT_BASE_CURRENCY_URI = 
			  Uri.parse("content://" + AUTHORITY + "/updateDefaultBaseCurrency");
	 
	 public static final Uri UPDATE_DEFAULT_RESULT_CURRENCY_URI = 
			  Uri.parse("content://" + AUTHORITY + "/updateDefaultResultCurrency");
	 
	 public static final Uri UPDATE_SET_DEFAULT_BASE_CURRENCY_LIST_URI = 
			  Uri.parse("content://" + AUTHORITY + "/updateSetDefaultBaseCurrencyList");
	 
	 public static final Uri UPDATE_SET_DEFAULT_BASE_CURRENCY_LIST_AMOUNT_URI = 
			  Uri.parse("content://" + AUTHORITY + "/updateSetDefaultBaseCurrencyList");


	 		 
		 
	 
	 // a content URI pattern matches content URIs using wildcard characters:
	 // *: Matches a string of any valid characters of any length.
	    // #: Matches a string of numeric characters of any length.
	 private static final UriMatcher uriMatcher;
	 static {
	  uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	  uriMatcher.addURI(AUTHORITY, "addCurrency", INSERT_ADD_CURRENCY);
	  uriMatcher.addURI(AUTHORITY, "getCurrencies", QUERY_GET_CURRENCIES);
	  uriMatcher.addURI(AUTHORITY, "getSelectedCurrencies", QUERY_GET_SELECTED_CURRENCIES);
	  uriMatcher.addURI(AUTHORITY, "updateExchangeRate", UPDATE_EXCHANGE_RATE);
	  uriMatcher.addURI(AUTHORITY, "getDefaultValues", QUERY_GET_DEFAULT_VALUES);
	  uriMatcher.addURI(AUTHORITY, "updateDefaultBaseCurrency", UPDATE_DEFAULT_BASE_CURRENCY);
	  uriMatcher.addURI(AUTHORITY, "updateDefaultResultCurrency", UPDATE_DEFAULT_RESULT_CURRENCY);
	  uriMatcher.addURI(AUTHORITY, "updateSetDefaultBaseCurrencyList", UPDATE_SET_DEFAULT_BASE_CURRENCY_LIST);
	  uriMatcher.addURI(AUTHORITY, "updateSetDefaultBaseCurrencyListAmount", UPDATE_SET_DEFAULT_BASE_CURRENCY_LIST_AMOUNT);

	  
	 }
	 
	 // system calls onCreate() when it starts up the provider.
	 @Override
	 public boolean onCreate() {
	  // get access to the database helper
	  dbHelper = new DatabaseHelper(getContext());
	  return false;
	 }
	 
	 //Return the MIME type corresponding to a content URI
	 @Override
	 public String getType(Uri uri) {
	   
	  switch (uriMatcher.match(uri)) {
	  case QUERY_GET_CURRENCIES: 
	   return "vnd.android.cursor.dir/vnd.com.pocketools.currency.currency";
	   
	  case QUERY_GET_SELECTED_CURRENCIES: 
		   return "vnd.android.cursor.dir/vnd.com.pocketools.currency.currency";
		   
	  case UPDATE_EXCHANGE_RATE: 
		   return "vnd.android.cursor.dir/vnd.com.pocketools.currency.currency";
		   
	  case QUERY_GET_DEFAULT_VALUES: 
		   return "vnd.android.cursor.dir/vnd.com.pocketools.currency.currency";
		   
	  case UPDATE_DEFAULT_BASE_CURRENCY: 
		   return "vnd.android.cursor.dir/vnd.com.pocketools.currency.currency";
		   
	  case UPDATE_DEFAULT_RESULT_CURRENCY: 
		   return "vnd.android.cursor.dir/vnd.com.pocketools.currency.currency";
		   
	  case UPDATE_SET_DEFAULT_BASE_CURRENCY_LIST: 
		   return "vnd.android.cursor.dir/vnd.com.pocketools.currency.currency";
		   
	  case UPDATE_SET_DEFAULT_BASE_CURRENCY_LIST_AMOUNT: 
		   return "vnd.android.cursor.dir/vnd.com.pocketools.currency.currency";
		   

		   
	  default: 
	   throw new IllegalArgumentException("Unsupported URI: " + uri);
	  }
	 }
	 
	 // The insert() method adds a new row to the appropriate table, using the values 
	 // in the ContentValues argument. If a column name is not in the ContentValues argument, 
	 // you may want to provide a default value for it either in your provider code or in 
	 // your database schema. 
	 @Override
	 public Uri insert(Uri uri, ContentValues values) {
		 
		 SQLiteDatabase db = dbHelper.getWritableDatabase();
		  int rowsAffected = 0;
		  Uri result = null;
		  
		  switch (uriMatcher.match(uri)) {
		  
		  
		    
		  case INSERT_ADD_CURRENCY:
			  
			  db.insert("currency", null, values);
			  getContext().getContentResolver().notifyChange(uri, null);
			  
			  Log.d(getClass().getName(),"INSERT_ADD_CURRENCY CALLED" );			  
			  break;
		  }
		  
		 return null;
		 
	  }
	  
	 
	 // The query() method must return a Cursor object, or if it fails, 
	 // throw an Exception. If you are using an SQLite database as your data storage, 
	 // you can simply return the Cursor returned by one of the query() methods of the 
	 // SQLiteDatabase class. If the query does not match any rows, you should return a 
	 // Cursor instance whose getCount() method returns 0. You should return null only 
	 // if an internal error occurred during the query process. 
	 @Override
	 public Cursor query(Uri uri, String[] projection, String selection,
	   String[] selectionArgs, String sortOrder) {
	 
	  SQLiteDatabase db = dbHelper.getWritableDatabase();  
	  String sql = "";
	  
	  switch (uriMatcher.match(uri)) {
	  
	  case INSERT_ADD_CURRENCY:
		  sql = "SELECT * FROM currency ORDER BY currency_type ASC";
		  break;
		  

	  case QUERY_GET_CURRENCIES:		  
		  sql = "SELECT * FROM currency ORDER BY currency_type ASC";
		
		   break;
		   
	  case QUERY_GET_DEFAULT_VALUES:		  
		  sql = "SELECT * FROM defaults";
		
		   break;
		  
	  case QUERY_GET_SELECTED_CURRENCIES:		  
		  sql = "SELECT * FROM currency WHERE exchange_rate_selected = 1 ORDER BY currency_type ASC";
		  
		   break;
		   
	  case QUERY_GET_EXCHANGE_RATES:		  
		  sql = "SELECT * FROM currency ORDER BY currency_type ASC";
		
		   break;
		  
	  case UPDATE_EXCHANGE_RATE:
		  
		  //Let's just return a general cursor. Doesn't matter as we are only interested in the ().update for this uri
		  sql = "SELECT * FROM currency ORDER BY currency_type ASC";
		
		   break;
		   
	  case UPDATE_DEFAULT_BASE_CURRENCY:
		  
		//Let's just return a general cursor. Doesn't matter as we are only interested in the ().update for this uri
		  sql = "SELECT * FROM defaults";
		  break;
		  
	  case UPDATE_DEFAULT_RESULT_CURRENCY:
		  
		//Let's just return a general cursor. Doesn't matter as we are only interested in the ().update for this uri
		  sql = "SELECT * FROM defaults";
		  break;
	
	  case UPDATE_SET_DEFAULT_BASE_CURRENCY_LIST:
		  
			//Let's just return a general cursor. Doesn't matter as we are only interested in the ().update for this uri
			  sql = "SELECT * FROM defaults";
			  break;
	
	  case UPDATE_SET_DEFAULT_BASE_CURRENCY_LIST_AMOUNT:
		  
			//Let's just return a general cursor. Doesn't matter as we are only interested in the ().update for this uri
			  sql = "SELECT * FROM defaults";
			  break;

	  
	  default:
	   throw new IllegalArgumentException("Unsupported URI: " + uri);
	  
	  }
	  
	  
	  Cursor c = db.rawQuery(sql, null);
	  c.setNotificationUri(getContext().getContentResolver(), uri);
	  
	  return c;
	 
	 }
	  
	 // The delete() method deletes rows based on the seletion or if an id is 
	 // provided then it deleted a single row. The methods returns the numbers
	 // of records delete from the database. If you choose not to delete the data
	 // physically then just update a flag here.
	 @Override
	 public int delete(Uri uri, String selection, String[] selectionArgs) {
	 
	  SQLiteDatabase db = dbHelper.getWritableDatabase();
	  switch (uriMatcher.match(uri)) {
	  
	  default:
	   throw new IllegalArgumentException("Unsupported URI: " + uri);
	  }
	 }
	 
	 
	 // The update method() is same as delete() which updates multiple rows
	 // based on the selection or a single row if the row id is provided. The
	 // update method returns the number of updated rows.
	 @Override
	 public int update(Uri uri, ContentValues values, String selection,
	   String[] selectionArgs) {
		 
	  SQLiteDatabase db = dbHelper.getWritableDatabase();
	  int rowsAffected = 0;
	  
	  switch (uriMatcher.match(uri)) {
	  
	  
	    
	  case UPDATE_EXCHANGE_RATE:
		  
		  rowsAffected = db.update("currency", values, selection , null);
		  getContext().getContentResolver().notifyChange(uri, null);
		  
		  Log.d(getClass().getName(),"UPDATE_EXCHANGE_RATE CALLED - SELECTION = " + selection + " : Rows affected = " + rowsAffected);
		  
		  break;
		  
	  case UPDATE_DEFAULT_BASE_CURRENCY:
		  
		  rowsAffected = db.update("defaults", values, selection , null);
		  getContext().getContentResolver().notifyChange(uri, null);
		  
		  Log.d(getClass().getName(),"UPDATE_DEFAULT_BASE_CURRENCY CALLED - SELECTION = " + selection);
		  
		  break;
		  
	  case UPDATE_DEFAULT_RESULT_CURRENCY:
		  
		  rowsAffected = db.update("defaults", values, selection , null);
		  getContext().getContentResolver().notifyChange(uri, null);
		  
		  Log.d(getClass().getName(),"UPDATE_DEFAULT_RESULT_CURRENCY CALLED - SELECTION = " + selection);
		  break;
		  
	  case UPDATE_SET_DEFAULT_BASE_CURRENCY_LIST:
		  
		  rowsAffected = db.update("defaults", values, selection , null);
		  getContext().getContentResolver().notifyChange(uri, null);
		  
		  Log.d(getClass().getName(),"UPDATE_SET_DEFAULT_BASE_CURRENCY_LIST CALLED - SELECTION = " + selection);
		  
		  break;
		  
	  case UPDATE_SET_DEFAULT_BASE_CURRENCY_LIST_AMOUNT:
		  
		  rowsAffected = db.update("defaults", values, selection , null);
		  getContext().getContentResolver().notifyChange(uri, null);
		  
		  Log.d(getClass().getName(),"UPDATE_SET_DEFAULT_BASE_CURRENCY_LIST_AMOUNT CALLED - SELECTION = " + selection);
		  
		  break;
		  
	  default:
		   throw new IllegalArgumentException("Unsupported URI: " + uri);
		  }  
		  
	 return rowsAffected;
		  
	 }
	 
	}
