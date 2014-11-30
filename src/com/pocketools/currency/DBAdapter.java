package com.pocketools.currency;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;




/**
* Simple notes database access helper class. Defines the basic CRUD operations
* for the notepad example, and gives the ability to list all notes as well as
* retrieve or modify a specific note.
* 
* This has been improved from the first version of this tutorial through the
* addition of better error handling and also using returning a Cursor instead
* of using a collection of inner classes (which is less scalable and not
* recommended).
*/

public class DBAdapter {

	   public static final String KEY_CURRENCY = "currency_type";
	   public static final String KEY_CURRENCY_DESCRIPTION = "currency_description";
	   public static final String KEY_CURRENCY_X_RATE = "exchange_rate";
	   public static final String KEY_CURRENCY_IS_SELECTED = "exchange_rate_selected";
	   public static final String KEY_CURRENCY_X_RATE_REFRESH_DATE = "xrate_refresh_date";
	   public static final String KEY_CURRENCY_SINGLE_X_RATE_DATE = "spare_1";
	   public static final String KEY_DEFAULT_VALUE = "default_value";
	   public static final String KEY_DEFAULT_TYPE = "default_type";


   private DatabaseHelper mDbHelper;
   private SQLiteDatabase mDb;
   
   
   
   /**
    * Database creation sql statement
    */

   private static final String CURRENCY_TABLE = "currency";


   private Context mCtx = null;

   

   /**
    * Constructor - takes the context to allow the database to be
    * opened/created
    * 
    * @param ctx the Context within which to work
    */
   public DBAdapter(Context ctx) {
       this.mCtx = ctx;
   }
   
   public Context getContext(){
	   return mCtx;
   }

   /**
    * Open the notes database. If it cannot be opened, try to create a new
    * instance of the database. If it cannot be created, throw an exception to
    * signal the failure
    * 
    * @return this (self reference, allowing this to be chained in an
    *         initialization call)
    * @throws SQLException if the database could be neither opened or created
    */
   public DBAdapter open() throws SQLException {
       mDbHelper = new DatabaseHelper(mCtx);
       mDb = mDbHelper.getWritableDatabase();
       return this;
   }
   
   public void close() {
       mDbHelper.close();
   }
   
   public SQLiteDatabase getDatabase(){
	   return mDb;
   }
   
   public void addDefaultPage(String default_page){	   
	   mDb.execSQL("INSERT INTO defaults VALUES (\"default_page\",\"" + default_page + "\")");
   }
   
   public void addDefaultLocation(String default_location){	   
	   mDb.execSQL("INSERT INTO defaults VALUES (\"default_location\",\"" + default_location + "\")");
   }
   
   public void addAdPreferences(String ad_preferences){	   
	   mDb.execSQL("INSERT INTO defaults VALUES (\"ad_preferences\",\"" + ad_preferences + "\")");
   }
   
   public void updateDefaultPage(String default_page){
	   String sql = "UPDATE defaults SET default_value = \"" + default_page + "\" WHERE default_type = \"default_page\"";
	   mDb.execSQL(sql);
   }
   
   public void addVisitNumberInDB(){	   
	   mDb.execSQL("INSERT INTO defaults VALUES (\"visit_number\",\"1\")");
   }
   
   public void updateVisitNumber(String visit_number){
	   String sql = "UPDATE defaults SET default_value = \"" + visit_number + "\" WHERE default_type = \"visit_number\"";
	   mDb.execSQL(sql);
   }
   
   public void updateDefaultLocation(String default_location){
	   String sql = "UPDATE defaults SET default_value = \"" + default_location + "\" WHERE default_type = \"default_location\"";
	   mDb.execSQL(sql);
   }
   
   public void updateAdPreferences(String ad_preferences){
	   String sql = "UPDATE defaults SET default_value = \"" + ad_preferences + "\" WHERE default_type = \"ad_preferences\"";
	   mDb.execSQL(sql);
   }
   
   
   public void setDefaultBaseCurrencyList(String currency_type){
	   
	   String sql = "UPDATE defaults SET default_value = \"" + currency_type + "\" WHERE default_type = \"default_base_currency_list\"";
	   mDb.execSQL(sql);
	   
   }
   
   public void setDefaultBaseCurrencyListAmount(String currency_amount){
	   
	   String sql = "UPDATE defaults SET default_value = \"" + currency_amount + "\" WHERE default_type = \"default_base_currency_amount\"";
	   mDb.execSQL(sql);
	   
   }
   
