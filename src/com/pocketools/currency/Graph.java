package com.pocketools.currency;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;


public class Graph extends FragmentActivity implements LoaderManager.LoaderCallbacks<Cursor> {
    /** Called when the activity is first created. */

    private ImageButton mImageButton;
    private View mGraphView = null;
    private TableRow mTimeframeRow;
    private String mCurrencyLeft;
    private String mCurrencyRight;
    private String mTimeframe;
    private Spinner mSpinnerLeft;
    private ArrayAdapter mLeftAdapter;
    private ArrayAdapter mRightAdapter;
    private Spinner mSpinnerRight;
    private int mSpinnerLeftPosition = 0;
    private int mSpinnerRightPosition = 0;
    private String mUrl;
    boolean firstCallLeft = true;
    boolean firstCallRight = true;
   // GoogleAnalyticsTracker mTracker;
   // DatabaseHelper mDb;
    ArrayList<String> currencyArray;
    //AdView mAdView;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        
      //  mTracker = GoogleAnalyticsTracker.getInstance();       
        // Start the tracker in manual dispatch mode...
      //  mTracker.start(PocketCurrency.ANALYTICS_UA_NUMBER, this);
       // mTracker.trackPageView("/Graph");
        
        
        
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,   
                WindowManager.LayoutParams.FLAG_FULLSCREEN);  
        
        if(mGraphView == null){
        	mGraphView = LayoutInflater.from(this).inflate(R.layout.graph, null);
        }
        
        setContentView(mGraphView);   
        
        
        
        
        currencyArray = new ArrayList<String>();
        
       //Let's add three of the main currencies up the top first
        currencyArray.add("EUR   -  Euro");
        currencyArray.add("GBP   -  United Kingdom Pounds");
        currencyArray.add("USD   -  United States Dollars");
        
        
      //  mDb = new DatabaseHelper(this);
      //  mDb.open();
               
        getSupportLoaderManager().initLoader(ConverterContentProvider.QUERY_GET_CURRENCIES, null, this);
        
        
        /*
        Cursor currencies = mDb.getCurrencies();
        startManagingCursor(currencies);
        
        StringBuffer currencylist = new StringBuffer();
        
        for(int i=0; i < currencies.getCount(); i++){
        	
        	currencies.moveToPosition(i);
        	
        	currencylist.append("+EUR" + currencies.getString(currencies.getColumnIndex(DatabaseHelper.KEY_CURRENCY)) + "=X");
        	
        	currencyArray.add(currencies.getString(currencies.getColumnIndex(DatabaseHelper.KEY_CURRENCY)) + "   -  " + 
        			currencies.getString(currencies.getColumnIndex(DatabaseHelper.KEY_CURRENCY_DESCRIPTION)));
        }
        */
        
        
       
     //   mAdView = (AdView) findViewById(R.id.ad_currency_graph);
               
        mTimeframe = this.getIntent().getExtras().getString("TIMEFRAME");
        mCurrencyLeft = this.getIntent().getExtras().getString("CURRENCY_LEFT");
        mCurrencyRight = this.getIntent().getExtras().getString("CURRENCY_RIGHT");
        
       
        String url = consts.get(mTimeframe) + mCurrencyLeft + mCurrencyRight + "=x";
     
       mImageButton = (ImageButton) findViewById(R.id.graphButton); 
       

       
       
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
       
       
       mSpinnerLeft = (Spinner)findViewById(R.id.graph_spinner_left);
       mLeftAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, currencyArray);
       mLeftAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       mSpinnerLeft.setAdapter(mLeftAdapter);
       mSpinnerLeft.setSelection(mSpinnerLeftPosition);
       
       
       mSpinnerRight = (Spinner)findViewById(R.id.graph_spinner_right);
       mRightAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, currencyArray);
       mRightAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
       mSpinnerRight.setAdapter(mRightAdapter);
       mSpinnerRight.setSelection(mSpinnerRightPosition);
       
       
       // This listener is used to set the selected timeframe from the Spinner.
       mSpinnerLeft.setOnItemSelectedListener(new OnItemSelectedListener() {
		public void onItemSelected(AdapterView parent, View v, int position,
             long id) {
				String longCurrency = (String)mLeftAdapter.getItem(position);
				mCurrencyLeft = longCurrency.substring(0,longCurrency.indexOf(" "));
				
				if(!firstCallLeft){
					
					//mAdView.setKeywords(PocketCurrency.AD_MOB_KEYWORD_HINT + mCurrencyLeft + " " + mCurrencyRight);
					//mAdView.requestFreshAd(); 
					 
					try{
						mUrl = consts.get(mTimeframe) + mCurrencyLeft + mCurrencyRight + "=x";
			        	new DownloadImageTask(v.getContext()).execute(mUrl);
		       	
			        }catch (Exception e){Log.e("PocketCurrency", "Application Exited Abnormally !");}
				}
				else
					firstCallLeft = false;
         	  	
		}
        public void onNothingSelected(AdapterView arg0) {
          // NOP
        }
      });
       
       
    // This listener is used to set the selected timeframe from the Spinner.
       mSpinnerRight.setOnItemSelectedListener(new OnItemSelectedListener() {
		public void onItemSelected(AdapterView parent, View v, int position,
             long id) {
				String longCurrency = (String)mRightAdapter.getItem(position);
				mCurrencyRight = longCurrency.substring(0,longCurrency.indexOf(" "));
				
				if(!firstCallRight){
					 
					try{
						mUrl = consts.get(mTimeframe) + mCurrencyLeft + mCurrencyRight + "=x";
			        	new DownloadImageTask(v.getContext()).execute(mUrl);
		       	
			        }catch (Exception e){Log.e("PocketCurrency", "Application Exited Abnormally !");}
				}
				else
					firstCallRight = false;
         	  	
		}
        public void onNothingSelected(AdapterView arg0) {
          // NOP
        }
      });
	

       
       
       
       
       
       mImageButton.setOnClickListener(new Button.OnClickListener() {
           public void onClick(View v) {
          	
           	Intent intent = new Intent(v.getContext(),GraphDuration.class); 
           	startActivityForResult(intent, Converter.CHOOSE_TIMEFRAME);
           }
       });
       
       

       mTimeframeRow = (TableRow)findViewById(R.id.graph_timeframeRow);
       mTimeframeRow.setClickable(true);
       
       
       mTimeframeRow.setOnClickListener(new Button.OnClickListener() {
           public void onClick(View v) {
          	
           	Intent intent = new Intent(v.getContext(),GraphDuration.class);         
           	startActivityForResult(intent, Converter.CHOOSE_TIMEFRAME);
           }
       });        
   
   
      TextView textView = (TextView)findViewById(R.id.graph_timeframe_1d);      
      if(mTimeframe.compareTo("1 day") == 0)  	  
    	  textView.setTextColor(Color.BLUE);   	  
      else 
    	  textView.setTextColor(Color.BLACK); 
      
      
      textView = (TextView)findViewById(R.id.graph_timeframe_5d);
      if(mTimeframe.compareTo("5 day") == 0)
    	  textView.setTextColor(Color.BLUE);   	  
      else 
    	  textView.setTextColor(Color.BLACK); 
      
      textView = (TextView)findViewById(R.id.graph_timeframe_3m);
      if(mTimeframe.compareTo("3 month") == 0)
    	  textView.setTextColor(Color.BLUE);   	 
      else 
    	  textView.setTextColor(Color.BLACK); 
      
      textView = (TextView)findViewById(R.id.graph_timeframe_1y);
      if(mTimeframe.compareTo("1 year") == 0)
    	  textView.setTextColor(Color.BLUE);   	  
      else 
    	  textView.setTextColor(Color.BLACK); 
      
      textView = (TextView)findViewById(R.id.graph_timeframe_2y);
      if(mTimeframe.compareTo("2 year") == 0)
    	  textView.setTextColor(Color.BLUE);   	  
      else 
    	  textView.setTextColor(Color.BLACK); 
      
      
      textView = (TextView)findViewById(R.id.graph_timeframe_5y);
      if(mTimeframe.compareTo("5 year") == 0)
    	  textView.setTextColor(Color.BLUE);   	  
      else 
    	  textView.setTextColor(Color.BLACK); 
       
      
       ConnectivityManager serviceConn = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
	    NetworkInfo networkInfo = serviceConn.getActiveNetworkInfo();
	    
	    if(networkInfo != null){        
	        try{
	        	new DownloadImageTask(this).execute(url);
       	
	        }catch (Exception e){Log.e("PocketCurrency", "Application Exited Abnormally !");}
	    } 
	    else{
	    	Toast.makeText(this, "Connection Error. Please check your connections and try again.", Toast.LENGTH_SHORT).show();
	    	finish();
	    }
	    
   	 //  mTracker.dispatch();
     
    }
    
    
    
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        // See which child activity is calling us back.
    	switch (requestCode) {
    	
	    	case Converter.CHOOSE_TIMEFRAME:
	    		
	    		
	    		if(resultCode == Converter.CHOOSE_TIMEFRAME_SUCCESS){

		    		mTimeframe = data.getExtras().getString("TIMEFRAME");
		            mUrl = consts.get(mTimeframe) + mCurrencyLeft + mCurrencyRight + "=x";
	            
		            ConnectivityManager serviceConn = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
		    	    NetworkInfo networkInfo = serviceConn.getActiveNetworkInfo();
		    	    
		    	    TextView textView = (TextView)findViewById(R.id.graph_timeframe_1d);      
		    	      if(mTimeframe.compareTo("1 day") == 0)  	  
		    	    	  textView.setTextColor(Color.BLUE);   	  
		    	      else 
		    	    	  textView.setTextColor(Color.BLACK); 
		    	      
		    	      
		    	      textView = (TextView)findViewById(R.id.graph_timeframe_5d);
		    	      if(mTimeframe.compareTo("5 day") == 0)
		    	    	  textView.setTextColor(Color.BLUE);   	  
		    	      else 
		    	    	  textView.setTextColor(Color.BLACK); 
		    	      
		    	      textView = (TextView)findViewById(R.id.graph_timeframe_3m);
		    	      if(mTimeframe.compareTo("3 month") == 0)
		    	    	  textView.setTextColor(Color.BLUE);   	 
		    	      else 
		    	    	  textView.setTextColor(Color.BLACK); 
		    	      
		    	      textView = (TextView)findViewById(R.id.graph_timeframe_1y);
		    	      if(mTimeframe.compareTo("1 year") == 0)
		    	    	  textView.setTextColor(Color.BLUE);   	  
		    	      else 
		    	    	  textView.setTextColor(Color.BLACK); 
		    	      
		    	      textView = (TextView)findViewById(R.id.graph_timeframe_2y);
		    	      if(mTimeframe.compareTo("2 year") == 0)
		    	    	  textView.setTextColor(Color.BLUE);   	  
		    	      else 
		    	    	  textView.setTextColor(Color.BLACK); 
		    	      
		    	      
		    	      textView = (TextView)findViewById(R.id.graph_timeframe_5y);
		    	      if(mTimeframe.compareTo("5 year") == 0)
		    	    	  textView.setTextColor(Color.BLUE);   	  
		    	      else 
		    	    	  textView.setTextColor(Color.BLACK); 
		    	    
		    	    if(networkInfo != null){        
		    	        try{
		    	        	new DownloadImageTask(this).execute(mUrl);
		           	
		    	        }catch (Exception e){Log.e("PocketCurrency", "Application Exited Abnormally !");}
		    	    } 
		    	    else{
		    	    	Toast.makeText(this, "Connection Error. Please check your connections and try again.", Toast.LENGTH_SHORT).show();
		    	    	finish();
		    	    }
	            
	    		}
		    	
		    	
                
              default:
            	  break;
        }
    }
    
    private class DownloadImageTask extends AsyncTask {
    	
    	ProgressDialog mProgressDialog;
    	Context mContext;
    	
    	public DownloadImageTask(Context context){
    		
    		mContext = context;
    		
    		mProgressDialog = new ProgressDialog(context);
    		mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
       	 	mProgressDialog.setMessage("Generating Chart ... ");
       	 	mProgressDialog.show();
    		
    	}
		
	     public Object doInBackground(Object... urls) {
	        // return loadImageFromNetwork(urls[0]);
	    	 return URLFetcher.getBitmap((String)urls[0]);
	    	 
	     }

	     public void onPostExecute(Object result) {	    	 

	    	 
	    	 if(result != null){
	    		 mImageButton.setImageBitmap((Bitmap)result); 
	    		 //setGraphTextViews(mTimeframe);
	    		 mProgressDialog.dismiss();
	    	 }  
	    	 
	    	 
	     }     
	}
    
    public final static HashMap<String, String> consts = new HashMap<String, String>();
    	static
    	{
    		consts.put("1 day","http://ichart.finance.yahoo.com/b?s=");
        	consts.put("5 day","http://ichart.finance.yahoo.com/w?s=");
        	consts.put("3 month","http://ichart.finance.yahoo.com/3m?");
        	consts.put("1 year","http://ichart.finance.yahoo.com/1y?");
        	consts.put("2 year","http://ichart.finance.yahoo.com/2y?");
        	consts.put("5 year","http://ichart.finance.yahoo.com/5y?");
    	}
	
	public final static String[] timeframeList = {"1d","5d","3m","1y","2y","5y"};
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {      
        Log.d(this.getLocalClassName(), "onSaveInstanceState()");
        
        super.onSaveInstanceState(outState);
    }
    
    @Override
    protected void onPause() {
       // Log.d(this.getLocalClassName(), "onPause()");

        super.onPause();
    }
    
    @Override
    protected void onStop() {      
       // Log.d(this.getLocalClassName(), "onStop()");
        
    	/*
        if(mDb.getDatabase().isOpen())
        	mDb.close();
  		*/
    	
        super.onStop();
       
    	
    }
    
    @Override
    protected void onDestroy() {
       // Log.d(this.getLocalClassName(), "onDestroy()");
        /*
        if(mDb.getDatabase().isOpen())
        	mDb.close();
        	
        	*/
    	
        
        super.onDestroy();
        
   //     mTracker.stop();
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
	
        super.onStart();  
}
    
	 // This is called when a new Loader needs to be created.
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
     String[] projection = { 
       DatabaseHelper.KEY_CURRENCY,
       DatabaseHelper.KEY_CURRENCY_DESCRIPTION,
       DatabaseHelper.KEY_CURRENCY_X_RATE,
       DatabaseHelper.KEY_CURRENCY_IS_SELECTED,
       DatabaseHelper.KEY_CURRENCY_X_RATE_REFRESH_DATE,
       DatabaseHelper.KEY_CURRENCY_SINGLE_X_RATE_DATE};
     
     
     Uri baseUri = null;
     
     if(id == ConverterContentProvider.QUERY_GET_CURRENCIES){
    	 baseUri = ConverterContentProvider.QUERY_GET_CURRENCIES_URI;
     }	     

     CursorLoader cursorLoader = new CursorLoader(this,
       baseUri, projection, null, null, null);
     
     return cursorLoader;
    }
    
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
    	
    	if(loader.getId() == ConverterContentProvider.QUERY_GET_CURRENCIES){
    		
    		for(int j=0;j< data.getCount(); j++){
	        	
	        	data.moveToPosition(j);
	        	
	        	currencyArray.add(data.getString(data.getColumnIndex(DatabaseHelper.KEY_CURRENCY)) + "   -  " + 
	        			data.getString(data.getColumnIndex(DatabaseHelper.KEY_CURRENCY_DESCRIPTION)));
	        	
	        }
    		
    	}
        
        
    }
    
    
    
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
     //do nothing
    }
    
    
    
    
   	
 
}