package com.nanoconverter.zlab;

import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TabHost;
import android.widget.Toast;
import android.widget.TabHost.TabSpec;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;

public class NanoConverter extends TabActivity {
 /** Called when the activity is first created. */
	public static NanoConverter mContext = null;
	public static final int LENGTH_LONG = 10;
	
	public static String BANK_ID = "CBR";
	
	private EditText text,amountmoney;
	
	int count = 37;
	
	public String[] sa = { "USD", "EUR", "CHF", "GBP", "JPY", "UAH", "RUB", "MDL", "BYR", "PLN", "LTL", "LVL", "AZN", "AUD", "AMD", "BGN", "BRL", "HUF", "DKK", "INR", "KZT", "CAD", "KGS", "CNY", "NOK", "RON", "XDR", "SGD", "TJS", "TRY", "TMT", "UZS", "CZK", "SEK", "ZAR", "KRW", "FOO" };
	public EditText[] course = new EditText[count];
	public EditText[] courserate = new EditText[count];
	public RadioButton[] from = new RadioButton[count];
	public RadioButton[] to = new RadioButton[count];
	public String[] moneycourse = new String[count];
	public LinearLayout[] moneycl = new LinearLayout[count];
	public View[] moneycls = new View[count];
	public String coursebydefaultis = "1";
	public boolean[] mactive = new boolean[count];
	public static boolean reverserates = false;
	
	public SharedPreferences settings_money;
	public SharedPreferences.Editor moneyeditor;
	
	private Button buttonrefresh;
	
	public double curentfromcourserate = 1.00;
	public double curenttocourserate = 1.00;

	public static String ListCurPreference,ListBankPreference,listUpdate,leftsideselected,rightsideselected;
	 	
    private ProgressDialog progressDialog;

