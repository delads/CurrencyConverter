package com.pocketools.currency;


import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;



import org.json.JSONArray;
import org.json.JSONObject;

//import com.adwhirl.AdWhirlLayout;
//import com.adwhirl.AdWhirlTargeting;
//import com.adwhirl.adapters.AdWhirlAdapter;
//import com.google.ads.AdSenseSpec;
//import com.google.ads.GoogleAdView;
//import com.google.ads.AdSenseSpec.AdType;
//import com.google.ads.*;
//import com.google.ads.AdRequest.Gender;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.mopub.mobileads.MoPubInterstitial;
import com.mopub.mobileads.MoPubView;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
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
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ImageView.ScaleType;
import android.widget.RelativeLayout.LayoutParams;



public class News extends ListFragment implements LoaderManager.LoaderCallbacks<Cursor>{
    /** Called when the activity is first created. */


	private String mCurrencyNews = "";
	//private TableLayout mCompanyNewsTable;
	private Context mContext;
	private AnimationDrawable mCompanyInfoAnimation;
	private boolean mCompanyInfoDisplayed = false;
	private NetworkInfo mNetworkInfo = null;
	private ImageView mCompanyInfoLoading;
	private ArrayList<String> mCurrencyArray;
	private int mSpinnerLeftPosition = 0;
	private ArrayAdapter<String> mLeftAdapter;
	private Spinner mLeftSpinner = null;
	private static final int MENU_INFO = 1004;
	private static final int MENU_SETTINGS = 1005;
	private static final int MENU_UPGRADE = 1006;
	private ArrayList<NewsItem> mNewsItems;
	private View mHeaderView;
	private boolean mIsAdsFreeVersion = false;
	private View mRootView = null;
	//private ArrayList<String> currencyArray;
	
	 // TODO: Replace this test id with your personal ad unit id
		private static final String MOPUB_BANNER_AD_UNIT_ID = "be18f21275cb47d1821026f2ace89224";
		private MoPubView moPubView;
    
    
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
	    private static final String CHANNEL_ID = "4907668568";
	    
