package com.pocketools.currency;

public class NewsItem {
	private String mHeading;
	private String mSource;
	private String mSummary;
	private String mFreshness;
	private String mURL;
	
	public NewsItem(){
		mHeading = "";
		mSource = "";
		mSummary = "";
		mURL = "";
		mFreshness = "";
		
	}
	public String getHeading(){return mHeading;}
	public String getSource(){return mSource;}
	public String getSummary(){return mSummary;}
	public String getFreshness(){return mFreshness;}
	public String getURL(){return mURL;}
	
	public void setHeading(String heading){mHeading = heading;}
	public void setSource(String source){mSource = source;}
	public void setSummary(String summary){mSummary = summary;}
	public void setFreshness(String freshness){mFreshness = freshness;}
	public void setURL(String url){mURL = url;}
	
}