    Handler handlerCloseThreadforce = new Handler() {@Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progressDialog.dismiss();}
    };
    Handler handlerCloseThread = new Handler() {@Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (Integer.parseInt(listUpdate) == 2){handlerCloseThreadforce.sendEmptyMessage(0);}}
    };
    Handler handlerERRThread = new Handler() {@Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.checkinternet), Toast.LENGTH_SHORT);
            LinearLayout ToastView = (LinearLayout) toast.getView();
            ImageView imageWorld = new ImageView(getApplicationContext());
            imageWorld.setImageResource(R.drawable.err);
            ToastView.addView(imageWorld, 0);
            toast.show();
        }
    };
    Handler handlerERRdevzero = new Handler() {@Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.devzero), Toast.LENGTH_SHORT);
            LinearLayout ToastView = (LinearLayout) toast.getView();
            ImageView imageWorld = new ImageView(getApplicationContext());
            imageWorld.setImageResource(R.drawable.err);
            ToastView.addView(imageWorld, 0);
            toast.show();
        }
    };
    Handler handlerERRdevnull = new Handler() {@Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Toast toast = Toast.makeText(getApplicationContext(), getString(R.string.devnull), Toast.LENGTH_SHORT);
            LinearLayout ToastView = (LinearLayout) toast.getView();
            ImageView imageWorld = new ImageView(getApplicationContext());
            imageWorld.setImageResource(R.drawable.err);
            ToastView.addView(imageWorld, 0);
            toast.show();
        }
    };
    Handler handlerGOODThread = new Handler() {@Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            
            UpdateRates();
            
            Toast toast3 = Toast.makeText(getApplicationContext(), getString(R.string.updatecomplete), Toast.LENGTH_SHORT);
            LinearLayout ToastView = (LinearLayout) toast3.getView();
            ImageView imageWorld = new ImageView(getApplicationContext());
            imageWorld.setImageResource(R.drawable.good);
            ToastView.addView(imageWorld, 0);
            toast3.show();
            
            settings_money = getSharedPreferences("lastupdatedate", 0);
            moneyeditor = settings_money.edit();
            SimpleDateFormat dateis = new SimpleDateFormat("dd.MM.yyyy");
       	   	String curentDate = dateis.format(new Date());
       	   	moneyeditor.putString("lastupdatedate", curentDate.toString());
        	moneyeditor.commit();
        }
    };
                            
 @Override
 
  public void onCreate(Bundle savedInstanceState) {

	 mContext = this;
     super.onCreate(savedInstanceState);
     setContentView(R.layout.main);
     buttonrefresh = (Button) findViewById(R.id.button2);
     text = (EditText) findViewById(R.id.editText1);
     getID();
     getRadio();
     setkey();
     amountmoney = (EditText) findViewById(R.id.editText246);
     amountmoney.setKeyListener(null);

     text.setOnKeyListener(new View.OnKeyListener() {
		public boolean onKey(View v, int keyCode, KeyEvent event) {
			if(event.getAction() == KeyEvent.ACTION_UP  && 
	    		    (keyCode != KeyEvent.KEYCODE_MENU) &&
	    		    (keyCode != KeyEvent.KEYCODE_BACK))
	    			{
						myClickHandler();
	    				return true;
	    			}
	    		return false;
		}
     }
     );
    
     /* Строим ТАБЫ */
     Resources res = getResources();
     TabHost tabHost = getTabHost();

     TabSpec Converter = tabHost.newTabSpec("tab1").setIndicator(getString(R.string.conversetab),
    		 res.getDrawable(R.drawable.ic_tab_convert));

     TabSpec OnlineCourse = tabHost.newTabSpec("tab2").setIndicator(getString(R.string.Coursestab),
    		 res.getDrawable(R.drawable.ic_tab_courses));

     tabHost.addTab(Converter.setContent(R.id.tab1));
     tabHost.addTab(OnlineCourse.setContent(R.id.tab2));
     /* Строим ТАБЫ */
     
     getPrefs();
     int checkBank = Integer.parseInt(ListBankPreference);
     int checkUPDT = Integer.parseInt(listUpdate);

     settings_money = getSharedPreferences("moneyupdatestr", 0);
     String[] separated = settings_money.getString("moneyupdatestr", "7777").split(",");
     
     if (separated[0].equals("7777") ){} else {
     for (int i=0;i<count;i++ ){try {course[i].setText(separated[i]);} catch (Exception ioe) {}
     }}
     courserate[36].setText(course[36].getText().toString());     
     
     settings_money = getSharedPreferences("lastupdatedate", 0);
     String datestored = settings_money.getString("lastupdatedate", "7777");
     SimpleDateFormat dateis = new SimpleDateFormat("dd.MM.yyyy");
	 String curentDate = dateis.format(new Date());

     UpdateRates();
     myClickHandler();
     
	/* autoupdate */
	      if (checkBank != 1){
	    	  	 if (checkUPDT != 0) {
	    		 if (checkUPDT == 1) {NanoConverter.mContext.processThread();}
	    	else if (checkUPDT == 2) {NanoConverter.mContext.processThreadforce();}
	    	else if (checkUPDT == 3) {if (!curentDate.toString().equals(datestored)){NanoConverter.mContext.processThread();}}}
	      } else {
	    	  for (int i=0;i<count;i++ ){
	    		  courserate[i].setText(course[i].getText().toString());
	  	    	}
	      }
	/* autoupdate */
 }

 public void getID() {for (int i=0;i<count;i++ ){
	int resID = getResources().getIdentifier("Course" + sa[i],"id", getPackageName());
	course[i] = (EditText)findViewById(resID);
		resID = getResources().getIdentifier("Course" + sa[i] + "rate","id", getPackageName());
	courserate[i] = (EditText)findViewById(resID);}
 }
 
 public void getRadio() {for (int i=0;i<count;i++ ){
	int resID = getResources().getIdentifier("from" + sa[i],"id", getPackageName());
 	from[i] = (RadioButton)findViewById(resID);
 		resID = getResources().getIdentifier("to" + sa[i],"id", getPackageName());
 	to[i] = (RadioButton)findViewById(resID);}
 

 settings_money = getSharedPreferences("fromStore", 0);
 String fromStore = settings_money.getString("fromStore", "0");
 
 for (int i=0;i<count;i++ ){
		if (fromStore.equals(String.valueOf(i))){
			from[i].setChecked(true);}}
 
 settings_money = getSharedPreferences("toStore", 0);
 String toStore = settings_money.getString("toStore", "0");
  for (int i=0;i<count;i++ ){
		if (toStore.equals(String.valueOf(i))){
			to[i].setChecked(true);}}
 }
 
 public void setkey() {for (int i=0;i<count;i++ ){
	 from[i].setOnClickListener(new OnClickListener() {public void onClick(View v) {myClickHandler();}});
	 to[i].setOnClickListener(new OnClickListener() {public void onClick(View v) {myClickHandler();}});}	 
 }
 
 public void getresID2() {for (int i=0;i<count;i++ ){
	int resID = getResources().getIdentifier(sa[i]+"cl","id", getPackageName());
	moneycl[i] = (LinearLayout)findViewById(resID);
		resID = getResources().getIdentifier(sa[i]+"cls","id", getPackageName());
	moneycls[i] = (View)findViewById(resID);}
 }
 
 protected void onResume() {
	 getRadio();
	 getresID2();
	 getPrefs();
	 
	 int checkBank = Integer.parseInt(ListBankPreference);
	 
	 for (int i=0;i<count;i++ ){
		 if (mactive[i] == false){from[i].setVisibility(View.GONE);to[i].setVisibility(View.GONE);moneycls[i].setVisibility(View.GONE);moneycl[i].setVisibility(View.GONE);}
		 if (mactive[i] == true){from[i].setVisibility(View.VISIBLE);to[i].setVisibility(View.VISIBLE);moneycl[i].setVisibility(View.VISIBLE);moneycls[i].setVisibility(View.VISIBLE);}
	 }

	      if (checkBank == 0){ BANK_ID = "CBR"; TurnONrates();} else
	      if (checkBank == 1){TurnOFFrates(); } else
	      if (checkBank == 2){ BANK_ID = "NBU";  TurnONrates();} else
	      if (checkBank == 3){ BANK_ID = "NBRB"; TurnONrates();} else
	      if (checkBank == 4){ BANK_ID = "BNM";  TurnONrates();} else
	      if (checkBank == 5){ BANK_ID = "AZ";  TurnONrates();} else
	      if (checkBank == 6){ BANK_ID = "ECB";  TurnONrates();} else
	      if (checkBank == 7){ BANK_ID = "FOREX";  TurnONrates();}
	      if (checkBank != 1){UpdateRates();}

	      super.onResume();
 }
 /* MENU */
 @Override
 public boolean onCreateOptionsMenu(Menu menu) {
     MenuInflater inflater = getMenuInflater();
     inflater.inflate(R.menu.mainmenu, menu);
     return true;
 }
 @Override
 public boolean onOptionsItemSelected(MenuItem item) {
     switch (item.getItemId()) {
     case R.id.quit:{
    	 finish();
    	 return true;}
     case R.id.settings:{
    	 Intent settingsActivity = new Intent(getBaseContext(),
    			 com.nanoconverter.zlab.Preferences.class);
    	 startActivity(settingsActivity);
    	 return true;}
     default:
         return super.onOptionsItemSelected(item);
     }
 }
 
 /* Сохраняем конфиг при выходе */
 @Override
 protected void onStop(){
    super.onStop();
    
    settings_money = getSharedPreferences("moneyupdatestr", 0);
    moneyeditor = settings_money.edit();
    
    int checkBank = Integer.parseInt(ListBankPreference);
	
    if (checkBank == 1) {
    	for (int i=0;i<count;i++ ){
    		course[i].setText(courserate[i].getText().toString());
        	}
    }
    course[36].setText(courserate[36].getText().toString());
    
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < count; i++) {
        sb.append(course[i].getText().toString()).append(",");
        }
		moneyeditor.putString("moneyupdatestr", sb.toString());
		moneyeditor.commit();

		for (int i=0;i<count;i++ ){
       		if (from[i].isChecked()){
       			settings_money = getSharedPreferences("fromStore", 0);
       		    moneyeditor = settings_money.edit();
       			moneyeditor.putString("fromStore", String.valueOf(i));
       			moneyeditor.commit();}
       		 if (to[i].isChecked()){
       			settings_money = getSharedPreferences("toStore", 0);
       		    moneyeditor = settings_money.edit();
       		    moneyeditor.putString("toStore", String.valueOf(i));
       			moneyeditor.commit();}}
 }

private void processThread() {
    
	Toast toast2 = Toast.makeText(getApplicationContext(), getString(R.string.updateinprogress), Toast.LENGTH_SHORT);
    LinearLayout ToastView = (LinearLayout) toast2.getView();
    ImageView imageWorld = new ImageView(getApplicationContext());
    imageWorld.setImageResource(R.drawable.dwnld);
    ToastView.addView(imageWorld, 0);
    toast2.show();
    
    bankIDcheck();
 }

