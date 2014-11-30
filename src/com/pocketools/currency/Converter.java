package com.pocketools.currency;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;

import com.mopub.mobileads.MoPubView;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;

import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.SQLException;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;

import android.os.AsyncTask;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;

import android.util.Log;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.content.CursorLoader;
import com.mopub.mobileads.MoPubView;


/**
 * A fragment that launches other parts of the demo application.
 */
public  class Converter extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{
	
	
	ArrayAdapter<String> mLeftAdapter;
	ArrayAdapter<String> mRightAdapter;
	String mCurrencyLeft = "USD";
	String mCurrencyRight = "EUR";
	String mRefreshDateText = "";
//	DBAdapter mDb;
	double mBaseAmount;
	ImageButton mGraph;
	 // TODO: Replace this test id with your personal ad unit id
		private static final String MOPUB_BANNER_AD_UNIT_ID = "2396829d6afe443ba9f585947777282e";
		private MoPubView moPubView;


	 
	
	private NotificationManager mNotificationManager;

    private static final int HELLO_ID = 8000;
    private static final int MENU_REFRESH = 8001;
    private static final int MENU_ADD_CURRENCY = 8002;
    private static final int MENU_INFO = 8008;
    private static final int MENU_SETTINGS = 8009;
    private static final int ALERT_DIALOG_USER_AGREEMENT = 8003;
    private static final int DIALOG_GRAPH_TIMEFRAME = 8004;
    public static final int CHOOSE_TIMEFRAME = 8005;
    public static final int CHOOSE_TIMEFRAME_SUCCESS = 8006;
    public static final int INTENT_REQUEST_CODE_ADD_CURRENCY = 8010;
    
    private static final int DIALOG_CANNOT_CONNECT_ID = 8001;
    


    
    int mSpinnerLeftPosition = 0;
    int mSpinnerRightPosition = 0;
    View mPocketCurrencyView = null;
    Spinner mLeftSpinner = null;
    Spinner mRightSpinner = null;
    EditText mResult;
    TextView mXrateUpdateMessage = null;
    double mXrate = 0.0;
    String mXrateString = "";
    EditText mLeftResult = null;
    EditText mRightResult = null;
    String mCurrencyBeingUpdated = "";
    DownloadXRatesTask mXratesTask;
    DownloadXRatesTask mSingleXratesTask;
    DownloadImageTask mDownloadImageTask;
	private AnimationDrawable mGraphAnimation;
	private boolean mChartDisplayed = false;
	private boolean mSwapDisplayed = true;
	private ImageView mGraphLoading;
	private Context mContext;
	
	private HashMap<String,Double> mRateMap;
	private HashMap<String, String> mRateDateMap;
	
	
    
    String mGraphURL = "";
    TableRow mTimeframeRow = null;
    boolean firstCallLeft = false;
    boolean firstCallRight = false;
   // public static final String ANALYTICS_UA_NUMBER = "UA-12361531-2";  
    public static final String AD_MOB_KEYWORD_HINT = "";
    private static final int URL_LOADER = 0;

    String mCurrencyString;
    ArrayList<String> currencyArray;
    ArrayList<String> mSelectedCurrencyArray;
    NetworkInfo mNetworkInfo = null;

    
   TableRow mGoogleAdRow = null;

    ImageView mSwapImage;
    String mLastUpdate;
    String mBaseCurrencyList="";

    @Override
    public void onCreate (Bundle savedInstanceState){
    	Log.e(this.getClass().getSimpleName(),"OnCreate");
    	
    	super.onCreate(savedInstanceState);
    }
	

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
    	
    	Log.e(this.getClass().getSimpleName(),"OnCreateView");
    	
        View rootView = inflater.inflate(R.layout.converter, container, false);
        
        this.setHasOptionsMenu(true);
    	
    	mContext = container.getContext();
    	
    	//mPocketCurrencyView = inflater.inflate(R.layout.main, container, false);
        
    	//setContentView(rootView);
    	
    	//Initialise some variables
        firstCallLeft = true;
        firstCallRight = true;
        
        currencyArray = new ArrayList<String>();
        mSelectedCurrencyArray = new ArrayList<String>();
        mRateMap = new HashMap<String,Double>();
        mRateDateMap = new HashMap<String,String>();
        
        boolean mCurrencyListUpdated = false;
        
        
        
        //Let's add three of the main currencies up the top first
        currencyArray.add("EUR   -  Euro");
        currencyArray.add("GBP   -  United Kingdom Pounds");
        currencyArray.add("USD   -  United States Dollars");
        
        
        getLoaderManager().initLoader(ConverterContentProvider.QUERY_GET_CURRENCIES, null, this);
        getLoaderManager().initLoader(ConverterContentProvider.QUERY_GET_SELECTED_CURRENCIES, null, this);
        getLoaderManager().initLoader(ConverterContentProvider.UPDATE_EXCHANGE_RATE, null, this);
        
        
        

        
       /* 
        Cursor defaults = mDb.getDefaultValues();
        getActivity().startManagingCursor(defaults);
        
        
        
        
        
        for(int i=0; i< defaults.getCount(); i++){
        	defaults.moveToPosition(i);
        	
        	
        	
        	String default_type = defaults.getString(defaults.getColumnIndex(DBAdapter.KEY_DEFAULT_TYPE));
        	
        	
        	if(default_type.compareTo("user_agreement_accepted") ==0){
        		String accepted = defaults.getString(defaults.getColumnIndex(DBAdapter.KEY_DEFAULT_VALUE));
        		
        	}        	
        	else if(default_type.compareTo("default_base_currency") ==0){
        		
        		if(defaults.getString(defaults.getColumnIndex(DBAdapter.KEY_DEFAULT_VALUE)).length() > 0)
        			mCurrencyLeft = defaults.getString(defaults.getColumnIndex(DBAdapter.KEY_DEFAULT_VALUE));
        	}
        	
        	else if(default_type.compareTo("default_result_currency") ==0){
        		
        		if(defaults.getString(defaults.getColumnIndex(DBAdapter.KEY_DEFAULT_VALUE)).length() > 0)
        				mCurrencyRight = defaults.getString(defaults.getColumnIndex(DBAdapter.KEY_DEFAULT_VALUE));
        	}
        	
        	else if(default_type.compareTo("default_base_currency_list") ==0)
        		mBaseCurrencyList = defaults.getString(defaults.getColumnIndex(DBAdapter.KEY_DEFAULT_VALUE));
        	
        }
        */
        
        

        
        mXrateUpdateMessage = (TextView)rootView.findViewById(R.id.xrate_update_message);  
        
        mLeftResult = (EditText)rootView.findViewById(R.id.currency_left_text);
        mRightResult = (EditText)rootView.findViewById(R.id.currency_right_text);

        mGraph = (ImageButton)rootView.findViewById(R.id.graph_button);
        
        mTimeframeRow = (TableRow)rootView.findViewById(R.id.timeframeRowMain);
        mTimeframeRow.setVisibility(View.GONE);
        mTimeframeRow.setClickable(true); 
        
        
        
