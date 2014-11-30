package com.pocketools.currency;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

import io.fabric.sdk.android.Fabric;
import android.R.color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterApiClient;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterAuthToken;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;
import com.twitter.sdk.android.core.internal.TwitterRequestHeaders;
import com.twitter.sdk.android.tweetcomposer.TweetComposer;
import com.twitter.sdk.android.tweetui.TweetUtils;
import com.twitter.sdk.android.core.models.Search;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.SearchService;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.core.services.FavoriteService;
import com.twitter.sdk.android.core.services.params.Geocode;
import com.twitter.sdk.android.core.services.params.Geocode.Distance;
import com.twitter.sdk.android.tweetui.BaseTweetView;
import com.twitter.sdk.android.tweetui.CompactTweetView;
import com.twitter.sdk.android.tweetui.LoadCallback;
import com.twitter.sdk.android.tweetui.TweetView;
import com.twitter.sdk.android.tweetui.TweetViewAdapter;
import com.twitter.sdk.android.tweetui.TweetViewFetchAdapter;
import com.twitter.sdk.android.Twitter;
import com.mopub.mobileads.MoPubErrorCode;
import com.mopub.mobileads.MoPubInterstitial;
import com.mopub.mobileads.MoPubInterstitial.InterstitialAdListener;
import com.mopub.mobileads.MoPubView;


import android.support.v4.app.ListFragment;
import android.text.TextUtils;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ListView;
//import android.widget.MultiAutoCompleteTextView;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.SearchView;

public class TwitterFragment extends ListFragment implements SearchView.OnQueryTextListener,InterstitialAdListener, AbsListView.OnScrollListener{
	
	private TwitterLoginButton loginButton;
	private TweetView mTweet;
	private View mRootView = null;
	private View mHeaderView = null;
	private Context mContext;
//	private TweetViewFetchAdapter<CompactTweetView> mAdapter;
	
	 private static final int COUNT = 100;
	 private static final String SEARCH_RESULT_TYPE = "mixed";
	 private static final String SEARCH_QUERY = "pic.twitter.com";
	 private static final int SEARCH_RADIUS = 5;
	 private static final double LATITUDE = 51.933056;
	 private static final double LONGITUDE = -8.568056;
	 private Map<String,String> mCoordinates;
	 private boolean mBottomTweetReachedFirstTime = false;
	 
	 
	  private long maxId;
	  private TweetViewAdapter mAdapter;
	    private boolean flagLoading;
	    private boolean endOfSearchResults;
	    
	    private static final String MOPUB_BANNER_AD_UNIT_ID = "b56d0ecea954471fa1e170832b91a6dd";
	    private static final String YOUR_INTERSTITIAL_AD_UNIT_ID_HERE = "f5ea98f5fc994564bdb82bb88f5cd82b";
		private MoPubView moPubView;
		private MoPubInterstitial mInterstitial;
		
		private boolean mUseMyLocation = true;
		private Switch mLocationSwitch;
		private ImageButton mCitySearchButton;
		private String mCitySearchString = "";

		private AutoCompleteTextView autoComplete;
		//private MultiAutoCompleteTextView multiAutoComplete;
		private ArrayAdapter<String> searchAdapter;


	


