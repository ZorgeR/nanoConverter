package com.nanoconverter.zlab;

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
	public String[] sa = { "USD", "EUR", "CHF", "GBP", "JPY", "UA", "RUB", "MDL", "BYR", "PLN", "LTL", "LVL" };
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

	Handler handlerUSD = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String USD = (String) msg.obj;
			course[0].setText(USD);
			/* call setText here */}
	};

	Handler handlerEUR = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String EUR = (String) msg.obj;
			course[1].setText(EUR);
		}
	};

	Handler handlerCHF = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String CHF = (String) msg.obj;
			course[2].setText(CHF);
		}
	};
	Handler handlerGBP = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String GBP = (String) msg.obj;
			course[3].setText(GBP);
		}
	};
	Handler handlerJPY = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String JPY = (String) msg.obj;
			course[4].setText(JPY);
		}
	};
	Handler handlerUA = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String UA = (String) msg.obj;
			course[5].setText(UA);
		}
	};
	Handler handlerMDL = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String MDL = (String) msg.obj;
			course[7].setText(MDL);
		}
	};
	Handler handlerBYR = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String BYR = (String) msg.obj;
			course[8].setText(BYR);
		}
	};
	Handler handlerPLN = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String PLN = (String) msg.obj;
			course[9].setText(PLN);
		}
	};
	Handler handlerLTL = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String LTL = (String) msg.obj;
			course[10].setText(LTL);
		}
	};
	Handler handlerLVL = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String LVL = (String) msg.obj;
			course[11].setText(LVL);
		}
	};
	Handler handlerRUB = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			String RUB = (String) msg.obj;
			course[6].setText(RUB);

			UpdateRates();
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
	      if (checkBank == 0){ BANK_ID = "CBR"; TurnONrates();}
	      if (checkBank == 1){TurnOFFrates(); }
	      if (checkBank == 2){ BANK_ID = "NBU";  TurnONrates();}
	      if (checkBank == 3){ BANK_ID = "NBRB"; TurnONrates();}
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
	  		 NodeList list = doc.getElementsByTagName("Value");
	  		   int count = list.getLength();
	  		   for(int i = 0; i<count; i++)
	  		   {
	  		   		Node n= list.item(i);
	  		   		n.getNodeValue();
	  		   		n.getFirstChild();
	  		   		n.getFirstChild().getNodeValue();
	  		   		String dateCurrencyStr  = n.getFirstChild().getNodeValue();
	  		   		String courseUSDnew = "0";
	  		   		String courseEURnew = "0";
	  		   		String courseCHFnew = "0";
	  		   		String courseGBPnew = "0";
	  		   		String courseJPYnew = "0";
	  		   		String courseUAnew = "0";
	  		   		String courseRUBnew = "1.00";
	  		   		String courseMDLnew = "0";
	  		   		String courseBYRnew = "0";
	  		   		String coursePLNnew = "0";
	  		   		String courseLTLnew = "0";
	  		   		String courseLVLnew = "0";
	  		   		
	  		   		if(i == 9)	{courseUSDnew = dateCurrencyStr.replace(",", ".");}
		  		   	if(i == 10)	{courseEURnew = dateCurrencyStr.replace(",", ".");}
	 		   		if(i == 2)	{courseGBPnew = dateCurrencyStr.replace(",", ".");}
	 		   		if(i == 31)	{courseCHFnew = dateCurrencyStr.replace(",", ".");}
	 		   		if(i == 34)	{courseJPYnew = dateCurrencyStr.replace(",", ".");
	 					   			float courseJPYtrue = ( Float.parseFloat(courseJPYnew) / 100 );
	 					   			courseJPYnew = (Float.toString(courseJPYtrue));
	 					   			}
	 		   		if(i == 28)	{courseUAnew = dateCurrencyStr.replace(",", ".");
	 					   			float courseUAtrue  = ( Float.parseFloat(courseUAnew) / 10 );
	 					   			courseUAnew = (Float.toString(courseUAtrue));
	 		   						}
		 		   	if(i == 18)	{courseMDLnew = dateCurrencyStr.replace(",", ".");
						   			float courseMDLtrue = ( Float.parseFloat(courseMDLnew) / 10 );
						   			courseMDLnew = (Float.toString(courseMDLtrue));
						   			}
			 		if(i == 4)	{courseBYRnew = dateCurrencyStr.replace(",", ".");
						   			float courseBYRtrue = ( Float.parseFloat(courseBYRnew) / 10000 );
						   			courseBYRnew = (Float.toString(courseBYRtrue));
						   			}
			 		if(i == 16)	{courseLVLnew = dateCurrencyStr.replace(",", ".");}
			 		if(i == 17)	{courseLTLnew = dateCurrencyStr.replace(",", ".");}
			 		if(i == 20)	{coursePLNnew = dateCurrencyStr.replace(",", ".");
		   			float coursePLNtrue = ( Float.parseFloat(coursePLNnew) / 10 );
		   			coursePLNnew = (Float.toString(coursePLNtrue));
		   			}
 		   		
			 		if (coursePLNnew != "0"){
			  		   	 Message msg = new Message();
			             msg.obj = coursePLNnew;
			             handlerPLN.sendMessage(msg);
				   		}
			 		if (courseLTLnew != "0"){
			  		   	 Message msg = new Message();
			             msg.obj = courseLTLnew;
			             handlerLTL.sendMessage(msg);
				   		}
			 		if (courseLVLnew != "0"){
			  		   	 Message msg = new Message();
			             msg.obj = courseLVLnew;
			             handlerLVL.sendMessage(msg);
				   		}
			 		
	  		   		if (courseUSDnew != "0"){
			  		   	 Message msg = new Message();
			             msg.obj = courseUSDnew;
			             handlerUSD.sendMessage(msg);
	  		   			}
		  		   	if (courseEURnew != "0"){
			  		   	 Message msg = new Message();
			             msg.obj = courseEURnew;
			             handlerEUR.sendMessage(msg);
		  		   		}
	  		   		if (courseCHFnew != "0"){
			  		   	 Message msg = new Message();
			             msg.obj = courseCHFnew;
			             handlerCHF.sendMessage(msg);
				   		}
			  		if (courseGBPnew != "0"){
			  		   	 Message msg = new Message();
			             msg.obj = courseGBPnew;
			             handlerGBP.sendMessage(msg);
				   		}
			  		if (courseJPYnew != "0"){
			  		   	 Message msg = new Message();
			             msg.obj = courseJPYnew;
			             handlerJPY.sendMessage(msg);
				   		}
			  		if (courseUAnew != "0"){
			  		   	 Message msg = new Message();
			             msg.obj = courseUAnew;
			             handlerUA.sendMessage(msg);
				   		}
			  		if (courseMDLnew != "0"){
			  		   	 Message msg = new Message();
			             msg.obj = courseMDLnew;
			             handlerMDL.sendMessage(msg);
				   		}
			  		if (courseBYRnew != "0"){
			  		   	 Message msg = new Message();
			             msg.obj = courseBYRnew;
			             handlerBYR.sendMessage(msg);
				   		}
			  		if (courseRUBnew != "0"){
			  		   	 Message msg = new Message();
			             msg.obj = courseRUBnew;
			             handlerRUB.sendMessage(msg);
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
		  		 NodeList list = doc.getElementsByTagName("Value");
		  		   int count = list.getLength();
		  		   for(int i = 0; i<count; i++)
		  		   {
		  		   		Node n= list.item(i);
		  		   		n.getNodeValue();
		  		   		n.getFirstChild();
		  		   		n.getFirstChild().getNodeValue();
		  		   		String dateCurrencyStr  = n.getFirstChild().getNodeValue();
		  		   		String courseUSDnew = "0";
		  		   		String courseEURnew = "0";
		  		   		String courseCHFnew = "0";
		  		   		String courseGBPnew = "0";
		  		   		String courseJPYnew = "0";
		  		   		String courseRUBnew = "0";
		  		   		String courseUAnew = "1.00";
		  		   		String courseMDLnew = "0";
		  		   		String courseBYRnew = "0";
		  		   		String coursePLNnew = "0";
		  		   		String courseLTLnew = "0";
		  		   		String courseLVLnew = "0";
		  		   		
		  		   		if(i == 5)	{courseUSDnew = dateCurrencyStr;
							   			float courseUSDtrue = ( Float.parseFloat(courseUSDnew) / 100 );
							   			courseUSDnew = (Float.toString(courseUSDtrue));}
			  		   	if(i == 6)	{courseEURnew = dateCurrencyStr;
							   			float courseEURtrue = ( Float.parseFloat(courseEURnew) / 100 );
							   			courseEURnew = (Float.toString(courseEURtrue));}
		 		   		if(i == 2)	{courseGBPnew = dateCurrencyStr;
							   			float courseGBPtrue = ( Float.parseFloat(courseGBPnew) / 100 );
							   			courseGBPnew = (Float.toString(courseGBPtrue));}
		 		   		if(i == 24)	{courseCHFnew = dateCurrencyStr;
							   			float courseCHFtrue = ( Float.parseFloat(courseCHFnew) / 100 );
							   			courseCHFnew = (Float.toString(courseCHFtrue));}
		 		   		if(i == 26)	{courseJPYnew = dateCurrencyStr;
		 					   			float courseJPYtrue = ( Float.parseFloat(courseJPYnew) / 1000 );
		 					   			courseJPYnew = (Float.toString(courseJPYtrue));
		 					   			}
		 		   		if(i == 15)	{courseRUBnew = dateCurrencyStr;
							   			float courseRUBtrue  = ( Float.parseFloat(courseRUBnew) / 10 );
							   			courseRUBnew = (Float.toString(courseRUBtrue));
		 		   						}
			 		   	if(i == 12)	{courseMDLnew = dateCurrencyStr;
							   			float courseMDLtrue = ( Float.parseFloat(courseMDLnew) / 100 );
							   			courseMDLnew = (Float.toString(courseMDLtrue));
							   			}
				 		if(i == 3)	{courseBYRnew = dateCurrencyStr;
							   			float courseBYRtrue = ( Float.parseFloat(courseBYRnew) / 10 );
							   			courseBYRnew = (Float.toString(courseBYRtrue));
							   			}
				 		if(i == 10)	{courseLVLnew = dateCurrencyStr;
							   			float courseLVLtrue = ( Float.parseFloat(courseLVLnew) / 100 );
							   			courseLVLnew = (Float.toString(courseLVLtrue));
							   			}
						if(i == 11)	{courseLTLnew = dateCurrencyStr;
							   			float courseLTLtrue = ( Float.parseFloat(courseLTLnew) / 100 );
							   			courseLTLnew = (Float.toString(courseLTLtrue));
							   			}
				 		if(i == 14)	{coursePLNnew = dateCurrencyStr;
							   			float coursePLNtrue = ( Float.parseFloat(coursePLNnew) / 100 );
							   			coursePLNnew = (Float.toString(coursePLNtrue));
			   							}
	 		   		
				 		if (coursePLNnew != "0"){
				  		   	 Message msg = new Message();
				             msg.obj = coursePLNnew;
				             handlerPLN.sendMessage(msg);
					   		}
				 		if (courseLTLnew != "0"){
				  		   	 Message msg = new Message();
				             msg.obj = courseLTLnew;
				             handlerLTL.sendMessage(msg);
					   		}
				 		if (courseLVLnew != "0"){
				  		   	 Message msg = new Message();
				             msg.obj = courseLVLnew;
				             handlerLVL.sendMessage(msg);
					   		}
		  		   		if (courseUSDnew != "0"){
				  		   	 Message msg = new Message();
				             msg.obj = courseUSDnew;
				             handlerUSD.sendMessage(msg);
		  		   			}
			  		   	if (courseEURnew != "0"){
				  		   	 Message msg = new Message();
				             msg.obj = courseEURnew;
				             handlerEUR.sendMessage(msg);
			  		   		}
		  		   		if (courseCHFnew != "0"){
				  		   	 Message msg = new Message();
				             msg.obj = courseCHFnew;
				             handlerCHF.sendMessage(msg);
					   		}
				  		if (courseGBPnew != "0"){
				  		   	 Message msg = new Message();
				             msg.obj = courseGBPnew;
				             handlerGBP.sendMessage(msg);
					   		}
				  		if (courseJPYnew != "0"){
				  		   	 Message msg = new Message();
				             msg.obj = courseJPYnew;
				             handlerJPY.sendMessage(msg);
					   		}
				  		if (courseUAnew != "0"){
				  		   	 Message msg = new Message();
				             msg.obj = courseUAnew;
				             handlerUA.sendMessage(msg);
					   		}
				  		if (courseMDLnew != "0"){
				  		   	 Message msg = new Message();
				             msg.obj = courseMDLnew;
				             handlerMDL.sendMessage(msg);
					   		}
				  		if (courseBYRnew != "0"){
				  		   	 Message msg = new Message();
				             msg.obj = courseBYRnew;
				             handlerBYR.sendMessage(msg);
					   		}
				  		if (courseRUBnew != "0"){
				  		   	 Message msg = new Message();
				             msg.obj = courseRUBnew;
				             handlerRUB.sendMessage(msg);
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
		  		 NodeList list = doc.getElementsByTagName("Value");
		  		   int count = list.getLength();
		  		   for(int i = 0; i<count; i++)
		  		   {
		  		   		Node n= list.item(i);
		  		   		n.getNodeValue();
		  		   		n.getFirstChild();
		  		   		n.getFirstChild().getNodeValue();
		  		   		String dateCurrencyStr  = n.getFirstChild().getNodeValue();
		  		   		String courseUSDnew = "0";
		  		   		String courseEURnew = "0";
		  		   		String courseCHFnew = "0";
		  		   		String courseGBPnew = "0";
		  		   		String courseJPYnew = "0";
		  		   		String courseRUBnew = "0";
		  		   		String courseUAnew = "0";
		  		   		String courseMDLnew = "1.00";
		  		   		String courseBYRnew = "0";
		  		   		String coursePLNnew = "0";
		  		   		String courseLTLnew = "0";
		  		   		String courseLVLnew = "0";
		  		   		
		  		   		if(i == 1)	{courseUSDnew = dateCurrencyStr;
							   			float courseUSDtrue = ( Float.parseFloat(courseUSDnew) / 1 );
							   			courseUSDnew = (Float.toString(courseUSDtrue));}
			  		   	if(i == 0)	{courseEURnew = dateCurrencyStr;
							   			float courseEURtrue = ( Float.parseFloat(courseEURnew) / 1 );
							   			courseEURnew = (Float.toString(courseEURtrue));}
		 		   		if(i == 17)	{courseGBPnew = dateCurrencyStr;
							   			float courseGBPtrue = ( Float.parseFloat(courseGBPnew) / 1 );
							   			courseGBPnew = (Float.toString(courseGBPtrue));}
		 		   		if(i == 13)	{courseCHFnew = dateCurrencyStr;
							   			float courseCHFtrue = ( Float.parseFloat(courseCHFnew) / 1 );
							   			courseCHFnew = (Float.toString(courseCHFtrue));}
		 		   		if(i == 25)	{courseJPYnew = dateCurrencyStr;
		 					   			float courseJPYtrue = ( Float.parseFloat(courseJPYnew) / 100 );
		 					   			courseJPYnew = (Float.toString(courseJPYtrue));
		 					   			}
		 		   		if(i == 2)	{courseRUBnew = dateCurrencyStr;
							   			float courseRUBtrue  = ( Float.parseFloat(courseRUBnew) / 1 );
							   			courseRUBnew = (Float.toString(courseRUBtrue));
		 		   						}
			 		   	if(i == 4)	{courseUAnew = dateCurrencyStr;
							   			float courseUAtrue  = ( Float.parseFloat(courseUAnew) / 1 );
							   			courseUAnew = (Float.toString(courseUAtrue));
										}
				 		if(i == 11)	{courseBYRnew = dateCurrencyStr;
							   			float courseBYRtrue = ( Float.parseFloat(courseBYRnew) / 100 );
							   			courseBYRnew = (Float.toString(courseBYRtrue));
							   			}
				 		if(i == 31)	{courseLVLnew = dateCurrencyStr;
							   			float courseLVLtrue = ( Float.parseFloat(courseLVLnew) / 1 );
							   			courseLVLnew = (Float.toString(courseLVLtrue));
							   			}
				 		if(i == 30)	{courseLTLnew = dateCurrencyStr;
							   			float courseLTLtrue = ( Float.parseFloat(courseLTLnew) / 1 );
							   			courseLTLnew = (Float.toString(courseLTLtrue));
							   			}
				 		if(i == 36)	{coursePLNnew = dateCurrencyStr;
							   			float coursePLNtrue = ( Float.parseFloat(coursePLNnew) / 1 );
							   			coursePLNnew = (Float.toString(coursePLNtrue));
										}
		
				 		if (coursePLNnew != "0"){
				  		   	 Message msg = new Message();
				             msg.obj = coursePLNnew;
				             handlerPLN.sendMessage(msg);
					   		}
				 		if (courseLTLnew != "0"){
				  		   	 Message msg = new Message();
				             msg.obj = courseLTLnew;
				             handlerLTL.sendMessage(msg);
					   		}
				 		if (courseLVLnew != "0"){
				  		   	 Message msg = new Message();
				             msg.obj = courseLVLnew;
				             handlerLVL.sendMessage(msg);
					   		}
		  		   		if (courseUSDnew != "0"){
				  		   	 Message msg = new Message();
				             msg.obj = courseUSDnew;
				             handlerUSD.sendMessage(msg);
		  		   			}
			  		   	if (courseEURnew != "0"){
				  		   	 Message msg = new Message();
				             msg.obj = courseEURnew;
				             handlerEUR.sendMessage(msg);
			  		   		}
		  		   		if (courseCHFnew != "0"){
				  		   	 Message msg = new Message();
				             msg.obj = courseCHFnew;
				             handlerCHF.sendMessage(msg);
					   		}
				  		if (courseGBPnew != "0"){
				  		   	 Message msg = new Message();
				             msg.obj = courseGBPnew;
				             handlerGBP.sendMessage(msg);
					   		}
				  		if (courseJPYnew != "0"){
				  		   	 Message msg = new Message();
				             msg.obj = courseJPYnew;
				             handlerJPY.sendMessage(msg);
					   		}
				  		if (courseUAnew != "0"){
				  		   	 Message msg = new Message();
				             msg.obj = courseUAnew;
				             handlerUA.sendMessage(msg);
					   		}
				  		if (courseMDLnew != "0"){
				  		   	 Message msg = new Message();
				             msg.obj = courseMDLnew;
				             handlerMDL.sendMessage(msg);
					   		}
				  		if (courseBYRnew != "0"){
				  		   	 Message msg = new Message();
				             msg.obj = courseBYRnew;
				             handlerBYR.sendMessage(msg);
					   		}
				  		if (courseRUBnew != "0"){
				  		   	 Message msg = new Message();
				             msg.obj = courseRUBnew;
				             handlerRUB.sendMessage(msg);
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
		  		 NodeList list = doc.getElementsByTagName("Rate");
		  		   int count = list.getLength();
		  		   for(int i = 0; i<count; i++)
		  		   {
		  		   		Node n= list.item(i);
		  		   		n.getNodeValue();
		  		   		n.getFirstChild();
		  		   		n.getFirstChild().getNodeValue();
		  		   		String dateCurrencyStr  = n.getFirstChild().getNodeValue();
		  		   		String courseUSDnew = "0";
		  		   		String courseEURnew = "0";
		  		   		String courseCHFnew = "0";
		  		   		String courseGBPnew = "0";
		  		   		String courseJPYnew = "0";
		  		   		String courseRUBnew = "0";
		  		   		String courseUAnew = "0";
		  		   		String courseMDLnew = "0";
		  		   		String courseBYRnew = "1.00";
		  		   		String coursePLNnew = "0";
		  		   		String courseLTLnew = "0";
		  		   		String courseLVLnew = "0";
		  		   		
		  		   		if(i == 4)	{courseUSDnew = dateCurrencyStr;
							   			float courseUSDtrue = ( Float.parseFloat(courseUSDnew) / 1 );
							   			courseUSDnew = (Float.toString(courseUSDtrue));}
			  		   	if(i == 5)	{courseEURnew = dateCurrencyStr;
							   			float courseEURtrue = ( Float.parseFloat(courseEURnew) / 1 );
							   			courseEURnew = (Float.toString(courseEURtrue));}
		 		   		if(i == 23)	{courseGBPnew = dateCurrencyStr;
							   			float courseGBPtrue = ( Float.parseFloat(courseGBPnew) / 1 );
							   			courseGBPnew = (Float.toString(courseGBPtrue));}
		 		   		if(i == 26)	{courseCHFnew = dateCurrencyStr;
							   			float courseCHFtrue = ( Float.parseFloat(courseCHFnew) / 1 );
							   			courseCHFnew = (Float.toString(courseCHFtrue));}
		 		   		if(i == 8)	{courseJPYnew = dateCurrencyStr;
		 					   			float courseJPYtrue = ( Float.parseFloat(courseJPYnew) / 10 );
		 					   			courseJPYnew = (Float.toString(courseJPYtrue));
		 					   			}
		 		   		if(i == 2)	{courseUAnew = dateCurrencyStr;
							   			float courseUAtrue  = ( Float.parseFloat(courseUAnew) / 1 );
							   			courseUAnew = (Float.toString(courseUAtrue));
				   						}
		 		   		if(i == 18)	{courseRUBnew = dateCurrencyStr;
							   			float courseRUBtrue  = ( Float.parseFloat(courseRUBnew) / 1 );
							   			courseRUBnew = (Float.toString(courseRUBtrue));
		 		   						}
			 		   	if(i == 16)	{courseMDLnew = dateCurrencyStr;
							   			float courseMDLtrue = ( Float.parseFloat(courseMDLnew) / 1 );
							   			courseMDLnew = (Float.toString(courseMDLtrue));
							   			}
			 		   	if(i == 14)	{courseLVLnew = dateCurrencyStr;
							   			float courseLVLtrue = ( Float.parseFloat(courseLVLnew) / 1 );
							   			courseLVLnew = (Float.toString(courseLVLtrue));
							   			}
						if(i == 15)	{courseLTLnew = dateCurrencyStr;
							   			float courseLTLtrue = ( Float.parseFloat(courseLTLnew) / 1 );
							   			courseLTLnew = (Float.toString(courseLTLtrue));
							   			}
						if(i == 7)	{coursePLNnew = dateCurrencyStr;
							   			float coursePLNtrue = ( Float.parseFloat(coursePLNnew) / 1 );
							   			coursePLNnew = (Float.toString(coursePLNtrue));
										}
				
						if (coursePLNnew != "0"){
				 		   	 Message msg = new Message();
				            msg.obj = coursePLNnew;
				            handlerPLN.sendMessage(msg);
					   		}
						if (courseLTLnew != "0"){
				 		   	 Message msg = new Message();
				            msg.obj = courseLTLnew;
				            handlerLTL.sendMessage(msg);
					   		}
						if (courseLVLnew != "0"){
				 		   	 Message msg = new Message();
				            msg.obj = courseLVLnew;
				            handlerLVL.sendMessage(msg);
					   		}
		  		   		if (courseUSDnew != "0"){
				  		   	 Message msg = new Message();
				             msg.obj = courseUSDnew;
				             handlerUSD.sendMessage(msg);
		  		   			}
			  		   	if (courseEURnew != "0"){
				  		   	 Message msg = new Message();
				             msg.obj = courseEURnew;
				             handlerEUR.sendMessage(msg);
			  		   		}
		  		   		if (courseCHFnew != "0"){
				  		   	 Message msg = new Message();
				             msg.obj = courseCHFnew;
				             handlerCHF.sendMessage(msg);
					   		}
				  		if (courseGBPnew != "0"){
				  		   	 Message msg = new Message();
				             msg.obj = courseGBPnew;
				             handlerGBP.sendMessage(msg);
					   		}
				  		if (courseJPYnew != "0"){
				  		   	 Message msg = new Message();
				             msg.obj = courseJPYnew;
				             handlerJPY.sendMessage(msg);
					   		}
				  		if (courseUAnew != "0"){
				  		   	 Message msg = new Message();
				             msg.obj = courseUAnew;
				             handlerUA.sendMessage(msg);
					   		}
				  		if (courseMDLnew != "0"){
				  		   	 Message msg = new Message();
				             msg.obj = courseMDLnew;
				             handlerMDL.sendMessage(msg);
					   		}
				  		if (courseBYRnew != "0"){
				  		   	 Message msg = new Message();
				             msg.obj = courseBYRnew;
				             handlerBYR.sendMessage(msg);
					   		}
				  		if (courseRUBnew != "0"){
				  		   	 Message msg = new Message();
				             msg.obj = courseRUBnew;
				             handlerRUB.sendMessage(msg);
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
        		   	
        		 amountmoney.setText(String.valueOf( Float.parseFloat(text.getText().toString()) * Float.parseFloat(curentfromcourserate) / Float.parseFloat(curenttocourserate) ));
         } else {
        	 
        	 	for (int i=0;i<count;i++ ){
        		 
        		 if (from[i].isChecked()){
        			 curentfromcourserate = courserate[i].getText().toString();
        			 }
        		 if (to[i].isChecked()){
        			 curenttocourserate = courserate[i].getText().toString();}
        	 }
          	   	
        	 amountmoney.setText(String.valueOf( Float.parseFloat(text.getText().toString()) * Float.parseFloat(curentfromcourserate) / Float.parseFloat(curenttocourserate) ));
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

            ListCurPreference = prefs.getString("listCurByDefault", "0"); //nr1 
            ListBankPreference = prefs.getString("listSourcesDefault", "0");
            listUpdate = prefs.getString("listUpdate", "0");
}
    
  public void UpdateRates() {
	  int checkBank = Integer.parseInt(ListBankPreference);
	  int checkCurd = Integer.parseInt(ListCurPreference);
	  if (checkBank == 1) {} else {

		  for (int i=0;i<count;i++ ){
			  if (checkCurd == i){coursebydefaultis = course[i].getText().toString();}
			  }
		  for (int i=0;i<count;i++ ){
			  courserate[i].setText(String.valueOf(Float.parseFloat(course[i].getText().toString()) / Float.parseFloat(coursebydefaultis)));
			  }
	  }
  }
}