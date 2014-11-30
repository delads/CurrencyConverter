package com.pocketools.currency;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.StringTokenizer;

/*
import com.adwhirl.AdWhirlLayout;
import com.adwhirl.AdWhirlTargeting;
import com.adwhirl.adapters.AdWhirlAdapter;
*/

//import com.google.ads.AdSenseSpec;
//import com.google.ads.GoogleAdView;
//import com.google.ads.AdSenseSpec.AdType;

//import com.google.ads.*;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
//import com.google.ads.AdView;
import com.google.ads.AdRequest.Gender;





import android.app.Activity;
import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.graphics.Color;
//import android.location.Location;
//import android.location.LocationManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.RelativeLayout.LayoutParams;

public class CurrencyList extends ListFragment  implements LoaderManager.LoaderCallbacks<Cursor>{
	
	private static final int HELLO_ID = 5000;
	private static final int MENU_REFRESH = 5001;
	private static final int MENU_EDIT_CURRENCY_LIST = 5002;
	private static final int MENU_SETTINGS = 5007;
	private static final int MENU_EDIT_BASE_CURRENCY = 5006;
    public static final int RELOAD_CURRENCY_LIST = 5003;
    public static final int RELOAD_CURRENCY_LIST_SUCCESS = 5004;
    public static final int CHOOSE_TIMEFRAME = 5005;
    
	
	Intent mIntent;
 //   private DBAdapter mDb;
    ArrayList<String> mCurrencyList;
    View mFooterView;
    String mDefaultCurrency;
    String mCompareCurrency;
    double mDefaultCurrencyVersusEuro;
    String mDefaultAmount;
    TextView mHeadingView;
    TextView mXrateUpdateMessage;
    String mRefreshDateText = "";
    DownloadXRatesTask mXratesTask;
    private NotificationManager mNotificationManager;
//    GoogleAnalyticsTracker mTracker;
    String mCurrencyString;
    //AdView mAdView;
    String mLastUpdate;
    Context mContext;
    boolean mIsAdsFreeVersion = false;
    View mRootView;
    ArrayList<String> mSelectedCurrencyArray;
	private HashMap<String,Double> mRateMap;
	private HashMap<String, String> mRateDateMap;
	private MyCurrencyAdapter mListAdapter;
	private int mCount = 0;
    
    
 // Replace with your own AdSense client ID.
    private static final String CLIENT_ID = "ca-mb-app-pub-1805290976571198";
    private static final String ADMOB_PUBLISHER_ID = "a14b045c31c8875";

    // Replace with your own company name.
    private static final String COMPANY_NAME = "DELADS";

    // Replace with your own application name.
    private static final String APP_NAME = "Currency Converter";

    // Replace with your own keywords used to target Google ad.
    // Join multiple words in a phrase with '+' and join multiple phrases with ','.
    private static final String KEYWORDS = "forex+online";