	  @Override
	  public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	  }
	  
	  @Override
	  public View onCreateView(LayoutInflater inflater, ViewGroup container,
	            Bundle savedInstanceState) {
	
	    mRootView = inflater.inflate(R.layout.twitter, container, false);

	    
	    moPubView = (MoPubView) mRootView.findViewById(R.id.twitter_banner);
		moPubView.setAdUnitId(MOPUB_BANNER_AD_UNIT_ID);
		moPubView.bringToFront();
		moPubView.loadAd();
		
	    
		mInterstitial = new MoPubInterstitial(getActivity(), YOUR_INTERSTITIAL_AD_UNIT_ID_HERE);
		mInterstitial.setInterstitialAdListener(this);
		mInterstitial.load();
			
	    
			return mRootView;
	  }
	  
	  
	  @Override
	    public void onAttach(Activity activity) {
	        super.onAttach(activity);
	        
	    	mContext = this.getActivity();
	    	//mAdapter =new TweetViewFetchAdapter<CompactTweetView>(mContext);
	    	
//	    	mAdapter = new TweetViewAdapter<BaseTweetView>(mContext);
	    	
	

	        
	    }
	  
	  private void loadTweets(Double latitude, Double longitude) {

		  
		  
		  getActivity().setProgressBarIndeterminateVisibility(true);
	      mCoordinates = getCityCoordinates();

	        
  	      Geocode geocode = null;
  	      
  	      //If either are null, then use current location
  	      if(latitude == null || longitude == null){   

    	        LocationManager lm = (LocationManager)getActivity().getSystemService(Context.LOCATION_SERVICE);
      	      	Location location = lm.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
      	      	
      	      	if(location != null){
      	      		latitude = location.getLatitude();
      	      		longitude = location.getLongitude();
      	      	}
  		      
  		      
  		    Log.d(getClass().getName(),"Latitude = " + latitude );
  		    Log.d(getClass().getName(),"Longitude = " + longitude );
  		      
  	      }
  	      else{
  	    	  
  	    	Log.d(getClass().getName(),"Latitude = " + latitude );
  		    Log.d(getClass().getName(),"Longitude = " + longitude );

  	      }
  	      
  	      int radius = SEARCH_RADIUS;
  	      if(mCitySearchString.compareTo("Blarney") == 0 ||
  	    		mCitySearchString.compareTo("Donoughmore") == 0 ||
  	    		mCitySearchString.compareTo("Tower") == 0 ||
  	    		mCitySearchString.compareTo("Enniskerry") == 0
  	    		  
  	    		  ){
  	    	  radius = 2;
  	      }
  	    	  
  	    geocode = new Geocode(latitude,longitude,radius,Distance.MILES); 
        
  	   // geocode = new Geocode(LATITUDE,LONGITUDE,SEARCH_RADIUS,Distance.MILES);
  	    
  	    	mAdapter = new TweetViewAdapter<BaseTweetView>(mContext);
  	    
	        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
	        final SearchService service = twitterApiClient.getSearchService();
	        
	        mBottomTweetReachedFirstTime = false;
	        
	        
	        service.tweets(SEARCH_QUERY,geocode, null, null, SEARCH_RESULT_TYPE, COUNT, null, null,
	                maxId, true, new Callback<Search>() {
	                    @Override
	                    public void success(Result<Search> searchResult) {
	                    	
	                    	Log.d(getClass().getName(),"Success getting Tweets. ");

	                    	
	                       getActivity().setProgressBarIndeterminateVisibility(false);
	                       final List<Tweet> tweets = searchResult.data.tweets;
	                       
	                       //Let's clean everything up first
	                       mAdapter.getTweets().removeAll(tweets);
                       
	                       mAdapter.getTweets().addAll(tweets);
	                       	                       
	                        mAdapter.notifyDataSetChanged();
	                        if (tweets.size() > 0) {
	                            maxId = tweets.get(tweets.size() - 1).id - 1;
	                        } else {
	                            endOfSearchResults = true;
	                        }
	                        flagLoading = false;
	                        
	                    	
	                        
	                    }

	                    @Override
	                    public void failure(TwitterException error) {

	                        getActivity().setProgressBarIndeterminateVisibility(false);
	                        Toast.makeText(getActivity(),
	                                "Error retrieving Tweets",
	                                Toast.LENGTH_SHORT).show();

	                        flagLoading = false;
	                    }
	                }
	        );
	        
	        
	        Object[] objectArray = mCoordinates.keySet().toArray();
	        
	        String[] coordinatesArray = convertToString(objectArray);
	        
	       // String[] cities = getResources().getStringArray(R.array.cityList);
		    searchAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,coordinatesArray);		    
		    
    
		    ListView listView = getListView();
            //Let's clean everything up first
		    
		    if(mHeaderView != null)
		    	listView.removeHeaderView(mHeaderView);
            
		    
		    listView.setOnScrollListener(this);
		    
            mHeaderView = LayoutInflater.from(getActivity()).inflate(R.layout.twitter_search, null);
            autoComplete = (AutoCompleteTextView)mHeaderView.findViewById(R.id.autoComplete);
            autoComplete.setText(mCitySearchString);
            autoComplete.setAdapter(searchAdapter);
            autoComplete.setThreshold(1);
            

                
            
			//Working with the switch button
			
			mLocationSwitch = (Switch)mHeaderView.findViewById(R.id.local_search_switch);
		
			if(mUseMyLocation){
				mLocationSwitch.setChecked(true);
				TextView text = (TextView)mHeaderView.findViewById(R.id.switch_text);
				text.setTextColor(Color.DKGRAY);
				
				autoComplete.setTextColor(R.drawable.abc_ic_search_api_holo_light);
			}
			
			else{
				mLocationSwitch.setChecked(false);
				TextView text = (TextView)mHeaderView.findViewById(R.id.switch_text);
				text.setTextColor(Color.LTGRAY);
				autoComplete.setTextColor(Color.DKGRAY);
			}
			
			
			mCitySearchButton = (ImageButton)mHeaderView.findViewById(R.id.city_search_button);
			
			
			mCitySearchButton.setOnClickListener(new Button.OnClickListener() {
	            public void onClick(View v) {
	            	
	            	mBottomTweetReachedFirstTime = false;
	            	
 	
	            	mCitySearchString = autoComplete.getText().toString();
	            	String latitudeLongitude = (String)mCoordinates.get(mCitySearchString);

	            	Double doubleLatitude = null;
	            	Double doubleLongitude = null;
	            	
	            	try{
	            	StringTokenizer tokenizer = new StringTokenizer(latitudeLongitude,",");
	            	while(tokenizer.hasMoreTokens()){
	            		doubleLatitude = Double.valueOf(tokenizer.nextToken());
	            		doubleLongitude = Double.valueOf(tokenizer.nextToken());
	            		break;
	            	}
	            	
	            	mUseMyLocation = false;
	            	loadTweets(doubleLatitude, doubleLongitude);
	            	
	            	
	            	}catch (Exception e){
	            		//do nothing
	            		
	            		Toast.makeText(getActivity(),
                                "Cannot show tweets from city : " + mCitySearchString,
                                Toast.LENGTH_SHORT).show();
	            	}

	            	
	            }
	        }); 
			
			mLocationSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

				@Override
				public void onCheckedChanged(CompoundButton buttonView,
						boolean ischecked) {
					if (ischecked) {
						mUseMyLocation = true;
						mBottomTweetReachedFirstTime = false;
						loadTweets(null,null);
						
					} else {
						mUseMyLocation = false;
					}
				}
			});

				


            
            listView.addHeaderView(mHeaderView);
	    	listView.setAdapter(mAdapter);
	    	
	        
	        
	        /*
	        
	        TwitterApiClient twitterApiClient = TwitterCore.getInstance().getApiClient();
	        final FavoriteService favoriteService = twitterApiClient.getFavoriteService();
	        
	        favoriteService.list(null, null, COUNT, null, null, true, new Callback<List<Tweet>>() {
	                    @Override
	                    public void success(Result<List<Tweet>> searchResult) {

	                        getActivity().setProgressBarIndeterminateVisibility(false);
	                        
	                        
	                        Iterator<Tweet> iter = searchResult.data.iterator();

	                        while(iter.hasNext()){
	                        	Tweet tweet = (Tweet)iter.next();
	                        	mAdapter.getTweets().add(tweet);
	                        }
	                        
	                        mAdapter.notifyDataSetChanged();
	                      
	                    	
	                    }

	                    @Override
	                    public void failure(TwitterException error) {

	                        getActivity().setProgressBarIndeterminateVisibility(false);
	                        Toast.makeText(getActivity(),
	                                "Error retrieving Tweets",
	                                Toast.LENGTH_SHORT).show();

	                        flagLoading = false;
	                    }
	                }
	        );
	        
	        */
	        
	        
	  }
	  
	  @Override
	  public void setUserVisibleHint(boolean isVisibleToUser) {
	      super.setUserVisibleHint(isVisibleToUser);
	      if (isVisibleToUser) {
	    	  
	    	  /*
	    	  if(mInterstitial != null)
	    		  mInterstitial.show();
	    	  */
	    	  
	      }
	      else {  }
	  }
	  	  

	    @Override
	    public void onActivityCreated(Bundle savedInstanceState)
	    {
	        // Tell the main activity that this fragment has menu elements to declare
	        super.onActivityCreated(savedInstanceState);
	        
		    
			loginButton = (TwitterLoginButton) mRootView.findViewById(R.id.twitter_login_button);
	 	
		    TwitterSession session =
		            Twitter.getSessionManager().getActiveSession();
		    
		    
		    if(session != null){	  
		    	loginButton.setVisibility(View.GONE);
		    	loadTweets(null,null);
		    	
		    	
		    }
		    else{
		    	Log.d(getClass().getName(),"TWITTER SESSION NOT CREATED YET");
		    	
		    	loginButton.setVisibility(View.VISIBLE);

		    }
		    
			loginButton.setCallback(new Callback<TwitterSession>() {
				@Override
				public void success(Result<TwitterSession> result) {					
					loginButton.setVisibility(View.GONE);
					
					 loadTweets(null,null);					
				}

				@Override
				public void failure(TwitterException exception) {
					// Do something on failure
					Log.e(getClass().getSimpleName(),"Failure in TwitterFragment Login");
				}
			});
				

	     
	    }

	    
		private Map<String,String> getCityCoordinates()
		{
		    final Map<String, String> myMap;
	        myMap = new HashMap<String, String>();
	        myMap.put("Shanghai","31.23,121.47");
	        myMap.put("Bombay","18.96,72.82");
	        myMap.put("Karachi","24.86,67.01");
	        myMap.put("Buenos Aires","-34.61,-58.37");
	        myMap.put("Delhi","28.67,77.21");
	        myMap.put("Istanbul","41.1,29");
	        myMap.put("Manila","14.62,120.97");
	        myMap.put("Sao Paulo","-23.53,-46.63");
	        myMap.put("Moscow","55.75,37.62");
	        myMap.put("Dhaka","23.7,90.39");
	        myMap.put("Soul","37.56,126.99");
	        myMap.put("Lagos","6.5,3.35");
	        myMap.put("Kinshasa","-4.31,15.32");
	        myMap.put("Tokyo","35.67,139.77");
	        myMap.put("Mexico City","19.43,-99.14");
	        myMap.put("Jakarta","-6.18,106.83");
	        myMap.put("New York","40.67,-73.94");
	        myMap.put("Tehran","35.67,51.43");
	        myMap.put("Cairo","30.06,31.25");
	        myMap.put("Lima","-12.07,-77.05");
	        myMap.put("Peking","39.93,116.4");
	        myMap.put("London","51.52,-0.1");
	        myMap.put("Bogota","4.63,-74.09");
	        myMap.put("Lahore","31.56,74.35");
	        myMap.put("Rio de Janeiro","-22.91,-43.2");
	        myMap.put("Bangkok","13.73,100.5");
	        myMap.put("Bagdad","33.33,44.44");
	        myMap.put("Bangalore","12.97,77.56");
	        myMap.put("Santiago","-33.46,-70.64");
	        myMap.put("Calcutta","22.57,88.36");
	        myMap.put("Singapore","1.3,103.85");
	        myMap.put("Toronto","43.65,-79.38");
	        myMap.put("Rangoon","16.79,96.15");
	        myMap.put("Ibadan","7.38,3.93");
	        myMap.put("Riyadh","24.65,46.77");
	        myMap.put("Madras","13.09,80.27");
	        myMap.put("Chongqing","29.57,106.58");
	        myMap.put("Ho Chi Minh City","10.78,106.69");
	        myMap.put("Xian","34.27,108.9");
	        myMap.put("Wuhan","30.58,114.27");
	        myMap.put("Alexandria","31.22,29.95");
	        myMap.put("Saint Petersburg","59.93,30.32");
	        myMap.put("Hyderabad","17.4,78.48");
	        myMap.put("Chengdu","30.67,104.07");
	        myMap.put("Abidjan","5.33,-4.03");
	        myMap.put("Ankara","39.93,32.85");
	        myMap.put("Ahmadabad","23.03,72.58");
	        myMap.put("Los Angeles","34.11,-118.41");
	        myMap.put("Tianjin","39.13,117.2");
	        myMap.put("Chattagam","22.33,91.81");
	        myMap.put("Sydney","-33.87,151.21");
	        myMap.put("Yokohama","35.47,139.62");
	        myMap.put("Melbourne","-37.81,144.96");
	        myMap.put("Shenyang","41.8,123.45");
	        myMap.put("Cape Town","-33.93,18.46");
	        myMap.put("Berlin","52.52,13.38");
	        myMap.put("Pusan","35.11,129.03");
	        myMap.put("Montreal","45.52,-73.57");
	        myMap.put("Harbin","45.75,126.65");
	        myMap.put("Durban","-29.87,30.99");
	        myMap.put("Gizeh","30.01,31.21");
	        myMap.put("Nanjing","32.05,118.78");
	        myMap.put("Casablanca","33.6,-7.62");
	        myMap.put("Pune","18.53,73.84");
	        myMap.put("Addis Abeba","9.03,38.74");
	        myMap.put("Pyongyang","39.02,125.75");
	        myMap.put("Surat","21.2,72.82");
	        myMap.put("Madrid","40.42,-3.71");
	        myMap.put("Guangzhou","23.12,113.25");
	        myMap.put("Jiddah","21.5,39.17");
	        myMap.put("Kanpur","26.47,80.33");
	        myMap.put("Nairobi","-1.29,36.82");
	        myMap.put("Jaipur","26.92,75.8");
	        myMap.put("Dar es Salaam","-6.82,39.28");
	        myMap.put("Salvador","-12.97,-38.5");
	        myMap.put("Chicago","41.84,-87.68");
	        myMap.put("Taiyuan","37.87,112.55");
	        myMap.put("al-Mawsil","36.34,43.14");
	        myMap.put("Faisalabad","31.41,73.11");
	        myMap.put("Changchun","43.87,125.35");
	        myMap.put("Izmir","38.43,27.15");
	        myMap.put("Taibei","25.02,121.45");
	        myMap.put("Osaka","34.68,135.5");
	        myMap.put("Lakhnau","26.85,80.92");
	        myMap.put("Kiev","50.43,30.52");
	        myMap.put("Luanda","-8.82,13.24");
	        myMap.put("Inchon","37.48,126.64");
	        myMap.put("Rome","41.89,12.5");
	        myMap.put("Dakar","14.72,-17.48");
	        myMap.put("Belo Horizonte","-19.92,-43.94");
	        myMap.put("Fortaleza","-3.78,-38.59");
	        myMap.put("Mashhad","36.27,59.57");
	        myMap.put("Maracaibo","10.73,-71.66");
	        myMap.put("Kabul","34.53,69.17");
	        myMap.put("Santo Domingo","18.48,-69.91");
	        myMap.put("Taegu","35.87,128.6");
	        myMap.put("Brasilia","-15.78,-47.91");
	        myMap.put("Umm Durman","15.65,32.48");
	        myMap.put("Nagpur","21.16,79.08");
	        myMap.put("Surabaya","-7.24,112.74");
	        myMap.put("Kano","12,8.52");
	        myMap.put("Medellin","6.29,-75.54");
	        myMap.put("Accra","5.56,-0.2");
	        myMap.put("Nagoya","35.15,136.91");
	        myMap.put("Benin","6.34,5.62");
	        myMap.put("Shijiazhuang","38.05,114.48");
	        myMap.put("Guayaquil","-2.21,-79.9");
	        myMap.put("Changsha","28.2,112.97");
	        myMap.put("Houston","29.77,-95.39");
	        myMap.put("Khartoum","15.58,32.52");
	        myMap.put("Paris","48.86,2.34");
	        myMap.put("Cali","3.44,-76.52");
	        myMap.put("Algiers","36.77,3.04");
	        myMap.put("Jinan","36.67,117");
	        myMap.put("Havanna","23.13,-82.39");
	        myMap.put("Tashkent","41.31,69.3");
	        myMap.put("Dalian","38.92,121.65");
	        myMap.put("Jilin","43.85,126.55");
	        myMap.put("Nanchang","28.68,115.88");
	        myMap.put("Zhengzhou","34.75,113.67");
	        myMap.put("Vancouver","49.28,-123.13");
	        myMap.put("Johannesburg","-26.19,28.04");
	        myMap.put("Bayrut","33.89,35.5");
	        myMap.put("Douala","4.06,9.71");
	        myMap.put("Jiulong","22.32,114.17");
	        myMap.put("Caracas","10.54,-66.93");
	        myMap.put("Kaduna","10.52,7.44");
	        myMap.put("Bucharest","44.44,26.1");
	        myMap.put("Ecatepec","19.6,-99.05");
	        myMap.put("Sapporo","43.06,141.34");
	        myMap.put("Port Harcourt","4.81,7.01");
	        myMap.put("Hangzhou","30.25,120.17");
	        myMap.put("Rawalpindi","33.6,73.04");
	        myMap.put("San'a","15.38,44.21");
	        myMap.put("Conakry","9.55,-13.67");
	        myMap.put("Curitiba","-25.42,-49.29");
	        myMap.put("al-Basrah","30.53,47.82");
	        myMap.put("Brisbane","-27.46,153.02");
	        myMap.put("Xinyang","32.13,114.07");
	        myMap.put("Medan","3.59,98.67");
	        myMap.put("Indore","22.72,75.86");
	        myMap.put("Manaus","-3.12,-60.02");
	        myMap.put("Kumasi","6.69,-1.63");
	        myMap.put("Hamburg","53.55,10");
	        myMap.put("Rabat","34.02,-6.84");
	        myMap.put("Minsk","53.91,27.55");
	        myMap.put("Patna","25.62,85.13");
	        myMap.put("Valencia","10.23,-67.98");
	        myMap.put("Bhopal","23.24,77.4");
	        myMap.put("Soweto","-26.28,27.84");
	        myMap.put("Warsaw","52.26,21.02");
	        myMap.put("Qingdao","36.07,120.32");
	        myMap.put("Vienna","48.22,16.37");
	        myMap.put("Yaounde","3.87,11.52");
	        myMap.put("Dubai","25.27,55.33");
	        myMap.put("Thana","19.2,72.97");
	        myMap.put("Aleppo","36.23,37.17");
	        myMap.put("Bekasi","-6.22,106.97");
	        myMap.put("Budapest","47.51,19.08");
	        myMap.put("Bamako","12.65,-7.99");
	        myMap.put("Ludhiana","30.91,75.84");
	        myMap.put("Harare","-17.82,31.05");
	        myMap.put("Esfahan","32.68,51.68");
	        myMap.put("Pretoria","-25.73,28.22");
	        myMap.put("Barcelona","41.4,2.17");
	        myMap.put("Lubumbashi","-11.66,27.48");
	        myMap.put("Bandung","-6.91,107.6");
	        myMap.put("Guadalajara","20.67,-103.35");
	        myMap.put("Tangshan","39.62,118.19");
	        myMap.put("Muqdisho","2.05,45.33");
	        myMap.put("Phoenix","33.54,-112.07");
	        myMap.put("Damascus","33.5,36.32");
	        myMap.put("Quito","-0.19,-78.5");
	        myMap.put("Agra","27.19,78.01");
	        myMap.put("Urumqi","43.8,87.58");
	        myMap.put("Davao","7.11,125.63");
	        myMap.put("Santa Cruz","-17.77,-63.21");
	        myMap.put("Antananarivo","-18.89,47.51");
	        myMap.put("Kobe","34.68,135.17");
	        myMap.put("Juarez","31.74,-106.49");
	        myMap.put("Tijuana","32.53,-117.02");
	        myMap.put("Recife","-8.08,-34.92");
	        myMap.put("Multan","30.2,71.45");
	        myMap.put("Ha Noi","21.03,105.84");
	        myMap.put("Gaoxiong","22.63,120.27");
	        myMap.put("Belem","-1.44,-48.5");
	        myMap.put("Cordoba","-31.4,-64.19");
	        myMap.put("Kampala","0.32,32.58");
	        myMap.put("Lome","6.17,1.35");
	        myMap.put("Hyderabad","25.38,68.37");
	        myMap.put("Suzhou","31.3,120.62");
	        myMap.put("Vadodara","22.31,73.18");
	        myMap.put("Gujranwala","32.16,74.18");
	        myMap.put("Bursa","40.2,29.08");
	        myMap.put("Mbuji-Mayi","-6.13,23.59");
	        myMap.put("Pimpri","18.62,73.8");
	        myMap.put("Karaj","35.8,50.97");
	        myMap.put("Kyoto","35.01,135.75");
	        myMap.put("Tangerang","-6.18,106.63");
	        myMap.put("Aba","5.1,7.35");
	        myMap.put("Kharkiv","49.98,36.22");
	        myMap.put("Puebla","19.05,-98.22");
	        myMap.put("Nashik","20.01,73.78");
	        myMap.put("Kuala Lumpur","3.16,101.71");
	        myMap.put("Philadelphia","40.01,-75.13");
	        myMap.put("Fukuoka","33.59,130.41");
	        myMap.put("Taejon","36.33,127.43");
	        myMap.put("Lanzhou","36.05,103.68");
	        myMap.put("Mecca","21.43,39.82");
	        myMap.put("Shantou","23.37,116.67");
	        myMap.put("Koyang","37.7,126.93");
	        myMap.put("Hefei","31.85,117.28");
	        myMap.put("Novosibirsk","55.04,82.93");
	        myMap.put("Porto Alegre","-30.04,-51.22");
	        myMap.put("Adana","37,35.32");
	        myMap.put("Makasar","-5.14,119.41");
	        myMap.put("Tabriz","38.08,46.3");
	        myMap.put("Narayanganj","23.62,90.5");
	        myMap.put("Faridabad","28.38,77.3");
	        myMap.put("Fushun","41.87,123.88");
	        myMap.put("Phnum Penh","11.57,104.92");
	        myMap.put("Luoyang","34.68,112.47");
	        myMap.put("Khulna","22.84,89.56");
	        myMap.put("Depok","-6.39,106.83");
	        myMap.put("Lusaka","-15.42,28.29");
	        myMap.put("Ghaziabad","28.66,77.41");
	        myMap.put("Handan","36.58,114.48");
	        myMap.put("San Antonio","29.46,-98.51");
	        myMap.put("Kawasaki","35.53,139.7");
	        myMap.put("Kwangju","35.16,126.91");
	        myMap.put("Peshawar","34.01,71.54");
	        myMap.put("Rajkot","22.31,70.79");
	        myMap.put("Suwon","37.26,127.01");
	        myMap.put("Mandalay","21.98,96.09");
	        myMap.put("Almaty","43.32,76.92");
	        myMap.put("Munich","48.14,11.58");
	        myMap.put("Mirat","28.99,77.7");
	        myMap.put("Baotou","40.6,110.05");
	        myMap.put("Milan","45.48,9.19");
	        myMap.put("Rongcheng","23.54,116.34");
	        myMap.put("Kalyan","19.25,73.16");
	        myMap.put("Montevideo","-34.87,-56.17");
	        myMap.put("Xianggangdao","22.27,114.14");
	        myMap.put("Yekaterinburg","56.85,60.6");
	        myMap.put("Ouagadougou","12.37,-1.53");
	        myMap.put("Guarulhos","-23.46,-46.49");
	        myMap.put("Semarang","-6.97,110.42");
	        myMap.put("Xuzhou","34.27,117.18");
	        myMap.put("Perth","-31.96,115.84");
	        myMap.put("Dallas","32.79,-96.77");
	        myMap.put("Stockholm","59.33,18.07");
	        myMap.put("Palembang","-2.99,104.75");
	        myMap.put("San Diego","32.81,-117.14");
	        myMap.put("Goiania","-16.72,-49.26");
	        myMap.put("Gaziantep","37.07,37.39");
	        myMap.put("Nizhniy Novgorod","56.33,44");
	        myMap.put("Shiraz","29.63,52.57");
	        myMap.put("Rosario","-32.94,-60.67");
	        myMap.put("Fuzhou","26.08,119.3");
	        myMap.put("Nezahualcoyotl","19.41,-99.03");
	        myMap.put("Saitama","35.87,139.64");
	        myMap.put("Shenzhen","22.53,114.13");
	        myMap.put("Yerevan","40.17,44.52");
	        myMap.put("Tripoli","32.87,13.18");
	        myMap.put("Anshan","41.12,122.95");
	        myMap.put("Varanasi","25.32,83.01");
	        myMap.put("Guiyang","26.58,106.72");
	        myMap.put("Baku","40.39,49.86");
	        myMap.put("Wuxi","31.58,120.3");
	        myMap.put("Prague","50.08,14.43");
	        myMap.put("Brazzaville","-4.25,15.26");
	        myMap.put("Subang Jaya","3.15,101.53");
	        myMap.put("Leon","21.12,-101.69");
	        myMap.put("Hiroshima","34.39,132.44");
	        myMap.put("Amritsar","31.64,74.87");
	        myMap.put("Huainan","32.63,116.98");
	        myMap.put("Barranquilla","10.96,-74.8");
	        myMap.put("Monrovia","6.31,-10.8");
	        myMap.put("'Amman","31.95,35.93");
	        myMap.put("Tbilisi","41.72,44.79");
	        myMap.put("Abuja","9.06,7.49");
	        myMap.put("Aurangabad","19.89,75.32");
	        myMap.put("Sofia","42.69,23.31");
	        myMap.put("Omsk","55,73.4");
	        myMap.put("Monterrey","25.67,-100.32");
	        myMap.put("Port Elizabeth","-33.96,25.59");
	        myMap.put("Navi Mumbai","19.11,73.06");
	        myMap.put("Maputo","-25.95,32.57");
	        myMap.put("Allahabad","25.45,81.84");
	        myMap.put("Samara","53.2,50.15");
	        myMap.put("Belgrade","44.83,20.5");
	        myMap.put("Campinas","-22.91,-47.08");
	        myMap.put("Sholapur","17.67,75.89");
	        myMap.put("Kazan","55.75,49.13");
	        myMap.put("Irbil","36.18,44.01");
	        myMap.put("Barquisimeto","10.05,-69.3");
	        myMap.put("K?benhavn","55.67,12.58");
	        myMap.put("Xianyang","34.37,108.7");
	        myMap.put("Baoding","38.87,115.48");
	        myMap.put("Guatemala","14.63,-90.55");
	        myMap.put("Maceio","-9.65,-35.75");
	        myMap.put("Nova Iguacu","-22.74,-43.47");
	        myMap.put("Kunming","25.05,102.7");
	        myMap.put("Taizhong","24.15,120.68");
	        myMap.put("Maiduguri","11.85,13.16");
	        myMap.put("Datong","40.08,113.3");
	        myMap.put("Dublin","53.33,-6.25");
	        myMap.put("Jabalpur","23.17,79.94");
	        myMap.put("Visakhapatnam","17.73,83.3");
	        myMap.put("Rostov-na-Donu","47.24,39.71");
	        myMap.put("Dnipropetrovs'k","48.45,34.98");
	        myMap.put("Shubra-El-Khema","30.11,31.25");
	        myMap.put("Srinagar","34.09,74.79");
	        myMap.put("Benxi","41.33,123.75");
	        myMap.put("Brussels","50.83,4.33");
	        myMap.put("al-Madinah","24.48,39.59");
	        myMap.put("Adelaide","-34.93,138.6");
	        myMap.put("Zapopan","20.72,-103.4");
	        myMap.put("Chelyabinsk","55.15,61.43");
	        myMap.put("Haora","22.58,88.33");
	        myMap.put("Calgary","51.05,-114.06");
	        myMap.put("Sendai","38.26,140.89");
	        myMap.put("Tegucigalpa","14.09,-87.22");
	        myMap.put("Ranchi","23.36,85.33");
	        myMap.put("Songnam","37.44,127.15");
	        myMap.put("Ilorin","8.49,4.55");
	        myMap.put("Fez","34.05,-5");
	        myMap.put("Ufa","54.78,56.04");
	        myMap.put("Klang","3.04,101.45");
	        myMap.put("Chandigarh","30.75,76.78");
	        myMap.put("Ahvaz","31.28,48.72");
	        myMap.put("Koyampattur","11.01,76.96");
	        myMap.put("Cologne","50.95,6.97");
	        myMap.put("Qom","34.65,50.95");
	        myMap.put("Odesa","46.47,30.73");
	        myMap.put("Donetsk","48,37.82");
	        myMap.put("Jodhpur","26.29,73.02");
	        myMap.put("Sao Luis","-2.5,-44.3");
	        myMap.put("Sao Goncalo","-22.84,-43.07");
	        myMap.put("Kitakyushu","33.88,130.86");
	        myMap.put("Huaibei","33.95,116.75");
	        myMap.put("Perm","58,56.25");
	        myMap.put("Changzhou","31.78,119.97");
	        myMap.put("Maisuru","12.31,76.65");
	        myMap.put("Guwahati","26.19,91.75");
	        myMap.put("Volgograd","48.71,44.48");
	        myMap.put("Konya","37.88,32.48");
	        myMap.put("Naples","40.85,14.27");
	        myMap.put("Vijayawada","16.52,80.63");
	        myMap.put("Ulsan","35.55,129.31");
	        myMap.put("San Jose","37.3,-121.85");
	        myMap.put("Birmingham","52.48,-1.91");
	        myMap.put("Chiba","35.61,140.11");
	        myMap.put("Ciudad Guayana","8.37,-62.62");
	        myMap.put("Kolwezi","-10.7,25.66");
	        myMap.put("Padang","-0.95,100.35");
	        myMap.put("Managua","12.15,-86.27");
	        myMap.put("Mendoza","-32.89,-68.83");
	        myMap.put("Gwalior","26.23,78.17");
	        myMap.put("Biskek","42.87,74.57");
	        myMap.put("Kathmandu","27.71,85.31");
	        myMap.put("El Alto","-16.5,-68.17");
	        myMap.put("Niamey","13.52,2.12");
	        myMap.put("Kigali","-1.94,30.06");
	        myMap.put("Qiqihar","47.35,124");
	        myMap.put("Ulaanbaatar","47.93,106.91");
	        myMap.put("Krasnoyarsk","56.02,93.06");
	        myMap.put("Madurai","9.92,78.12");
	        myMap.put("Edmonton","53.57,-113.54");
	        myMap.put("Asgabat","37.95,58.38");
	        myMap.put("al-H?artum Bah?ri","15.64,32.52");
	        myMap.put("Arequipa","-16.39,-71.53");
	        myMap.put("Marrakesh","31.63,-8");
	        myMap.put("Bandar Lampung","-5.44,105.27");
	        myMap.put("Pingdingshan","33.73,113.3");
	        myMap.put("Cartagena","10.4,-75.5");
	        myMap.put("Hubli","15.36,75.13");
	        myMap.put("La Paz","-16.5,-68.15");
	        myMap.put("Wenzhou","28.02,120.65");
	        myMap.put("Ottawa","45.42,-75.71");
	        myMap.put("Johor Bahru","1.48,103.75");
	        myMap.put("Mombasa","-4.04,39.66");
	        myMap.put("Lilongwe","-13.97,33.8");
	        myMap.put("Turin","45.08,7.68");
	        myMap.put("Duque de Caxias","-22.77,-43.31");
	        myMap.put("Abu Dhabi","24.48,54.37");
	        myMap.put("Jalandhar","31.33,75.57");
	        myMap.put("Warri","5.52,5.76");
	        myMap.put("Valencia","39.48,-0.39");
	        myMap.put("Oslo","59.91,10.75");
	        myMap.put("Taian","36.2,117.12");
	        myMap.put("ad-Dammam","26.43,50.1");
	        myMap.put("Mira Bhayandar","19.29,72.85");
	        myMap.put("Salem","11.67,78.16");
	        myMap.put("Pietermaritzburg","-29.61,30.39");
	        myMap.put("Naucalpan","19.48,-99.23");
	        myMap.put("H?ims","34.73,36.72");
	        myMap.put("Bhubaneswar","20.27,85.84");
	        myMap.put("Hamamatsu","34.72,137.73");
	        myMap.put("Saratov","51.57,46.03");
	        myMap.put("Detroit","42.38,-83.1");
	        myMap.put("Kirkuk","35.47,44.39");
	        myMap.put("Sakai","34.57,135.48");
	        myMap.put("Onitsha","6.14,6.78");
	        myMap.put("Quetta","30.21,67.02");
	        myMap.put("Aligarh","27.89,78.06");
	        myMap.put("Voronezh","51.72,39.26");
	        myMap.put("Freetown","8.49,-13.24");
	        myMap.put("Tucuman","-26.83,-65.22");
	        myMap.put("Bogor","-6.58,106.79");
	        myMap.put("Niigata","37.92,139.04");
	        myMap.put("Thiruvananthapuram","8.51,76.95");
	        myMap.put("Jacksonville","30.33,-81.66");
	        myMap.put("Bareli","28.36,79.41");
	        myMap.put("Cebu","10.32,123.9");
	        myMap.put("Kota","25.18,75.83");
	        myMap.put("Natal","-5.8,-35.22");
	        myMap.put("Shihung","37.46,126.89");
	        myMap.put("Puchon","37.48,126.77");
	        myMap.put("Tiruchchirappalli","10.81,78.69");
	        myMap.put("Trujillo","-8.11,-79.03");
	        myMap.put("Sharjah","25.37,55.41");
	        myMap.put("Kermanshah","34.38,47.06");
	        myMap.put("Qinhuangdao","39.93,119.62");
	        myMap.put("Anyang","36.08,114.35");
	        myMap.put("Bhiwandi","19.3,73.05");
	        myMap.put("an-Najaf","32,44.34");
	        myMap.put("Sao Bernardo do Campo","-23.71,-46.54");
	        myMap.put("Teresina","-5.1,-42.8");
	        myMap.put("Nanning","22.82,108.32");
	        myMap.put("Antalya","36.89,30.71");
	        myMap.put("Campo Grande","-20.45,-54.63");
	        myMap.put("Indianapolis","39.78,-86.15");
	        myMap.put("Jaboatao","-8.11,-35.02");
	        myMap.put("Zaporizhzhya","47.85,35.17");
	        myMap.put("Hohhot","40.82,111.64");
	        myMap.put("Marseille","43.31,5.37");
	        myMap.put("Moradabad","28.84,78.76");
	        myMap.put("Zhangjiakou","40.83,114.93");
	        myMap.put("Liuzhou","24.28,109.25");
	        myMap.put("Nouakchott","18.09,-15.98");
	        myMap.put("Rajshahi","24.37,88.59");
	        myMap.put("Yantai","37.53,121.4");
	        myMap.put("Tainan","23,120.19");
	        myMap.put("Xining","36.62,101.77");
	        myMap.put("Port-au-Prince","18.54,-72.34");
	        myMap.put("Hegang","47.4,130.37");
	        myMap.put("Akure","7.25,5.19");
	        myMap.put("N'Djamena","12.11,15.05");
	        myMap.put("Guadalupe","25.68,-100.26");
	        myMap.put("Cracow","50.06,19.96");
	        myMap.put("Malang","-7.98,112.62");
	        myMap.put("Hengyang","26.89,112.62");
	        myMap.put("Athens","37.98,23.73");
	        myMap.put("Puyang","35.7,114.98");
	        myMap.put("San Francisco","37.77,-122.45");
	        myMap.put("Jerusalem","31.78,35.22");
	        myMap.put("Amsterdam","52.37,4.89");
	        myMap.put("?odz","51.77,19.46");
	        myMap.put("Merida","20.97,-89.62");
	        myMap.put("Austin","30.31,-97.75");
	        myMap.put("Abeokuta","7.16,3.35");
	        myMap.put("Xinxiang","35.32,113.87");
	        myMap.put("Raipur","21.24,81.63");
	        myMap.put("Tunis","36.84,10.22");
	        myMap.put("Columbus","39.99,-82.99");
	        myMap.put("Chihuahua","28.63,-106.08");
	        myMap.put("L'viv","49.83,24");
	        myMap.put("Cotonou","6.36,2.44");
	        myMap.put("Pekan Baru","0.56,101.43");
	        myMap.put("Blantyre","-15.79,34.99");
	        myMap.put("La Plata","-34.92,-57.96");
	        myMap.put("Bulawayo","-20.17,28.58");
	        myMap.put("Tangier","35.79,-5.81");
	        myMap.put("Kayseri","38.74,35.48");
	        myMap.put("Tolyatti","53.48,49.51");
	        myMap.put("Foshan","23.03,113.12");
	        myMap.put("Ningbo","29.88,121.55");
	        myMap.put("Langfang","39.52,116.68");
	        myMap.put("Ampang Jaya","3.15,101.77");
	        myMap.put("Liaoyang","41.28,123.18");
	        myMap.put("Riga","56.97,24.13");
	        myMap.put("Changzhi","35.22,111.75");
	        myMap.put("Kryvyy Rih","47.92,33.35");
	        myMap.put("Libreville","0.39,9.45");
	        myMap.put("Chonju","35.83,127.14");
	        myMap.put("Fort Worth","32.75,-97.34");
	        myMap.put("as-Sulaymaniyah","35.56,45.43");
	        myMap.put("Osasco","-23.53,-46.78");
	        myMap.put("Zamboanga","6.92,122.08");
	        myMap.put("Tlalnepantla","19.54,-99.19");
	        myMap.put("Gorakhpur","26.76,83.36");
	        myMap.put("San Luis Potosi","22.16,-100.98");
	        myMap.put("Sevilla","37.4,-5.98");
	        myMap.put("Zhuzhou","27.83,113.15");
	        myMap.put("Zagreb","45.8,15.97");
	        myMap.put("Huangshi","30.22,115.1");
	        myMap.put("Puente Alto","-33.61,-70.57");
	        myMap.put("Shaoguan","24.8,113.58");
	        myMap.put("Matola","-25.97,32.46");
	        myMap.put("Guilin","25.28,110.28");
	        myMap.put("Aguascalientes","21.88,-102.3");
	        myMap.put("Shizuoka","34.98,138.39");
	        myMap.put("Benghazi","32.12,20.07");
	        myMap.put("Fuxin","42.01,121.65");
	        myMap.put("Joao Pessoa","-7.12,-34.86");
	        myMap.put("Ipoh","4.6,101.07");
	        myMap.put("Contagem","-19.91,-44.1");
	        myMap.put("Dushanbe","38.57,68.78");
	        myMap.put("Zhanjiang","21.2,110.38");
	        myMap.put("Xingtai","37.07,114.49");
	        myMap.put("Okayama","34.67,133.92");
	        myMap.put("Yogyakarta","-7.78,110.37");
	        myMap.put("Bhilai","21.21,81.38");
	        myMap.put("Zigong","29.4,104.78");
	        myMap.put("Mudanjiang","44.58,129.6");
	        myMap.put("Wahran","35.7,-0.62");
	        myMap.put("Enugu","6.44,7.51");
	        myMap.put("Santo Andre","-23.65,-46.53");
	        myMap.put("Colombo","6.93,79.85");
	        myMap.put("Chimalhuacan","19.44,-98.96");
	        myMap.put("Shatian","22.38,114.19");
	        myMap.put("Memphis","35.11,-90.01");
	        myMap.put("Kumamoto","32.8,130.71");
	        myMap.put("Sao Jose dos Campos","-23.2,-45.88");
	        myMap.put("Zhangdian","36.8,118.06");
	        myMap.put("Acapulco","16.85,-99.92");
	        myMap.put("Xiangtan","27.85,112.9");
	        myMap.put("Quebec","46.82,-71.23");
	        myMap.put("Dasmarinas","14.33,120.93");
	        myMap.put("Zaria","11.08,7.71");
	        myMap.put("Nantong","32.02,120.82");
	        myMap.put("Charlotte","35.2,-80.83");
	        myMap.put("Pointe Noire","-4.77,11.87");
	        myMap.put("Shaoyang","27,111.2");
	        myMap.put("Queretaro","20.59,-100.4");
	        myMap.put("Hamilton","43.26,-79.85");
	        myMap.put("Islamabad","33.72,73.06");
	        myMap.put("Panjin","41.18,122.05");
	        myMap.put("Saltillo","25.42,-101");
	        myMap.put("Ansan","37.35,126.86");
	        myMap.put("Jamshedpur","22.79,86.2");
	        myMap.put("Zaragoza","41.65,-0.89");
	        myMap.put("Cancun","21.17,-86.83");
	        myMap.put("Dandong","40.13,124.4");
	        myMap.put("Frankfurt","50.12,8.68");
	        myMap.put("Palermo","38.12,13.36");
	        myMap.put("Haikou","20.05,110.32");
	        myMap.put("'Adan","12.79,45.03");
	        myMap.put("Amravati","20.95,77.76");
	        myMap.put("Winnipeg","49.88,-97.17");
	        myMap.put("Sagamihara","35.58,139.38");
	        myMap.put("Zhangzhou","24.52,117.67");
	        myMap.put("Gazzah","31.53,34.44");
	        myMap.put("Kataka","20.47,85.88");
	        myMap.put("El Paso","31.85,-106.44");
	        myMap.put("Krasnodar","45.03,38.98");
	        myMap.put("Kuching","1.55,110.34");
	        myMap.put("Wroc?aw","51.11,17.03");
	        myMap.put("Asmara","15.33,38.94");
	        myMap.put("Zhenjiang","32.22,119.43");
	        myMap.put("Baltimore","39.3,-76.61");
	        myMap.put("Benoni","-26.15,28.33");
	        myMap.put("Mersin","36.81,34.63");
	        myMap.put("Izhevsk","56.85,53.23");
	        myMap.put("Yancheng","33.39,120.12");
	        myMap.put("Hermosillo","29.07,-110.97");
	        myMap.put("Yuanlong","22.44,114.02");
	        myMap.put("Uberlandia","-18.9,-48.28");
	        myMap.put("Ulyanovsk","54.33,48.4");
	        myMap.put("Bouake","7.69,-5.03");
	        myMap.put("Santiago","19.48,-70.69");
	        myMap.put("Mexicali","32.65,-115.47");
	        myMap.put("Hai Phong","20.86,106.68");
	        myMap.put("Anyang","37.39,126.92");
	        myMap.put("Dadiangas","6.1,125.25");
	        myMap.put("Morelia","19.72,-101.18");
	        myMap.put("Oshogbo","7.77,4.56");
	        myMap.put("Chongju","36.64,127.5");
	        myMap.put("Jos","9.93,8.89");
	        myMap.put("al-'Ayn","24.23,55.74");
	        myMap.put("Sorocaba","-23.49,-47.47");
	        myMap.put("Bikaner","28.03,73.32");
	        myMap.put("Taizhou","32.49,119.9");
	        myMap.put("Antipolo","14.59,121.18");
	        myMap.put("Xiamen","24.45,118.08");
	        myMap.put("Cochabamba","-17.38,-66.17");
	        myMap.put("Culiacan","24.8,-107.39");
	        myMap.put("Yingkou","40.67,122.28");
	        myMap.put("Kagoshima","31.59,130.56");
	        myMap.put("Siping","43.17,124.33");
	        myMap.put("Orumiyeh","37.53,45");
	        myMap.put("Luancheng","37.88,114.65");
	        myMap.put("Diyarbak?r","37.92,40.23");
	        myMap.put("Yaroslavl","57.62,39.87");
	        myMap.put("Mixco","14.64,-90.6");
	        myMap.put("Banjarmasin","-3.33,114.59");
	        myMap.put("Chisinau","47.03,28.83");
	        myMap.put("Djibouti","11.59,43.15");
	        myMap.put("Seattle","47.62,-122.35");
	        myMap.put("Stuttgart","48.79,9.19");
	        myMap.put("Khabarovsk","48.42,135.12");
	        myMap.put("Rotterdam","51.93,4.48");
	        myMap.put("Jinzhou","41.12,121.1");
	        myMap.put("Kisangani","0.53,25.19");
	        myMap.put("San Pedro Sula","15.47,-88.03");
	        myMap.put("Bengbu","32.95,117.33");
	        myMap.put("Irkutsk","52.33,104.24");
	        myMap.put("Shihezi","44.3,86.03");
	        myMap.put("Maracay","10.33,-67.47");
	        myMap.put("Cucuta","7.88,-72.51");
	        myMap.put("Bhavnagar","21.79,72.13");
	        myMap.put("Port Said","31.26,32.29");
	        myMap.put("Denver","39.77,-104.87");
	        myMap.put("Genoa","44.42,8.93");
	        myMap.put("Jiangmen","22.58,113.08");
	        myMap.put("Dortmund","51.51,7.48");
	        myMap.put("Barnaul","53.36,83.75");
	        myMap.put("Washington","38.91,-77.02");
	        myMap.put("Veracruz","19.19,-96.14");
	        myMap.put("Ribeirao Preto","-21.17,-47.8");
	        myMap.put("Vladivostok","43.13,131.9");
	        myMap.put("Mar del Plata","-38,-57.58");
	        myMap.put("Boston","42.34,-71.02");
	        myMap.put("Eskisehir","39.79,30.52");
	        myMap.put("Warangal","18.01,79.58");
	        myMap.put("Zahedan","29.5,60.83");
	        myMap.put("Essen","51.47,7");
	        myMap.put("Dusseldorf","51.24,6.79");
	        myMap.put("Kaifeng","34.85,114.35");
	        myMap.put("Kingston","17.99,-76.8");
	        myMap.put("Glasgow","55.87,-4.27");
	        myMap.put("Funabashi","35.7,139.99");
	        myMap.put("Shah Alam","3.07,101.56");
	        myMap.put("Maoming","21.92,110.87");
	        myMap.put("Hachioji","35.66,139.33");
	        myMap.put("Meknes","33.9,-5.56");
	        myMap.put("Hamhung","39.91,127.54");
	        myMap.put("Villa Nueva","14.53,-90.59");
	        myMap.put("Sargodha","32.08,72.67");
	        myMap.put("Las Vegas","36.21,-115.22");
	        myMap.put("Resht","37.3,49.63");
	        myMap.put("Cangzhou","38.32,116.87");
	        myMap.put("Tanggu","39,117.67");
	        myMap.put("Helsinki","60.17,24.94");
	        myMap.put("Malaga","36.72,-4.42");
	        myMap.put("Milwaukee","43.06,-87.97");
	        myMap.put("Nashville","36.17,-86.78");
	        myMap.put("Ife","7.48,4.56");
	        myMap.put("Changde","29.03,111.68");
	        myMap.put("at-Ta'if","21.26,40.38");
	        myMap.put("Surakarta","-7.57,110.82");
	        myMap.put("Poznan","52.4,16.9");
	        myMap.put("Barcelona","10.13,-64.72");
	        myMap.put("Bloemfontein","-29.15,26.23");
	        myMap.put("Lopez Mateos","19.57,-99.26");
	        myMap.put("Bangui","4.36,18.56");
	        myMap.put("Reynosa","26.08,-98.28");
	        myMap.put("Xigong","22.33,114.25");
	        myMap.put("Cuiaba","-15.61,-56.09");
	        myMap.put("Shiliguri","26.73,88.42");
	        myMap.put("Oklahoma City","35.47,-97.51");
	        myMap.put("Louisville","38.22,-85.74");
	        myMap.put("Jiamusi","46.83,130.35");
	        myMap.put("Huaiyin","33.58,119.03");
	        myMap.put("Welkom","-27.97,26.73");
	        myMap.put("Kolhapur","16.7,74.22");
	        myMap.put("Ulhasnagar","19.23,73.15");
	        myMap.put("Rajpur","22.44,88.44");
	        myMap.put("Bremen","53.08,8.81");
	        myMap.put("San Salvador","13.69,-89.19");
	        myMap.put("Maanshan","31.73,118.48");
	        myMap.put("Tembisa","-25.99,28.22");
	        myMap.put("Banqiao","25.02,121.44");
	        myMap.put("Toluca","19.29,-99.67");
	        myMap.put("Portland","45.54,-122.66");
	        myMap.put("Gold Coast","-28.07,153.44");
	        myMap.put("Kota Kinabalu","5.97,116.07");
	        myMap.put("Vilnius","54.7,25.27");
	        myMap.put("Agadir","30.42,-9.61");
	        myMap.put("Ajmer","26.45,74.64");
	        myMap.put("Orenburg","51.78,55.1");
	        myMap.put("Neijiang","29.58,105.05");
	        myMap.put("Salta","-24.79,-65.41");
	        myMap.put("Guntur","16.31,80.44");
	        myMap.put("Novokuznetsk","53.75,87.1");
	        myMap.put("Yangzhou","32.4,119.43");
	        myMap.put("Durgapur","23.5,87.31");
	        myMap.put("Shashi","30.32,112.23");
	        myMap.put("Asuncion","-25.3,-57.63");
	        myMap.put("Aparecida de Goiania","-16.82,-49.24");
	        myMap.put("Ribeirao das Neves","-19.76,-44.08");
	        myMap.put("Petaling Jaya","3.1,101.62");
	        myMap.put("Sangli-Miraj","16.86,74.57");
	        myMap.put("Dehra Dun","30.34,78.05");
	        myMap.put("Maturin","9.75,-63.17");
	        myMap.put("Torreon","25.55,-103.43");
	        myMap.put("Jiaozuo","35.25,113.22");
	        myMap.put("Zhuhai","22.28,113.57");
	        myMap.put("Nanded","19.17,77.29");
	        myMap.put("Suez","29.98,32.54");
	        myMap.put("Tyumen","57.15,65.53");
	        myMap.put("Albuquerque","35.12,-106.62");
	        myMap.put("Cagayan","8.45,124.67");
	        myMap.put("Mwanza","-2.52,32.89");
	        myMap.put("Petare","10.52,-66.83");
	        myMap.put("Soledad","10.92,-74.77");
	        myMap.put("Uijongbu","37.74,127.04");
	        myMap.put("Yueyang","29.38,113.1");
	        myMap.put("Feira de Santana","-12.25,-38.97");
	        myMap.put("Ta'izz","13.6,44.04");
	        myMap.put("Tucson","32.2,-110.89");
	        myMap.put("Naberezhnyye Chelny","55.69,52.32");
	        myMap.put("Kerman","30.3,57.08");
	        myMap.put("Matsuyama","33.84,132.77");
	        myMap.put("Garoua","9.3,13.39");
	        myMap.put("Tlaquepaque","20.64,-103.32");
	        myMap.put("Tuxtla Gutierrez","16.75,-93.12");
	        myMap.put("Jamnagar","22.47,70.07");
	        myMap.put("Jammu","32.71,74.85");
	        myMap.put("Gulbarga","17.34,76.82");
	        myMap.put("Chiclayo","-6.76,-79.84");
	        myMap.put("Hanover","52.4,9.73");
	        myMap.put("Bucaramanga","7.13,-73.13");
	        myMap.put("Bahawalpur","29.39,71.67");
	        myMap.put("Goteborg","57.72,12.01");
	        myMap.put("Zhunmen","22.41,113.98");
	        myMap.put("Bhatpara","22.89,88.42");
	        myMap.put("Ryazan","54.62,39.74");
	        myMap.put("Calamba","14.21,121.15");
	        myMap.put("Changwon","35.27,128.62");
	        myMap.put("Aracaju","-10.91,-37.07");
	        myMap.put("Zunyi","27.7,106.92");
	        myMap.put("Lipetsk","52.62,39.62");
	        myMap.put("Dresden","51.05,13.74");
	        myMap.put("Saharanpur","29.97,77.54");
	        myMap.put("H?amah","35.15,36.73");
	        myMap.put("Niyala","12.06,24.89");
	        myMap.put("San Nicolas de los Garza","25.75,-100.3");
	        myMap.put("Higashiosaka","34.67,135.59");
	        myMap.put("al-H?illah","32.48,44.43");
	        myMap.put("Leipzig","51.35,12.4");
	        myMap.put("Xuchang","34.02,113.82");
	        myMap.put("Wuhu","31.35,118.37");
	        myMap.put("Boma","-5.85,13.05");
	        myMap.put("Kananga","-5.89,22.4");
	        myMap.put("Mykolayiv","46.97,32");
	        myMap.put("Atlanta","33.76,-84.42");
	        myMap.put("Londrina","-23.3,-51.18");
	        myMap.put("Tabuk","28.39,36.57");
	        myMap.put("Cuautitlan Izcalli","19.65,-99.25");
	        myMap.put("Nuremberg","49.45,11.05");
	        myMap.put("Santa Fe","-31.6,-60.69");
	        myMap.put("Joinville","-26.32,-48.84");
	        myMap.put("Cork","51.89,-8.47");
	        myMap.put("Blarney","51.9330555,-8.568056");
	        myMap.put("Tower","51.9255745,-8.604067");
	        myMap.put("Donoughmore","51.9896265,-8.7422123");
	        myMap.put("Enniskerry","53.1935032,-6.1707299");
	        myMap.put("Galway","53.2705583,-9.0566677");
	        myMap.put("Belfast","54.59488,-5.9266324");
	        myMap.put("Manchester","53.4722454,-2.2235922");
	        myMap.put("Leeds","53.8060025,-1.5357323");
	        myMap.put("Edinburgh","55.9410655,-3.2053836");
	        
	        
	        
	        
	        
	        
	        return myMap;

		}
		
	    public static String[] convertToString(Object[] objectArray){
	        String[] strArray = new String[objectArray.length];
	        for (int i = 0; i < objectArray.length; i++)
	        {
	            strArray[i] = new String((String) objectArray[i]);
	        }
	        return strArray;
	     }
	    

	    

		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			super.onActivityResult(requestCode, resultCode, data);
			loginButton.onActivityResult(requestCode, resultCode, data);
		}
		
		 // InterstitialAdListener methods
	    @Override
	    public void onInterstitialLoaded(MoPubInterstitial interstitial) {
	        if (interstitial.isReady()) {
	           // mInterstitial.show();
	        } else {
	            // Other code
	        }
	    }
	    
	    @Override
	    public void onInterstitialFailed(MoPubInterstitial interstitial, MoPubErrorCode errorCode) {}
	 
	    @Override
	    public void onInterstitialShown(MoPubInterstitial interstitial) {}
	 
	    @Override
	    public void onInterstitialClicked(MoPubInterstitial interstitial) {}
	 
	    @Override
	    public void onInterstitialDismissed(MoPubInterstitial interstitial) {
	    	mInterstitial.destroy();
	    }
	    
	
		

	  @Override
	  public void onResume() {
	    super.onResume();
	  }

	  @Override
	  public void onDestroy() {

	    super.onDestroy();
	    
	    if(mInterstitial != null )
	    	mInterstitial.destroy();
	    
	    if(moPubView != null)
	    	moPubView.destroy();
	    
	  }
	  
	    public void onStart() {
	    	Log.e(this.getClass().getSimpleName(), "onStart()");
		    	
	        super.onStart();  
	}

		@Override
		public boolean onQueryTextSubmit(String query) {
			// TODO Auto-generated method stub
			return false;
		}

		@Override
		public boolean onQueryTextChange(String newText) {
			// TODO Auto-generated method stub
			
			//if(TextUtils.isEmpty(newText))
			return false;		
		}

		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem,
				int visibleItemCount, int totalItemCount) {
			// TODO Auto-generated method stub
			/*
			if (view.getLastVisiblePosition() == view.getAdapter().getCount() - 1
					&& view.getChildAt(view.getChildCount() - 1).getBottom() <= view.getHeight()) {
					//scroll end reached
					//Write your code here
					
				Log.e(this.getClass().getSimpleName(), "Reached End of scrolling");
            }
            */
			
			//Algorithm to check if the last item is visible or not
	        final int lastItem = firstVisibleItem + visibleItemCount;
	        if(lastItem == totalItemCount && mBottomTweetReachedFirstTime == false && totalItemCount > 5){                 
	        // you have reached end of list, load more data   
	        	
	        	if(mInterstitial != null)
		    		  mInterstitial.show();
	        	
	        	Log.d(this.getClass().getSimpleName(), "Reached End of scrolling");
	        	mBottomTweetReachedFirstTime = true;
	        }
			
			
			
		}
		
		

}


