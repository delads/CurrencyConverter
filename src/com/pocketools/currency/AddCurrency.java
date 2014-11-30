package com.pocketools.currency;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.LoaderManager;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Loader;
import android.widget.CursorAdapter;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class AddCurrency extends Activity implements LoaderCallbacks<Cursor>{
	
	private String mCurrencySymbol;
	private EditText mCompanyName;
	//DBAdapter mDb;
	private TextView mUserMessage;
    ArrayList<String> currencyArray;
    private Context mContext;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);   
        setContentView(R.layout.add_currency); 
        
      //  mDb = new DBAdapter(this);
      //  mDb.open();
        
        currencyArray = new ArrayList<String>();
        mContext = this;
        
     
        this.getLoaderManager().initLoader(ConverterContentProvider.QUERY_GET_CURRENCIES, null, this);
        
        
        
        mCompanyName = (EditText)findViewById(R.id.add_stock_search);
        mUserMessage = (TextView)findViewById(R.id.user_message);
        
        mUserMessage.setText("");
   
        Button search_button = (Button)findViewById(R.id.add_stock_search_image);
        search_button.setOnClickListener(new Button.OnClickListener() {
        	public void onClick(View v) {   
        		mCurrencySymbol = mCompanyName.getText().toString().toUpperCase();
        		
        		
        		if(mCurrencySymbol.length() > 0){
        		
	            	//Let's hide the keyboard if it's still showing
	            	InputMethodManager imm = (InputMethodManager)getSystemService(v.getContext().INPUT_METHOD_SERVICE);
	            	imm.hideSoftInputFromWindow(mCompanyName.getWindowToken(), 0);
	            	
	        		
	        		if(mCurrencySymbol != null){    
	        			
	        			boolean notInDB = true;
	        			
	        			//Let's check to see if we already have the Currency in the Database
	        			
	        			Iterator<String> iter = currencyArray.iterator();
	        			while(iter.hasNext()){
	        				String currencyString = iter.next();
	        				
	        				if(currencyString.startsWith(mCurrencySymbol))
	        					notInDB = false;
	        				
	        			}
	        			
	        			/*
	        			Cursor c = mDb.getExchangeRate(mCurrencySymbol);
	        			if(c.getCount() > 0){
	        				//Then it's already in the database
	        				notInDB = false;
	        			}
	        			*/
	        			
	        			if(notInDB){
		        			String url = "http://finance.yahoo.com/d/quotes.csv?s=EUR" + mCurrencySymbol + "=X&f=l1";
		        			Log.d(getLocalClassName(),url);
			        		new SearchTickerTask(v.getContext()).execute(url);   
	        			}
	        			else
	        				mUserMessage.setText("Currency is already added !");	        			
	        		}
        		
        		}
       		}            	
		});
        
        
        
        TextView symbolLookup = (TextView)findViewById(R.id.currency_symbol_lookup);
        
        symbolLookup.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {   
        		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://en.wikipedia.org/wiki/ISO_4217"))); 
       		}            	
		});       
    }
    
    
    
    private class SearchTickerTask extends AsyncTask {
    	
    	ProgressDialog mProgressDialog;
    	Context mContext;
    	//public DBAdapter mThreadDB;
    	
    	public SearchTickerTask(Context context){
    		
    		mContext = context;
    		
    		mProgressDialog = new ProgressDialog(context);
    		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
       	 	mProgressDialog.setMessage("Searching for Currency ... ");
       	 	mProgressDialog.show();
    		
    	}
    			
	     public Object doInBackground(Object... urls) {
	    	 return URLFetcher.getString((String)urls[0]);
	    	 
	     }
	     	     
	     public void onPreExecute(){
	    	 /*
	    	 
	    	 mThreadDB = new DBAdapter(mContext);
	    	 mThreadDB.open();
	    	 mThreadDB.getDatabase().setLockingEnabled(true);

    		 while(mThreadDB.getDatabase().isDbLockedByOtherThreads()){
    			 try{
    			 Log.d(getLocalClassName(),"mThreadDB.getDatabase().isDbLockedByOtherThreads()");
    			 Thread.sleep(1000);
    			 }catch(InterruptedException e){}
    		 }	
    		 */
	    	 
	     }
	     
		 public void onCancelled(){
			 /*
			 mThreadDB.close();
			 mThreadDB = null;
			 */
			 
			 super.onCancelled();
		 }
 

	     public void onPostExecute(Object result) {	  
   	 
	    	 if(result != null){	    		 
	    		   		 
		    	 String resultString = (String)result;
		    	 
		    	 try{
			    	 StringTokenizer tokenizer = new StringTokenizer(resultString, "\n");
			    	 while(tokenizer.hasMoreTokens()){
			    		 String token = tokenizer.nextToken();
			    		 String rate = token.trim();
			    		 
			    		 
			    		 try{
			    			 

			    			   Double x_rate = Double.parseDouble(rate);
			    			   
		    			       ContentValues initialValues = new ContentValues();
		    			       initialValues.put(DBAdapter.KEY_CURRENCY, mCurrencySymbol);
		    			       initialValues.put(DBAdapter.KEY_CURRENCY_DESCRIPTION, mCurrencySymbol);
		    			       initialValues.put(DBAdapter.KEY_CURRENCY_X_RATE, x_rate);
		    			       initialValues.put(DBAdapter.KEY_CURRENCY_IS_SELECTED, 1);
		    			       initialValues.put(DBAdapter.KEY_CURRENCY_SINGLE_X_RATE_DATE, Long.toString(System.currentTimeMillis()));
		    			       
		    			       mContext.getContentResolver().insert(ConverterContentProvider.INSERT_ADD_CURRENCY_URI, initialValues);
			    			       
			    			 
			    			 /*
			    			 
			    		     ContentValues values = new ContentValues();
			    		     values.put("default_value", mCurrencyLeft);
			    				
			    				String where = "default_type = \"default_base_currency\"";
			    		        mContext.getContentResolver().update(ConverterContentProvider.UPDATE_DEFAULT_BASE_CURRENCY_URI, values, where, null);
			    		        
			    			 
			    			 
			    			 

			    			 mThreadDB.addCurrency(mCurrencySymbol, mCurrencySymbol, rateDouble, 1, Long.toString(System.currentTimeMillis()));
			    			 */
		    			       
			    			 mUserMessage.setText("\"" + mCurrencySymbol + "\" successfully added");

			    		 }
			    		 catch(NumberFormatException e){
			    			 Log.e(getLocalClassName(),"NumberFormatException");
			    			 mUserMessage.setText("Problem Loading Currency. Please try again");
			    			// mThreadDB.close();
			    		 }

			    	 }	
		    	 
		    	 }catch(Exception e){
		    		 Log.e(getLocalClassName(),"Exception"); 
		    		 mUserMessage.setText("Problem Loading Currency. Please try again");
		    		// mThreadDB.close();
		    	 }
		    	 
	    	 }   
	    	 
	    	 
    		 mProgressDialog.dismiss();
    		// mThreadDB.close();
    				    	 
	     }     
	     
	}
   
  /*  
    public boolean onKeyDown(int keyCode, KeyEvent event) { 
    	  if (keyCode == KeyEvent.KEYCODE_BACK) { 
    		  
    		  Intent intent = new Intent(this,PocketCurrency.class);
              startActivity(intent);            
              //Let's finish the current activity
              finish();            
              
    	  }  
    	  return true;
    	} 
    */

    
	
	@Override
    protected void onSaveInstanceState(Bundle outState) {      
        Log.d(this.getLocalClassName(), "onSaveInstanceState()");
        
        super.onSaveInstanceState(outState);
    }
    
    @Override
    protected void onPause() {
        Log.d(this.getLocalClassName(), "onPause()");
        
        /*
        if(mDb.getDatabase().isOpen())
        	mDb.close();

		*/
        
        super.onPause();
    }
    
    @Override
    protected void onStop() {      
        Log.d(this.getLocalClassName(), "onStop()");
        /*
        if(mDb.getDatabase().isOpen())
        	mDb.close();
  */
        super.onStop();
    }
    
    @Override
    protected void onDestroy() {
        Log.d(this.getLocalClassName(), "onDestroy()");
        
        /*
        if(mDb.getDatabase().isOpen())
        	mDb.close();
        */
        
        super.onDestroy();
        

    }
    
    @Override
    protected void onResume() {
    	Log.d(this.getLocalClassName(), "onResume()");
    	
    	/*
    	if(!mDb.getDatabase().isOpen())
    		mDb.open();

    	*/
    	
    	super.onResume();
    }
    
    @Override
    protected void onRestart() {        
    	Log.d(this.getLocalClassName(), "onRestart()");
    	
    	/*
    	if(!mDb.getDatabase().isOpen())
    		mDb.open();

      */
    	
        super.onRestart();
    }
    
    
    protected void onStart() {
    	Log.d(this.getLocalClassName(), "onStart()");
    	
    	/*
    	if(!mDb.getDatabase().isOpen())
    		mDb.open();
	*/
    	
        super.onStart();  
}

	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		// TODO Auto-generated method stub
		
	     String[] projection = { 
	  	       DBAdapter.KEY_CURRENCY,
	  	       DBAdapter.KEY_CURRENCY_DESCRIPTION,
	  	       DBAdapter.KEY_CURRENCY_X_RATE,
	  	       DBAdapter.KEY_CURRENCY_IS_SELECTED,
	  	       DBAdapter.KEY_CURRENCY_X_RATE_REFRESH_DATE,
	  	       DBAdapter.KEY_CURRENCY_SINGLE_X_RATE_DATE};
  	     
	  	     Uri baseUri = null;
	  	     
	  	     if(id == ConverterContentProvider.UPDATE_EXCHANGE_RATE){
	  	    	 baseUri = ConverterContentProvider.UPDATE_EXCHANGE_RATE_URI;
	  	  	 }
	  	     
	  	     else if(id == ConverterContentProvider.QUERY_GET_CURRENCIES){
		    	 baseUri = ConverterContentProvider.QUERY_GET_CURRENCIES_URI;
		     }
	  	     
	  	   else if(id == ConverterContentProvider.INSERT_ADD_CURRENCY){
		    	 baseUri = ConverterContentProvider.INSERT_ADD_CURRENCY_URI;
		  	 }
	  	     CursorLoader cursorLoader = new CursorLoader(this,
	  	       baseUri, projection, null, null, null);
	  	     
	  	     return cursorLoader;

	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		// TODO Auto-generated method stub
		
    	if(loader.getId() == ConverterContentProvider.QUERY_GET_CURRENCIES ){
    		
    		for(int j=0;j< data.getCount(); j++){
	        	
	        	data.moveToPosition(j);
	        	
	        	currencyArray.add(data.getString(data.getColumnIndex(DBAdapter.KEY_CURRENCY)) + "   -  " + 
	        			data.getString(data.getColumnIndex(DBAdapter.KEY_CURRENCY_DESCRIPTION)));
	        	
	        }
    	}
		
	}

	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
	}

}
