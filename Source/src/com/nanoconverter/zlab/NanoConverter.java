package com.nanoconverter.zlab;

import java.math.BigDecimal;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
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
	
	int count = 12;
	
	public String[] sa = { "USD", "EUR", "CHF", "GBP", "JPY", "UAH", "RUB", "MDL", "BYR", "PLN", "LTL", "LVL" };
	public EditText[] course = new EditText[count];
	public EditText[] courserate = new EditText[count];
	public RadioButton[] from = new RadioButton[count];
	public RadioButton[] to = new RadioButton[count];
	public String moneyupdatestr;
	public String[] moneycourse = new String[count];
	public LinearLayout[] moneycl = new LinearLayout[count];
	public View[] moneycls = new View[count];
	public String coursebydefaultis = "1";
	public boolean[] mactive = new boolean[count];
	
	public SharedPreferences settings_money;
	public SharedPreferences.Editor moneyeditor;
	
	private Button buttonrefresh;
	
	public String curentfromcourserate = "1.00";
	public String curenttocourserate = "1.00";

	public static String ListCurPreference,ListBankPreference,listUpdate;
	 	
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

     settings_money = getSharedPreferences(moneyupdatestr, 0);
     SharedPreferences settingsm = getSharedPreferences(moneyupdatestr, 0);
     String[] separated = settingsm.getString(moneyupdatestr, "7777").split(",");
     if (separated[0].equals("7777") ){} else {
     for (int i=0;i<count;i++ ){
    	 course[i].setText(separated[i]);
     }}
     
     UpdateRates();
     myClickHandler();
     
     int checkBank = Integer.parseInt(ListBankPreference);
     int checkUPDT = Integer.parseInt(listUpdate);

	/* autoupdate */
	      if (checkBank != 1){
	    	  if (checkUPDT != 0){
			      if (checkUPDT == 2){
			    	  NanoConverter.mContext.processThreadforce();
			      } else {
			    	  NanoConverter.mContext.processThread();}
			    	  }
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
	      if (checkBank == 4){ BANK_ID = "BNM";  TurnONrates();}

	      if (checkBank == 1){
	      } else {
	     	  	 UpdateRates();
	     	  	 }
	      
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
    
    settings_money = getSharedPreferences(moneyupdatestr, 0);
    moneyeditor = settings_money.edit();
    
    int checkBank = Integer.parseInt(ListBankPreference);
	
    if (checkBank == 1) {
    	for (int i=0;i<count;i++ ){
    		course[i].setText(courserate[i].getText().toString());
        	}
    }
    
    StringBuilder sb = new StringBuilder();
    for (int i = 0; i < count; i++) {
        sb.append(course[i].getText().toString()).append(",");
        }
    
		moneyeditor.putString(moneyupdatestr, sb.toString());
		moneyeditor.commit();
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
	  		
	  		   String[] sas = { "USD", "EUR", "CHF", "GBP", "JPY", "UAH", "RUB", "MDL", "BYR", "PLN", "LTL", "LVL" };
	  		   String[] coursenew = new String[12];
	  		   for(int i = 0; i<12; i++){coursenew[i] = "0";}
	  		   
	  		   for(int i = 0; i<len; i++)
	  		   {
	  			   /* ID */
	  			   	Node ch= charlist.item(i);
	  			    ch.getNodeValue();
	  		   		ch.getFirstChild().getNodeValue();
	  		   		String chStr  = ch.getFirstChild().getNodeValue();
	  		   		int[] chpos = new int[12];
	  		   		for(int j = 0; j<12; j++){
	  		   			if (chStr.equals(sas[j])) {
	  		   			chpos[j] = i;
	  		   			}
	  		   		}
	  		   		
	  		   		/* rate */
		  		   	Node r= nomlist.item(i);
	  			    r.getNodeValue();
	  		   		r.getFirstChild().getNodeValue();
	  		   		String nStr  = r.getFirstChild().getNodeValue();
	  		   		int[] nd = new int[12];
	  		   		for(int j = 0; j<12; j++){
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
	  		   		
		  		   	for(int j = 0; j<12; j++){
		  		   	if(i == chpos[j])	{coursenew[j] = dateCurrencyStr.replace(",", ".");
			   			coursetrue = ( Double.parseDouble(coursenew[j]) / nd[j] );
			   			coursenew[j] = (Double.toString(coursetrue));
			   			}
	  		   		}
		  		   	
		  		  if (i == len-1){
		  			  for(int j = 0; j<12; j++){
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
			  		
			  		   String[] sas = { "USD", "EUR", "CHF", "GBP", "JPY", "UAH", "RUB", "MDL", "BYR", "PLN", "LTL", "LVL" };
			  		   String[] coursenew = new String[12];
			  		   for(int i = 0; i<12; i++){coursenew[i] = "0";}
			  		   
			  		   for(int i = 0; i<len; i++)
			  		   {
			  			   /* ID */
			  			   	Node ch= charlist.item(i);
			  			    ch.getNodeValue();
			  		   		ch.getFirstChild().getNodeValue();
			  		   		String chStr  = ch.getFirstChild().getNodeValue();
			  		   		int[] chpos = new int[12];
			  		   		for(int j = 0; j<12; j++){
			  		   			if (chStr.equals(sas[j])) {
			  		   			chpos[j] = i;
			  		   			}
			  		   		}
			  		   		
			  		   		/* rate */
				  		   	Node r= nomlist.item(i);
			  			    r.getNodeValue();
			  		   		r.getFirstChild().getNodeValue();
			  		   		String nStr  = r.getFirstChild().getNodeValue();
			  		   		int[] nd = new int[12];
			  		   		for(int j = 0; j<12; j++){
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
			  		   		
				  		   	for(int j = 0; j<12; j++){
				  		   	if(i == chpos[j])	{coursenew[j] = dateCurrencyStr.replace(",", ".");
					   			coursetrue = ( Double.parseDouble(coursenew[j]) / nd[j] );
					   			coursenew[j] = (Double.toString(coursetrue));
					   			}
			  		   		}
				  		   	
				  		  if (i == len-1){
				  			  for(int j = 0; j<12; j++){
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
			  		
			  		   String[] sas = { "USD", "EUR", "CHF", "GBP", "JPY", "UAH", "RUB", "MDL", "BYR", "PLN", "LTL", "LVL" };
			  		   String[] coursenew = new String[12];
			  		   for(int i = 0; i<12; i++){coursenew[i] = "0";}
			  		   
			  		   for(int i = 0; i<len; i++)
			  		   {
			  			   /* ID */
			  			   	Node ch= charlist.item(i);
			  			    ch.getNodeValue();
			  		   		ch.getFirstChild().getNodeValue();
			  		   		String chStr  = ch.getFirstChild().getNodeValue();
			  		   		int[] chpos = new int[12];
			  		   		for(int j = 0; j<12; j++){
			  		   			if (chStr.equals(sas[j])) {
			  		   			chpos[j] = i;
			  		   			}
			  		   		}
			  		   		
			  		   		/* rate */
				  		   	Node r= nomlist.item(i);
			  			    r.getNodeValue();
			  		   		r.getFirstChild().getNodeValue();
			  		   		String nStr  = r.getFirstChild().getNodeValue();
			  		   		int[] nd = new int[12];
			  		   		for(int j = 0; j<12; j++){
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
			  		   		
				  		   	for(int j = 0; j<12; j++){
				  		   	if(i == chpos[j])	{coursenew[j] = dateCurrencyStr.replace(",", ".");
					   			coursetrue = ( Double.parseDouble(coursenew[j]) / nd[j] );
					   			coursenew[j] = (Double.toString(coursetrue));
					   			}
			  		   		}
				  		   	
				  		  if (i == len-1){
				  			  for(int j = 0; j<12; j++){
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
			  		
			  		   String[] sas = { "USD", "EUR", "CHF", "GBP", "JPY", "UAH", "RUB", "MDL", "BYR", "PLN", "LTL", "LVL" };
			  		   String[] coursenew = new String[12];
			  		   for(int i = 0; i<12; i++){coursenew[i] = "0";}
			  		   
			  		   for(int i = 0; i<len; i++)
			  		   {
			  			   /* ID */
			  			   	Node ch= charlist.item(i);
			  			    ch.getNodeValue();
			  		   		ch.getFirstChild().getNodeValue();
			  		   		String chStr  = ch.getFirstChild().getNodeValue();
			  		   		int[] chpos = new int[12];
			  		   		for(int j = 0; j<12; j++){
			  		   			if (chStr.equals(sas[j])) {
			  		   			chpos[j] = i;
			  		   			}
			  		   		}
			  		   		
			  		   		/* rate */
				  		   	Node r= nomlist.item(i);
			  			    r.getNodeValue();
			  		   		r.getFirstChild().getNodeValue();
			  		   		String nStr  = r.getFirstChild().getNodeValue();
			  		   		int[] nd = new int[12];
			  		   		for(int j = 0; j<12; j++){
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
			  		   		
				  		   	for(int j = 0; j<12; j++){
				  		   	if(i == chpos[j])	{coursenew[j] = dateCurrencyStr.replace(",", ".");
					   			coursetrue = ( Double.parseDouble(coursenew[j]) / nd[j] );
					   			coursenew[j] = (Double.toString(coursetrue));
					   			}
			  		   		}
				  		   	
				  		  if (i == len-1){
				  			  for(int j = 0; j<12; j++){
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
 
 /// Обработчик пересчета
 public void myClickHandler() {
	  int checkBank = Integer.parseInt(ListBankPreference);

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
        		 
        		 if (from[i].isChecked()){
        			 curentfromcourserate = course[i].getText().toString();
        			 }
        		 if (to[i].isChecked()){
        			 curenttocourserate = course[i].getText().toString();}
        	 }
        		   	
	  		   		BigDecimal x = new BigDecimal(Double.parseDouble(text.getText().toString()) * Double.parseDouble(curentfromcourserate) / Double.parseDouble(curenttocourserate));
	  		   		x = x.setScale(2, BigDecimal.ROUND_HALF_UP); // Точность округления расчетов при курсах от банка
	  		   		amountmoney.setText(x.toString());
         } else {
        	 
        	 	for (int i=0;i<count;i++ ){
        		 
        		 if (from[i].isChecked()){
        			 curentfromcourserate = courserate[i].getText().toString();
        			 }
        		 if (to[i].isChecked()){
        			 curenttocourserate = courserate[i].getText().toString();}
        	 }
	        	 	BigDecimal x = new BigDecimal(Double.parseDouble(text.getText().toString()) * Double.parseDouble(curentfromcourserate) / Double.parseDouble(curenttocourserate));
	  		   		x = x.setScale(2, BigDecimal.ROUND_HALF_UP); // Точность округления расчетов при курсах от пользователя
	  		   		amountmoney.setText(x.toString());
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
	    
	    UpdateRates();
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
            
            String bkgr = prefs.getString("bkgcheckbox", "0");
            View maintabhost = findViewById(android.R.id.tabhost);
            View scrl1 = findViewById(R.id.scroll1);
            View scrl2 = findViewById(R.id.scroll2);
            
            if (bkgr.equals("0")){
            	maintabhost.setBackgroundColor(Color.BLACK);
                scrl1.setBackgroundDrawable(getResources().getDrawable(R.drawable.bkgb));scrl2.setBackgroundDrawable(getResources().getDrawable(R.drawable.bkgb));
                } else
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
            	scrl1.setBackgroundDrawable(null);scrl2.setBackgroundDrawable(null);
                }
}
    
  public void UpdateRates() {
	  int checkBank = Integer.parseInt(ListBankPreference);
	  int checkCurd = Integer.parseInt(ListCurPreference);
	  if (checkBank == 1) {} else {

		  for (int i=0;i<count;i++ ){
			  if (checkCurd == i){coursebydefaultis = course[i].getText().toString();}
			  }
		  for (int i=0;i<count;i++ ){
			  BigDecimal y = new BigDecimal(Double.parseDouble(course[i].getText().toString()) / Double.parseDouble(coursebydefaultis));
	  		  y = y.setScale(4, BigDecimal.ROUND_HALF_UP);  // Точность округления вкладки курсы
			  courserate[i].setText(y.toString());
			  }
	  }
  }
}