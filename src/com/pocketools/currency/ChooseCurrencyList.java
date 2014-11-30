package com.pocketools.currency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import android.app.Activity;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class ChooseCurrencyList extends Activity {
	
    private DBAdapter mDb;
    private LinkedList<String> mSelection;
    private HashMap<String,String> mCurrencyList;
	
	
    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
                
        requestWindowFeature(Window.FEATURE_NO_TITLE); 
        
        setContentView(R.layout.choose_currency_list);
        
        mDb = new DBAdapter(this);
        mDb.open();
        
        
        
        mCurrencyList = new HashMap<String,String>();
        
        mDb = new DBAdapter(this);
        mDb.open();
        
        Cursor currencies = mDb.getCurrencies();
        startManagingCursor(currencies);
        
        StringBuffer currencylist = new StringBuffer();
        
        for(int i=0; i < currencies.getCount(); i++){
        	
        	currencies.moveToPosition(i);
        	
        	currencylist.append("+EUR" + currencies.getString(currencies.getColumnIndex(DBAdapter.KEY_CURRENCY)) + "=X");
        	
        	mCurrencyList.put(currencies.getString(currencies.getColumnIndex(DBAdapter.KEY_CURRENCY)), 
        			currencies.getString(currencies.getColumnIndex(DBAdapter.KEY_CURRENCY_DESCRIPTION)));
        }
        
        
        
      //  HashMap<String,Integer> currencyMap = new HashMap<String, Integer>();
        mSelection = new LinkedList<String>();

     
        Cursor c = mDb.getCurrencies();      
        startManagingCursor(c);
        
        TableLayout table = (TableLayout)findViewById(R.id.categoryQuickSetupList);
		
		for(int i=0; i< c.getCount(); i++){
			c.moveToPosition(i);
			String currencyName = c.getString(c.getColumnIndex(DBAdapter.KEY_CURRENCY));
			int isSelected = c.getInt(c.getColumnIndex(DBAdapter.KEY_CURRENCY_IS_SELECTED));
			//currencyMap.put(currencyName, isSelected);
			
			TableRow row = new TableRow(this);
        	row.setMinimumWidth(ViewGroup.LayoutParams.FILL_PARENT);
        	row.setGravity(Gravity.CENTER_VERTICAL);
        	row.setBackgroundColor(Color.WHITE);
        	row.setPadding(15, 10, 0, 10);
        	
        	
	        TextView view = new TextView(this);
	        view.setText(currencyName);
	       
	        
	        if(isSelected == 1)
	        	view.setTextColor(Color.rgb(176, 196, 222));
	        else
	        	view.setTextColor(Color.rgb(0,0,128));
	        
	        view.setTextSize(24);
	        row.addView(view);
	        
	        view = new TextView(this);
	        view.setText(mCurrencyList.get(currencyName));
	        view.setTextColor(Color.rgb(176, 196, 222));
	        view.setTextSize(16);
	        view.setGravity(Gravity.LEFT);
	        view.setPadding(5, 0, 0, 0);
	        row.addView(view);	

        	
	        final CheckBox checkBox = new CheckBox(this);	
	        //checkBox.setPadding(5, 0, 15, 0);
	        checkBox.setTag(currencyName);
	        //checkBox.setGravity(Gravity.RIGHT);
	        
	        
	        if(isSelected == 1){
	        	checkBox.setChecked(true);
	        	checkBox.setEnabled(true);
	        	mSelection.add(currencyName);
	        }
	        
	        checkBox.setOnClickListener(new OnClickListener() {
	            public void onClick(View v) {
	            	
	            	String checkBoxId = (String)v.getTag();
	            	
	                // Perform action on clicks
	                if (checkBox.isChecked()) {
	                	mSelection.add(checkBoxId);
	                } else {
	                	mSelection.remove((String)checkBoxId);
	                    
	                }
	            }
	        });
       	
	        row.addView(checkBox);
	        
	        view = new TextView(this);
	        view.setWidth(40);	       
	        row.addView(view);	        
	        table.addView(row);
 
	        view = new TextView(this);
	        view.setMaxHeight(1);
	        view.setBackgroundColor(Color.rgb(192, 192, 192));
	        
	        table.addView(view);		
		}
        	
		
    	 //Set up Button for OK
        final Button buttonOK = (Button) findViewById(R.id.category_quick_ok);
        buttonOK.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {
            	
            	//Clear all selections
            	mDb.unselectAllCurrencies();
            	
            	Iterator<String> iter = mSelection.iterator();
            	while(iter.hasNext()){
            		String next = iter.next();
            		mDb.updateExchangeRateSelected(next,1);            		
            	}
            	
            	
            	Toast.makeText(v.getContext(), "Currencies Added", Toast.LENGTH_SHORT).show();
            	setResult(CurrencyList.RELOAD_CURRENCY_LIST_SUCCESS);
            	
            	finish();
            }
        });
        
        //Set up Button for Cancel
        final Button buttonCancel = (Button) findViewById(R.id.category_quick_cancel);
        buttonCancel.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {           	
            	finish();
            }
        });
    }
    
  
    
    @Override
    protected void onSaveInstanceState(Bundle outState) {      
        Log.d(this.getLocalClassName(), "onSaveInstanceState()");
        
        super.onSaveInstanceState(outState);
    }
    
    @Override
    protected void onPause() {
        Log.d(this.getLocalClassName(), "onPause()");

        if(mDb.getDatabase().isOpen())
        	mDb.close();

        super.onPause();
    }
    
    @Override
    protected void onStop() {      
        Log.d(this.getLocalClassName(), "onStop()");
        

        if(mDb.getDatabase().isOpen())
        	mDb.close();
        
        super.onStop();
    }
    
    @Override
    protected void onDestroy() {
        Log.d(this.getLocalClassName(), "onDestroy()");
        
        super.onDestroy();
    }
    
    @Override
    protected void onResume() {
    	Log.d(this.getLocalClassName(), "onResume()");
    	
    	if(!mDb.getDatabase().isOpen())
    		mDb.open();
    	
    	super.onResume();
    }
    
    @Override
    protected void onRestart() {        
    	Log.d(this.getLocalClassName(), "onRestart()");
    	
    	if(!mDb.getDatabase().isOpen())
    		mDb.open();
    	
    	
      
        super.onRestart();
    }
    
    
    protected void onStart() {
    	Log.d(this.getLocalClassName(), "onStart()");

        
    	
    	if(!mDb.getDatabase().isOpen())
    		mDb.open();
    	
    	super.onStart();
    }
                
 
}
