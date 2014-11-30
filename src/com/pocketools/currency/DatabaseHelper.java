package com.pocketools.currency;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	
	   private static final String TAG = "CurrencyDbAdapter";
	   private static final String DATABASE_NAME = "pocket_currency";
	   private static final String CURRENCY_TABLE = "currency";
	   private static final String DEFAULTS_TABLE = "defaults";	   
	   private static final int DATABASE_VERSION = 33;
	   
	   public static final String KEY_CURRENCY = "currency_type";
	   public static final String KEY_CURRENCY_DESCRIPTION = "currency_description";
	   public static final String KEY_CURRENCY_X_RATE = "exchange_rate";
	   public static final String KEY_CURRENCY_IS_SELECTED = "exchange_rate_selected";
	   public static final String KEY_CURRENCY_X_RATE_REFRESH_DATE = "xrate_refresh_date";
	   public static final String KEY_CURRENCY_SINGLE_X_RATE_DATE = "spare_1";
	   public static final String KEY_DEFAULT_VALUE = "default_value";
	   public static final String KEY_DEFAULT_TYPE = "default_type";
	   
	   
	   
	   private static final String CURRENCY_TABLE_CREATE =
		       "CREATE TABLE currency (" + 
		       						   "_id INT," +
		       						   "currency_type TEXT, " +
		       						   "currency_description TEXT, " +
		       						   "exchange_rate REAL, " +
		       						   "exchange_rate_selected INT, " + 
		       						   "spare_1 TEXT);";
		   
		   private static final String DEFAULTS_TABLE_CREATE =
		       "CREATE TABLE defaults (" + 
		       						   "default_type TEXT, " +
		       						   "default_value TEXT);";

    DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

 	   Log.w(TAG, "Creating new Database");
        db.execSQL(CURRENCY_TABLE_CREATE);     
        db.execSQL(DEFAULTS_TABLE_CREATE);

        db.execSQL("INSERT INTO currency VALUES (0,\"AFN\",\"Afghanistan Afghanis\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (1,\"AED\",\"United Arab Emirates Dirhams\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (2,\"ALL\",\"Albania Leke\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (3,\"ARS\",\"Argentina Pesos\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (4,\"AUD\",\"Australia Dollars\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (5,\"BBD\",\"Barbados Dollars\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (6,\"BDT\",\"Bangladesh Taka\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (7,\"BGN\",\"Bulgaria Leva\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (8,\"BHD\",\"Bahrain Dinars\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (9,\"BMD\",\"Bermuda Dollars\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (10,\"BRL\",\"Brazil Reais\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (11,\"BSD\",\"Bahamas Dollars\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (12,\"CAD\",\"Canada Dollars\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (13,\"CHF\",\"Switzerland Francs\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (14,\"CLP\",\"Chile Pesos\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (15,\"COP\",\"Colombia Pesos\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (16,\"CRC\",\"Costa Rica Colones\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (17,\"CNY\",\"China Yuan Renminbi\",0,1,\"\")");
        db.execSQL("INSERT INTO currency VALUES (18,\"CZK\",\"Czech Republic Koruny\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (19,\"DKK\",\"Denmark Kroner\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (20,\"DOP\",\"Dominican Republic Pesos\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (21,\"DZD\",\"Algeria Dinars\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (22,\"EEK\",\"Estonia Krooni\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (23,\"EGP\",\"Egypt Pounds\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (24,\"EUR\",\"Euro\",1,1,\"\")");
        db.execSQL("INSERT INTO currency VALUES (25,\"FJD\",\"Fiji Dollars\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (26,\"GBP\",\"United Kingdom Pounds\",0,1,\"\")");
        db.execSQL("INSERT INTO currency VALUES (27,\"HKD\",\"Hong Kong Dollars\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (28,\"HRK\",\"Croatia Kuna\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (29,\"HUF\",\"Hungary Forint\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (30,\"IDR\",\"Indonesia Rupiahs\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (31,\"ILS\",\"Israel New Shekels\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (32,\"INR\",\"India Rupees\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (33,\"IQD\",\"Iraq Dinars\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (34,\"IRR\",\"Iran Rials\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (35,\"ISK\",\"Iceland Kronur\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (36,\"JMD\",\"Jamaica Dollars\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (37,\"JOD\",\"Jordan Dinars\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (38,\"KES\",\"Kenya Shillings\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (39,\"KRW\",\"South Korea Won\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (40,\"KWD\",\"Kuwait Dinars\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (41,\"LBP\",\"Lebanon Pounds\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (42,\"LKR\",\"Sri Lanka Rupees\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (43,\"MAD\",\"Morocco Dirhams\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (44,\"MUR\",\"Mauritius Rupees\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (45,\"MXN\",\"Mexico Pesos\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (46,\"MYR\",\"Malaysia Ringgits\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (47,\"NOK\",\"Norway Kroner\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (48,\"NZD\",\"New Zealand Dollars\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (49,\"OMR\",\"Oman Rials\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (50,\"PEN\",\"Peru Nuevos Soles\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (51,\"PHP\",\"Philippines Pesos\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (52,\"PKR\",\"Pakistan Rupees\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (53,\"PLN\",\"Poland Zlotych\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (54,\"QAR\",\"Qatar Riyals\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (55,\"RON\",\"Romania New Lei\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (56,\"RUB\",\"Russia Rubles\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (57,\"SAR\",\"Saudi Arabia Riyals\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (58,\"SDG\",\"Sudan Pounds\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (59,\"SEK\",\"Sweden Kronor\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (60,\"SGD\",\"Singapore Dollars\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (61,\"THB\",\"Thailand Baht\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (62,\"TND\",\"Tunisia Dinars\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (63,\"JPY\",\"Japan Yen\",0,1,\"\")");
        db.execSQL("INSERT INTO currency VALUES (64,\"TRY\",\"Turkey Lira\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (65,\"TTD\",\"Trinidad and Tobago Dollars\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (66,\"TWD\",\"Taiwan New Dollars\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (67,\"USD\",\"United States Dollars\",1.40,1,\"\")");
        db.execSQL("INSERT INTO currency VALUES (68,\"VEF\",\"Venezuela Bolivares Fuertes\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (69,\"VND\",\"Vietnam Dong\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (70,\"XAF\",\"CFA BEAC Francs\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (71,\"XAG\",\"Silver Ounces\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (72,\"XAU\",\"Gold Ounces\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (73,\"XCD\",\"Eastern Caribbean Dollars\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (74,\"XDR\",\"IMF Special Drawing Right\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (75,\"XOF\",\"CFA BCEAO Francs\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (76,\"XPD\",\"Palladium Ounces\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (77,\"XPF\",\"CFP Francs\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (78,\"XPT\",\"Platinum Ounces\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (79,\"ZAR\",\"South Africa Rand\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (80,\"ZMK\",\"Zambia Kwacha\",0,0,\"\")");
        db.execSQL("INSERT INTO currency VALUES (81,\"NGN\",\"Nigerian Naira\",0,0,\"\")");
        
        
        db.execSQL("INSERT INTO defaults VALUES (\"default_base_currency\",\"EUR\")");
        db.execSQL("INSERT INTO defaults VALUES (\"default_result_currency\",\"USD\")");
        db.execSQL("INSERT INTO defaults VALUES (\"refresh_date\",\"2009-10-13\")");
        db.execSQL("INSERT INTO defaults VALUES (\"user_agreement_accepted\",\"false\")");
        db.execSQL("INSERT INTO defaults VALUES (\"default_base_currency_list\",\"USD\")");
        db.execSQL("INSERT INTO defaults VALUES (\"default_base_currency_amount\",\"1\")");
        
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG, "Upgrading database from version " + oldVersion + " to "
                + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + CURRENCY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + DEFAULTS_TABLE);
        onCreate(db);
    }
}