private void processThreadforce() {
	progressDialog = ProgressDialog.show(NanoConverter.mContext, getString(R.string.wait), getString(R.string.updateinprogress));

	new Thread() {
		public void run() {
		killLongForce();
		}
	}.start();
	bankIDcheck();
 }

public void bankIDcheck() {
	if (BANK_ID == "CBR"){
        new Thread() {
            public void run() {
           	 runLongProcessCBR();
           	 handlerCloseThread.sendEmptyMessage(0);
            }
        }.start();}
   	if (BANK_ID == "NBU"){
   		new Thread() {
   	         public void run() {
   	        	 runLongProcessNBU();
   	        	 handlerCloseThread.sendEmptyMessage(0);
   	         }
   	     }.start();
   	}
   	if (BANK_ID == "NBRB"){
   		new Thread() {
  	         public void run() {
  	        	 runLongProcessNBRB();
  	        	 handlerCloseThread.sendEmptyMessage(0);
  	         }
  	     }.start();
  	}
   	if (BANK_ID == "BNM"){
   		new Thread() {
  	         public void run() {
  	        	 runLongProcessBNM();
  	        	 handlerCloseThread.sendEmptyMessage(0);
  	         }
  	     }.start();
  	}
   	if (BANK_ID == "AZ"){
   		new Thread() {
  	         public void run() {
  	        	 runLongProcessAZ();
  	        	 handlerCloseThread.sendEmptyMessage(0);
  	         }
  	     }.start();
  	}
   	if (BANK_ID == "ECB"){
   		new Thread() {
  	         public void run() {
  	        	 runLongProcessECB();
  	        	 handlerCloseThread.sendEmptyMessage(0);
  	         }
  	     }.start();
  	}
   	if (BANK_ID == "FOREX") {
   		new Thread() {
   			public void run() {
   				runLongProcessFOREX();
   				handlerCloseThread.sendEmptyMessage(0);
   			}
 	     }.start();
   	}
}

