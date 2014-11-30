/*
 * Copyright 2012 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pocketools.currency;

import com.crashlytics.android.Crashlytics;
import com.mopub.common.MoPub;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.SQLException;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;




public class MainActivity extends ActionBarActivity implements ActionBar.TabListener {
	
	private static final int HELLO_ID = 1000;
    private static final int MENU_REFRESH = 1001;
    private static final int MENU_ADD_CURRENCY = 1002;
    private static final int MENU_EDIT_CURRENCY_LIST = 1003;
    private static final int CONVERTER_TAB_INDEX = 1;
    private static final int TWITTER_TAB_INDEX = 3;
    private static final int NEWS_TAB_INDEX = 0;
    private static final int CURRENCY_TAB_INDEX = 2;
    private static final int INTENT_REQUEST_CODE_TWITTER_AUTH = 200;
    private static final int INTENT_REQUEST_CODE_ADD_CURRENCY = 201;
    private static final int INTENT_REQUEST_CODE_EDIT_CURRENCY_LIST = 202;
    private static final int RELOAD_CURRENCY_LIST = 202;
    
    
   // ArrayList<String> mSelectedCurrencyArray;
   // String mCurrencyString;
   // ArrayList<String> currencyArray;
    private NotificationManager mNotificationManager;
    
    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
	private static final String TWITTER_KEY = "dw9Nngg2vkkXH2dVj9QgxrIxC";
	private static final String TWITTER_SECRET = "8xAZQEetL51To9fa62oULdC8SlGUryOLvC3vAaWrLIur6tz9jJ";
	

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide fragments for each of the
     * three primary sections of the app. We use a {@link android.support.v4.app.FragmentPagerAdapter}
     * derivative, which will keep every loaded fragment in memory. If this becomes too memory
     * intensive, it may be best to switch to a {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    AppSectionsPagerAdapter mAppSectionsPagerAdapter;
    FragmentManager mFragmentManager;

    /**
     * The {@link ViewPager} that will display the three primary sections of the app, one at a
     * time.
     */
    ViewPager mViewPager;
    
    
    protected void onStart() {
        super.onStart();
        
        final ActionBar actionBar = getActionBar();
        actionBar.selectTab(getActionBar().getTabAt(CONVERTER_TAB_INDEX));
    }
    
    

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
		getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        
        
		final TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
		Fabric.with(this, new Twitter(authConfig), new MoPub(), new Crashlytics());
		
		
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three primary sections
        // of the app.
        
        mFragmentManager = getSupportFragmentManager();
        mAppSectionsPagerAdapter = new AppSectionsPagerAdapter(mFragmentManager);
        
        // Set up the action bar.
        final ActionBar actionBar = getActionBar();

        // Specify that the Home/Up button should not be enabled, since there is no hierarchical
        // parent.
        actionBar.setHomeButtonEnabled(false);

        // Specify that we will be displaying tabs in the action bar.
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        
        //actionBar.setDisplayOptions(0);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayOptions(3);
        
        
        


        // Set up the ViewPager, attaching the adapter and setting up a listener for when the
        // user swipes between sections.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mAppSectionsPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                // When swiping between different app sections, select the corresponding tab.
                // We can also use ActionBar.Tab#select() to do this if we have a reference to the
                // Tab.
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mAppSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by the adapter.
            // Also specify this Activity object, which implements the TabListener interface, as the
            // listener for when this tab is selected.

            actionBar.addTab(
                    actionBar.newTab()
                            .setText(mAppSectionsPagerAdapter.getPageTitle(i))
                            .setTabListener(this));

        }
      
        
     //   currencyArray = new ArrayList<String>();
     //   mSelectedCurrencyArray = new ArrayList<String>();
        
    }
    
    
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);    
        // Pass the activity result to the fragment, which will
        // then pass the result to the login button.
 
        
        /*
       if(requestCode == INTENT_REQUEST_CODE_ADD_CURRENCY){
    	   
    	   //Refresh the lists
           if(currencyArray != null)
        	   currencyArray.clear();
           
           if(mSelectedCurrencyArray != null)
        	   mSelectedCurrencyArray.clear();
           
           
    	   getSupportLoaderManager().initLoader(ConverterContentProvider.QUERY_GET_SELECTED_CURRENCIES, null, this);
	    	Log.d(getClass().getName(),"Passed Back by the Add Currency Code");
    	   
       }
       */
       /*
       if(requestCode == RELOAD_CURRENCY_LIST){
    	   
    	   String fragmentTag =  "android:switcher:" + R.id.pager + ":" + CURRENCY_TAB_INDEX;
	       Fragment fragment = getSupportFragmentManager().findFragmentByTag(fragmentTag);
	       
	        if (fragment != null) {
	            fragment.onActivityResult(requestCode, resultCode, data);
	        } 
    	   
    	   
       }
       */
        
      
       // Apparently, when FragmentPagerAdapter creates a Fragment, it automatically assigns
       // it the following Tag in the form of "android:switcher:" + viewPager_id + ":" + tab_index"
       String fragmentTag =  "android:switcher:" + R.id.pager + ":" + TWITTER_TAB_INDEX;
       Fragment fragment = getSupportFragmentManager().findFragmentByTag(fragmentTag);
       
        if (fragment != null) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }        
       
  
    }
    /*
    public boolean onCreateOptionsMenu(Menu menu) {
    	
        getMenuInflater().inflate(R.menu.main, menu);
        
        
        menu.add(0,MENU_REFRESH,0,"Refresh Rates")
 		.setIcon(android.R.drawable.ic_menu_rotate);
        
        menu.add(0,MENU_ADD_CURRENCY,0,"Add Currency")
 		.setIcon(android.R.drawable.ic_menu_add); 
        

        menu.add(0,MENU_EDIT_CURRENCY_LIST,0,"Edit Currency List")
 		.setIcon(android.R.drawable.ic_menu_edit); 

 		
        
        

        return super.onCreateOptionsMenu(menu);
    }
    */
    
    

    
    /* Handles item selections */
    /*
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {       
            
        case MENU_REFRESH:      
        	
        	//Let's check the network status once more
        	ConnectivityManager serviceConn = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
    	    NetworkInfo networkInfo = serviceConn.getActiveNetworkInfo();
    	    
    	    if(networkInfo != null){
    	    	//If we do have a connection, then spawn another thread to actually
        		// retrieve x-rates   
    	    	
    	    	//mXrateUpdateMessage.setText("Updating X-Rates ...");
    	    	//mSwapDisplayed = false;
    	    	
    	        startRefreshNotification();
    	        setProgressBarIndeterminateVisibility(true);
    	    	
    	    	getSupportLoaderManager().initLoader(ConverterContentProvider.QUERY_GET_SELECTED_CURRENCIES, null, this);
    	    	
    	    	
    	      //  mXratesTask = new DownloadXRatesTask();
    	      //  mXratesTask.execute(mCurrencyString); 
    	      //  mDb.updateRateDate(Long.toString(System.currentTimeMillis()));
    	    }
    	    
    	    else{
    	    	
    	    	//setTitle(mSyncMessage + mRefreshDateText);
    	    //	mXrateUpdateMessage.setText("Rate : " + mXrate + mRefreshDateText);
    	    	Toast.makeText(this, "Connection Error. Please check your connections and try again.", Toast.LENGTH_SHORT).show();
            }	
        	return true;
        	
   	
        	
        case MENU_ADD_CURRENCY:
        	
        	Intent intent = new Intent(this,AddCurrency.class);
        	startActivityForResult(intent, INTENT_REQUEST_CODE_ADD_CURRENCY);
        	
        	
        case MENU_EDIT_CURRENCY_LIST:
        	
        	intent = new Intent(this,ChooseCurrencyList.class);
        	startActivityForResult(intent, INTENT_REQUEST_CODE_EDIT_CURRENCY_LIST);
            
            
            return true;
            

        }
        
    
        
       
        
        return false;
    }
    */
    
    /*
    private void startRefreshNotification(){
    	
    	// Get the notification manager serivce.
        mNotificationManager = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);
        
        int icon = android.R.drawable.stat_notify_sync;
        CharSequence tickerText = "X-Rate sync with Yahoo Finance";
        long when = System.currentTimeMillis();

        Notification notification = new Notification(icon, tickerText, when);

        
        Context context = getBaseContext(); 
        CharSequence contentTitle = "Currency Converter";
        CharSequence contentText = "Refreshing X-Rates from Yahoo Finance";
        Intent notificationIntent = new Intent(context, MainActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context, 0, notificationIntent, 0);

        notification.setLatestEventInfo(context, contentTitle, contentText, contentIntent);
        mNotificationManager.notify(HELLO_ID, notification);

    }
    */
    
    
 
  
    // This is called when a new Loader needs to be created.
    /*
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
     
     else if(id == ConverterContentProvider.QUERY_GET_SELECTED_CURRENCIES){
    	 baseUri = ConverterContentProvider.QUERY_GET_SELECTED_CURRENCIES_URI;
  	 }
     
     else if(id == ConverterContentProvider.UPDATE_EXCHANGE_RATE){
    	 baseUri = ConverterContentProvider.UPDATE_EXCHANGE_RATE_URI;
  	 }

//     Log.e(getActivity().getLocalClassName(),"BaseUri = " + baseUri.getLastPathSegment());
     
     CursorLoader cursorLoader = new CursorLoader(this, baseUri, projection, null, null, null);
     
     return cursorLoader;
    }
    */
    
    
    /*
    
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    	
    	
    	

    	if(loader.getId() == ConverterContentProvider.QUERY_GET_SELECTED_CURRENCIES ){
    		
    		for(int j=0;j< data.getCount(); j++){
	        	
	        	data.moveToPosition(j);
	        	
	        	String currency = (String)data.getString(data.getColumnIndex(DBAdapter.KEY_CURRENCY));
 	
	        	mSelectedCurrencyArray.add(currency);

	        	
	        	
	        	Log.e(this.getLocalClassName(),"MAIN ACTIVITY - Added " + data.getString(data.getColumnIndex(DBAdapter.KEY_CURRENCY)) + " to Selected Currency Array");
	        	
	        }
    		
    	}
    	
    	StringBuffer currencylist = new StringBuffer();
    	
    	Iterator<String> selectedCurr = mSelectedCurrencyArray.iterator();
        
        
        while(selectedCurr.hasNext()){    	
        	currencylist.append("+EUR" + selectedCurr.next() + "=X");
        }
        
       
        String temp = currencylist.toString();      
        mCurrencyString = "http://finance.yahoo.com/d/quotes.csv?s=" + temp + "&f=sl1d1t1";
    	
        
        DownloadXRatesTask task = new DownloadXRatesTask();
        task.execute(mCurrencyString);
    	
    		
    }
    
    */
    
    /*
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
     //do nothing
    }
    */
    

    
    

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to one of the primary
     * sections of the app.
     */
    public static class AppSectionsPagerAdapter extends FragmentPagerAdapter {

        public AppSectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int i) {
        		
            switch (i) {
                case CONVERTER_TAB_INDEX:
                	return new Converter();
                	
                case NEWS_TAB_INDEX:
                	return new News();
                    
                case TWITTER_TAB_INDEX: 	
                	return new TwitterFragment();

                case CURRENCY_TAB_INDEX:               	
                	return new CurrencyList();
                	
                default:
                	return new Converter();
                	
            }

        }

        @Override
        public int getCount() {
            return 4;
        }

        @Override
        public CharSequence getPageTitle(int position) {
        	
        	switch(position){
        		case 0:
        			return "News";
        		
        		case 1:
        			return "Converter";
        			     		
        		case 2:
        			return "Currencies";
        			
        		case 3:
        			return "Twitter";
        			
        		default:
        			return "Converter";       		
        	}
        }
        
        

        
        
    }
    

    
   /* 
    private class DownloadXRatesTask extends AsyncTask<String, Integer, String>{
    	
    		

		public String doInBackground(String... urls) {	 
	    	 
	    	 ////Log.d("PocketCurrency","Executing DownloadXRatesTask");
 	 
	    	 String result = URLFetcher.getString((String)urls[0]);
	    	 
	    	 
	    	 if(result != null){	    		 
	    		 
	    		 
	    		 
		    	 String resultString = (String)result;
		    	 
	    		 //Initialise the date String. We will come across this first in the xml file
	    		 //so no worries here
		    	 String date = "";
		    	 
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
	    			 
			    			 ContentValues values = new ContentValues();
			    			 values.put("exchange_rate", rateDouble);
			    			 values.put("spare_1", time);
			    			
			    			String where = "currency_type = '" + currencyBeingUpdated + "'";
			    			getContentResolver().update(ConverterContentProvider.UPDATE_EXCHANGE_RATE_URI, values, where, null);
			    			 
			    			 //TODO
			    			//Need to update this in the DB 
			    			// mRefreshDateText = getUpdateInterval(time);
			    			// mLastUpdate = time;
				    		 
				    		 
				    		 
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
	    	 
	    	 setProgressBarIndeterminateVisibility(false);

	     }
	     
		 public void onPreExecute(){
			    	 
			    	//Do Nothing
			     }
			     
	     public void onCancelled(){
	    	     	 
	    	 super.onCancelled();
	     }

	 }
	 
	 */
    
    
    
    
	 
	 
}