        mLeftResult.addTextChangedListener(new TextWatcher(){
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
 			
    			if(mLeftResult.isFocused()){
	        		if( !IsValid(s) ){
	        			//mLeftResult.setText(currentWord);
	        			//mLeftResult.setSelection(start);	

	        		}
	        		else {
	        			
	        			if(s.length() > 0){
		        			Double leftAmount = Double.parseDouble(s.toString());
		        			Double rightResult = leftAmount * mXrate;
	        			
		        			DecimalFormat decimal = new DecimalFormat("#0.00");  		        		
		              	  	mRightResult.setText(decimal.format(rightResult));

	        			}
	        			else{
	        				mRightResult.setText(s);
	        			}
	        		}
    			}
    		}
        }
        );
        
        
        mRightResult.addTextChangedListener(new TextWatcher(){
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
    			
    			if(mRightResult.isFocused()){
    				
    				
	        		if( !IsValid(s) ){
	        			//mRightResult.setText(currentWord);
	        			//mRightResult.setSelection(start);
	
	        		}
	        		else {
	        			
	        			if(s.length() > 0){
		        			Double rightAmount = Double.parseDouble(s.toString());
		        			Double leftResult = rightAmount / mXrate;
		        			
		        			DecimalFormat decimal = new DecimalFormat("#0.00");   	
		              	  	mLeftResult.setText(decimal.format(leftResult));
	        			}
	        			else{
	        				mLeftResult.setText(s);
	        			}
	        		}
    			}
    		}
        }
        );
        
        
        Iterator<String> iter = currencyArray.iterator();
	    
	    int count = 0;
	    
        while(iter.hasNext()){
        	      	
        	String longCurrency = iter.next();

        	String currency = longCurrency.substring(0,longCurrency.indexOf(" "));     	
        	
        	if(currency.compareTo(mCurrencyLeft) == 0)
        		mSpinnerLeftPosition = count;
        	
        	if(currency.compareTo(mCurrencyRight) == 0)
        		mSpinnerRightPosition = count;
        	
        	count++;
        	
        }      
        
        mLeftSpinner = (Spinner) rootView.findViewById(R.id.currency_spinner_base);
        mLeftAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, currencyArray);
        mLeftAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLeftSpinner.setAdapter(mLeftAdapter);
        mLeftSpinner.setSelection(mSpinnerLeftPosition);
           
     // This listener is used to set the selected timeframe from the Spinner.
        mLeftSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		public void onItemSelected(AdapterView parent, View v, int position,
              long id) {
				String longCurrency = (String)mLeftAdapter.getItem(position);
				mCurrencyLeft = longCurrency.substring(0,longCurrency.indexOf(" "));
				mSpinnerLeftPosition = position;
         	  	
				//mDb.getDatabase().setLockingEnabled(true);
          	  	//mDb.setDefaultBaseCurrency(mCurrencyLeft);  
				
   	  	
          	  	mXrate = calculateExchange(mCurrencyLeft, mCurrencyRight); 
          	    DecimalFormat decimal4 = new DecimalFormat("#0.0000");   	
          	    mXrateString = decimal4.format(mXrate);
          	    
          	  // mXrateUpdateMessage.setText("Rate : " + mXrateString +  mRefreshDateText);
          	  	
          	  DecimalFormat decimal2 = new DecimalFormat("#0.00");  
            	
            	if(mLeftResult.getText().toString().length() > 0){
            		double resultRight = Double.parseDouble(mLeftResult.getText().toString()) * mXrate;
          	  		mRightResult.setText(decimal2.format(resultRight));
            	}
          	  	
          	  	
          	            	   
          	   //This prevents downloading the graph when the spinner is initiatied
          	   
          	   if(!firstCallLeft){
          		   
          		   
          		 ContentValues values = new ContentValues();
				 values.put("default_value", mCurrencyLeft);
				
				String where = "default_type = \"default_base_currency\"";
				mContext.getContentResolver().update(ConverterContentProvider.UPDATE_DEFAULT_BASE_CURRENCY_URI, values, where, null);
				
				
          	   
	          	   try{
	          		   
		          		decimal4 = new DecimalFormat("#0.0000");
		           	  	mXrateString = decimal4.format(mXrate);
		           	  
		           	  	
		           	 //Cursor rateDate = mDb.getExchangeRateDate(mCurrencyLeft);
		           // getActivity().startManagingCursor(rateDate);
		            // rateDate.moveToFirst();
		           	  	
		         	// mLastUpdate = rateDate.getString(rateDate.getColumnIndex("spare_1"));
		           	  	mLastUpdate = mRateDateMap.get(mCurrencyLeft);
		         	 
		         	 if(isOverdueRefresh()){
		         	  	
		           	    //mXrateUpdateMessage.setText("Rate : " + mXrateString + " on " + mRefreshDateText);
		           	  	String currencyString = "http://finance.yahoo.com/d/quotes.csv?s=EUR" + mCurrencyLeft + "=X&f=sl1d1t1";
		           	  	new DownloadXRatesTask().execute(currencyString);
		           	  	mXrateUpdateMessage.setText("Updating X-Rates ...");
		           	  	mSwapDisplayed = false;
		         	 }
		         	 else{
		         		mRefreshDateText = getUpdateInterval(mLastUpdate);
		         		mXrateUpdateMessage.setText("Rate : " + mXrateString +  mRefreshDateText);
		         	 }
	          		    
	          		   	mGraphURL = "http://ichart.finance.yahoo.com/3m?" + mCurrencyLeft + mCurrencyRight + "=x"; 
	          		   	new DownloadImageTask().execute(mGraphURL);
	          		   	
	          	   }catch(Exception e){Log.e("Currency Converter", "Error Downloading Currency Graphs");}
	          	  	
	          	   }
          	   else{
          		   firstCallLeft = false;
          	   }
          		   
          	  
          	          	  	
          	  	
          }
	        public void onNothingSelected(AdapterView arg0) {
	          // NOP
	        }
	      });
        
        
        mRightSpinner = (Spinner) rootView.findViewById(R.id.currency_spinner_new);
        mRightAdapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, currencyArray);
        mRightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mRightSpinner.setAdapter(mRightAdapter);
        mRightSpinner.setSelection(mSpinnerRightPosition);
        
        mRightSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            public void onItemSelected(AdapterView parent, View v, int position,
                long id) {
          	  	String longCurrency = (String)mRightAdapter.getItem(position);
          	  mCurrencyRight = longCurrency.substring(0,longCurrency.indexOf(" "));
          	  mSpinnerRightPosition = position;
          	  
          	  //mDb.getDatabase().setLockingEnabled(true);
          	  //mDb.setDefaultResultCurrency(mCurrencyRight);
			
			
			
        	  
	          	mXrate = calculateExchange(mCurrencyLeft, mCurrencyRight);  
	          	DecimalFormat decimal4 = new DecimalFormat("#0.0000");   	
	        	mXrateString = decimal4.format(mXrate);
	        	
	        //	mXrateUpdateMessage.setText("Rate : " + mXrateString + mRefreshDateText);
	        	
	        	
	        	DecimalFormat decimal2 = new DecimalFormat("#0.00"); 
	        	
	        	try{
	        	
		        	if(mLeftResult.getText().toString().length() > 0){
		        		double resultRight = Double.parseDouble(mLeftResult.getText().toString()) * mXrate;	
		        		mRightResult.setText(decimal2.format(resultRight));
		        	}
	        	
	        	}catch(NumberFormatException e){}
	      	  		
	      	  	
	      	  	
	      	   //Prevents the graph being downloaded when the spinners are initiated
	      	  
	      	   if(!firstCallRight){
	      		   
	      		   
		      		 ContentValues values = new ContentValues();
					 values.put("default_value", mCurrencyRight);
					
					String where = "default_type = \"default_result_currency\"";
					mContext.getContentResolver().update(ConverterContentProvider.UPDATE_DEFAULT_RESULT_CURRENCY_URI, values, where, null);
				
	      		   
			      	 try{		      		
			      	   	
			      		 /*
			      	   Cursor rateDate = mDb.getExchangeRateDate(mCurrencyRight);
			      	  
			             getActivity().startManagingCursor(rateDate);
			             rateDate.moveToFirst();
			         	 mLastUpdate = rateDate.getString(rateDate.getColumnIndex("spare_1"));
			         	 */
			      	   
			      	   mLastUpdate = mRateDateMap.get(mCurrencyRight);
			      	   
			         	decimal4 = new DecimalFormat("#0.0000");
			      	  	mXrateString = decimal4.format(mXrate);
			      	  	
			      	   
			      	  if(isOverdueRefresh()){
			           	  	String currencyString = "http://finance.yahoo.com/d/quotes.csv?s=EUR" + mCurrencyRight + "=X&f=sl1d1t1";
			           	  	new DownloadXRatesTask().execute(currencyString);
			           	  	mXrateUpdateMessage.setText("Updating X-Rates ...");
			           	  	mSwapDisplayed = false;
			         	 }
			      	else{
		         		mRefreshDateText = getUpdateInterval(mLastUpdate);
		         		mXrateUpdateMessage.setText("Rate : " + mXrateString +  mRefreshDateText);
		         	 }
		          		    
		       		   	String graphURL = "http://ichart.finance.yahoo.com/3m?" + mCurrencyLeft + mCurrencyRight + "=x";
		       		   	new DownloadImageTask().execute(graphURL);
		       		   	
		       	   }catch(Exception e){Log.e("Currency Converter", "Error Downloading Currency Graphs");}
       	  	
	      	   }
	      	   else
	      		   firstCallRight = false;
	      		   
          	  
            }
  	        public void onNothingSelected(AdapterView arg0) {
  	          // NOP
  	        }
  	      });
        

        mGraph.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
           	
            	Intent intent = new Intent(v.getContext(),GraphDuration.class);          	
            	startActivityForResult(intent, CHOOSE_TIMEFRAME);
            }
        });
        
        
        mSwapImage  = (ImageView)rootView.findViewById(R.id.button_swap_currency);
       // mSwapImage.setBackgroundResource(R.drawable.abc_ic_search);       
        mSwapImage.setClickable(true);
        
        
        mSwapImage.setOnClickListener(new View.OnClickListener() {
        	public void onClick(View v) {
        	       		
        	
                String temp = mCurrencyRight;
                mCurrencyRight = mCurrencyLeft;
                mCurrencyLeft = temp;
                
                int tempPosition = mSpinnerRightPosition;
                mSpinnerRightPosition = mSpinnerLeftPosition;
                mSpinnerLeftPosition = tempPosition;
                
                mLeftSpinner.setSelection(mSpinnerLeftPosition);
                mRightSpinner.setSelection(mSpinnerRightPosition);

            	
       		}            	
		});
     
        
        mTimeframeRow.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {         	
            	Intent intent = new Intent(v.getContext(),GraphDuration.class);          	
            	startActivityForResult(intent, CHOOSE_TIMEFRAME);
            }
        });
        
        
        

        /*
         * This is the section where we refresh the rates. Just look for the left currency
         * as an indicator whether we need to refresh or not
         */
        
        /*
        Cursor rateDate = mDb.getExchangeRateDate(mCurrencyLeft);
        getActivity().startManagingCursor(rateDate);
        rateDate.moveToFirst();
        
        
        try{
    		mLastUpdate = rateDate.getString(rateDate.getColumnIndex("spare_1"));
    		mRefreshDateText = getUpdateInterval(mLastUpdate);
    	}catch(NumberFormatException e){}
    	
    	*/
        
        try{
    		mLastUpdate = mRateDateMap.get(mCurrencyLeft);
    		mRefreshDateText = getUpdateInterval(mLastUpdate);
    	}catch(NumberFormatException e){}
        
       
        StringBuffer currencylist = new StringBuffer();
        
        currencylist.append("+EUR" + mCurrencyLeft + "=X");
        currencylist.append("+EUR" + mCurrencyRight + "=X");
        
        Iterator<String> selectedCurr = mSelectedCurrencyArray.iterator();
        
        
        while(selectedCurr.hasNext()){    	
        	currencylist.append("+EUR" + selectedCurr.next() + "=X");
        }
        
        
        //Let's make sure we add the Base currency of "Currency List" also.
        currencylist.append("+EUR" + mBaseCurrencyList + "=X");
        
        String temp = currencylist.toString();      
        mCurrencyString = "http://finance.yahoo.com/d/quotes.csv?s=" + temp + "&f=sl1d1t1";
        
        Log.e(getActivity().getLocalClassName(),"XRate refresh string = " + mCurrencyString);
    
		
		if(mNetworkInfo != null && mChartDisplayed == false){
	        	startGraphAnimation(); 
		}
		
		
		
		if(mNetworkInfo != null && mSwapDisplayed == false){
	        	startSwapAnimation(); 
		}
		
		
		
	    moPubView = (MoPubView) rootView.findViewById(R.id.mopub_sample_ad);
		moPubView.setAdUnitId(MOPUB_BANNER_AD_UNIT_ID);
		moPubView.loadAd();
		
		
        
        
        return rootView;
   

  
        }
    
    /*
    @Override
    public void onActivityCreated(Bundle savedInstanceState)
    {
        // Tell the main activity that this fragment has menu elements to declare
        super.onActivityCreated(savedInstanceState);
           
    }
    */
    
    
    
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        // See which child activity is calling us back.
    	switch (requestCode) {
    	
	    	case CHOOSE_TIMEFRAME:
	    		
	    		if(resultCode == Converter.CHOOSE_TIMEFRAME_SUCCESS){
		    		String timeframe = data.getExtras().getString("TIMEFRAME");
		    		Intent intent = new Intent(mContext,Graph.class);
	        		intent.putExtra("TIMEFRAME", timeframe);
	        		intent.putExtra("CURRENCY_LEFT", mCurrencyLeft);
	        		intent.putExtra("CURRENCY_RIGHT", mCurrencyRight);
	      		
	        		startActivity(intent);
	    		}
	    		
	    	case INTENT_REQUEST_CODE_ADD_CURRENCY: 
	    	    	   
	    	    	   //Refresh the lists
	    	           if(currencyArray != null)
	    	        	   currencyArray.clear();
	    	           
	    	           if(mSelectedCurrencyArray != null)
	    	        	   mSelectedCurrencyArray.clear();
	    	           
	    	           
	    	    	   getActivity().getSupportLoaderManager().initLoader(ConverterContentProvider.QUERY_GET_SELECTED_CURRENCIES, null, this);
	    		    	Log.d(getClass().getName(),"Passed Back by the Add Currency Code");
	    	    	   
	    	       
		    		    	
                
              default:
            	  break;
        }
    }
    
    
    private void startGraphAnimation(){
    	mGraphLoading = (ImageView)getView().findViewById(R.id.graph_loading);
    	mGraphLoading.setBackgroundResource(R.anim.spin_animation);

    	
    	// Get the background, which has been compiled to an AnimationDrawable object.
    	 mGraphAnimation = (AnimationDrawable) mGraphLoading.getBackground();

    	 // Start the animation (looped playback by default).
    	 mGraphAnimation.start();
    }
    
    
    
    
    private void startSwapAnimation(){
    	
    	mRightResult.setText("");
    	mRightResult.setHint("Loading..");
    	mRightResult.setEnabled(false);
	
    	/*
    	mSwapImage = (ImageView)(findViewById(R.id.button_swap_currency));
    	mSwapImage.setBackgroundResource(R.anim.spin_animation);

   	
     	// Get the background, which has been compiled to an AnimationDrawable object.
     	mSwapAnimation = (AnimationDrawable) mSwapImage.getBackground();

     	 // Start the animation (looped playback by default).
     	mSwapAnimation.start();
    	*/
    	 
    	 
    }
    
    
    