    // Replace with your own AdSense channel ID.
    private static final String CHANNEL_ID = "4325823571";

	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	  }
    
    
	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
        
        /*
        mTracker = GoogleAnalyticsTracker.getInstance();
        
        mTracker.startNewSession(PocketCurrency.ANALYTICS_UA_NUMBER, this);
        mTracker.trackPageView("/CurrencyList_2_4");
        //mTracker.dispatch();
        */
        

        mRootView = inflater.inflate(R.layout.currency_list, container, false);
        this.setHasOptionsMenu(true);
        
        mSelectedCurrencyArray = new ArrayList<String>();
        mRateMap = new HashMap<String,Double>();
        mRateDateMap = new HashMap<String,String>();
        
        return mRootView;
	  }
	  
	  @Override
	    public void onAttach(Activity activity) {
	        super.onAttach(activity);
	        
	    	mContext = this.getActivity();

	        
	    }
	  
	    @Override
	    public void onActivityCreated(Bundle savedInstanceState)
	    {
	    	
      // Tell the main activity that this fragment has menu elements to declare
      super.onActivityCreated(savedInstanceState);
/*
        mDb = new DBAdapter(getActivity());
        mDb.open();
        
        
        Cursor defaults = mDb.getDefaultValues();
        getActivity().startManagingCursor(defaults);
        
        for(int i=0; i< defaults.getCount(); i++){
        	defaults.moveToPosition(i);
        	
        	String default_type = defaults.getString(defaults.getColumnIndex(DBAdapter.KEY_DEFAULT_TYPE));      	
        	if(default_type.compareTo("default_base_currency_list") ==0)
        		mDefaultCurrency = defaults.getString(defaults.getColumnIndex(DBAdapter.KEY_DEFAULT_VALUE));
        	
        	else if(default_type.compareTo("default_base_currency_amount") ==0)
        		mDefaultAmount = defaults.getString(defaults.getColumnIndex(DBAdapter.KEY_DEFAULT_VALUE));
        	
        	else if(default_type.compareTo("refresh_date") == 0){
        		mLastUpdate = defaults.getString(defaults.getColumnIndex(DBAdapter.KEY_DEFAULT_VALUE));
        		mRefreshDateText = "Last update " + getUpdateInterval(mLastUpdate);
        	}
        	
        	else if(default_type.compareTo("ads_free_version") ==0){       		
        		mIsAdsFreeVersion = true;
        	}
        	
        }
       */
      
      getLoaderManager().initLoader(ConverterContentProvider.QUERY_GET_DEFAULT_VALUES, null, this);
      getLoaderManager().initLoader(ConverterContentProvider.QUERY_GET_SELECTED_CURRENCIES, null, this);
      
        
       // mAdView = (AdView) findViewById(R.id.ad_currency_list);
       // mAdView.setKeywords(PocketCurrency.AD_MOB_KEYWORD_HINT + mDefaultCurrency);
        /*
        Cursor c = mDb.getExchangeRate(mDefaultCurrency);
        getActivity().startManagingCursor(c);
        c.moveToPosition(0);
        mDefaultCurrencyVersusEuro = (double)c.getDouble(c.getColumnIndex(DBAdapter.KEY_CURRENCY_X_RATE));
        */
        
    	//Cursor currencies = mDb.getSelectedCurrencies();      
    	//getActivity().startManagingCursor(currencies);
        
        mListAdapter = new MyCurrencyAdapter(mContext,mSelectedCurrencyArray);       
        ListView listView = getListView();
        
        // Changing layout to put this under the menu button as per standard Android layouts
         
        // mFooterView = LayoutInflater.from(this).inflate(R.layout.list_footer, null);
        // listView.addFooterView(mFooterView);
        
        
        View headerView = LayoutInflater.from(getActivity()).inflate(R.layout.list_header, null);
        listView.addHeaderView(headerView);
        
        listView.setAdapter(mListAdapter);
        
        
        
        mHeadingView = (TextView)mRootView.findViewById(R.id.currency_list_heading);
         
        mHeadingView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
           	
            	Intent intent = new Intent (v.getContext(),EditBaseCurrency.class);
    			startActivityForResult(intent,RELOAD_CURRENCY_LIST);    			
            }
        });
        
        
        
        mXrateUpdateMessage = (TextView)mRootView.findViewById(R.id.xrate_list_update_message);
        mXrateUpdateMessage.setText("X-Rates on " + mRefreshDateText);
        
        
        /*
       
        ImageButton backButton = (ImageButton)findViewById(R.id.back_button);
        
        backButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
           	
    	    	finish();		
            }
        });
        

        
        mFooterView.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View v){
        		
        		Intent intent = new Intent (v.getContext(),ChooseCurrencyList.class);
    			startActivityForResult(intent, RELOAD_CURRENCY_LIST);  
        	}
        });
        
        */
        
        /*
        
        ImageButton editListButton = (ImageButton)findViewById(R.id.edit_button_list);    
        editListButton.setOnClickListener(new View.OnClickListener(){
        	public void onClick(View v){
        		
        		Intent intent = new Intent (v.getContext(),ChooseCurrencyList.class);
    			startActivityForResult(intent, RELOAD_CURRENCY_LIST);  
        	}
        });
        */
        
               
        /*
        ImageButton editButton = (ImageButton)headerView.findViewById(R.id.edit_base_button);      
        editButton.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
           	
            	Intent intent = new Intent (v.getContext(),EditBaseCurrency.class);
            	startActivityForResult(intent,RELOAD_CURRENCY_LIST);   			
            }
        });  
        */
        
        
        

        
        
        if(!mIsAdsFreeVersion){
        	
        	/*
       	   // Create the adView
		      AdView adView = new AdView(this, AdSize.BANNER, ADMOB_PUBLISHER_ID);
	
		      // Lookup your LinearLayout assuming its been given
		      // the attribute android:id="@+id/mainLayout"
		      LinearLayout layout = (LinearLayout)findViewById(R.id.list_adwhirl_layout);
	
		      // Add the adView to it
		      layout.addView(adView);

		      AdRequest request = new AdRequest();
		      
	            LocationManager lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
	  	      	Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
	  	      
	  	      	request.setLocation(location);
	  	      
	  	      	Set<String> keywords = new HashSet<String>(Arrays.asList(PocketCurrency.AD_KEYWORDS));
	  	      	request.setKeywords(keywords);
	  	      
	  	      	adView.loadAd(request);

			 */
        	
        	
        	/*
			 
		    	AdWhirlAdapter.setGoogleAdSenseCompanyName(COMPANY_NAME); 
		    	AdWhirlAdapter.setGoogleAdSenseAppName(APP_NAME); 
		    	 
		    	// Optional Google AdSense network parameters 
		    	AdWhirlAdapter.setGoogleAdSenseChannel(CHANNEL_ID); 
		    	AdWhirlAdapter.setGoogleAdSenseExpandDirection("TOP"); 
		    	
		    	AdWhirlTargeting.setKeywords(KEYWORDS);


		    	
		    	TableLayout layout = (TableLayout) findViewById(R.id.list_adwhirl_layout); 
		    	AdWhirlLayout adWhirlLayout = new AdWhirlLayout(this, "ae63b132e3394d66b354d9d72c839b5d"); 
		    	adWhirlLayout.setBackgroundColor(Color.BLACK);
		    	
		    	final int DIP_HEIGHT = 50; 
		    	final float DENSITY = getResources().getDisplayMetrics().density; 
		    	int scaledHeight = (int) (DENSITY * DIP_HEIGHT + 0.5f); 
		    	adWhirlLayout.setMaxHeight(scaledHeight);
		    	


		    	
		    	RelativeLayout.LayoutParams adWhirlLayoutParams = 
		    	    new RelativeLayout.LayoutParams( 
		    	        LayoutParams.WRAP_CONTENT, 
		    	        LayoutParams.WRAP_CONTENT);

		    	
		    	layout.addView(adWhirlLayout, adWhirlLayoutParams); 
		    	layout.invalidate(); 
		        
		    	
		    	*/		    	
			 
        }
        
        
     // Set up GoogleAdView.
        /*
        GoogleAdView adView = (GoogleAdView) findViewById(R.id.adview_currency_list);
        AdSenseSpec adSenseSpec =
            new AdSenseSpec(CLIENT_ID)     // Specify client ID. (Required)
            .setCompanyName(COMPANY_NAME)  // Set company name. (Required)
            .setAppName(APP_NAME)          // Set application name. (Required)
            .setKeywords(KEYWORDS)         // Specify keywords.
            .setChannel(CHANNEL_ID)        // Set channel ID.
            .setAdType(AdType.TEXT)        // Set ad type to Text.
            .setAdTestEnabled(false);       // Keep true while testing.
        
        adView.showAds(adSenseSpec);
        */
        
        // Required Google AdSense network parameters 
        
        
   //     mTracker.dispatch();
    
    }

    
    
    private String getUpdateInterval(String lastUpdate){
    	
    	long lastUpdateLong;
    	
    	try{
    		lastUpdateLong = Long.parseLong(lastUpdate);
    	}catch(Exception e){
    		lastUpdateLong = System.currentTimeMillis();
    	}
    	
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
    
    
	private boolean isOverdueRefresh(){
	    	
	    	try{   	
		    	long lastUpdateLong = Long.parseLong(mLastUpdate);
		    	long currentTime = System.currentTimeMillis();
		    	long intervalSecs = (currentTime - lastUpdateLong)/1000;
		    	
		    	//If we're over 15 minutes, then refresh 
		    	if(intervalSecs > 900){
		    		return true;	
		    	}
		    	else{
		    		return false;
		    		
		    	}
		    }
	    	catch(NumberFormatException e){ return true; }
	    }

    /*
    private void setCurrencyList(){
    	
    	Cursor c = mDb.getSelectedCurrencies();      
    	getActivity().startManagingCursor(c);
        
        ListAdapter listAdapter = new MyCurrencyAdapter(getActivity(),c);       
        ListView listView = getListView();
        listView.setAdapter(listAdapter);
    }
    */
    
    
    
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        
        menu.add(0,MENU_REFRESH,0,"Refresh")
 		.setIcon(android.R.drawable.ic_menu_rotate); 
        
        menu.add(0,MENU_EDIT_BASE_CURRENCY,0,"Edit Base Currency")
 		.setIcon(android.R.drawable.ic_menu_rotate); 
        
        menu.add(0,MENU_EDIT_CURRENCY_LIST,0,"Edit Currency List")
 		.setIcon(android.R.drawable.ic_menu_edit); 
        
        super.onCreateOptionsMenu(menu,inflater);
    }
    
    
    
    /* Handles item selections */
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {  
        
            
           case MENU_REFRESH:      
        	
        	refreshRates();       	
        	return true;
        
        
	    case MENU_EDIT_CURRENCY_LIST:
	    	
	    	Intent intent = new Intent (getActivity(),ChooseCurrencyList.class);
			startActivityForResult(intent, RELOAD_CURRENCY_LIST);  
			return true;
			
	    case MENU_EDIT_BASE_CURRENCY:
	    	
	    	intent = new Intent (getActivity(),EditBaseCurrency.class);
        	startActivityForResult(intent,RELOAD_CURRENCY_LIST);   	
        	
			return true;
    	
        }
        
        return false;
    }
    
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        // See which child activity is calling us back.
    	switch (requestCode) {
    	
    		case RELOAD_CURRENCY_LIST:
	    		
	    		if(resultCode == RELOAD_CURRENCY_LIST_SUCCESS){
	    			
	    		//	if(!mDb.getDatabase().isOpen())
	    	    //		mDb.open();
	    			
	    			//refreshRates();
	    			
	    			if(mSelectedCurrencyArray != null)
	    	        	   mSelectedCurrencyArray.clear();
	    	           
	    			getLoaderManager().initLoader(ConverterContentProvider.QUERY_GET_DEFAULT_VALUES, null, this);
	    	    	getActivity().getSupportLoaderManager().initLoader(ConverterContentProvider.QUERY_GET_SELECTED_CURRENCIES, null, this);
	    		    	Log.d(getClass().getName(),"Passed Back by the Add Currency Code");
	    	        
	    		}	    	
                
              default:
            	  break;
        }
    }
    
    
    private class DownloadXRatesTask extends AsyncTask<String, Integer, String>{
		
	     public String doInBackground(String... urls) {	 
	 /*
	    	 String result = URLFetcher.getString((String)urls[0]);

	    	 if(result != null){	    		 
	    		 if(!mDb.getDatabase().isOpen())
	    			 mDb.open();

	    		 
		    	 String resultString = (String)result;
		    	 
		    	 
		    	 try{
			    	 StringTokenizer tokenizer = new StringTokenizer(resultString, "\n");
			    	 while(tokenizer.hasMoreTokens()){
			    		 String token = tokenizer.nextToken();
			    		 
			    		 String currency = "";
			    		 String rate = "0";
			    		 
			    		 
			    		 StringTokenizer subtokenizer = new StringTokenizer(token,",");
			    		 String subToken = subtokenizer.nextToken();		    		 
			    		 currency = subToken.substring(4,7); //"EURGBP=X"
			    		 
			    		 
			    		 rate = subtokenizer.nextToken();
			    		 double rateDouble = Double.parseDouble(rate);
			    		 		    		 
			    		 mDb.updateExchangeRate(currency, rateDouble,Long.toString(System.currentTimeMillis()));
			    		 
			    	 }
			    	 
			    	 mDb.updateRateDate(Long.toString(System.currentTimeMillis()));
			    	 mRefreshDateText = "Last update ( 0 Seconds ago ) ";
			    	 
			    	 
			    	 
		    	 }catch(Exception e){ return null;}
		    	 
	    	 }    	 
	    	 return result;
	    	 
	    	 */
	    	 
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
			    		 String currencyBeingUpdated = subToken.substring(4,7); //"EURGBP=X"
			    		 
			    		 
			    		 rate = subtokenizer.nextToken();
			    		 double rateDouble = Double.parseDouble(rate);
		
			    		 
			    		 try{
			    			 String time = Long.toString(System.currentTimeMillis());
			    			 
			    			// mThreadDB.updateExchangeRate(currency, rateDouble,time );
			    			 
			    			 ContentValues values = new ContentValues();
			    			 
			    			 values.put("spare_1", time);
			    			 values.put("exchange_rate", rateDouble);
			    			
			    			String where = "currency_type = '" + currencyBeingUpdated + "'";
			    			mContext.getContentResolver().update(ConverterContentProvider.UPDATE_EXCHANGE_RATE_URI, values, where, null);
			    			 
			    			 
			    			 mRefreshDateText = getUpdateInterval(time);
			    			 mLastUpdate = time;
			    			 
			    			 Log.e(getClass().getName(),"UPDATE TIME AS " + mRefreshDateText);
				    		 
				    		 //Log.d("PocketCurrency",currency + ":" + rateDouble);
				    		 
				    		 
				    		 /*
				    		 String date1 = subtokenizer.nextToken();
				    		 String date2 = subtokenizer.nextToken();
				 
				    		 String dateExtended = date1 + " " + date2;
				    		 mRefreshDateText = dateExtended.replaceAll("\"", "");				    		 
				    		 mRefreshDateText = mRefreshDateText.trim() + " (EST)";
				    		 */
			    			 
			    			 
			    			 mRateDateMap.put(currencyBeingUpdated, time);
					         mRateMap.put(currencyBeingUpdated,rateDouble);
					         
				    		 
			    		 }catch(SQLException e){ 
			    			 break;}
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
	         mNotificationManager.cancel(HELLO_ID);
	         mXrateUpdateMessage.setText("X-Rates on " + mRefreshDateText);
	         /*
	         if(!mDb.getDatabase().isOpen())
	 	    		mDb.open();         
	         
	         Cursor defaultCurrency = mDb.getExchangeRate(mDefaultCurrency);
	         getActivity().startManagingCursor(defaultCurrency);
	         defaultCurrency.moveToPosition(0);
	         mDefaultCurrencyVersusEuro = (double)defaultCurrency.getDouble(defaultCurrency.getColumnIndex(DBAdapter.KEY_CURRENCY_X_RATE));

	         
 			Cursor c = mDb.getSelectedCurrencies();      
 			getActivity().startManagingCursor(c);
 	        
 	        mListAdapter = new MyCurrencyAdapter(mContext,c);       
 	        ListView listView = getListView();
 	        listView.setAdapter(mListAdapter);
 	        */
	         
	         //TODO
	         //come back to here as there's some problem with defaultCurrency
	         
	         if(mRateMap.get(mDefaultCurrency) != null)
	        	 	mDefaultCurrencyVersusEuro = mRateMap.get(mDefaultCurrency);
	         
	         
	         mListAdapter.notifyDataSetChanged();
	         
	         
	     }


	 }
    
    private void refreshRates(){
    	//Let's check the network status once more
    	ConnectivityManager serviceConn = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = serviceConn.getActiveNetworkInfo();
	    
	    if(networkInfo != null){
	    	//If we do have a connection, then spawn another thread to actually
    		// retrieve x-rates   
	    	
	    	mXrateUpdateMessage.setText("Updating Rates ...");
	    	////Log.d(getLocalClassName(),"Refreshing Rates");
	    	
	    	
	    	/*
	    	Cursor defaults = mDb.getDefaultValues();
	    	getActivity().startManagingCursor(defaults);
	        
	        for(int i=0; i< defaults.getCount(); i++){
	        	defaults.moveToPosition(i);
	        	
	        	String default_type = defaults.getString(defaults.getColumnIndex(DBAdapter.KEY_DEFAULT_TYPE));      	
	        	if(default_type.compareTo("default_base_currency_list") ==0)
	        		mDefaultCurrency = defaults.getString(defaults.getColumnIndex(DBAdapter.KEY_DEFAULT_VALUE));
	        	
	        }
	    	
	    	
	    	Cursor currencies = mDb.getSelectedCurrencies();
	    	getActivity().startManagingCursor(currencies);
	        
	        StringBuffer currencylist = new StringBuffer();
	        
	        for(int i=0; i < currencies.getCount(); i++){
	        	currencies.moveToPosition(i);
	        	currencylist.append("+EUR" + currencies.getString(currencies.getColumnIndex(DBAdapter.KEY_CURRENCY)) + "=X");
	        }
	        
	      //Let's make sure we add the Base currency of "Currency List" also.
	        currencylist.append("+EUR" + mDefaultCurrency + "=X");
	        
	        String temp = currencylist.toString();
	        //String trimmedList = temp.replaceFirst("+", "");
	        
	        mCurrencyString = "http://finance.yahoo.com/d/quotes.csv?s=" + temp + "&f=sl1d1t1";
	        
	    	
	        startRefreshNotification();
	        mXratesTask = new DownloadXRatesTask();
	        mXratesTask.execute(mCurrencyString); 
	        
	        mDb.updateRateDate(Long.toString(System.currentTimeMillis()));
	        
	        
	        */
	    	
	    	getLoaderManager().initLoader(ConverterContentProvider.QUERY_GET_SELECTED_CURRENCIES, null, this);
	    	
	    	
	    }
	    
	    else{
	    	
	    	//Let's not update the rates, but let's notify the the Currency Array Adapter that the list has changed at least
	    	mListAdapter.notifyDataSetChanged();
	    	
	    	//setTitle(mSyncMessage + mRefreshDateText);
	    	mXrateUpdateMessage.setText("X-Rates on " + mRefreshDateText);
	    	Toast.makeText(getActivity(), "Connection Error. Please check your connections and try again.", Toast.LENGTH_SHORT).show();
        }	
	    
    }
    
	   private void startRefreshNotification(){
	    	
	    	// Get the notification manager serivce.
	        mNotificationManager = (NotificationManager) getActivity().getSystemService(getActivity().NOTIFICATION_SERVICE);
	        
	        int icon = android.R.drawable.stat_notify_sync;
	        CharSequence tickerText = "X-Rate sync with Yahoo Finance";
	        long when = System.currentTimeMillis();
	
	        Notification notification = new Notification(icon, tickerText, when);
	
	        
	        Context context = getActivity().getApplicationContext();
	        CharSequence contentTitle = "Currency Converter";
	        CharSequence contentText = "Refreshing X-Rates";
	        Intent notificationIntent = new Intent(getActivity(), MainActivity.class);
	        PendingIntent contentIntent = PendingIntent.getActivity(getActivity(), 0, notificationIntent, 0);
	
	        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
	        mNotificationManager.notify(HELLO_ID, notification);
	
	    }
    
    
    @Override
    public void onSaveInstanceState(Bundle outState) {      
        //Log.d(this.getLocalClassName(), "onSaveInstanceState()");
        
        super.onSaveInstanceState(outState);
    }
    
    @Override
    public void onPause() {
        //Log.d(this.getLocalClassName(), "onPause()");

        //if(mDb.getDatabase().isOpen())
        //	mDb.close();

        super.onPause();
    }
    
    @Override
    public void onStop() {      
        //Log.d(this.getLocalClassName(), "onStop()");
        
        	mNotificationManager.cancel(HELLO_ID);
        
        if(mXratesTask != null && mXratesTask.getStatus() == AsyncTask.Status.RUNNING)
        	mXratesTask.cancel(true);
        
       // if(mDb.getDatabase().isOpen())
        //	mDb.close();
        
        super.onStop();
    }
    
    @Override
    public void onDestroy() {
        //Log.d(this.getLocalClassName(), "onDestroy()");
        
        super.onDestroy();
       // mTracker.stop();
    }
    
    @Override
    public void onResume() {
    	//Log.d(this.getLocalClassName(), "onResume()");
    	
    	//if(!mDb.getDatabase().isOpen())
    	//	mDb.open();
    	
    	
    	getLoaderManager().initLoader(ConverterContentProvider.QUERY_GET_DEFAULT_VALUES, null, this);
    	
    	super.onResume();
    }
    
    
    
    public void onStart() {
    	//Log.d(this.getLocalClassName(), "onStart()");

        /*
    	
    	if(!mDb.getDatabase().isOpen())
    		mDb.open();
    	
    	
    	Cursor defaults = mDb.getDefaultValues();
    	getActivity().startManagingCursor(defaults);
        
        for(int i=0; i< defaults.getCount(); i++){
        	defaults.moveToPosition(i);
        	
        	String default_type = defaults.getString(defaults.getColumnIndex(DBAdapter.KEY_DEFAULT_TYPE));      	
        	if(default_type.compareTo("default_base_currency_list") ==0)
        		mDefaultCurrency = defaults.getString(defaults.getColumnIndex(DBAdapter.KEY_DEFAULT_VALUE));
        	
        	else if(default_type.compareTo("default_base_currency_amount") ==0)
        		mDefaultAmount = defaults.getString(defaults.getColumnIndex(DBAdapter.KEY_DEFAULT_VALUE));
        	
        	else if(default_type.compareTo("refresh_date") == 0)
        		mLastUpdate = defaults.getString(defaults.getColumnIndex(DBAdapter.KEY_DEFAULT_VALUE));
        }
        
        mHeadingView.setText(mDefaultAmount + " " + mDefaultCurrency + " = ");
        
        
        Cursor c = mDb.getExchangeRate(mDefaultCurrency);
        getActivity().startManagingCursor(c);
        c.moveToPosition(0);
        mDefaultCurrencyVersusEuro = (double)c.getDouble(c.getColumnIndex(DBAdapter.KEY_CURRENCY_X_RATE));
        
        if(isOverdueRefresh()){
        	refreshRates();
        }
 
        
    	setCurrencyList();
    	*/
      
    	if(isOverdueRefresh()){
        	refreshRates();
        }
    	super.onStart();
    }
    
    
    
    private class MyCurrencyAdapter extends ArrayAdapter<String> {
    	  private final Context context;
    	  private final ArrayList<String> list;

    	  public MyCurrencyAdapter(Context context, ArrayList<String> list) {
    	    super(context, R.layout.list_content, list);
    	    this.context = context;
    	    this.list = list;
    	  }

    	  @Override
    	  public View getView(int position, View convertView, ViewGroup parent) {
    	    LayoutInflater inflater = (LayoutInflater) context
    	        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    	    View rowView = inflater.inflate(R.layout.list_content, parent, false);
    	    
 	    
    	    String longCurrencyName = mSelectedCurrencyArray.get(position);
    	    mCompareCurrency = longCurrencyName.substring(0, longCurrencyName.indexOf(" "));
    	    Double currencyRateVersusEuro = null;
    	    
    	    currencyRateVersusEuro = mRateMap.get(mCompareCurrency);
    	    
    	   // Set<String> keyset = mRateMap.keySet();
    	   // Iterator iter = keyset.iterator();
    	    
    	    /*
    	    while(iter.hasNext()){
    	    	String key = (String)iter.next();
    	    	Double value = mRateMap.get(key);
    	    //	Log.d(this.getClass().getName(),"ITEMS IN MRATEMAP : " + key + " : " + value);
    	     	    */
    	    
    	    
    	    //A user may have added a currency that doesn't exist and the rate value maybe null
    	    if(currencyRateVersusEuro != null){ 
	    	    Log.e(this.getClass().getName(),"Currency Versus Euro : count " + mCount++ + " : " + currencyRateVersusEuro);
	    	    
	    	    
	    	    double currencyRate = currencyRateVersusEuro / mDefaultCurrencyVersusEuro;
	   			
				double multiple = Double.parseDouble(mDefaultAmount);
				double currencyResult = currencyRate * multiple;
				
				DecimalFormat decimal = new DecimalFormat("#0.0000");
			
				String currencyRateString = decimal.format(currencyResult);
				String inverseRateString = decimal.format(1/currencyRate);
			
	    		TextView t = (TextView) rowView.findViewById(R.id.currencyName);
	         	t.setText(mCompareCurrency);
	            
	           	t = (TextView) rowView.findViewById(R.id.currencyRate);
	            t.setText(currencyRateString);
	            	
	            t = (TextView) rowView.findViewById(R.id.currency_inverse);
	            t.setText("1 " + mCompareCurrency + " = " + inverseRateString + " " + mDefaultCurrency);
    	    }
    	    else
    	    	Log.e(this.getClass().getName(),"Received NULL from mRateMap. Currency = " + mCompareCurrency );
            
    	    return rowView;
    	  }
    	}
   
    
    /*
    private class MyCurrencyAdapter extends CursorAdapter{
    	//private Cursor mCursor;
    	//private Context mContext;
    	private final LayoutInflater mInflater;
 
    	public MyCurrencyAdapter(Context context, Cursor cursor) {
            super(context, cursor, true);
            mInflater = LayoutInflater.from(context);
           // mContext = context;
    	}
    	
    	
 
    	@Override
    	public void bindView(View view, Context context, Cursor cursor) {
    		// TODO Auto-generated method stub
    		
    			mCompareCurrency = cursor.getString(cursor.getColumnIndex(DBAdapter.KEY_CURRENCY));
    			double currencyRateVersusEuro = cursor.getDouble(cursor.getColumnIndex(DBAdapter.KEY_CURRENCY_X_RATE));
    			
    			//Log.d(getLocalClassName(),"Currency Versus Euro = " + currencyRateVersusEuro);
    			
    			double currencyRate = currencyRateVersusEuro / mDefaultCurrencyVersusEuro;
    			   			
    			double multiple = Double.parseDouble(mDefaultAmount);
    			double currencyResult = currencyRate * multiple;
    			
    			DecimalFormat decimal = new DecimalFormat("#0.0000");
			
    			String currencyRateString = decimal.format(currencyResult);
    			String inverseRateString = decimal.format(1/currencyRate);
    		
	    		TextView t = (TextView) view.findViewById(R.id.currencyName);
	         	t.setText(mCompareCurrency);
	            
	           	t = (TextView) view.findViewById(R.id.currencyRate);
	            t.setText(currencyRateString);
	            	
	            t = (TextView) view.findViewById(R.id.currency_inverse);
	            t.setText("1 " + mCompareCurrency + " = " + inverseRateString + " " + mDefaultCurrency);
	                        
     
	            
    	}
    	
    	 
    	@Override
    	public View newView(Context context, Cursor cursor, ViewGroup parent) {
    		// TODO Auto-generated method stub
    		
	    		final View view = mInflater.inflate(R.layout.list_content, parent, false);
	    		return view;
    	}
    	

    }
    */
    

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
	  	     
	  	     if(id == ConverterContentProvider.QUERY_GET_DEFAULT_VALUES){
	  	    	 baseUri = ConverterContentProvider.QUERY_GET_DEFAULT_VALUES_URI;
	  	     }
	  	     else if(id == ConverterContentProvider.UPDATE_EXCHANGE_RATE){
		    	 baseUri = ConverterContentProvider.UPDATE_EXCHANGE_RATE_URI;
		  	 }
	  	     else if(id == ConverterContentProvider.QUERY_GET_SELECTED_CURRENCIES){
		    	 baseUri = ConverterContentProvider.QUERY_GET_SELECTED_CURRENCIES_URI;
		     }
	  	     
	  	    
	  	     
	  	     CursorLoader cursorLoader = new CursorLoader(getActivity(),
	  	       baseUri, projection, null, null, null);
	  	     
	  	     return cursorLoader;
	}

	@Override
	public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
		
		/*
		String default_type = defaults.getString(defaults.getColumnIndex(DBAdapter.KEY_DEFAULT_TYPE));      	
    	if(default_type.compareTo("default_base_currency_list") ==0)
    		mDefaultCurrency = defaults.getString(defaults.getColumnIndex(DBAdapter.KEY_DEFAULT_VALUE));
    	
    	else if(default_type.compareTo("default_base_currency_amount") ==0)
    		mDefaultAmount = defaults.getString(defaults.getColumnIndex(DBAdapter.KEY_DEFAULT_VALUE));
    	
    	else if(default_type.compareTo("refresh_date") == 0){
    		mLastUpdate = defaults.getString(defaults.getColumnIndex(DBAdapter.KEY_DEFAULT_VALUE));
    		mRefreshDateText = "Last update " + getUpdateInterval(mLastUpdate);
    	}
		*/
		

		
			if(loader.getId() == ConverterContentProvider.QUERY_GET_DEFAULT_VALUES ){
    		
	    		for(int j=0;j< data.getCount(); j++){
		        	
		        	data.moveToPosition(j);
	        	
		        	String default_type = data.getString(data.getColumnIndex(DBAdapter.KEY_DEFAULT_TYPE));
		        	
	   	
		        	if(default_type.compareTo("default_base_currency_list") ==0){
		        		
		        		if(data.getString(data.getColumnIndex(DBAdapter.KEY_DEFAULT_VALUE)).length() > 0)
		        			mDefaultCurrency = data.getString(data.getColumnIndex(DBAdapter.KEY_DEFAULT_VALUE));        		
		        	}
		        	
		        	else if(default_type.compareTo("default_base_currency_amount") ==0){
		        		
		        		if(data.getString(data.getColumnIndex(DBAdapter.KEY_DEFAULT_VALUE)).length() > 0)
		        			mDefaultAmount = data.getString(data.getColumnIndex(DBAdapter.KEY_DEFAULT_VALUE));   
		        	}
		        	
		        	else if(default_type.compareTo("refresh_date") ==0){
		        		
		        		if(data.getString(data.getColumnIndex(DBAdapter.KEY_DEFAULT_VALUE)).length() > 0){
		        			mLastUpdate = data.getString(data.getColumnIndex(DBAdapter.KEY_DEFAULT_VALUE));
		        			mRefreshDateText = "Last update " + getUpdateInterval(mLastUpdate);
		        		}
		        	}
	    		}
	    		
	            
	            mHeadingView.setText(mDefaultAmount + " " + mDefaultCurrency + " = ");
	     
			}
			
			else if(loader.getId() == ConverterContentProvider.QUERY_GET_SELECTED_CURRENCIES ){
				
				StringBuffer currencyList = new StringBuffer();
	    		
	    		for(int j=0;j< data.getCount(); j++){
		        	
		        	data.moveToPosition(j);
		        	String currencyString = data.getString(data.getColumnIndex(DBAdapter.KEY_CURRENCY)) + "   -  " + 
		        			data.getString(data.getColumnIndex(DBAdapter.KEY_CURRENCY_DESCRIPTION));
		        	
		        	boolean alreadyInList = false;
		        	
		        	for(String s : mSelectedCurrencyArray){
		        		if(s.equals(currencyString)){
		        			alreadyInList = true;
		        			break;
		        		}		        			
		        	}
		        	
		        	if(!alreadyInList){
		        		mSelectedCurrencyArray.add(currencyString);
		        		//Log.e(getActivity().getLocalClassName(),"ADDING TO SELECTED_CURRENCY_ARRAY : " + currencyString);
		        		
		        	
		        	

		        	String currency = (String)data.getString(data.getColumnIndex(DBAdapter.KEY_CURRENCY));
		        	String date = (String)data.getString(data.getColumnIndex(DBAdapter.KEY_CURRENCY_SINGLE_X_RATE_DATE));
		        	Double rate = (Double)data.getDouble(data.getColumnIndex(DBAdapter.KEY_CURRENCY_X_RATE));
		        	
		        	
		        	currencyList.append("+EUR" + currency + "=X");
		   
		        	
		        	mRateDateMap.put(currency, date);
		        	mRateMap.put(currency,rate);
		        	
		        	}
		        	
		        	
		        }
	    		
	    		//mDefaultCurrencyVersusEuro = mRateMap.get(mDefaultCurrency);

	    		
	    		/*
	    		mLastUpdate = mRateDateMap.get(mCurrencyLeft);
	    		mRefreshDateText = getUpdateInterval(mLastUpdate);
	    		
	    		DecimalFormat decimal4 = new DecimalFormat("#0.0000");
	        	 mXrateString = decimal4.format(mXrate);
	        	 mXrateUpdateMessage.setText("Rate : " + mXrateString +  mRefreshDateText);
	        	 */
	    		
	    		
	    		/*
	    		 * StringBuffer currencylist = new StringBuffer();
	        
	        for(int i=0; i < currencies.getCount(); i++){
	        	currencies.moveToPosition(i);
	        	currencylist.append("+EUR" + currencies.getString(currencies.getColumnIndex(DBAdapter.KEY_CURRENCY)) + "=X");
	        }
	        
	      //Let's make sure we add the Base currency of "Currency List" also.
	        currencylist.append("+EUR" + mDefaultCurrency + "=X");
	        
	        String temp = currencylist.toString();
	        //String trimmedList = temp.replaceFirst("+", "");
	        
	        mCurrencyString = "http://finance.yahoo.com/d/quotes.csv?s=" + temp + "&f=sl1d1t1";
	        
	    	
	        startRefreshNotification();
	        mXratesTask = new DownloadXRatesTask();
	        mXratesTask.execute(mCurrencyString); 
	    		 */


				currencyList.append("+EUR" + mDefaultCurrency + "=X");
	        	String temp = currencyList.toString();
	        	mCurrencyString = "http://finance.yahoo.com/d/quotes.csv?s=" + temp + "&f=sl1d1t1";
				
		    	startRefreshNotification();
		        mXratesTask = new DownloadXRatesTask();
		        mXratesTask.execute(mCurrencyString);
	    		
	    	}
			
 
	    	

	}


	@Override
	public void onLoaderReset(Loader<Cursor> arg0) {
		// TODO Auto-generated method stub
		
	} 
}