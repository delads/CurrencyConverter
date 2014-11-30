package com.pocketools.currency;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

//import com.google.android.apps.analytics.GoogleAnalyticsTracker;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;



public class EditBaseCurrency extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor>{
    /** Called when the activity is first created. */
	
//	DBAdapter mDb;
	String mDefaultCurrency;
	String mDefaultCurrencyNew;
	String mDefaultAmount;
	ArrayAdapter<String> mArrayAdapter;
	EditText mEditTextAmount;
//	GoogleAnalyticsTracker mTracker;
	ArrayList<String> currencyArray;
	Intent mIntent;
	View mRootView;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*
        mTracker = GoogleAnalyticsTracker.getInstance();       
        // Start the tracker in manual dispatch mode...
        mTracker.startNewSession(PocketCurrency.ANALYTICS_UA_NUMBER, this);
        mTracker.trackPageView("/EditBaseCurrency");
        */
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);       
        mRootView = LayoutInflater.from(this).inflate(R.layout.edit_base_currency, null);
        setContentView(mRootView);
        
        
        currencyArray = new ArrayList<String>();
        getSupportLoaderManager().initLoader(ConverterContentProvider.QUERY_GET_SELECTED_CURRENCIES, null, this);
        
       

    

        
        //mDb = new DBAdapter(this);
        //mDb.open();
        /*
        
        Cursor currencies = mDb.getCurrencies();
        startManagingCursor(currencies);
        
       // StringBuffer currencylist = new StringBuffer();
        
        for(int i=0; i < currencies.getCount(); i++){
        	
        	currencies.moveToPosition(i);
        	
       // 	currencylist.append("+EUR" + currencies.getString(currencies.getColumnIndex(DBAdapter.KEY_CURRENCY)) + "=X");
        	
        	currencyArray.add(currencies.getString(currencies.getColumnIndex(DBAdapter.KEY_CURRENCY)) + "   -  " + 
        			currencies.getString(currencies.getColumnIndex(DBAdapter.KEY_CURRENCY_DESCRIPTION)));
        }
        */
        
        //String temp = currencylist.toString();
        //String temp = "EURUSD=X";
        
        /*
        Cursor defaults = mDb.getDefaultValues();
        startManagingCursor(defaults);
        
        String defaultCurrency = "USD";
        
        for(int i=0; i< defaults.getCount(); i++){
        	defaults.moveToPosition(i);
        	
        	String default_type = defaults.getString(defaults.getColumnIndex(DBAdapter.KEY_DEFAULT_TYPE)); 
        	
        	
        	if(default_type.compareTo("default_base_currency_list") ==0)
        		defaultCurrency = defaults.getString(defaults.getColumnIndex(DBAdapter.KEY_DEFAULT_VALUE));
        	
        	if(default_type.compareTo("default_base_currency_amount") ==0)
        		mDefaultAmount = defaults.getString(defaults.getColumnIndex(DBAdapter.KEY_DEFAULT_VALUE));
        }
        
        int spinnerPosition = 0;
        
	    Iterator<String> iter = currencyArray.iterator();
	    int count = 0;
	    
        while(iter.hasNext()){
	      	
        	String longCurrency = iter.next();
        	
        	String currency = longCurrency.substring(0,longCurrency.indexOf(" "));     	
        	
        	if(currency.compareTo(defaultCurrency) == 0)
        		spinnerPosition = count;
        	
        	count++;
        	
        }    
        */
        
        
        Spinner spinner = (Spinner) findViewById(R.id.currency_spinner_base);
        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, currencyArray);
        mArrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(mArrayAdapter);
 //       spinner.setSelection(0);
        
        
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
    		public void onItemSelected(AdapterView parent, View v, int position,
                  long id) {
    				String selectedCurrencyLong = (String)mArrayAdapter.getItem(position);
    				mDefaultCurrencyNew = selectedCurrencyLong.substring(0,selectedCurrencyLong.indexOf(" ")); 				
              }
    	        public void onNothingSelected(AdapterView arg0) {
    	          // NOP
    	        }
    	      });
        
        
        
        mEditTextAmount = (EditText)findViewById(R.id.edit_base_amount);
        mEditTextAmount.setText("1");
        /*
        mEditTextAmount.addTextChangedListener(new TextWatcher(){
    		String currentWord;
    		private boolean IsValid( CharSequence s ){
    			try{
    				if(s.length() > 0)
    					Double.parseDouble(s.toString());
    			}
    			catch(Exception e){ return false;}
    			
    			return true;
    		}

    		public void beforeTextChanged(CharSequence s, int start, int count, int after){
    			currentWord = s.toString();
    		}
    		
    		public void afterTextChanged(Editable s){}
        		
        		
    		public void onTextChanged(CharSequence s, int start, int before, int count){

        		if( !IsValid(s) ){
        			mEditTextAmount.setText(currentWord);
        			mEditTextAmount.setSelection(start);	
        		}
    		}
    		
    		
        
        });
        
        */
        
        
        Button saveButton = (Button)findViewById(R.id.set_base_currency_button);      
        saveButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	/*
            	mDb.setDefaultBaseCurrencyList(mDefaultCurrencyNew);
            	mDb.setDefaultBaseCurrencyListAmount(mEditTextAmount.getText().toString());
            	
            	mDb.close();
            	*/
            	
            	ContentValues values = new ContentValues();
   			 	values.put("default_value", mDefaultCurrencyNew);
   			
   			 	String where = "default_type = \"default_base_currency_list\"";
   			 	getContentResolver().update(ConverterContentProvider.UPDATE_SET_DEFAULT_BASE_CURRENCY_LIST_URI, values, where, null);
   	  
   	        
   			 	
   			    values = new ContentValues();
			 	values.put("default_value", mEditTextAmount.getText().toString());
			
			 	where = "default_type = \"default_base_currency_amount\"";
			 	getContentResolver().update(ConverterContentProvider.UPDATE_SET_DEFAULT_BASE_CURRENCY_LIST_AMOUNT_URI, values, where, null);
   	        
            	
            	setResult(CurrencyList.RELOAD_CURRENCY_LIST_SUCCESS, mIntent);
    	    	finish(); 			
            }
        });
        
        
     //   mTracker.dispatch();
       
    }
    
   
    @Override
    protected void onSaveInstanceState(Bundle outState) {      
        //Log.d(this.getLocalClassName(), "onSaveInstanceState()");
        
        super.onSaveInstanceState(outState);
    }
    
    @Override
    protected void onPause() {
        //Log.d(this.getLocalClassName(), "onPause()");
        /*
        if(mDb.getDatabase().isOpen())
        	mDb.close();
*/
        super.onPause();
    }
    
    @Override
    protected void onStop() {      
        //Log.d(this.getLocalClassName(), "onStop()");
        

        /*
        if(mDb.getDatabase().isOpen())
        	mDb.close();
  */
        super.onStop();
    }
    
    @Override
    protected void onDestroy() {
        //Log.d(this.getLocalClassName(), "onDestroy()");
        /*
        if(mDb.getDatabase().isOpen())
        	mDb.close();
        */
    	
        super.onDestroy();
        
      //  mTracker.stop();
    }
    
    @Override
    protected void onResume() {
    	//Log.d(this.getLocalClassName(), "onResume()");
    	/*
    	if(!mDb.getDatabase().isOpen())
    		mDb.open();
    	*/
    	
    	super.onResume();
    }
    
    @Override
    protected void onRestart() {        
    	//Log.d(this.getLocalClassName(), "onRestart()");
    	/*
    	if(!mDb.getDatabase().isOpen())
    		mDb.open();
      */
    	
        super.onRestart();
    }
    
    
    protected void onStart() {
    	//Log.d(this.getLocalClassName(), "onStart()");
    	/*
    	if(!mDb.getDatabase().isOpen())
    		mDb.open();
	*/
    	
        super.onStart();  
}


	@Override
	public Loader<Cursor> onCreateLoader(int id, Bundle args) {
		String[] projection = { 
			       DBAdapter.KEY_CURRENCY,
			       DBAdapter.KEY_CURRENCY_DESCRIPTION,
			       DBAdapter.KEY_CURRENCY_X_RATE,
			       DBAdapter.KEY_CURRENCY_IS_SELECTED,
			       DBAdapter.KEY_CURRENCY_X_RATE_REFRESH_DATE,
			       DBAdapter.KEY_CURRENCY_SINGLE_X_RATE_DATE};
			     
	     
			     Uri baseUri = null;
			     
			     if(id == ConverterContentProvider.QUERY_GET_SELECTED_CURRENCIES){
			    	 baseUri = ConverterContentProvider.QUERY_GET_SELECTED_CURRENCIES_URI;
			     }	


//			     Log.e(getActivity().getLocalClassName(),"BaseUri = " + baseUri.getLastPathSegment());
			     
			     CursorLoader cursorLoader = new CursorLoader(this,
			       baseUri, projection, null, null, null);
			     
			     return cursorLoader;
	}


	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		
    	if(loader.getId() == ConverterContentProvider.QUERY_GET_SELECTED_CURRENCIES ){
    		
    		for(int j=0;j< data.getCount(); j++){
	        	
	        	data.moveToPosition(j);
	        	String currencyString = data.getString(data.getColumnIndex(DBAdapter.KEY_CURRENCY)) + "   -  " + 
	        			data.getString(data.getColumnIndex(DBAdapter.KEY_CURRENCY_DESCRIPTION));
	        	
	        	boolean alreadyInList = false;
	        	
	        	for(String s : currencyArray){
	        		if(s.equals(currencyString)){
	        			alreadyInList = true;
	        			break;
	        		}		        			
	        	}
	        	
	        	if(!alreadyInList)
	        		currencyArray.add(currencyString);
	        	

	        	mArrayAdapter.notifyDataSetChanged();
	        }

    		
    	}
		
	}


	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
	}

 
}