   public void addDefaultNewsCurrency(String currency_type){
	   
	   String sql = "INSERT INTO defaults VALUES (\"default_news_currency\",\"" + currency_type + "\")";
	   mDb.execSQL(sql);
	   
   }
   
   public void upgradeToAdsFreeVersion(){
	   String sql = "INSERT INTO defaults VALUES (\"ads_free_version\",\"true\")";
	   mDb.execSQL(sql);
   }
   
   
   public void setDealInstructionsSeen(String seen){
	   String sql = "INSERT INTO defaults VALUES (\"deal_instructions_seen\",\"" + seen + "\")";
	   mDb.execSQL(sql);
   }
   
   
   
   public void updateDefaultNewsCurrency(String currency_type){
	   
	   String sql = "UPDATE defaults SET default_value = \"" + currency_type + "\" WHERE default_type = \"default_news_currency\"";
	   mDb.execSQL(sql);
	   
   }
   
  
   
   public void setDefaultBaseCurrency(String currency_type){
   
	   String sql = "UPDATE defaults SET default_value = \"" + currency_type + "\" WHERE default_type = \"default_base_currency\"";
	   mDb.execSQL(sql);
	   
   }
   
   
   public void setDefaultResultCurrency(String currency_type){
	   
	   String sql = "UPDATE defaults SET default_value = \"" + currency_type + "\" WHERE default_type = \"default_result_currency\"";
	   mDb.execSQL(sql);
	   
   }
   
   

   
   public void setUserAgreement(String accpeted_rejected){
	   String sql = "UPDATE defaults SET default_value = \"" + accpeted_rejected + "\" WHERE default_type = \"user_agreement_accepted\"";
	   mDb.execSQL(sql);
   }
   
   
   public Cursor getDefaultValues(){
	   String sql = "SELECT * from defaults";
	   return mDb.rawQuery(sql, null);
   }
   
   public void updateExchangeRate(String currency_type, double exchange_rate, String rateDate){
	   
	   String sql = "UPDATE currency SET exchange_rate = " + exchange_rate + " , spare_1 = \"" + rateDate + "\" WHERE currency_type = \"" + currency_type + "\" ";
	   mDb.execSQL(sql);
	   
   }
   
   public void updateExchangeRateSelected(String currency_type, int is_selected){
	   
	   String sql = "UPDATE currency SET exchange_rate_selected = " + is_selected + " WHERE currency_type = \"" + currency_type + "\" ";
	   mDb.execSQL(sql);
	   
   }
   
   public void unselectAllCurrencies(){
	   String sql = "UPDATE currency SET exchange_rate_selected = 0";
	   mDb.execSQL(sql);
   }
   
   public void updateRateDate(String xrate_refresh_date){
	   
	   String sql = "UPDATE defaults SET default_value = \"" + xrate_refresh_date + "\" WHERE default_type = \"refresh_date\"";
	   mDb.execSQL(sql);
	   
   }
   
   public Cursor getExchangeRate(String currency_type){
	   String sql = "SELECT exchange_rate from currency WHERE currency_type = \"" + currency_type + "\"";
	   return mDb.rawQuery(sql, null);
   }
   
   public Cursor getExchangeRateDate(String currency_type){
	   String sql = "SELECT spare_1 from currency WHERE currency_type = \"" + currency_type + "\"";
	   return mDb.rawQuery(sql, null);
   }
   
   
 
   public Cursor getCurrencies(){
	   String sql = "SELECT * FROM currency ORDER BY currency_type ASC";
	   
	   return mDb.rawQuery(sql, null);
   }
   
   public Cursor getSelectedCurrencies(){
	   String sql = "SELECT * FROM currency WHERE exchange_rate_selected = 1 ORDER BY currency_type ASC";
	   return mDb.rawQuery(sql, null);
   }
   
   public Cursor getCurrencyDetails(String currency){
	   String sql = "SELECT * FROM currency WHERE currency_type = \"" + currency + "\"";
	   return mDb.rawQuery(sql, null);
   }
   
   
   public long addCurrency(String symbol, String name, double x_rate, int selected, String last_updated) {
       ContentValues initialValues = new ContentValues();
       initialValues.put(KEY_CURRENCY, symbol);
       initialValues.put(KEY_CURRENCY_DESCRIPTION, name);
       initialValues.put(KEY_CURRENCY_X_RATE, x_rate);
       initialValues.put(KEY_CURRENCY_IS_SELECTED, selected);
       initialValues.put(KEY_CURRENCY_SINGLE_X_RATE_DATE, last_updated);

       return mDb.insert(CURRENCY_TABLE, null, initialValues);
   }

   
   
   

   
   
}