	  @Override
	    public void onAttach(Activity activity) {
	        super.onAttach(activity);
	        
	    	mContext = this.getActivity();

	        
	    }
	  
	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	  }
	  
    
	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
    

      //  getActivity().setContentView(R.layout.news); 
        
        mRootView = inflater.inflate(R.layout.news, container, false);
        
        
        moPubView = (MoPubView) mRootView.findViewById(R.id.mopub_sample_ad);
		moPubView.setAdUnitId(MOPUB_BANNER_AD_UNIT_ID);
		moPubView.loadAd();
		
        return mRootView;
	  }
	  
	    @Override
	    public void onActivityCreated(Bundle savedInstanceState)
	    {
	    	
        // Tell the main activity that this fragment has menu elements to declare
        super.onActivityCreated(savedInstanceState);

        
        
        mCurrencyArray = new ArrayList<String>();
        
        //Let's add three of the main currencies up the top first
        mCurrencyArray.add("EUR   -  Euro");
        mCurrencyArray.add("GBP   -  United Kingdom Pounds");
        mCurrencyArray.add("USD   -  United States Dollars");
        
        getLoaderManager().initLoader(ConverterContentProvider.QUERY_GET_CURRENCIES, null, this);
        
    
        /*
        for(int j=0;j< allCurrencies.getCount(); j++){
        	
        	allCurrencies.moveToPosition(j);
        	
        	mCurrencyArray.add(allCurrencies.getString(allCurrencies.getColumnIndex(DBAdapter.KEY_CURRENCY)) + "   -  " + 
        			allCurrencies.getString(allCurrencies.getColumnIndex(DBAdapter.KEY_CURRENCY_DESCRIPTION)));
        	
        }
   
        
        Cursor defaults = mDb.getDefaultValues();
        getActivity().startManagingCursor(defaults);
        *
        boolean hasNewsCurrencyDefaultSet = false;
        String baseCurrency = "";
        */
        /*
        for(int i=0; i< defaults.getCount(); i++){
        	defaults.moveToPosition(i);
        	
        	String default_type = defaults.getString(defaults.getColumnIndex(DBAdapter.KEY_DEFAULT_TYPE)); 
        	
        	if(default_type.compareTo("default_news_currency") ==0){
        		
        		if(defaults.getString(defaults.getColumnIndex(DBAdapter.KEY_DEFAULT_VALUE)).length() > 0){
        			mCurrencyNews = defaults.getString(defaults.getColumnIndex(DBAdapter.KEY_DEFAULT_VALUE));
        			hasNewsCurrencyDefaultSet = true;
        		}
        	}
        		
    		else if(default_type.compareTo("default_base_currency") ==0){
        		
        		if(defaults.getString(defaults.getColumnIndex(DBAdapter.KEY_DEFAULT_VALUE)).length() > 0){
        			baseCurrency = defaults.getString(defaults.getColumnIndex(DBAdapter.KEY_DEFAULT_VALUE));
        		}
        	}
        	
    		else if(default_type.compareTo("ads_free_version") ==0){       		
        		mIsAdsFreeVersion = true;
        	}

        }
        
        */
        
        
        //Log.d(getLocalClassName(),"Base Currency is = " + baseCurrency);
        /*
        //We should only be executing this once. Once this is set, then we never go in here again
        if(!hasNewsCurrencyDefaultSet){
        	mDb.addDefaultNewsCurrency(baseCurrency);
        	mCurrencyNews = baseCurrency;
        }
        */
        
        //Log.d(getLocalClassName(),"mCurrencyNews = " + mCurrencyNews);
        

        
        
        
        
        for(int j=0; j< mCurrencyArray.size(); j++){
	      	
        	String longCurrency = mCurrencyArray.get(j);
        	
        	String currency = longCurrency.substring(0,longCurrency.indexOf(" "));     	
        	
        	if(currency.compareTo(mCurrencyNews) == 0)
        		mSpinnerLeftPosition = j;
        	
        }      
        
        
        LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mHeaderView = vi.inflate(R.layout.news_header, null);
        
        mLeftSpinner = (Spinner) mHeaderView.findViewById(R.id.currency_spinner_news);
        mLeftAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, mCurrencyArray);
        mLeftAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mLeftSpinner.setAdapter(mLeftAdapter);
        mLeftSpinner.setSelection(mSpinnerLeftPosition,false);
       
     // This listener is used to set the selected timeframe from the Spinner.
        mLeftSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {
		public void onItemSelected(AdapterView parent, View v, int position,
              long id) {
				String longCurrency = (String)mLeftAdapter.getItem(position);
				mCurrencyNews = longCurrency.substring(0,longCurrency.indexOf(" "));
         	  	
				//mDb.getDatabase().setLockingEnabled(true);
          	  	//mDb.updateDefaultNewsCurrency(mCurrencyNews); 
          	  	
          	  String placeholder = "EUR";
              if(mCurrencyNews.compareToIgnoreCase("EUR")==0){
              	//Log.d(getLocalClassName(),"mCurrencyNews = EUR");
              	placeholder = "GBP";
              }

              String url = "http://www.google.com/finance/company_news?q=" + mCurrencyNews + placeholder  + "&output=json&num=20";
              //Log.d(getLocalClassName(),url);
              
              mCompanyInfoDisplayed = false;
             // TableRow companyNews = (TableRow)findViewById(R.id.company_news_row);
             // companyNews.setVisibility(View.GONE);
              
              
              mNewsItems.clear();
 		     ListAdapter listAdapter = new MyNewsAdapter(mContext,com.pocketools.currency.R.layout.news_content,mNewsItems); 		        
	         getListView().setAdapter(listAdapter);
              
              
              TableRow row = (TableRow)mRootView.findViewById(R.id.company_info_animation);
			  row.setVisibility(View.VISIBLE);   
			    
              new DownloadNewsTask().execute(url);
     	  	
          }
	        public void onNothingSelected(AdapterView arg0) {
	          // NOP
	        }
	      });       


        
        
        
        mNewsItems = new ArrayList<NewsItem>();

        
        ListAdapter listAdapter = new MyNewsAdapter(getActivity(),com.pocketools.currency.R.layout.news_content,mNewsItems); 
        ListView listView = getListView();
        listView.addHeaderView(mHeaderView);
        listView.setAdapter(listAdapter);
        
        
        
        
        if(!mIsAdsFreeVersion){
        	
        	/*
        	
        	 // Create the adView
		      AdView adView = new AdView(this, AdSize.BANNER, ADMOB_PUBLISHER_ID);
	
		      // Lookup your LinearLayout assuming been given
		      // the attribute android:id="@+id/mainLayout"
		      LinearLayout layout = (LinearLayout)findViewById(R.id.news_adwhirl_layout);
	
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
			 
		        // Required Google AdSense network parameters 
		    	AdWhirlAdapter.setGoogleAdSenseCompanyName(COMPANY_NAME); 
		    	AdWhirlAdapter.setGoogleAdSenseAppName(APP_NAME); 
		    	 
		    	// Optional Google AdSense network parameters 
		    	AdWhirlAdapter.setGoogleAdSenseChannel(CHANNEL_ID); 
		    	AdWhirlAdapter.setGoogleAdSenseExpandDirection("TOP"); 
		    	
		    	AdWhirlTargeting.setKeywords(KEYWORDS);
		   	
		    	LinearLayout layout = (LinearLayout) findViewById(R.id.news_adwhirl_layout); 
		    	AdWhirlLayout adWhirlLayout = new AdWhirlLayout(this, "ae63b132e3394d66b354d9d72c839b5d"); 
		    	adWhirlLayout.setBackgroundColor(Color.BLACK);
		    	
		    	final int DIP_HEIGHT = 50; 
		    	final float DENSITY = getResources().getDisplayMetrics().density; 
		    	int scaledHeight = (int) (DENSITY * DIP_HEIGHT + 0.5f); 
		    	adWhirlLayout.setMaxHeight(scaledHeight);
		    	
		    	*/
				
		    	
		    	/*
		    	final int DIP_WIDTH = 320; 
		    	final int DIP_HEIGHT = 52; 
		    	final float DENSITY = getResources().getDisplayMetrics().density; 
		    	int scaledWidth = (int) (DENSITY * DIP_WIDTH + 0.5f); 
		    	int scaledHeight = (int) (DENSITY * DIP_HEIGHT + 0.5f); 
		    	
		    	RelativeLayout.LayoutParams adWhirlLayoutParams = 
		    	    new RelativeLayout.LayoutParams(scaledWidth, scaledHeight); 
				*/
				
				
		    	
		    	RelativeLayout.LayoutParams adWhirlLayoutParams = 
		    	    new RelativeLayout.LayoutParams( 
		    	        LayoutParams.WRAP_CONTENT, 
		    	        LayoutParams.WRAP_CONTENT);

		    	
		    	/*
		    	layout.addView(adWhirlLayout, adWhirlLayoutParams); 
		    	layout.invalidate(); 
		        */
			 
        }
        
        
        // Set up GoogleAdView.
        /*
        GoogleAdView adView = (GoogleAdView) findViewById(R.id.adview_news);
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
        
       
             
		ConnectivityManager serviceConn = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
	    mNetworkInfo = serviceConn.getActiveNetworkInfo();
    
        
        if(mNetworkInfo != null){
     
        //mCompanyNewsTable = (TableLayout)findViewById(com.pocketools.currency.R.id.company_news_table);
       
        String placeholder = "EUR";
        if(mCurrencyNews.compareToIgnoreCase("EUR")==0){
        	//Log.d(getLocalClassName(),"mCurrencyNews = EUR");
        	placeholder = "GBP";
        }

        String url = "http://www.google.com/finance/company_news?q=" + mCurrencyNews + placeholder  + "&output=json&num=20";
        //Log.d(getLocalClassName(),url);
        
        new DownloadNewsTask().execute(url);
        
       
        }  // if(mNetworkInfo != null){
        else{
        	//Log.d(getLocalClassName(),"Connection Error");
	    	Toast.makeText(getActivity(), "Connection Error. Please check your connections and try again.", Toast.LENGTH_SHORT).show();
        }
                 
 
    }
    
    
    public void onListItemClick(ListView parent, View v, int position, long id) { 
    	
    	//Position is not zero counting. Need to reduce by one
    	NewsItem item = (NewsItem)mNewsItems.get(position-1);
    	
    	if(item.getURL().length() > 1)
    		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(item.getURL())));
    }
    

	
	
	private class DownloadNewsTask extends AsyncTask {
		
		String mUrl = "";
		
	     public Object doInBackground(Object... urls) {
	    	 
	    	 return URLFetcher.getString((String)urls[0]); 
	     }

	     public void onPostExecute(Object result) {	 
	    	 
	    	 String jsonResult = (String)result;
	    	 mNewsItems = new ArrayList<NewsItem>();

	    	 
	    	 	mCompanyInfoDisplayed = true;
	    	 	

	    	 
	    	 if(jsonResult != null){

    	 
		    	 String trimmedResult = Utilities.searchForString("{clusters:", ",results_per_page", new StringBuffer(jsonResult));
		    	 String decodedResult = trimmedResult;
		    	 
		    	 if(trimmedResult != null){
		    		 decodedResult = trimmedResult.replace("x26#39;", "'");
		    		 decodedResult = decodedResult.replace("x26amp;", "&");
		    	 }
		    	 
		    	 
	    	 
		    	 try{
		    		 
		    		// String decodedResult = java.net.URLDecoder.decode(trimmedResult,"UTF-8");

	    			 JSONArray array = new JSONArray(decodedResult);	    			 
		    		 
	    			 if(array.length() > 1){
	    				 
	    				 
	    			 
		    		 
		    		 //Let's loop through first and fetch all thumbnails
		    		 for(int i=0;i< array.length();i++){
		    			 
		    			 
		    			 JSONObject element = array.getJSONObject(i);
		    			 String id = element.getString("id");
		    			 
		    			 if(id.compareTo("-1") !=0){
		    				 
		    				 NewsItem article = new NewsItem();
		  
			    			 JSONArray newsArray = element.getJSONArray("a");
			    			 
			    			 JSONObject newsObject = newsArray.getJSONObject(0);
			    			 
			    			 String title = newsObject.getString("t");
			    			 
			    			 String decodedTitle = title;
			    			 try{
			    				 decodedTitle = java.net.URLDecoder.decode(title,"UTF-8");
			    			 }catch(Exception e){}
			    			 
			    			 
			    			 String newsSource = newsObject.getString("s");			    			 
			    			 String url = newsObject.getString("u");		    			 
			    			 String summary = newsObject.getString("sp");
			    			 String publishDate = newsObject.getString("d");
			    			 
			    			 article.setHeading(title);
			    			 article.setSource(newsSource);
			    			 article.setSummary(summary);
			    			 article.setFreshness(publishDate);
			    			 article.setURL(url);
			    			 
			    			 mNewsItems.add(article);
	    				 
		    			 }	
		    		 }
		    		 
		    		 }else{
		    			 
		    			 mNewsItems = new ArrayList<NewsItem>();
		    			 	NewsItem item = new NewsItem();
		    			 	item.setSummary("No News Available for " + mCurrencyNews);
		    			 	mNewsItems.add(item);

		    		 }
	    			 
		    	 }catch(Exception e){
		    		 
		    		 mNewsItems = new ArrayList<NewsItem>();
    			 	NewsItem item = new NewsItem();
    			 	item.setSummary("No News Available for " + mCurrencyNews);
    			 	mNewsItems.add(item);

		    	 }
		    	 
	    	 
	    	 }else{
	    		 
	    		 mNewsItems = new ArrayList<NewsItem>();
 			 	NewsItem item = new NewsItem();
 			 	item.setSummary("No News Available for " + mCurrencyNews);
 			 	mNewsItems.add(item);
  		 
	    	 }
	    	 
	    	 
		     ListAdapter listAdapter = new MyNewsAdapter(mContext,com.pocketools.currency.R.layout.news_content,mNewsItems); 		        
	         getListView().setAdapter(listAdapter);
	         
	         
	    	 	if(mCompanyInfoAnimation != null)
	    	 		mCompanyInfoAnimation.stop();
			    
			    TableRow row = (TableRow)mRootView.findViewById(R.id.company_info_animation);
			    row.setVisibility(View.GONE); 	 	
	         
	     } 

	     
	}
		/*
	@Override
    public void onWindowFocusChanged(boolean hasFocus){
		
 	
    	if(hasFocus){ 		
    		
    		if(mNetworkInfo != null && mCompanyInfoDisplayed == false){
	        	mCompanyInfoLoading = (ImageView)findViewById(R.id.company_info_loading);
	        	mCompanyInfoLoading.setBackgroundResource(R.anim.spin_animation);
	        	
	        	// Get the background, which has been compiled to an AnimationDrawable object.
	        	 mCompanyInfoAnimation = (AnimationDrawable) mCompanyInfoLoading.getBackground();
	
	        	 // Start the animation (looped playback by default).
	        	 mCompanyInfoAnimation.start();	        	 
    		}		
    		
    	}
    	
    	super.onWindowFocusChanged(hasFocus);
    }
    */
	
	/*
    public boolean onCreateOptionsMenu(Menu menu) {
    	boolean result = super.onCreateOptionsMenu(menu);

    	
	    	menu.add(0,MENU_SETTINGS,0,"Settings")
	 		.setIcon(android.R.drawable.ic_menu_preferences);
    	
	    	menu.add(0,MENU_INFO,0,"Info")
	 		.setIcon(android.R.drawable.ic_menu_help);
	    	
    	  return result;

    }
    */

    
    
    
    private class MyNewsAdapter extends ArrayAdapter<NewsItem>{
    	
    	private ArrayList<NewsItem> mList;
 
    	public MyNewsAdapter(Context context, int resourceId, ArrayList<NewsItem> arrayList) {
            super(context, resourceId, arrayList);
            this.mList = arrayList;
            mContext = context;
    	}
    	
    	 
    	@Override
    	public View getView(int position, View convertView, ViewGroup parent) {
    		// TODO Auto-generated method stub
    		
    		LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    		View view = vi.inflate(R.layout.news_content, null);
            
    		
    		NewsItem article = (NewsItem)mList.get(position);
    		
    		TextView heading = (TextView)view.findViewById(R.id.news_heading);
    		heading.setText(article.getHeading());
    		//Log.d(getLocalClassName(),article.getHeading());
    		
    		TextView source = (TextView)view.findViewById(R.id.news_source);
    		source.setText(article.getSource());
    		
    		TextView summary = (TextView)view.findViewById(R.id.news_summary);
    		summary.setText(article.getSummary());
    		
    		TextView freshness = (TextView)view.findViewById(R.id.news_freshness);
    		freshness.setText(article.getFreshness());
	    	
    		return view;
    	}

    } 
    
	
    
    
    @Override
    public void onSaveInstanceState(Bundle outState) {      
        //Log.d(this.getLocalClassName(), "onSaveInstanceState()");
        
        super.onSaveInstanceState(outState);
    }
    
    @Override
    public void onPause() {
        //Log.d(this.getLocalClassName(), "onPause()");

        super.onPause();
        
        
    }
    
    @Override
    public void onStop() {      
        //Log.d(this.getLocalClassName(), "onStop()");
        
        super.onStop();
    }

    
    @Override
    public void onDestroy() {
        //Log.d(this.getLocalClassName(), "onDestroy()");
    	
    	if(moPubView != null)
    		moPubView.destroy();
    	
        super.onDestroy();
    }
    
    @Override
    public void onResume() {
    	//Log.d(this.getLocalClassName(), "onResume()");
    	
    	
    	super.onResume();
    }
    


    
    public void onStart() {
    	//Log.d(this.getLocalClassName(), "onStart()");

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

//     Log.e(getActivity().getLocalClassName(),"BaseUri = " + baseUri.getLastPathSegment());
     
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
	        	
	        	for(String s : mCurrencyArray){
	        		if(s.equals(currencyString)){
	        			alreadyInList = true;
	        			break;
	        		}		        			
	        	}
	        	
	        	if(!alreadyInList)
	        		mCurrencyArray.add(currencyString);
	        	
	        	/*
	        	String currency = (String)data.getString(data.getColumnIndex(DBAdapter.KEY_CURRENCY));
	        	String date = (String)data.getString(data.getColumnIndex(DBAdapter.KEY_CURRENCY_SINGLE_X_RATE_DATE));
	        	Double rate = (Double)data.getDouble(data.getColumnIndex(DBAdapter.KEY_CURRENCY_X_RATE));
	   
	        	
	        	mRateDateMap.put(currency, date);
	        	mRateMap.put(currency,rate);
	        	*/
	        	
	        	
	        	
	        //	Log.e(getActivity().getLocalClassName(),currency + " : " + rate + " : " + date);
	        	
	        }
    		
	
    	}
    	
    	
    	
    	        
    }
    
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
     //do nothing
    }
   
    
}