private void killLongForce() {
	try {
	Thread.sleep(10*1000);
	handlerCloseThreadforce.sendEmptyMessage(0);
	} catch (Exception ioe) {
	    	}
}
 private void runLongProcessCBR() {
	
  try {
	  	 boolean sec = true;
	  		Document doc = null;
	  		
	  	    try {
	  	    		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	  	     	   	DocumentBuilder db = dbf.newDocumentBuilder();	
	  	     	   	URL url = new URL("http://www.cbr.ru/scripts/XML_daily.asp");
	  	     	   	doc = db.parse(new InputSource(url.openStream()));
	  		     } catch (Exception ioe) {
	  		    	 sec = false;
	  		    	handlerERRThread.sendEmptyMessage(0);
	  		    	 }
	  	if (sec == true){
	  		try {
	  		NodeList charlist = doc.getElementsByTagName("CharCode");
	  		NodeList nomlist = doc.getElementsByTagName("Nominal");
	  		NodeList list = doc.getElementsByTagName("Value");
	  		   int len = list.getLength();
	  		
	  		   String[] sas = { "USD", "EUR", "CHF", "GBP", "JPY", "UAH", "RUB", "MDL", "BYR", "PLN", "LTL", "LVL", "AZN", "AUD", "AMD", "BGN", "BRL", "HUF", "DKK", "INR", "KZT", "CAD", "KGS", "CNY", "NOK", "RON", "XDR", "SGD", "TJS", "TRY", "TMT", "UZS", "CZK", "SEK", "ZAR", "KRW" };
	  		   String[] coursenew = new String[36];
	  		   for(int i = 0; i<36; i++){coursenew[i] = "0";}
	  		   
	  		   for(int i = 0; i<len; i++)
	  		   {
	  			   /* ID */
	  			   	Node ch= charlist.item(i);
	  			    ch.getNodeValue();
	  		   		ch.getFirstChild().getNodeValue();
	  		   		String chStr  = ch.getFirstChild().getNodeValue();
	  		   		int[] chpos = new int[36];
	  		   		for(int j = 0; j<36; j++){chpos[j] = 7777;}
	  		   		for(int j = 0; j<36; j++){
	  		   			if (chStr.equals(sas[j])) {
	  		   			chpos[j] = i;
	  		   			}
	  		   		}
	  		   		
	  		   		/* rate */
		  		   	Node r= nomlist.item(i);
	  			    r.getNodeValue();
	  		   		r.getFirstChild().getNodeValue();
	  		   		String nStr  = r.getFirstChild().getNodeValue();
	  		   		int[] nd = new int[36];
	  		   		for(int j = 0; j<36; j++){nd[j] = 1;}
	  		   		for(int j = 0; j<36; j++){
	  		   			if (chStr.equals(sas[j])) {
	  		   			nd[j] = Integer.parseInt(nStr);
	  		   			}
	  		   		}
	  			   
	  			   /* data */
	  			    Node n= list.item(i);
	  		   		n.getNodeValue();
	  		   		n.getFirstChild().getNodeValue();
	  		   		String dateCurrencyStr  = n.getFirstChild().getNodeValue();
  		   		
	  		   		coursenew[6] = "1.00";
	  		   		
	  		   		double coursetrue;
	  		   		
		  		   	for(int j = 0; j<36; j++){
		  		   	if(i == chpos[j])	{coursenew[j] = dateCurrencyStr.replace(",", ".");
			   			coursetrue = ( Double.parseDouble(coursenew[j]) / nd[j] );
			   			coursenew[j] = (Double.toString(coursetrue));
			   			}
	  		   		}
		  		   	
		  		  if (i == len-1){
		  			  for(int j = 0; j<36; j++){
			  		   	 course[j].setText(coursenew[j]);
				   			}
		  		  }
	  		   }
	  		 
	  		 handlerGOODThread.sendEmptyMessage(0);
	  		 
	  			} catch (Exception ioe) {
	  		    	 //donothing
	  		    	}
	  	} else { }	  	 
     } catch (Exception ioe) {
    	 
     }
 }
 
 private void runLongProcessNBU() {

	  try {
		  	 boolean sec = true;
		  		Document doc = null;
		  		
		  	    try {
		  	    		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		  	     	   	DocumentBuilder db = dbf.newDocumentBuilder();	
		  	     	   	URL url = new URL("http://pfsoft.com.ua/service/currency/");
		  	     	   	doc = db.parse(new InputSource(url.openStream()));
		  		     } catch (Exception ioe) {
		  		    	 sec = false;
		  		    	handlerERRThread.sendEmptyMessage(0);
		  		    	 }
		  	if (sec == true){
		  		try {
			  		NodeList charlist = doc.getElementsByTagName("CharCode");
			  		NodeList nomlist = doc.getElementsByTagName("Nominal");
			  		NodeList list = doc.getElementsByTagName("Value");
			  		   int len = list.getLength();
			  		
			  		   String[] sas = { "USD", "EUR", "CHF", "GBP", "JPY", "UAH", "RUB", "MDL", "BYR", "PLN", "LTL", "LVL", "AZM", "AUD", "AMD", "BGN", "BRL", "HUF", "DKK", "INR", "KZT", "CAD", "KGS", "CNY", "NOK", "RON", "XDR", "SGD", "TJS", "TRY", "TMT", "UZS", "CZK", "SEK", "ZAR", "KRW" };
			  		   String[] coursenew = new String[36];
			  		   for(int i = 0; i<36; i++){coursenew[i] = "0";}
			  		   
			  		   for(int i = 0; i<len; i++)
			  		   {
			  			   /* ID */
			  			   	Node ch= charlist.item(i);
			  			    ch.getNodeValue();
			  		   		ch.getFirstChild().getNodeValue();
			  		   		String chStr  = ch.getFirstChild().getNodeValue();
			  		   		int[] chpos = new int[36];
			  		   		for(int j = 0; j<36; j++){chpos[j] = 7777;}
			  		   		for(int j = 0; j<36; j++){
			  		   			if (chStr.equals(sas[j])) {
			  		   			chpos[j] = i;
			  		   			}
			  		   		}
			  		   		
			  		   		/* rate */
				  		   	Node r= nomlist.item(i);
			  			    r.getNodeValue();
			  		   		r.getFirstChild().getNodeValue();
			  		   		String nStr  = r.getFirstChild().getNodeValue();
			  		   		int[] nd = new int[36];
			  		   		for(int j = 0; j<36; j++){nd[j] = 1;}
			  		   		for(int j = 0; j<36; j++){
			  		   			if (chStr.equals(sas[j])) {
			  		   			nd[j] = Integer.parseInt(nStr);
			  		   			}
			  		   		}
			  			   
			  			   /* data */
			  			    Node n= list.item(i);
			  		   		n.getNodeValue();
			  		   		n.getFirstChild().getNodeValue();
			  		   		String dateCurrencyStr  = n.getFirstChild().getNodeValue();
		  		   		
			  		   		coursenew[5] = "1.00";
			  		   		
			  		   		double coursetrue;
			  		   		
				  		   	for(int j = 0; j<36; j++){
				  		   	if(i == chpos[j])	{coursenew[j] = dateCurrencyStr.replace(",", ".");
					   			coursetrue = ( Double.parseDouble(coursenew[j]) / nd[j] );
					   			coursenew[j] = (Double.toString(coursetrue));
					   			}
			  		   		}
				  		   	
				  		  if (i == len-1){
				  			  for(int j = 0; j<36; j++){
					  		   	 course[j].setText(coursenew[j]);
						   			}
				  		  }
			  		   }
			  		 
			  		 handlerGOODThread.sendEmptyMessage(0);
			  		 
			  			} catch (Exception ioe) {
		  		    	 //donothing
		  		    	}
		  	} else { }
	     } catch (Exception ioe) {
	    	 
	     }
	 }
 
 private void runLongProcessBNM() {

	  try {
		  	 boolean sec = true;
		  		Document doc = null;
		  		
		  	    try {
		  	    		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		  	     	   	DocumentBuilder db = dbf.newDocumentBuilder();
		  	     	   	
		  	     	   	SimpleDateFormat dateis = new SimpleDateFormat("dd.MM.yyyy"); // "yyyyMMdd_HHmmss"
		  	     	   	String curentDate = dateis.format(new Date());
		  	     	   	String uri = "http://www.bnm.md/md/official_exchange_rates?get_xml=1&date="+curentDate;
		  	     	   	
		  	     	   	URL url = new URL(uri);
		  	     	   	
		  	     	   	doc = db.parse(new InputSource(url.openStream()));
		  		     } catch (Exception ioe) {
		  		    	 sec = false;
		  		    	handlerERRThread.sendEmptyMessage(0);
		  		    	 }
		  	if (sec == true){
		  		try {
			  		NodeList charlist = doc.getElementsByTagName("CharCode");
			  		NodeList nomlist = doc.getElementsByTagName("Nominal");
			  		NodeList list = doc.getElementsByTagName("Value");
			  		   int len = list.getLength();
			  		
			  		   String[] sas = { "USD", "EUR", "CHF", "GBP", "JPY", "UAH", "RUB", "MDL", "BYR", "PLN", "LTL", "LVL", "AZN", "AUD", "AMD", "BGN", "BRL", "HUF", "DKK", "INR", "KZT", "CAD", "KGS", "CNY", "NOK", "RON", "XDR", "SGD", "TJS", "TRY", "TMT", "UZS", "CZK", "SEK", "ZAR", "KRW" };
			  		   String[] coursenew = new String[36];
			  		   for(int i = 0; i<36; i++){coursenew[i] = "0";}
			  		   
			  		   for(int i = 0; i<len; i++)
			  		   {
			  			   /* ID */
			  			   	Node ch= charlist.item(i);
			  			    ch.getNodeValue();
			  		   		ch.getFirstChild().getNodeValue();
			  		   		String chStr  = ch.getFirstChild().getNodeValue();
			  		   		int[] chpos = new int[36];
			  		   		for(int j = 0; j<36; j++){chpos[j] = 7777;}
			  		   		for(int j = 0; j<36; j++){
			  		   			if (chStr.equals(sas[j])) {
			  		   			chpos[j] = i;
			  		   			}
			  		   		}
			  		   		
			  		   		/* rate */
				  		   	Node r= nomlist.item(i);
			  			    r.getNodeValue();
			  		   		r.getFirstChild().getNodeValue();
			  		   		String nStr  = r.getFirstChild().getNodeValue();
			  		   		int[] nd = new int[36];
			  		   		for(int j = 0; j<36; j++){nd[j] = 1;}
			  		   		for(int j = 0; j<36; j++){
			  		   			if (chStr.equals(sas[j])) {
			  		   			nd[j] = Integer.parseInt(nStr);
			  		   			}
			  		   		}
			  			   
			  			   /* data */
			  			    Node n= list.item(i);
			  		   		n.getNodeValue();
			  		   		n.getFirstChild().getNodeValue();
			  		   		String dateCurrencyStr  = n.getFirstChild().getNodeValue();
		  		   		
			  		   		coursenew[7] = "1.00";
			  		   		
			  		   		double coursetrue;
			  		   		
				  		   	for(int j = 0; j<36; j++){
				  		   	if(i == chpos[j])	{coursenew[j] = dateCurrencyStr.replace(",", ".");
					   			coursetrue = ( Double.parseDouble(coursenew[j]) / nd[j] );
					   			coursenew[j] = (Double.toString(coursetrue));
					   			}
			  		   		}
				  		   	
				  		  if (i == len-1){
				  			  for(int j = 0; j<36; j++){
					  		   	 course[j].setText(coursenew[j]);
						   			}
				  		  }
			  		   }
			  		 
			  		 handlerGOODThread.sendEmptyMessage(0);
			  		 
			  			} catch (Exception ioe) {
		  		    	 //donothing
		  		    	}
		  	} else { }
	     } catch (Exception ioe) {
	    	 
	     }
	 }
 
 private void runLongProcessNBRB() {

	  try {
		  	 boolean sec = true;
		  		Document doc = null;
		  		
		  	    try {
		  	    		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		  	     	   	DocumentBuilder db = dbf.newDocumentBuilder();	
		  	     	   	URL url = new URL("http://www.nbrb.by/Services/XmlExRates.aspx");
		  	     	   	doc = db.parse(new InputSource(url.openStream()));
		  		     } catch (Exception ioe) {
		  		    	 sec = false;
		  		    	handlerERRThread.sendEmptyMessage(0);
		  		    	 }
		  	if (sec == true){
		  		try {
			  		NodeList charlist = doc.getElementsByTagName("CharCode");
			  		NodeList nomlist = doc.getElementsByTagName("Scale");
			  		NodeList list = doc.getElementsByTagName("Rate");
			  		   int len = list.getLength();
			  		
			  		   String[] sas = { "USD", "EUR", "CHF", "GBP", "JPY", "UAH", "RUB", "MDL", "BYR", "PLN", "LTL", "LVL", "AZN", "AUD", "AMD", "BGN", "BRL", "HUF", "DKK", "INR", "KZT", "CAD", "KGS", "CNY", "NOK", "RON", "XDR", "SGD", "TJS", "TRY", "TMT", "UZS", "CZK", "SEK", "ZAR", "KRW" };
			  		   String[] coursenew = new String[36];
			  		   for(int i = 0; i<36; i++){coursenew[i] = "0";}
			  		   
			  		   for(int i = 0; i<len; i++)
			  		   {
			  			   /* ID */
			  			   	Node ch= charlist.item(i);
			  			    ch.getNodeValue();
			  		   		ch.getFirstChild().getNodeValue();
			  		   		String chStr  = ch.getFirstChild().getNodeValue();
			  		   		int[] chpos = new int[36];
			  		   		for(int j = 0; j<36; j++){chpos[j] = 7777;}
			  		   		for(int j = 0; j<36; j++){
			  		   			if (chStr.equals(sas[j])) {
			  		   			chpos[j] = i;
			  		   			}
			  		   		}
			  		   		
			  		   		/* rate */
				  		   	Node r= nomlist.item(i);
			  			    r.getNodeValue();
			  		   		r.getFirstChild().getNodeValue();
			  		   		String nStr  = r.getFirstChild().getNodeValue();
			  		   		int[] nd = new int[36];
			  		   		for(int j = 0; j<36; j++){nd[j] = 1;}
			  		   		for(int j = 0; j<36; j++){
			  		   			if (chStr.equals(sas[j])) {
			  		   			nd[j] = Integer.parseInt(nStr);
			  		   			}
			  		   		}
			  			   
			  			   /* data */
			  			    Node n= list.item(i);
			  		   		n.getNodeValue();
			  		   		n.getFirstChild().getNodeValue();
			  		   		String dateCurrencyStr  = n.getFirstChild().getNodeValue();
		  		   		
			  		   		coursenew[8] = "1.00";
			  		   		
			  		   		double coursetrue;
			  		   		
				  		   	for(int j = 0; j<36; j++){
				  		   	if(i == chpos[j])	{coursenew[j] = dateCurrencyStr.replace(",", ".");
					   			coursetrue = ( Double.parseDouble(coursenew[j]) / nd[j] );
					   			coursenew[j] = (Double.toString(coursetrue));
					   			}
			  		   		}
				  		   	
				  		  if (i == len-1){
				  			  for(int j = 0; j<36; j++){
					  		   	 course[j].setText(coursenew[j]);
						   			}
				  		  }
			  		   }
			  		 
			  		 handlerGOODThread.sendEmptyMessage(0);
			  		 
			  			} catch (Exception ioe) {
		  		    	 //donothing
		  		    	}
		  	} else { }
		  	// Thread.sleep(1*1000);
	     } catch (Exception ioe) {
	    	 
	     }
	 }
 
 private void runLongProcessAZ() {

	  try {
		  	 boolean sec = true;
		  		Document doc = null;
		  		
		  	    try {
		  	    		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		  	     	   	DocumentBuilder db = dbf.newDocumentBuilder();
		  	     	   	
		  	     	   	SimpleDateFormat dateis = new SimpleDateFormat("dd.MM.yyyy"); // "yyyyMMdd_HHmmss"
		  	     	   	String curentDate = dateis.format(new Date());
		  	     	   	String uri = "http://www.cbar.az/currencies/"+curentDate+".xml";

		  	     	   	URL url = new URL(uri);
		  	     	   	
		  	     	   	doc = db.parse(new InputSource(url.openStream()));
		  		     } catch (Exception ioe) {
		  		    	 sec = false;
		  		    	handlerERRThread.sendEmptyMessage(0);
		  		    	 }
		  	if (sec == true){
		  		try {
			  		NodeList charlist = doc.getElementsByTagName("Valute");
			  		NodeList nomlist = doc.getElementsByTagName("Nominal");
			  		NodeList list = doc.getElementsByTagName("Value");
			  		   int len = list.getLength();

			  		   String[] sas = { "USD", "EUR", "CHF", "GBP", "JPY", "UAH", "RUR", "MDL", "BYR", "PLN", "LTL", "LVL", "AZN", "AUD", "AMD", "BGN", "BRL", "HUF", "DKK", "INR", "KZT", "CAD", "KGS", "CNY", "NOK", "RON", "XDR", "SGD", "TJS", "TRY", "TMT", "UZS", "CZK", "SEK", "ZAR", "KRW" };
			  		   String[] coursenew = new String[36];
			  		   for(int i = 0; i<36; i++){coursenew[i] = "0";}

			  		   for(int i = 0; i<len; i++)
			  		   {
			  			   /* ID */
			  			   	Node ch= charlist.item(i);
			  			    ch.getNodeValue();
			  			    ch.getFirstChild().getNodeValue();
			  			    String chStr = ch.getAttributes().getNamedItem("Code").getNodeValue();
			  		   		int[] chpos = new int[36];
			  		   		for(int j = 0; j<36; j++){chpos[j] = 7777;}
			  		   		for(int j = 0; j<36; j++){
			  		   			if (chStr.equals(sas[j])) {
			  		   			chpos[j] = i;
			  		   			}
			  		   		}
			  		   		
			  		   		/* rate */
				  		   	Node r= nomlist.item(i);
			  			    r.getNodeValue();
			  		   		r.getFirstChild().getNodeValue();
			  		   		String nStr  = r.getFirstChild().getNodeValue();
			  		   		int[] nd = new int[36];
			  		   		for(int j = 0; j<36; j++){nd[j] = 1;}
			  		   		for(int j = 0; j<36; j++){
			  		   			if (chStr.equals(sas[j])) {
			  		   			nd[j] = Integer.parseInt(nStr);
			  		   			}
			  		   		}
			  			   
			  			   /* data */
			  			    Node n= list.item(i);
			  		   		n.getNodeValue();
			  		   		n.getFirstChild().getNodeValue();
			  		   		String dateCurrencyStr  = n.getFirstChild().getNodeValue();
		  		   		
			  		   		coursenew[12] = "1.00";
			  		   		
			  		   		double coursetrue;
			  		   		
				  		   	for(int j = 0; j<36; j++){
				  		   	if(i == chpos[j])	{coursenew[j] = dateCurrencyStr.replace(",", ".");
					   			coursetrue = ( Double.parseDouble(coursenew[j]) / nd[j] );
					   			coursenew[j] = (Double.toString(coursetrue));
					   			}
			  		   		}
				  		   	
				  		  if (i == len-1){
				  			  for(int j = 0; j<36; j++){
					  		   	 course[j].setText(coursenew[j]);
						   			}
				  		  }
			  		   }
			  		 
			  		 handlerGOODThread.sendEmptyMessage(0);
			  		 
			  			} catch (Exception ioe) {
		  		    	 //donothing
		  		    	}
		  	} else {}
	     } catch (Exception ioe) {
	    	 
	     }
	 }
 
 private void runLongProcessECB() {
	  try {
		  	 boolean sec = true;
		  		Document doc = null;
		  		
		  	    try {
		  	    		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		  	     	   	DocumentBuilder db = dbf.newDocumentBuilder();
		  	     	   	String uri = "http://www.ecb.int/stats/eurofxref/eurofxref-daily.xml";

		  	     	   	URL url = new URL(uri);
		  	     	   	
		  	     	   	doc = db.parse(new InputSource(url.openStream()));
		  		     } catch (Exception ioe) {
		  		    	 sec = false;
		  		    	handlerERRThread.sendEmptyMessage(0);
		  		    	 }
		  	if (sec == true){
		  		try {
			  		NodeList list = doc.getElementsByTagName("Cube");
			  		int len = list.getLength()-2;
			  		
			  		   String[] sas = { "USD", "EUR", "CHF", "GBP", "JPY", "UAH", "RUB", "MDL", "BYR", "PLN", "LTL", "LVL", "AZN", "AUD", "AMD", "BGN", "BRL", "HUF", "DKK", "INR", "KZT", "CAD", "KGS", "CNY", "NOK", "RON", "XDR", "SGD", "TJS", "TRY", "TMT", "UZS", "CZK", "SEK", "ZAR", "KRW" };
			  		   String[] coursenew = new String[36];
			  		   for(int i = 0; i<36; i++){coursenew[i] = "0";}

			  		   for(int i = 0; i<len; i++)
			  		   {
			  			   /* ID */
			  			    String chStr = list.item(i+2).getAttributes().getNamedItem("currency").getNodeValue();
			  		   		int[] chpos = new int[36];
			  		   		for(int j = 0; j<36; j++){chpos[j] = 7777;}
			  		   		for(int j = 0; j<36; j++){
			  		   			if (chStr.equals(sas[j])) {
			  		   			chpos[j] = i;
			  		   			}
			  		   		}
			  		   		
			  		   		/* rate */
			  		   		int[] nd = new int[36];
			  		   		for(int j = 0; j<36; j++){nd[j] = 1;}
			  			   
			  			   /* data */
			  			    String dateCurrencyStr = list.item(i+2).getAttributes().getNamedItem("rate").getNodeValue();

			  		   		coursenew[1] = "1.00";

			  		   		double coursetrue;
			  		   		
				  		   	for(int j = 0; j<36; j++){
				  		   	if(i == chpos[j])	{coursenew[j] = dateCurrencyStr.replace(",", ".");
					   			coursetrue = ( 1/Double.parseDouble(coursenew[j]) );
					   			coursenew[j] = (Double.toString(coursetrue));
					   			}
			  		   		}
				  		   	
				  		  if (i == len-1){
				  			  for(int j = 0; j<36; j++){
					  		   	 course[j].setText(coursenew[j]);
						   			}
				  		  }
			  		   }
			  		 
			  		 handlerGOODThread.sendEmptyMessage(0);
			  		 
			  			} catch (Exception ioe) {
		  		    	 //donothing
		  		    	}
		  	} else {}
	     } catch (Exception ioe) {
	    	 
	     }
	 }
 
 private void runLongProcessFOREX() {

	  try {
		  	 boolean sec = true;
		  		Document doc = null;
		  		
		  	    try {
		  	    		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		  	     	   	DocumentBuilder db = dbf.newDocumentBuilder();	
		  	     	   	URL url = new URL("http://rss.timegenie.com/forex.xml");
		  	     	   	doc = db.parse(new InputSource(url.openStream()));
		  		     } catch (Exception ioe) {
		  		    	 sec = false;
		  		    	handlerERRThread.sendEmptyMessage(0);
		  		    	 }
		  	if (sec == true){
		  		try {
			  		NodeList charlist = doc.getElementsByTagName("code");
			  		NodeList list = doc.getElementsByTagName("rate");
			  		   int len = list.getLength();
			  		
			  		   String[] sas = { "USD", "EUR", "CHF", "GBP", "JPY", "UAH", "RUB", "MDL", "BYR", "PLN", "LTL", "LVL", "AZM", "AUD", "AMD", "BGN", "BRL", "HUF", "DKK", "INR", "KZT", "CAD", "KGS", "CNY", "NOK", "RON", "XDR", "SGD", "TJS", "TRY", "TMT", "UZS", "CZK", "SEK", "ZAR", "KRW" };
			  		   String[] coursenew = new String[36];
			  		   for(int i = 0; i<36; i++){coursenew[i] = "0";}
			  		   
			  		   for(int i = 0; i<len; i++)
			  		   {
			  			   /* ID */
			  			   	Node ch= charlist.item(i);
			  			    ch.getNodeValue();
			  		   		ch.getFirstChild().getNodeValue();
			  		   		String chStr  = ch.getFirstChild().getNodeValue();
			  		   		int[] chpos = new int[36];
			  		   		for(int j = 0; j<36; j++){chpos[j] = 7777;}
			  		   		for(int j = 0; j<36; j++){
			  		   			if (chStr.equals(sas[j])) {
			  		   			chpos[j] = i;
			  		   			}
			  		   		}
			  		   		
			  			   /* data */
			  			    Node n= list.item(i);
			  		   		n.getNodeValue();
			  		   		n.getFirstChild().getNodeValue();
			  		   		String dateCurrencyStr  = n.getFirstChild().getNodeValue();

			  		   		double coursetrue;

				  		   	for(int j = 0; j<36; j++){
				  		   	if(i == chpos[j])	{coursenew[j] = dateCurrencyStr.replace(",", ".");
					   			coursetrue = ( 1/Double.parseDouble(coursenew[j]) );
					   			coursenew[j] = (Double.toString(coursetrue));
					   			}
			  		   		}
				  		   	
				  		  if (i == len-1){
				  			  for(int j = 0; j<36; j++){
					  		   	 course[j].setText(coursenew[j]);
						   			}
				  		  }
			  		   }

			  		 handlerGOODThread.sendEmptyMessage(0);
			  		 
			  			} catch (Exception ioe) {
		  		    	 //donothing
		  		    	}
		  	} else { }
	     } catch (Exception ioe) {
	    	 
	     }
	 }
 
 
 /// Обработчик пересчета
 public void myClickHandler() {
	  int checkBank = Integer.parseInt(ListBankPreference);
	  course[36].setText(courserate[36].getText().toString());

	    for (int i=0;i<count;i++ ){
	    	int resID = getResources().getIdentifier("from" + sa[i],"id", getPackageName());
	    	from[i] = (RadioButton)findViewById(resID);
	    	}
	    
	    for (int i=0;i<count;i++ ){
	    	int resID = getResources().getIdentifier("to" + sa[i],"id", getPackageName());
	    	to[i] = (RadioButton)findViewById(resID);
	    	}
	    
         if (text.getText().length() == 0) {amountmoney.setText("0.00");return;} else
         if (text.getText().toString().equals("-")) {amountmoney.setText("0.00");return;} else
        	 
         if (checkBank != 1) {
        	 for (int i=0;i<count;i++ ){
        	 try {
        		if (from[i].isChecked()){	if ((i == 36) && (reverserates)){	curentfromcourserate = (1/Double.parseDouble(course[i].getText().toString()));
        			 				 } else if (to[36].isChecked()) {			curentfromcourserate = Double.parseDouble(courserate[i].getText().toString());
        			 				 } else {									curentfromcourserate = Double.parseDouble(course[i].getText().toString());}
        		}
        		 if (to[i].isChecked()){	if ((i == 36) && (reverserates)){	curenttocourserate = (1/Double.parseDouble(course[i].getText().toString()));
        			 				 } else if (from[36].isChecked()) {			curenttocourserate = Double.parseDouble(courserate[i].getText().toString());
		        			 		 } else {									curenttocourserate = Double.parseDouble(course[i].getText().toString());}
        		 }
          	 } catch (Exception ioe) {handlerERRdevnull.sendEmptyMessage(0);}
        	 }
        	 
        	 try {		BigDecimal x = new BigDecimal(Double.parseDouble(text.getText().toString()) * curentfromcourserate / curenttocourserate);
        	  		   	x = x.setScale(2, BigDecimal.ROUND_HALF_UP); // Точность округления расчетов при курсах от банка
        	  		   	amountmoney.setText(x.toString());
        	 } catch (Exception ioe) {handlerERRdevzero.sendEmptyMessage(0);}
        	 
         } else {
				for (int i = 0; i < count; i++) {
	        	try {
					if (from[i].isChecked()) {
						curentfromcourserate = Double.parseDouble(courserate[i].getText().toString());
					}
					if (to[i].isChecked()) {
						curenttocourserate = Double.parseDouble(courserate[i].getText().toString());
					}
	          	} catch (Exception ioe) {handlerERRdevnull.sendEmptyMessage(0);}
				}
				
	        	 if (reverserates){
		        		 try {	BigDecimal x = new BigDecimal(Double.parseDouble(text.getText().toString()) * curenttocourserate / curentfromcourserate);
				  		   		x = x.setScale(2, BigDecimal.ROUND_HALF_UP); // Точность округления расчетов при курсах от пользователя
				  		   		amountmoney.setText(x.toString());
		        		 } catch (Exception ioe) {handlerERRdevzero.sendEmptyMessage(0);}
				  } else {
					  try {	BigDecimal x = new BigDecimal(Double.parseDouble(text.getText().toString()) * curentfromcourserate / curenttocourserate);
			  		   		x = x.setScale(2, BigDecimal.ROUND_HALF_UP); // Точность округления расчетов при курсах от пользователя
			  		   		amountmoney.setText(x.toString());
		        	 	} catch (Exception ioe) {handlerERRdevzero.sendEmptyMessage(0);}
				  }
         }
     }
 
 /* Обработчик обновления */
 public void myClickHandler2(View view) {
     switch (view.getId()) {
     case R.id.button2:
 		NanoConverter.mContext.processThread();
     }
 }
 
  public void TurnONrates() {
	  for (int i=0;i<count;i++ ){
		  courserate[i].setKeyListener(null);
		  }
	    buttonrefresh.setEnabled(true);
	    courserate[36].setKeyListener(course[0].getKeyListener());
}
  
  public void TurnOFFrates() {
	  for (int i=0;i<count;i++ ){
		  courserate[i].setKeyListener(course[0].getKeyListener());
		  }
	    buttonrefresh.setEnabled(false);
  }
  