private String getUpdateInterval(String lastUpdate){
    	
	try{
	    	long lastUpdateLong = Long.parseLong(lastUpdate);
	    	long currentTime = System.currentTimeMillis();
	    	long intervalSecs = (currentTime - lastUpdateLong)/1000;
	    	
	    	String interval = "";
	    	
	    	if(intervalSecs < 60){
	    		interval = intervalSecs + " Seconds";
	    	}
	    	
	    	else if(intervalSecs > 60 && intervalSecs < 3600){
	    		interval = (intervalSecs/60) + " Minutes";
	    	}
	    	
	    	if(intervalSecs > 3600){
	    		interval = (intervalSecs/3600) + " Hours";
	    	}
	 	
	    	return " ( " + interval + " ago )";
	    	
		}
		catch (Exception e){
			return lastUpdate;
		}
    }
    
    
    private boolean isOverdueRefresh(){
    	
    	try{   	
	    	long lastUpdateLong = Long.parseLong(mLastUpdate);
	    	long currentTime = System.currentTimeMillis();
	    	long intervalSecs = (currentTime - lastUpdateLong)/1000;
	    	
	    	//If we're over 15 minutes, then refresh 
	    	if(intervalSecs > 900){
	    		mSwapDisplayed = false;
	    		return true;	
	    	}
	    	else{
	    		mSwapDisplayed = true;
	    		return false;
	    		
	    	}
	    }
    	catch(NumberFormatException e){ return true; }
    }
    
    private void startRefreshNotification(){
    	
    	// Get the notification manager serivce.
        mNotificationManager = (NotificationManager) getActivity().getSystemService(mContext.NOTIFICATION_SERVICE);
        
        int icon = android.R.drawable.stat_notify_sync;
        CharSequence tickerText = "X-Rate sync with Yahoo Finance";
        long when = System.currentTimeMillis();

        Notification notification = new Notification(icon, tickerText, when);

        
        Context context = mContext; 
        CharSequence contentTitle = "Currency Converter";
        CharSequence contentText = "Refreshing X-Rates from Yahoo Finance";
        Intent notificationIntent = new Intent(mContext, Converter.class);
        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 0, notificationIntent, 0);

        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
        mNotificationManager.notify(HELLO_ID, notification);

    }

    
    
    private double calculateExchange(String leftCurrency, String rightCurrency){
    	
    	/*
    	if(!db.getDatabase().isOpen())
    		db.open();
    	
    	Cursor left = db.getExchangeRate(leftCurrency);
    	getActivity().startManagingCursor(left);
    	
    	left.moveToFirst();
    	
    	double leftXrate = left.getDouble(left.getColumnIndex(DBAdapter.KEY_CURRENCY_X_RATE));
    	
    	Cursor right = db.getExchangeRate(rightCurrency);
    	getActivity().startManagingCursor(right);
    	
    	right.moveToFirst();
    	
    	double rightXrate = right.getDouble(right.getColumnIndex(DBAdapter.KEY_CURRENCY_X_RATE));
    	*/
    	
        ////Log.d("PocketCurrency","Left Rate = " + leftXrate + " : right Rate = " + rightXrate + " : XRate = " + rightXrate / leftXrate);
        

    	double leftXrate = 0.0;
    	double rightXrate = 0.0;
    	
    	try{
	    	leftXrate = mRateMap.get(leftCurrency);
	    	rightXrate = mRateMap.get(rightCurrency);
    	}catch (Exception e){
    		//Sometimes when starting the application first, mRateMap may not have actually populated yet before the functions above are
    		//called resulting in a nullpointerexception. The choice here was to circle with a thread or return infinity. The user will get
    		//an incorrect first answer but it will be obvious they need to refresh
    		
    		//However, this only normally happens on first install (since there is a lag setting up the database. It's fine returning
    		//this value as it's updated once more on load, returning the correct value
    		
    		return 0;
    	}
    	
    	
    	return  (rightXrate / leftXrate) ;	
    	
    }
    
    
	private class DownloadImageTask extends AsyncTask {
		
	     public Object doInBackground(Object... urls) {
	    	 
	    	 ////Log.d("PocketCurrency","DownloadImageTask");
	    	 
	    	 return URLFetcher.getBitmap((String)urls[0]); 
	     }

	     public void onPostExecute(Object result) {	    	 

	    	
	    	 if(result != null){
	    		 mGraph.setImageBitmap((Bitmap)result);    
	    		//setGraphTextViews();
	    		 mTimeframeRow.setVisibility(View.VISIBLE);
	    	 }
	    	
	    	 
	    	 mChartDisplayed = true;
			    
		    if(mGraphAnimation != null)
		    	mGraphAnimation.stop();
		    
		    View view = getView();
		    
		    if(view != null){
		    	 TableRow row = (TableRow)getView().findViewById(R.id.graph_animation);
				    if(row !=null)
				    	row.setVisibility(View.GONE);
		    }
		    

		   
	    	 
	    	 //Once this is done and shown, we can dispatch the Analytics tracker
	    	 //This means it will not slow down the application
	    	 //Log.d(getLocalClassName(),"DownloadImageTask COMPLETE");
	    	// mTracker.dispatch();
		     
	    	 
	     }

	    	
	}
	
	

	
	
	
	
	
	
	 private class DownloadXRatesTask extends AsyncTask<String, Integer, String>{
	    	
	    	
	    	public DBAdapter mThreadDB;
			
		     public String doInBackground(String... urls) {	 
		    	 
		    	 ////Log.d("PocketCurrency","Executing DownloadXRatesTask");
	 	 
		    	 String result = URLFetcher.getString((String)urls[0]);
		    	 
		    	 
		    	 if(result != null){	    		 
		    		 
		    		 
		    		 
			    	 String resultString = (String)result;
			    	 
		    		 //Initialise the date String. We will come across this first in the xml file
		    		 //so no worries here
			    	 String date = mRefreshDateText;
			    	 
			    	 try{
				    	 StringTokenizer tokenizer = new StringTokenizer(resultString, "\n");
				    	 while(tokenizer.hasMoreTokens()){
				    		 String token = tokenizer.nextToken();
				    		 
				    		 //String currency = "";
				    		 String rate = "0";
				    		 
				    		 
				    		 StringTokenizer subtokenizer = new StringTokenizer(token,",");
				    		 String subToken = subtokenizer.nextToken();		    		 
				    		 mCurrencyBeingUpdated = subToken.substring(4,7); //"EURGBP=X"
				    		 
				    		 
				    		 rate = subtokenizer.nextToken();
				    		 double rateDouble = Double.parseDouble(rate);
			
				    		 
				    		 try{
				    			 String time = Long.toString(System.currentTimeMillis());
				    			 
				    			// mThreadDB.updateExchangeRate(currency, rateDouble,time );
				    			 
				    			 ContentValues values = new ContentValues();
				    			 
				    			 values.put("spare_1", time);
				    			 values.put("exchange_rate", rateDouble);
				    			
				    			String where = "currency_type = '" + mCurrencyBeingUpdated + "'";
				    			mContext.getContentResolver().update(ConverterContentProvider.UPDATE_EXCHANGE_RATE_URI, values, where, null);
				    			 
				    			 
				    			 mRefreshDateText = getUpdateInterval(time);
				    			 mLastUpdate = time;
					    		 
					    		 //Log.d("PocketCurrency",currency + ":" + rateDouble);
					    		 
					    		 
					    		 /*
					    		 String date1 = subtokenizer.nextToken();
					    		 String date2 = subtokenizer.nextToken();
					 
					    		 String dateExtended = date1 + " " + date2;
					    		 mRefreshDateText = dateExtended.replaceAll("\"", "");				    		 
					    		 mRefreshDateText = mRefreshDateText.trim() + " (EST)";
					    		 */
				    			 
				
					    		 
				    		 }catch(SQLException e){ break;}
				    	 }	
			    	 
			    	 }catch(Exception e){
			    		 //Log.e(getLocalClassName(),e.getLocalizedMessage());
			    		 return null;
			    	 }
			    	 
		    	 }   
		    	 	
		    	     	 
		    	 return result;
		    	 
		     }
		     

		     public void onPostExecute(String result) {

	    		 //Let's make sure the user is updated accordingly
		    	 if(mNotificationManager != null)
		    		 mNotificationManager.cancel(HELLO_ID);
		    	 
		    	 getActivity().setProgressBarIndeterminateVisibility(false);
		         
		         /*
		         if(mSwapAnimation != null){
				    	mSwapAnimation.stop();
				    	mSwapDisplayed = true;
		         }
		         */
				    
				   // mSwapImage.setBackgroundResource(R.drawable.refresh_old);
				    mRightResult.setHint("");
				    mRightResult.setEnabled(true);

	        	//We need to update the result in the right hand side due to the 
	    		 //refresh in rates	  
				    
				    
				 
				    
				    
				   /* 
				    
	    		 mXrate = calculateExchange(mCurrencyLeft, mCurrencyRight, mThreadDB);
	    		
	    		 
	    		 DecimalFormat decimal2 = new DecimalFormat("#0.00");
	    		 
	    		 String leftResultString = mLeftResult.getText().toString();
	    		// Log.d(getLocalClassName(),"LEFT STRING = " + leftResultString);
	    		 
	    		 if(leftResultString.length() > 0){    		 
	    			 try{
			    		 double resultRight = Double.parseDouble(mLeftResult.getText().toString()) * mXrate;	
			        	 mRightResult.setText(decimal2.format(resultRight));
			        	// Log.d(getLocalClassName(),"CHANGED RIGHT RESULT TO  " + decimal2.format(resultRight));
			        	 
	    			 }catch(NumberFormatException e){
	    				// Log.d(getLocalClassName(),"NUMBER FORMAT EXCEPTION" + e.getMessage());
	    			 }       	 
	    		 }

	        	 DecimalFormat decimal4 = new DecimalFormat("#0.0000");
	        	 mXrateString = decimal4.format(mXrate);
	        	 mXrateUpdateMessage.setText("Rate : " + mXrateString +  mRefreshDateText);
	        	 
	        	 //Log.d(getLocalClassName(),"Recalculating XRates"); 


				    
				    */
				    
				    
	    

		         
		         mThreadDB.close();
		         //Log.d(getLocalClassName(),"DownloadXRates COMPLETE");         
		     }
		     
			 public void onPreExecute(){
				    	 
				    	 mThreadDB = new DBAdapter(mContext);
				    	 mThreadDB.open();
				    	 mThreadDB.getDatabase().setLockingEnabled(true);

			    		 while(mThreadDB.getDatabase().isDbLockedByOtherThreads()){
			    			 try{
			    			 //Log.d(getLocalClassName(),"mThreadDB.getDatabase().isDbLockedByOtherThreads()");
			    			 Thread.sleep(1000);
			    			 }catch(InterruptedException e){}
			    		 }
			    		 
				    	 
				     }
				     
		     public void onCancelled(){
		    	 mThreadDB.close();
		    	 mThreadDB = null;
		    	 
		    	 super.onCancelled();
		     }

		 }
	 
	 /*
	 private void setGraphTextViews(){
 	   	
	    	mTimeframeRow.removeAllViews();
	    	
	    	TextView view = new TextView(mContext);
	    	view.setPadding(10, 0, 0, 0);
	    	view.setText("1d");
	    	view.setTextColor(Color.BLUE);
	    	mTimeframeRow.addView(view);
	    	
	    	view = new TextView(mContext);
	    	view.setPadding(10, 0, 0, 0);
	    	view.setText("5d");
	    	view.setTextColor(Color.BLUE);
	    	mTimeframeRow.addView(view);
	    	
	    	view = new TextView(mContext);
	    	view.setPadding(10, 0, 0, 0);
	    	view.setText("3m");
	    	view.setTextColor(Color.BLACK);
	    	mTimeframeRow.addView(view);
	    	
	    	view = new TextView(mContext);
	    	view.setPadding(10, 0, 0, 0);
	    	view.setText("1y");
	    	view.setTextColor(Color.BLUE);
	    	mTimeframeRow.addView(view);
	    	
	    	view = new TextView(mContext);
	    	view.setPadding(10, 0, 0, 0);
	    	view.setText("2y");
	    	view.setTextColor(Color.BLUE);
	    	mTimeframeRow.addView(view);
	    	
	    	view = new TextView(mContext);
	    	view.setPadding(10, 0, 0, 0);
	    	view.setText("5y");
	    	view.setTextColor(Color.BLUE);
	    	mTimeframeRow.addView(view);

	    }
	*/
	 
	
	 @Override
	    public void onSaveInstanceState(Bundle outState) {      
	        Log.e(this.getClass().getSimpleName(), "onSaveInstanceState()");
	        
	        
	        
	        super.onSaveInstanceState(outState);
	    }
	    
	    @Override
	    public void onPause() {
	        Log.e(this.getClass().getSimpleName(), "onPause()");
	        
	        if(mNotificationManager != null)
	        	mNotificationManager.cancel(HELLO_ID);
	        
	        if(mXratesTask != null && mXratesTask.getStatus() == AsyncTask.Status.RUNNING){
	        	boolean success = mXratesTask.cancel(true);
	        	//Log.d(getLocalClassName(),"Cancelling mXratesTask = " + success);
	        }
	        
	        if(mSingleXratesTask != null && mSingleXratesTask.getStatus() == AsyncTask.Status.RUNNING){
	        	boolean success = mSingleXratesTask.cancel(true);
	        	//Log.d(getLocalClassName(),"Cancelling mSingleXratesTask = " + success);
	        }
	        
	        if(mDownloadImageTask != null && mDownloadImageTask.getStatus() == AsyncTask.Status.RUNNING){
	        	boolean success = mDownloadImageTask.cancel(true);
	        	//Log.d(getLocalClassName(),"Cancelling mDownloadImageTask = " + success);
	        }
	        
	        
	        mXrateUpdateMessage.setText("Rate : " + mXrateString +  mRefreshDateText);
	        
	        
	        ContentValues values = new ContentValues();
			 values.put("default_value", mCurrencyLeft);
			
			String where = "default_type = \"default_base_currency\"";
	        mContext.getContentResolver().update(ConverterContentProvider.UPDATE_DEFAULT_BASE_CURRENCY_URI, values, where, null);
	        
	        
	        values = new ContentValues();
	        values.put("default_value", mCurrencyRight);
			where = "default_type = \"default_result_currency\"";
			
	        mContext.getContentResolver().update(ConverterContentProvider.UPDATE_DEFAULT_RESULT_CURRENCY_URI, values, where, null);
	        
	        /*
	        if(mDb.getDatabase().isOpen())
	        	mDb.close();

	        */
	        
	      //Let's set the defaults before pausing application
	       // if(!mDb.getDatabase().isOpen()){
	        //	mDb.open();
	        	
	        	//TODO	        	
	        	//mDb.setDefaultBaseCurrency(mCurrencyLeft);
	            //mDb.setDefaultResultCurrency(mCurrencyRight);
	            
	            //Let's not close the database just because the app has stopped. 
	            //We can let this to the onDestroyView()
	            
	           // mDb.close();
	        	
	       // }
	        
	        /*
	        ContentValues values = new ContentValues();
			 values.put("default_value", mCurrencyLeft);
			
			String where = "default_type = \"default_base_currency\"";
			mContext.getContentResolver().update(ConverterContentProvider.UPDATE_DEFAULT_BASE_CURRENCY_URI, values, where, null);
			
			values = new ContentValues();
			 values.put("default_value", mCurrencyRight);
			
			where = "default_type = \"default_result_currency\"";
			mContext.getContentResolver().update(ConverterContentProvider.UPDATE_DEFAULT_RESULT_CURRENCY_URI, values, where, null);
	        */
	        
	        
	        
	        super.onPause();
	    }
	    
	    @Override
	    public void onStop() {      
	        Log.e(this.getClass().getSimpleName(), "onStop()");
	        
	        
	        
	        if(mNotificationManager != null)
	        	mNotificationManager.cancel(HELLO_ID);
	        
	        if(mXratesTask != null && mXratesTask.getStatus() == AsyncTask.Status.RUNNING)
	        	mXratesTask.cancel(true);
	        
	        if(mSingleXratesTask != null && mSingleXratesTask.getStatus() == AsyncTask.Status.RUNNING)
	        	mSingleXratesTask.cancel(true);
	        
	        mXrateUpdateMessage.setText("Rate : " + mXrateString +  mRefreshDateText);
	        
	        if(mDownloadImageTask != null && mDownloadImageTask.getStatus() == AsyncTask.Status.RUNNING)
	        	mDownloadImageTask.cancel(true);
	        
	        
	        ContentValues values = new ContentValues();
			 values.put("default_value", mCurrencyLeft);
			
			String where = "default_type = \"default_base_currency\"";
	        mContext.getContentResolver().update(ConverterContentProvider.UPDATE_DEFAULT_BASE_CURRENCY_URI, values, where, null);
	        
	        
	        values = new ContentValues();
	        values.put("default_value", mCurrencyRight);
			where = "default_type = \"default_result_currency\"";
			
	        mContext.getContentResolver().update(ConverterContentProvider.UPDATE_DEFAULT_RESULT_CURRENCY_URI, values, where, null);
			
	        
			
	        
	    //TODO    
	        /*
	        //Let's set the defaults before stopping application
	        if(!mDb.getDatabase().isOpen()){
	        	mDb.open();
	        	
	        	mDb.setDefaultBaseCurrency(mCurrencyLeft);
	            mDb.setDefaultResultCurrency(mCurrencyRight);
	            
	            //Let's not close the database just because the app has stopped. 
	            //We can let this to the onDestroyView()
	            
	           // mDb.close();
	        	
	        }
	        */
	        /*
	        
	        ContentValues values = new ContentValues();
			 values.put("default_value", mCurrencyLeft);
			
			String where = "default_type = \"default_base_currency\"";
			mContext.getContentResolver().update(ConverterContentProvider.UPDATE_DEFAULT_BASE_CURRENCY_URI, values, where, null);
			
			values = new ContentValues();
			 values.put("default_value", mCurrencyRight);
			
			where = "default_type = \"default_result_currency\"";
			mContext.getContentResolver().update(ConverterContentProvider.UPDATE_DEFAULT_RESULT_CURRENCY_URI, values, where, null);
*/
	        
	  
	        super.onStop();
	    }
	    
	    @Override
	    public void onDestroy() {
	        Log.e(this.getClass().getSimpleName(), "onDestroy()");
	        
	        if(moPubView != null)
			  moPubView.destroy();
	        
	        super.onDestroy();
	        
	       // mTracker.stopSession();
	    }
	    
	    
	    @Override
	    public void onDestroyView() {
	        Log.e(this.getClass().getSimpleName(), "onDestroyView()");
        
	        super.onDestroyView();
	        
	       // mTracker.stopSession();
	    }
	    
	    @Override
	    public void onResume() {
	    	Log.e(this.getClass().getSimpleName(), "Starting - onResume()");
	    	
	    	getLoaderManager().initLoader(ConverterContentProvider.QUERY_GET_DEFAULT_VALUES, null, this);

	    	
	    	/*
	    	
	    	if(!mDb.getDatabase().isOpen())
	    		mDb.open();
	    	
	    	
	    	
	    	Cursor defaults = mDb.getDefaultValues();
	        getActivity().startManagingCursor(defaults);
	        
	        for(int i=0; i< defaults.getCount(); i++){
	        	defaults.moveToPosition(i);
	        	
	        	String default_type = defaults.getString(defaults.getColumnIndex(DBAdapter.KEY_DEFAULT_TYPE));      	
	        	
	        	//TODO
	        	if(default_type.compareTo("refresh_date") == 0)
	        		mLastUpdate = defaults.getString(defaults.getColumnIndex(DBAdapter.KEY_DEFAULT_VALUE));

	        }
	        
	        */
	    	
	    	
	        
	        
	        mRefreshDateText = getUpdateInterval(mLastUpdate);
	  		mXrateUpdateMessage.setText("Rate : " + mXrateString +  mRefreshDateText);
	  		
	  		
	  		mSwapDisplayed = true;
	        
	  		
	  		if(mNetworkInfo != null && mChartDisplayed == false)
	        	startGraphAnimation(); 
	  		
	  		
	  		Log.d(this.getClass().getSimpleName(), "Finishing - onResume()");
	        	

	    	super.onResume();
	    }
	    
	    
	    
	    
	    

	    
	    
	    public void onStart() {
	    	Log.e(this.getClass().getSimpleName(), "onStart()");
	    	
	    	
	    	
	    	ConnectivityManager serviceConn = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		    mNetworkInfo = serviceConn.getActiveNetworkInfo();
		    
		    if(mNetworkInfo != null){
		        
		        try{
		        	
		        	if(isOverdueRefresh()){
	    	        
		        		mSwapDisplayed = false;
		        		
			        	mXrateUpdateMessage.setText("Updating X-Rates ...");	
			        	mXratesTask = new DownloadXRatesTask();
			        	mXratesTask.execute(mCurrencyString);
			        	
			        	//TODO
			        	//mDb.updateRateDate(Long.toString(System.currentTimeMillis()));
		    	        startRefreshNotification();
		        	}
		        	
		        	else{
		                mXrateUpdateMessage.setText("Rate : " + mXrateString  + mRefreshDateText); 
		        	}
	      	
		        	String graphURL = "http://ichart.finance.yahoo.com/3m?" + mCurrencyLeft + mCurrencyRight + "=x";
		        	mDownloadImageTask = new DownloadImageTask();
		        	mDownloadImageTask.execute(graphURL);
		        	
		        	
	        	
		        }catch (Exception e){
		        //	Log.e("PocketCurrency", "Application Exited Abnormally !");
		        }
		    } 
		    
		    if(mNetworkInfo != null && mChartDisplayed == false)
	        	startGraphAnimation(); 
		    	
	        super.onStart();  
	}
	    
	
	 // This is called when a new Loader needs to be created.
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
	     
	     if(id == ConverterContentProvider.QUERY_GET_CURRENCIES){
	    	 baseUri = ConverterContentProvider.QUERY_GET_CURRENCIES_URI;
	     }	
	     
	     else if(id == ConverterContentProvider.QUERY_GET_DEFAULT_VALUES){
	    	 baseUri = ConverterContentProvider.QUERY_GET_DEFAULT_VALUES_URI;
	     }
	     
	     else if(id == ConverterContentProvider.QUERY_GET_SELECTED_CURRENCIES){
	    	 baseUri = ConverterContentProvider.QUERY_GET_SELECTED_CURRENCIES_URI;
	  	 }
	     
	     else if(id == ConverterContentProvider.UPDATE_EXCHANGE_RATE){
	    	 baseUri = ConverterContentProvider.UPDATE_EXCHANGE_RATE_URI;
	  	 }