private void getPrefs() {

             SharedPreferences prefs = PreferenceManager
                             .getDefaultSharedPreferences(getBaseContext());
             
        	 for (int i=0;i<count;i++ ){
        	    	boolean resID = prefs.getBoolean("checkbox"+sa[i], true);
        	    	mactive[i] = resID;
        	 }

            ListCurPreference = prefs.getString("listCurByDefault", "0");
            ListBankPreference = prefs.getString("listSourcesDefault", "0");
            listUpdate = prefs.getString("listUpdate", "0");
            reverserates = prefs.getBoolean("revratesswitch", false);

            String bkgr = prefs.getString("bkgcheckbox", "0");
            View maintabhost = findViewById(android.R.id.tabhost);
            View scrl1 = findViewById(R.id.scroll1);
            View scrl2 = findViewById(R.id.scroll2);
            
            if (bkgr.equals("0")){
            	maintabhost.setBackgroundColor(Color.BLACK);
                scrl1.setBackgroundDrawable(getResources().getDrawable(R.drawable.bkgb));scrl2.setBackgroundDrawable(getResources().getDrawable(R.drawable.bkgb));} else
            if (bkgr.equals("1")){
            	maintabhost.setBackgroundColor(Color.BLACK);
            	scrl1.setBackgroundDrawable(null);scrl2.setBackgroundDrawable(null);} else
            if (bkgr.equals("2")){
            	maintabhost.setBackgroundColor(Color.DKGRAY);
            	scrl1.setBackgroundDrawable(null);scrl2.setBackgroundDrawable(null);} else
            if (bkgr.equals("3")){
            	maintabhost.setBackgroundDrawable(getWallpaper());
            	scrl1.setBackgroundDrawable(null);scrl2.setBackgroundDrawable(null);} else
            if (bkgr.equals("4")){
            	maintabhost.setBackgroundDrawable(getResources().getDrawable(R.drawable.drr));
                scrl1.setBackgroundDrawable(null);scrl2.setBackgroundDrawable(null);} else
            if (bkgr.equals("5")){
            	maintabhost.setBackgroundDrawable(getResources().getDrawable(R.drawable.dgr));
                scrl1.setBackgroundDrawable(null);scrl2.setBackgroundDrawable(null);} else
            if (bkgr.equals("6")){
            	maintabhost.setBackgroundDrawable(getResources().getDrawable(R.drawable.ggr));
                scrl1.setBackgroundDrawable(null);scrl2.setBackgroundDrawable(null);} else
            if (bkgr.equals("7")){
            	maintabhost.setBackgroundDrawable(getResources().getDrawable(R.drawable.gdr));
            	scrl1.setBackgroundDrawable(null);scrl2.setBackgroundDrawable(null);} else
            if (bkgr.equals("8")){
            	maintabhost.setBackgroundDrawable(getResources().getDrawable(R.drawable.bkgmain));
            	scrl1.setBackgroundDrawable(null);scrl2.setBackgroundDrawable(null);} else
            if (bkgr.equals("9")){
                maintabhost.setBackgroundDrawable(getResources().getDrawable(R.drawable.flower));
                scrl1.setBackgroundDrawable(null);scrl2.setBackgroundDrawable(null);
                }
}
    
  public void UpdateRates() {
	  int checkBank = Integer.parseInt(ListBankPreference);
	  int checkCurd = Integer.parseInt(ListCurPreference);
	  BigDecimal y= new BigDecimal(0);

	  if (checkBank == 1) {
		  for (int j=0;j<count;j++){from[j].setEnabled(true);to[j].setEnabled(true);courserate[j].setEnabled(true);}
	  } else {
		  zerocheck();

		  for (int i=0;i<count;i++ ){
			  if (checkCurd == i && Double.parseDouble(course[i].getText().toString()) != 0){coursebydefaultis = course[i].getText().toString();}
			  if (checkCurd == i && Double.parseDouble(course[i].getText().toString()) == 0){{Toast toast2 = Toast.makeText(getApplicationContext(), getString(R.string.zerocheck), Toast.LENGTH_LONG);toast2.show();coursebydefaultis = course[0].getText().toString();}}
			  }

		  for (int i=0;i<count;i++ ){
			  if (i != 36){
			  if (reverserates){
				  	if (Float.parseFloat(course[i].getText().toString())!=0){
					y = new BigDecimal(Double.parseDouble(coursebydefaultis)/Double.parseDouble(course[i].getText().toString()));} else {y = new BigDecimal(0);}
			  } else {
					y = new BigDecimal(Double.parseDouble(course[i].getText().toString())/Double.parseDouble(coursebydefaultis));}

	  		  y = y.setScale(4, BigDecimal.ROUND_HALF_UP);  // Точность округления вкладки курсы
			  courserate[i].setText(y.toString());
			  }}
	  }
  }
  public void zerocheck() {
	  for (int j=0;j<count;j++){
		  try {
          if (Double.parseDouble(course[j].getText().toString()) == 0){
          	from[j].setEnabled(false);
          	to[j].setEnabled(false);
          	courserate[j].setEnabled(false);
          	from[36].setEnabled(true);to[36].setEnabled(true);courserate[36].setEnabled(true);
  		 } else {
  			from[j].setEnabled(true);to[j].setEnabled(true);courserate[j].setEnabled(true);
  		 }} catch (Exception ioe) {handlerERRdevnull.sendEmptyMessage(0);}
          }
  }
}