//	     Log.e(getActivity().getLocalClassName(),"BaseUri = " + baseUri.getLastPathSegment());
	     
	     CursorLoader cursorLoader = new CursorLoader(getActivity(),
	       baseUri, projection, null, null, null);
	     
	     return cursorLoader;
	    }
	    
	    @Override
	    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
	    	
	
	    	if(loader.getId() == ConverterContentProvider.QUERY_GET_CURRENCIES ){
	    		
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
		        	

		        	String currency = (String)data.getString(data.getColumnIndex(DBAdapter.KEY_CURRENCY));
		        	String date = (String)data.getString(data.getColumnIndex(DBAdapter.KEY_CURRENCY_SINGLE_X_RATE_DATE));
		        	Double rate = (Double)data.getDouble(data.getColumnIndex(DBAdapter.KEY_CURRENCY_X_RATE));
		   
		        	
		        	mRateDateMap.put(currency, date);
		        	mRateMap.put(currency,rate);
		        	
		        	
		        	
		        //	Log.e(getActivity().getLocalClassName(),currency + " : " + rate + " : " + date);
		        	
		        }
	    		
	    		mLastUpdate = mRateDateMap.get(mCurrencyLeft);
	    		mRefreshDateText = getUpdateInterval(mLastUpdate);
	    		
	    		DecimalFormat decimal4 = new DecimalFormat("#0.0000");
	        	 mXrateString = decimal4.format(mXrate);
	        	 mXrateUpdateMessage.setText("Rate : " + mXrateString +  mRefreshDateText);
	        	 
	        	 
	        	 //Now that this has populated, we can call the default values

	             getLoaderManager().initLoader(ConverterContentProvider.QUERY_GET_DEFAULT_VALUES, null, this);

	    		
	    	}
	    	
	    	
	    	else if(loader.getId() == ConverterContentProvider.QUERY_GET_DEFAULT_VALUES ){
	    		
	    		for(int j=0;j< data.getCount(); j++){
		        	
		        	data.moveToPosition(j);
	        	
		        	String default_type = data.getString(data.getColumnIndex(DBAdapter.KEY_DEFAULT_TYPE));
		        	
       	
		        	if(default_type.compareTo("default_base_currency") ==0){
		        		
		        		if(data.getString(data.getColumnIndex(DBAdapter.KEY_DEFAULT_VALUE)).length() > 0)
		        			mCurrencyLeft = data.getString(data.getColumnIndex(DBAdapter.KEY_DEFAULT_VALUE));
		        		
		        		
		        		Iterator<String> iter = currencyArray.iterator();
		        	    
		        	    int count = 0;
		        	    
		                while(iter.hasNext()){
		                	      	
		                	String longCurrency = iter.next();

		                	String currency = longCurrency.substring(0,longCurrency.indexOf(" "));     	
		                	
		                	if(currency.compareTo(mCurrencyLeft) == 0){
		                		mSpinnerLeftPosition = count;
		                		mLeftSpinner.setSelection(mSpinnerLeftPosition);
		                		mLeftAdapter.notifyDataSetChanged();
		                		
		                	}
		                	
		              
		                	
		                	count++;
		                	
		                }  
		                
		                
		        		
		        	}
		        	
		        	else if(default_type.compareTo("default_result_currency") ==0){
		        		
		        		
		        		((Spinner) mRightSpinner).getSelectedView();
		        		mRightSpinner.setEnabled(false);
		        		
		        		if(data.getString(data.getColumnIndex(DBAdapter.KEY_DEFAULT_VALUE)).length() > 0)
		        				mCurrencyRight = data.getString(data.getColumnIndex(DBAdapter.KEY_DEFAULT_VALUE));
		        		
		        		
		        		Iterator<String> iter = currencyArray.iterator();
		        	    
		        	    int count = 0;
		        	    
		                while(iter.hasNext()){
		                	      	
		                	String longCurrency = iter.next();

		                	String currency = longCurrency.substring(0,longCurrency.indexOf(" "));     	
		                	
		                	
		                	if(currency.compareTo(mCurrencyRight) == 0){
		                		mSpinnerRightPosition = count;
		                		mRightSpinner.setSelection(mSpinnerRightPosition);
		                		
		                	}
		                	
		                	count++;
		                	
		                } 
		                
		                
		                mRightAdapter.notifyDataSetChanged();
		                mRightSpinner.setEnabled(true);
		                
		                
		                
		        	}
		        	
		        	else if(default_type.compareTo("default_base_currency_list") ==0)
		        		mBaseCurrencyList = data.getString(data.getColumnIndex(DBAdapter.KEY_DEFAULT_VALUE));

		   
		        	
		        }
	    		
	    		
	    		
	    	}
	    	
	    	
	    	
	    	
	    	//TODO
	    	//This map maybe updated after the UI thread is updated in onPostExecute of DownloadXRatesTask
	    	//Need to work out a way of updating the UI as soon as this has registered a change
	    	
	    	
	    	else if(loader.getId() == ConverterContentProvider.UPDATE_EXCHANGE_RATE){
	    		
	    		Log.d(getActivity().getLocalClassName(),"ConverterContentProvider.UPDATE_EXCHANGE_RATE called");
	    		
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
		        	
		        	String currency = (String)data.getString(data.getColumnIndex(DBAdapter.KEY_CURRENCY));
		        	
		        	if(currency.compareTo(mCurrencyBeingUpdated) ==0 ){
		        		String date = (String)data.getString(data.getColumnIndex(DBAdapter.KEY_CURRENCY_SINGLE_X_RATE_DATE));
			        	Double rate = (Double)data.getDouble(data.getColumnIndex(DBAdapter.KEY_CURRENCY_X_RATE));
			   
			        	
			        	mRateDateMap.put(currency, date);
			        	mRateMap.put(currency,rate);
			        	
			        	
			        	 mXrate = calculateExchange(mCurrencyLeft, mCurrencyRight);
				    		
			    		 
			    		 DecimalFormat decimal2 = new DecimalFormat("#0.00");
			    		 
			    		 String leftResultString = mLeftResult.getText().toString();
			    		Log.d(getClass().getName(),"DOOOOO  - LEFT STRING = " + leftResultString);
			    		 
			    		 if(leftResultString.length() > 0){    		 
			    			 try{
					    		 double resultRight = Double.parseDouble(mLeftResult.getText().toString()) * mXrate;	
					        	 mRightResult.setText(decimal2.format(resultRight));
					        	// Log.d(getLocalClassName(),"CHANGED RIGHT RESULT TO  " + decimal2.format(resultRight));
					        	 
			    			 }catch(NumberFormatException e){
			    				// Log.d(getLocalClassName(),"NUMBER FORMAT EXCEPTION" + e.getMessage());
			    			 }       	 
			    		 }

			        	Log.d(getActivity().getLocalClassName(),"JUST UPDATED " + currency + " : " + rate + " : " + date);
		        	}      	
		        	
		        }
	    		
	    		mLastUpdate = mRateDateMap.get(mCurrencyLeft);
	    		mRefreshDateText = getUpdateInterval(mLastUpdate);
	    		
	    		 DecimalFormat decimal4 = new DecimalFormat("#0.0000");
	        	 mXrateString = decimal4.format(mXrate);
	        	 mXrateUpdateMessage.setText("Rate : " + mXrateString +  mRefreshDateText);
	        	 Log.d(getActivity().getLocalClassName(),"JUST UPDATED " + "Rate : " + mXrateString +  mRefreshDateText);
	    		
	    	}		
	    			
	    	
	    	else if(loader.getId() == ConverterContentProvider.QUERY_GET_SELECTED_CURRENCIES){
	    		
	    		for(int j=0;j< data.getCount(); j++){
		        	
		        	data.moveToPosition(j);
		        	
		        	String currency = (String)data.getString(data.getColumnIndex(DBAdapter.KEY_CURRENCY));
     	
		        	mSelectedCurrencyArray.add(currency);

		        	
		        	
		        	//Log.d(getActivity().getLocalClassName(),"Added " + data.getString(data.getColumnIndex(DBAdapter.KEY_CURRENCY)) + " to Selected Currency Array");
		        	
		        }
	    		
	    	}
	    	        
	    }
	    
	    @Override
	    public void onLoaderReset(Loader<Cursor> loader) {
	     //do nothing
	    }
	    
	    @Override
	    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
	        inflater.inflate(R.menu.main, menu);
	        
	        menu.add(0,MENU_REFRESH,0,"Refresh")
	 		.setIcon(android.R.drawable.ic_menu_rotate); 
	        
	        menu.add(0,MENU_ADD_CURRENCY,0,"Add Currency")
	 		.setIcon(android.R.drawable.ic_menu_add); 
	        
	        
	        super.onCreateOptionsMenu(menu,inflater);
	    }
	    
	    
	    
	    /* Handles item selections */
	    public boolean onOptionsItemSelected(MenuItem item) {
	        switch (item.getItemId()) {  
	        
	        case MENU_REFRESH:      
	        	
	        	//Let's check the network status once more
	        	ConnectivityManager serviceConn = (ConnectivityManager) this.getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
	    	    NetworkInfo networkInfo = serviceConn.getActiveNetworkInfo();
	    	    
	    	    if(networkInfo != null){
	    	    	//If we do have a connection, then spawn another thread to actually
	        		// retrieve x-rates   
	    	    	
	    	    	//mXrateUpdateMessage.setText("Updating X-Rates ...");
	    	    	//mSwapDisplayed = false;
	    	    	
	    	        startRefreshNotification();
	    	        getActivity().setProgressBarIndeterminateVisibility(true);
	    	    	
	    	    	getActivity().getSupportLoaderManager().initLoader(ConverterContentProvider.QUERY_GET_SELECTED_CURRENCIES, null, this);
	    	    	
	    	    	
	    	        mXratesTask = new DownloadXRatesTask();
	    	        mXratesTask.execute(mCurrencyString); 
	    	      //  mDb.updateRateDate(Long.toString(System.currentTimeMillis()));
	    	    }
	    	    
	    	    else{
	    	    	
	    	    	//setTitle(mSyncMessage + mRefreshDateText);
	    	    //	mXrateUpdateMessage.setText("Rate : " + mXrate + mRefreshDateText);
	    	    	Toast.makeText(getActivity(), "Connection Error. Please check your connections and try again.", Toast.LENGTH_SHORT).show();
	            }	
	        	return true;
	        	
	   	
	        	
	        case MENU_ADD_CURRENCY:
	        	
	        	Intent intent = new Intent(getActivity(),AddCurrency.class);
	        	startActivityForResult(intent, INTENT_REQUEST_CODE_ADD_CURRENCY);
	        	
 
	            return true;
	            

	        }
	        
	    
	        
	       
	        
	        return false;
	    }
	    

	    
	    
	   

}

