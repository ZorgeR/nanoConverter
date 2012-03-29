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
	
	private EditText text;
	private Button buttonrefresh;
	
	public String curentfromcourserate = "1.00";
	public String curenttocourserate = "1.00";
	
	private static EditText courseUSD;
	private static EditText courseEUR;
	private static EditText courseCHF;
	private static EditText courseGBP;
	private static EditText courseJPY;
	private static EditText courseUA;
	private static EditText courseRUB;
	private static EditText courseMDL;
	private static EditText courseBYR;
	private static EditText coursePLN;
	private static EditText courseLTL;
	private static EditText courseLVL;
	// rate conversation
	private static EditText courseUSDrate;
	private static EditText courseEURrate;
	private static EditText courseCHFrate;
	private static EditText courseGBPrate;
	private static EditText courseJPYrate;
	private static EditText courseUArate;
	private static EditText courseRUBrate;
	private static EditText courseMDLrate;
	private static EditText courseBYRrate;
	private static EditText coursePLNrate;
	private static EditText courseLTLrate;
	private static EditText courseLVLrate;
	// rate
	private static EditText amountmoney;
	///////////////
	public static final String money_USD_update = "money_USD_store";
	public static final String money_EUR_update = "money_EUR_store";
	public static final String money_CHF_update = "money_CHF_store";
	public static final String money_GBP_update = "money_GBP_store";
	public static final String money_JPY_update = "money_JPY_store";
	public static final String money_UA_update  = "money_UA_store";
	public static final String money_RUB_update = "money_RUB_store";
	public static final String money_MDL_update = "money_MDL_store";
	public static final String money_BYR_update = "money_BYR_store";
	public static final String money_PLN_update = "money_PLN_store";
	public static final String money_LTL_update = "money_LTL_store";
	public static final String money_LVL_update = "money_LVL_store";

////////////////////////////
	 boolean checkboxUSDPreference;
	 boolean checkboxEURPreference;
	 boolean checkboxCHFPreference;
	 boolean checkboxGBPPreference;
	 boolean checkboxJPYPreference;
	 boolean checkboxUAPreference;
	 boolean checkboxRUBPreference;
	 boolean checkboxMDLPreference;
	 boolean checkboxBYRPreference;
	 boolean checkboxPLNPreference;
	 boolean checkboxLTLPreference;
	 boolean checkboxLVLPreference;
	 
	 public static String ListCurPreference;
	 public static String ListBankPreference;
	 public static String listUpdate;
	 
	///////////////////////////////////////////////////////////
	
	//private Button clickBtn;
    private ProgressDialog progressDialog;

    Handler handlerCloseThreadforce = new Handler() {
    	 
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            progressDialog.dismiss();
        }
    };
    Handler handlerCloseforce = new Handler() {
   	 
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            handlerCloseThreadforce.sendEmptyMessage(0);
        }
    };
    Handler handlerCloseThread = new Handler() {
 
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            // progressDialog.dismiss();
        }
    };
    Handler handlerERRThread = new Handler() {
    	 
        @Override
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
    Handler handlerGOODThread = new Handler() {
   	 
        @Override
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
			String USD = (String)msg.obj;
			courseUSD.setText(USD);
			//call setText here
        }
        };

        Handler handlerEUR = new Handler() {
            @Override
            public void handleMessage(Message msg) {
    			String EUR = (String)msg.obj;
    			courseEUR.setText(EUR);
    			//call setText here
            }
            };
            
            Handler handlerCHF = new Handler() {
                @Override
                public void handleMessage(Message msg) {
        			String CHF = (String)msg.obj;
        			courseCHF.setText(CHF);
        			//call setText here
                }
                };
                Handler handlerGBP = new Handler() {
                    @Override
                    public void handleMessage(Message msg) {
            			String GBP = (String)msg.obj;
            			courseGBP.setText(GBP);
            			//call setText here
                    }
                    };
                    Handler handlerJPY = new Handler() {
                        @Override
                        public void handleMessage(Message msg) {
                			String JPY = (String)msg.obj;
                			courseJPY.setText(JPY);
                			//call setText here
                        }
                        };
                        Handler handlerUA = new Handler() {
                            @Override
                            public void handleMessage(Message msg) {
                    			String UA = (String)msg.obj;
                    			courseUA.setText(UA);
                    			//call setText here
                            }
                            };
                            Handler handlerMDL = new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                        			String MDL = (String)msg.obj;
                        			courseMDL.setText(MDL);
                        			//call setText here
                                }
                                };
                                Handler handlerBYR = new Handler() {
                                    @Override
                                    public void handleMessage(Message msg) {
                            			String BYR = (String)msg.obj;
                            			courseBYR.setText(BYR);
                            			//call setText here
                                    }
                                    };
                                    Handler handlerPLN = new Handler() {
                                        @Override
                                        public void handleMessage(Message msg) {
                                			String PLN = (String)msg.obj;
                                			coursePLN.setText(PLN);
                                			//call setText here
                                        }
                                        };
                                        Handler handlerLTL = new Handler() {
                                            @Override
                                            public void handleMessage(Message msg) {
                                    			String LTL = (String)msg.obj;
                                    			courseLTL.setText(LTL);
                                    			//call setText here
                                            }
                                            };
                                            Handler handlerLVL = new Handler() {
                                                @Override
                                                public void handleMessage(Message msg) {
                                        			String LVL = (String)msg.obj;
                                        			courseLVL.setText(LVL);
                                        			//call setText here
                                                }
                                                };
                            Handler handlerRUB = new Handler() {
                                @Override
                                public void handleMessage(Message msg) {
                        			String RUB = (String)msg.obj;
                        			courseRUB.setText(RUB);
                        			//call setText here
                        			UpdateRates();
                                }
                                };
                            
 @Override
 
  public void onCreate(Bundle savedInstanceState) {

	 mContext = this;
     super.onCreate(savedInstanceState);
     setContentView(R.layout.main);
     
     buttonrefresh = (Button) findViewById(R.id.button2);

     text = (EditText) findViewById(R.id.editText1); /// Поле ввода
     courseUSD = (EditText) findViewById(R.id.CourseUSD);
     courseEUR = (EditText) findViewById(R.id.CourseEUR);
     courseCHF = (EditText) findViewById(R.id.CourseCHF);
     courseGBP = (EditText) findViewById(R.id.CourseGBP);
     courseJPY = (EditText) findViewById(R.id.CourseJPY);
     courseUA  = (EditText) findViewById(R.id.CourseUA);
     courseRUB = (EditText) findViewById(R.id.CourseRUB);
     courseMDL = (EditText) findViewById(R.id.CourseMDL);
     courseBYR = (EditText) findViewById(R.id.CourseBYR);
     coursePLN = (EditText) findViewById(R.id.CoursePLN);
     courseLTL = (EditText) findViewById(R.id.CourseLTL);
     courseLVL = (EditText) findViewById(R.id.CourseLVL);

     courseUSDrate = (EditText) findViewById(R.id.CourseUSDrate);
     courseEURrate = (EditText) findViewById(R.id.CourseEURrate);
     courseCHFrate = (EditText) findViewById(R.id.CourseCHFrate);
     courseGBPrate = (EditText) findViewById(R.id.CourseGBPrate);
     courseJPYrate = (EditText) findViewById(R.id.CourseJPYrate);
     courseUArate  = (EditText) findViewById(R.id.CourseUArate);
     courseRUBrate = (EditText) findViewById(R.id.CourseRUBrate);
     courseMDLrate = (EditText) findViewById(R.id.CourseMDLrate);
     courseBYRrate = (EditText) findViewById(R.id.CourseBYRrate);
     coursePLNrate = (EditText) findViewById(R.id.CoursePLNrate);
     courseLTLrate = (EditText) findViewById(R.id.CourseLTLrate);
     courseLVLrate = (EditText) findViewById(R.id.CourseLVLrate);

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
     
	 RadioButton fromUSDButton = (RadioButton) findViewById(R.id.fromUSD);
	 RadioButton fromEURButton = (RadioButton) findViewById(R.id.fromEUR);
	 RadioButton fromCHFButton = (RadioButton) findViewById(R.id.fromCHF);
	 RadioButton fromGBPButton = (RadioButton) findViewById(R.id.fromGBP);
	 RadioButton fromJPYButton = (RadioButton) findViewById(R.id.fromJPY);
	 RadioButton fromUAButton  = (RadioButton) findViewById(R.id.fromUA);
	 RadioButton fromRUBButton = (RadioButton) findViewById(R.id.fromRUB);
	 RadioButton fromMDLButton = (RadioButton) findViewById(R.id.fromMDL);
	 RadioButton fromBYRButton = (RadioButton) findViewById(R.id.fromBYR);
	 RadioButton fromPLNButton = (RadioButton) findViewById(R.id.fromPLN);
	 RadioButton fromLTLButton = (RadioButton) findViewById(R.id.fromLTL);
	 RadioButton fromLVLButton = (RadioButton) findViewById(R.id.fromLVL);
    
	 RadioButton toUSDButton = (RadioButton) findViewById(R.id.toUSD);
	 RadioButton toEURButton = (RadioButton) findViewById(R.id.toEUR);
	 RadioButton toCHFButton = (RadioButton) findViewById(R.id.toCHF);
	 RadioButton toGBPButton = (RadioButton) findViewById(R.id.toGBP);
	 RadioButton toJPYButton = (RadioButton) findViewById(R.id.toJPY);
	 RadioButton toUAButton  = (RadioButton) findViewById(R.id.toUA);
	 RadioButton toRUBButton = (RadioButton) findViewById(R.id.toRUB);
	 RadioButton toMDLButton = (RadioButton) findViewById(R.id.toMDL);
	 RadioButton toBYRButton = (RadioButton) findViewById(R.id.toBYR);
	 RadioButton toPLNButton = (RadioButton) findViewById(R.id.toPLN);
	 RadioButton toLTLButton = (RadioButton) findViewById(R.id.toLTL);
	 RadioButton toLVLButton = (RadioButton) findViewById(R.id.toLVL);
	 
fromUSDButton.setOnClickListener(new OnClickListener() {public void onClick(View v) {myClickHandler();}});
fromEURButton.setOnClickListener(new OnClickListener() {public void onClick(View v) {myClickHandler();}});
fromCHFButton.setOnClickListener(new OnClickListener() {public void onClick(View v) {myClickHandler();}});
fromGBPButton.setOnClickListener(new OnClickListener() {public void onClick(View v) {myClickHandler();}});
fromJPYButton.setOnClickListener(new OnClickListener() {public void onClick(View v) {myClickHandler();}});
fromUAButton.setOnClickListener(new OnClickListener() {public void onClick(View v) {myClickHandler();}});
fromRUBButton.setOnClickListener(new OnClickListener() {public void onClick(View v) {myClickHandler();}});
fromMDLButton.setOnClickListener(new OnClickListener() {public void onClick(View v) {myClickHandler();}});
fromBYRButton.setOnClickListener(new OnClickListener() {public void onClick(View v) {myClickHandler();}});
fromPLNButton.setOnClickListener(new OnClickListener() {public void onClick(View v) {myClickHandler();}});
fromLTLButton.setOnClickListener(new OnClickListener() {public void onClick(View v) {myClickHandler();}});
fromLVLButton.setOnClickListener(new OnClickListener() {public void onClick(View v) {myClickHandler();}});

toUSDButton.setOnClickListener(new OnClickListener() {public void onClick(View v) {myClickHandler();}});
toEURButton.setOnClickListener(new OnClickListener() {public void onClick(View v) {myClickHandler();}});
toCHFButton.setOnClickListener(new OnClickListener() {public void onClick(View v) {myClickHandler();}});
toGBPButton.setOnClickListener(new OnClickListener() {public void onClick(View v) {myClickHandler();}});
toJPYButton.setOnClickListener(new OnClickListener() {public void onClick(View v) {myClickHandler();}});
toUAButton.setOnClickListener(new OnClickListener() {public void onClick(View v) {myClickHandler();}});
toRUBButton.setOnClickListener(new OnClickListener() {public void onClick(View v) {myClickHandler();}});
toMDLButton.setOnClickListener(new OnClickListener() {public void onClick(View v) {myClickHandler();}});
toBYRButton.setOnClickListener(new OnClickListener() {public void onClick(View v) {myClickHandler();}});
toPLNButton.setOnClickListener(new OnClickListener() {public void onClick(View v) {myClickHandler();}});
toLTLButton.setOnClickListener(new OnClickListener() {public void onClick(View v) {myClickHandler();}});
toLVLButton.setOnClickListener(new OnClickListener() {public void onClick(View v) {myClickHandler();}});
//
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
     
     SharedPreferences settings_USD = getSharedPreferences(money_USD_update, 0);
     SharedPreferences settings_EUR = getSharedPreferences(money_EUR_update, 0);
     SharedPreferences settings_CHF = getSharedPreferences(money_CHF_update, 0);
     SharedPreferences settings_GBP = getSharedPreferences(money_GBP_update, 0);
     SharedPreferences settings_JPY = getSharedPreferences(money_JPY_update, 0);
     SharedPreferences settings_UA = getSharedPreferences(money_UA_update, 0);
     SharedPreferences settings_RUB = getSharedPreferences(money_RUB_update, 0);
     SharedPreferences settings_MDL = getSharedPreferences(money_MDL_update, 0);
     SharedPreferences settings_BYR = getSharedPreferences(money_BYR_update, 0);
     SharedPreferences settings_PLN = getSharedPreferences(money_PLN_update, 0);
     SharedPreferences settings_LTL = getSharedPreferences(money_LTL_update, 0);
     SharedPreferences settings_LVL = getSharedPreferences(money_LVL_update, 0);

     String money_USD_course = settings_USD.getString(money_USD_update, "7777");
     String money_EUR_course = settings_EUR.getString(money_EUR_update, "7777");
     String money_CHF_course = settings_CHF.getString(money_CHF_update, "7777");
     String money_GBP_course = settings_GBP.getString(money_GBP_update, "7777");
     String money_JPY_course = settings_JPY.getString(money_JPY_update, "7777");
     String money_UA_course = settings_UA.getString(money_UA_update, "7777");
     String money_RUB_course = settings_RUB.getString(money_RUB_update, "7777");
     String money_MDL_course = settings_MDL.getString(money_MDL_update, "7777");
     String money_BYR_course = settings_BYR.getString(money_BYR_update, "7777");
     String money_PLN_course = settings_PLN.getString(money_PLN_update, "7777");
     String money_LTL_course = settings_LTL.getString(money_LTL_update, "7777");
     String money_LVL_course = settings_LVL.getString(money_LVL_update, "7777");
     
     if (money_USD_course != "7777"){ courseUSD.setText(money_USD_course); }
     if (money_EUR_course != "7777"){ courseEUR.setText(money_EUR_course); }
     if (money_CHF_course != "7777"){ courseCHF.setText(money_CHF_course); }
     if (money_GBP_course != "7777"){ courseGBP.setText(money_GBP_course); }
     if (money_JPY_course != "7777"){ courseJPY.setText(money_JPY_course); }
     if (money_UA_course != "7777"){ courseUA.setText(money_UA_course); }
     if (money_RUB_course != "7777"){ courseRUB.setText(money_RUB_course); }
     if (money_MDL_course != "7777"){ courseMDL.setText(money_MDL_course); }
     if (money_BYR_course != "7777"){ courseBYR.setText(money_BYR_course); }
     if (money_PLN_course != "7777"){ coursePLN.setText(money_PLN_course); }
     if (money_LTL_course != "7777"){ courseLTL.setText(money_LTL_course); }
     if (money_LVL_course != "7777"){ courseLVL.setText(money_LVL_course); }
     
     UpdateRates();
     
	 /* GET XML FORCE */
     
     int checkBank = Integer.parseInt(ListBankPreference);
     int checkUPDT = Integer.parseInt(listUpdate);
     
	      if (checkBank != 1){
	      if (checkUPDT == 2){NanoConverter.mContext.processThreadforce();
	      } else { /* do nothing */ }
	 /* GET XML FORCE */

	 /* GET XML AUTO */
	      if (checkUPDT == 1){NanoConverter.mContext.processThread();    	 
	      } else { /* do nothing */ }
	 /* GET XML AUTO */
	      } else {
	    courseUSDrate.setText(courseUSD.getText().toString());
		courseEURrate.setText(courseEUR.getText().toString());
		courseCHFrate.setText(courseCHF.getText().toString());
		courseGBPrate.setText(courseGBP.getText().toString());
		courseJPYrate.setText(courseJPY.getText().toString());
		courseUArate.setText(courseUA.getText().toString());
		courseRUBrate.setText(courseRUB.getText().toString());
		courseMDLrate.setText(courseMDL.getText().toString());
		courseBYRrate.setText(courseBYR.getText().toString());
		coursePLNrate.setText(coursePLN.getText().toString());
		courseLTLrate.setText(courseLTL.getText().toString());
		courseLVLrate.setText(courseLVL.getText().toString());
	      }
 }

 protected void onResume() {

	 RadioButton fromUSDButton = (RadioButton) findViewById(R.id.fromUSD);
	 RadioButton fromEURButton = (RadioButton) findViewById(R.id.fromEUR);
	 RadioButton fromCHFButton = (RadioButton) findViewById(R.id.fromCHF);
	 RadioButton fromGBPButton = (RadioButton) findViewById(R.id.fromGBP);
	 RadioButton fromJPYButton = (RadioButton) findViewById(R.id.fromJPY);
	 RadioButton fromUAButton  = (RadioButton) findViewById(R.id.fromUA);
	 RadioButton fromRUBButton = (RadioButton) findViewById(R.id.fromRUB);
	 RadioButton fromMDLButton = (RadioButton) findViewById(R.id.fromMDL);
	 RadioButton fromBYRButton = (RadioButton) findViewById(R.id.fromBYR);
	 RadioButton fromPLNButton = (RadioButton) findViewById(R.id.fromPLN);
	 RadioButton fromLTLButton = (RadioButton) findViewById(R.id.fromLTL);
	 RadioButton fromLVLButton = (RadioButton) findViewById(R.id.fromLVL);
    
	 RadioButton toUSDButton = (RadioButton) findViewById(R.id.toUSD);
	 RadioButton toEURButton = (RadioButton) findViewById(R.id.toEUR);
	 RadioButton toCHFButton = (RadioButton) findViewById(R.id.toCHF);
	 RadioButton toGBPButton = (RadioButton) findViewById(R.id.toGBP);
	 RadioButton toJPYButton = (RadioButton) findViewById(R.id.toJPY);
	 RadioButton toUAButton  = (RadioButton) findViewById(R.id.toUA);
	 RadioButton toRUBButton = (RadioButton) findViewById(R.id.toRUB);
	 RadioButton toMDLButton = (RadioButton) findViewById(R.id.toMDL);
	 RadioButton toBYRButton = (RadioButton) findViewById(R.id.toBYR);
	 RadioButton toPLNButton = (RadioButton) findViewById(R.id.toPLN);
	 RadioButton toLTLButton = (RadioButton) findViewById(R.id.toLTL);
	 RadioButton toLVLButton = (RadioButton) findViewById(R.id.toLVL);
	 
	 LinearLayout USDcl = (LinearLayout)findViewById(R.id.USDcl);
	 LinearLayout EURcl = (LinearLayout)findViewById(R.id.EURcl);
	 LinearLayout CHFcl = (LinearLayout)findViewById(R.id.CHFcl);
	 LinearLayout GBPcl = (LinearLayout)findViewById(R.id.GBPcl);
	 LinearLayout JPYcl = (LinearLayout)findViewById(R.id.JPYcl);
	 LinearLayout UAcl = (LinearLayout)findViewById(R.id.UAcl);
	 LinearLayout RUBcl = (LinearLayout)findViewById(R.id.RUBcl);
	 LinearLayout MDLcl = (LinearLayout)findViewById(R.id.MDLcl);
	 LinearLayout BYRcl = (LinearLayout)findViewById(R.id.BYRcl);
	 LinearLayout PLNcl = (LinearLayout)findViewById(R.id.PLNcl);
	 LinearLayout LTLcl = (LinearLayout)findViewById(R.id.LTLcl);
	 LinearLayout LVLcl = (LinearLayout)findViewById(R.id.LVLcl);
	 
	 /*
	 String moneyliastarray[];
	 moneyliastarray = new String[9];
	 moneyliastarray[0] = "USD";
	 moneyliastarray[1] = "EUR";
	 moneyliastarray[2] = "CHF";
	 moneyliastarray[3] = "GBP";
	 moneyliastarray[4] = "JPY";
	 moneyliastarray[5] = "UA";
	 moneyliastarray[6] = "RUB";
	 moneyliastarray[7] = "MDL";
	 moneyliastarray[8] = "BYR";
	  */
	 
	 View USDcls = (View)findViewById(R.id.USDcls);
	 View EURcls = (View)findViewById(R.id.EURcls);
	 View CHFcls = (View)findViewById(R.id.CHFcls);
	 View GBPcls = (View)findViewById(R.id.GBPcls);
	 View JPYcls = (View)findViewById(R.id.JPYcls);
	 View UAcls = (View)findViewById(R.id.UAcls);
	 View RUBcls = (View)findViewById(R.id.RUBcls);
	 View MDLcls = (View)findViewById(R.id.MDLcls);
	 View BYRcls = (View)findViewById(R.id.BYRcls);
	 View PLNcls = (View)findViewById(R.id.PLNcls);
	 View LTLcls = (View)findViewById(R.id.LTLcls);
	 View LVLcls = (View)findViewById(R.id.LVLcls);

	 getPrefs();
	 
	 int checkBank = Integer.parseInt(ListBankPreference);
	 
	 if (checkboxUSDPreference == false){fromUSDButton.setVisibility(View.GONE);toUSDButton.setVisibility(View.GONE);USDcls.setVisibility(View.GONE);USDcl.setVisibility(View.GONE);}
	 if (checkboxEURPreference == false){fromEURButton.setVisibility(View.GONE);toEURButton.setVisibility(View.GONE);EURcls.setVisibility(View.GONE);EURcl.setVisibility(View.GONE);}
	 if (checkboxCHFPreference == false){fromCHFButton.setVisibility(View.GONE);toCHFButton.setVisibility(View.GONE);CHFcls.setVisibility(View.GONE);CHFcl.setVisibility(View.GONE);}
	 if (checkboxGBPPreference == false){fromGBPButton.setVisibility(View.GONE);toGBPButton.setVisibility(View.GONE);GBPcls.setVisibility(View.GONE);GBPcl.setVisibility(View.GONE);}
	 if (checkboxJPYPreference == false){fromJPYButton.setVisibility(View.GONE);toJPYButton.setVisibility(View.GONE);JPYcls.setVisibility(View.GONE);JPYcl.setVisibility(View.GONE);}
	 if (checkboxUAPreference == false){fromUAButton.setVisibility(View.GONE);toUAButton.setVisibility(View.GONE);UAcls.setVisibility(View.GONE);UAcl.setVisibility(View.GONE);}
	 if (checkboxRUBPreference == false){fromRUBButton.setVisibility(View.GONE);toRUBButton.setVisibility(View.GONE);RUBcls.setVisibility(View.GONE);RUBcl.setVisibility(View.GONE);}
	 if (checkboxMDLPreference == false){fromMDLButton.setVisibility(View.GONE);toMDLButton.setVisibility(View.GONE);MDLcls.setVisibility(View.GONE);MDLcl.setVisibility(View.GONE);}
	 if (checkboxBYRPreference == false){fromBYRButton.setVisibility(View.GONE);toBYRButton.setVisibility(View.GONE);BYRcls.setVisibility(View.GONE);BYRcl.setVisibility(View.GONE);}
	 if (checkboxPLNPreference == false){fromPLNButton.setVisibility(View.GONE);toPLNButton.setVisibility(View.GONE);PLNcls.setVisibility(View.GONE);PLNcl.setVisibility(View.GONE);}
	 if (checkboxLTLPreference == false){fromLTLButton.setVisibility(View.GONE);toLTLButton.setVisibility(View.GONE);LTLcls.setVisibility(View.GONE);LTLcl.setVisibility(View.GONE);}
	 if (checkboxLVLPreference == false){fromLVLButton.setVisibility(View.GONE);toLVLButton.setVisibility(View.GONE);LVLcls.setVisibility(View.GONE);LVLcl.setVisibility(View.GONE);}
	 
	 if (checkboxUSDPreference == true){fromUSDButton.setVisibility(View.VISIBLE);toUSDButton.setVisibility(View.VISIBLE);USDcl.setVisibility(View.VISIBLE);USDcls.setVisibility(View.VISIBLE);}
	 if (checkboxEURPreference == true){fromEURButton.setVisibility(View.VISIBLE);toEURButton.setVisibility(View.VISIBLE);EURcl.setVisibility(View.VISIBLE);EURcls.setVisibility(View.VISIBLE);}
	 if (checkboxCHFPreference == true){fromCHFButton.setVisibility(View.VISIBLE);toCHFButton.setVisibility(View.VISIBLE);CHFcl.setVisibility(View.VISIBLE);CHFcls.setVisibility(View.VISIBLE);}
	 if (checkboxGBPPreference == true){fromGBPButton.setVisibility(View.VISIBLE);toGBPButton.setVisibility(View.VISIBLE);GBPcl.setVisibility(View.VISIBLE);GBPcls.setVisibility(View.VISIBLE);}
	 if (checkboxJPYPreference == true){fromJPYButton.setVisibility(View.VISIBLE);toJPYButton.setVisibility(View.VISIBLE);JPYcl.setVisibility(View.VISIBLE);JPYcls.setVisibility(View.VISIBLE);}
	 if (checkboxUAPreference == true){fromUAButton.setVisibility(View.VISIBLE);toUAButton.setVisibility(View.VISIBLE);UAcl.setVisibility(View.VISIBLE);UAcls.setVisibility(View.VISIBLE);}
	 if (checkboxRUBPreference == true){fromRUBButton.setVisibility(View.VISIBLE);toRUBButton.setVisibility(View.VISIBLE);RUBcl.setVisibility(View.VISIBLE);RUBcls.setVisibility(View.VISIBLE);}
	 if (checkboxMDLPreference == true){fromMDLButton.setVisibility(View.VISIBLE);toMDLButton.setVisibility(View.VISIBLE);MDLcl.setVisibility(View.VISIBLE);MDLcls.setVisibility(View.VISIBLE);}
	 if (checkboxBYRPreference == true){fromBYRButton.setVisibility(View.VISIBLE);toBYRButton.setVisibility(View.VISIBLE);BYRcl.setVisibility(View.VISIBLE);BYRcls.setVisibility(View.VISIBLE);}
	 if (checkboxPLNPreference == true){fromPLNButton.setVisibility(View.VISIBLE);toPLNButton.setVisibility(View.VISIBLE);PLNcls.setVisibility(View.VISIBLE);PLNcl.setVisibility(View.VISIBLE);}
	 if (checkboxLTLPreference == true){fromLTLButton.setVisibility(View.VISIBLE);toLTLButton.setVisibility(View.VISIBLE);LTLcls.setVisibility(View.VISIBLE);LTLcl.setVisibility(View.VISIBLE);}
	 if (checkboxLVLPreference == true){fromLVLButton.setVisibility(View.VISIBLE);toLVLButton.setVisibility(View.VISIBLE);LVLcls.setVisibility(View.VISIBLE);LVLcl.setVisibility(View.VISIBLE);}

	 
	 /* 
	  * buttonrefresh.
	  * GONE, INVISIBLE, and VISIBLE
	  * 
	  */

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
 //// MENU MENU
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
     case R.id.about:{
    	 new AlertDialog.Builder(this)
    		.setTitle(R.string.about)
    		.setMessage(/*R.string.abouttext*/ "nanoConverter 0.7.0"+"\n"+""+"\n"+"Простой и удобный конвертер валют для Android."+"\n"
    		+""+"\n"+"Z-lab - 2012")
    			.show();
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
    
    
    SharedPreferences settings_USD = getSharedPreferences(money_USD_update, 0);
    SharedPreferences settings_EUR = getSharedPreferences(money_EUR_update, 0);
    SharedPreferences settings_CHF = getSharedPreferences(money_CHF_update, 0);
    SharedPreferences settings_GBP = getSharedPreferences(money_GBP_update, 0);
    SharedPreferences settings_JPY = getSharedPreferences(money_JPY_update, 0);
    SharedPreferences settings_UA = getSharedPreferences(money_UA_update, 0);
    SharedPreferences settings_RUB = getSharedPreferences(money_RUB_update, 0);
    SharedPreferences settings_MDL = getSharedPreferences(money_MDL_update, 0);
    SharedPreferences settings_BYR = getSharedPreferences(money_BYR_update, 0);
    SharedPreferences settings_PLN = getSharedPreferences(money_PLN_update, 0);
    SharedPreferences settings_LTL = getSharedPreferences(money_LTL_update, 0);
    SharedPreferences settings_LVL = getSharedPreferences(money_LVL_update, 0);
    
    SharedPreferences.Editor editUSD = settings_USD.edit();
    SharedPreferences.Editor editEUR = settings_EUR.edit();
    SharedPreferences.Editor editCHF = settings_CHF.edit();
    SharedPreferences.Editor editGBP = settings_GBP.edit();
    SharedPreferences.Editor editJPY = settings_JPY.edit();
    SharedPreferences.Editor editUA = settings_UA.edit();
    SharedPreferences.Editor editRUB = settings_RUB.edit();
    SharedPreferences.Editor editMDL = settings_MDL.edit();
    SharedPreferences.Editor editBYR = settings_BYR.edit();
    SharedPreferences.Editor editPLN = settings_PLN.edit();
    SharedPreferences.Editor editLTL = settings_LTL.edit();
    SharedPreferences.Editor editLVL = settings_LVL.edit();
    
    
    int checkBank = Integer.parseInt(ListBankPreference);
	
    if (checkBank == 1) {
    	courseUSD.setText(courseUSDrate.getText().toString());
    	courseEUR.setText(courseEURrate.getText().toString());
    	courseCHF.setText(courseCHFrate.getText().toString());
    	courseGBP.setText(courseGBPrate.getText().toString());
    	courseJPY.setText(courseJPYrate.getText().toString());
    	courseUA.setText(courseUArate.getText().toString());
    	courseRUB.setText(courseRUBrate.getText().toString());
    	courseMDL.setText(courseMDLrate.getText().toString());
    	courseBYR.setText(courseBYRrate.getText().toString());
    	coursePLN.setText(coursePLNrate.getText().toString());
    	courseLTL.setText(courseLTLrate.getText().toString());
    	courseLVL.setText(courseLVLrate.getText().toString());
    }
    
    editUSD.putString(money_USD_update, courseUSD.getText().toString());
    editEUR.putString(money_EUR_update, courseEUR.getText().toString());
    editCHF.putString(money_CHF_update, courseCHF.getText().toString());
    editGBP.putString(money_GBP_update, courseGBP.getText().toString());
    editJPY.putString(money_JPY_update, courseJPY.getText().toString());
     editUA.putString(money_UA_update, courseUA.getText().toString());
    editRUB.putString(money_RUB_update, courseRUB.getText().toString());
    editMDL.putString(money_MDL_update, courseMDL.getText().toString());
    editBYR.putString(money_BYR_update, courseBYR.getText().toString());
    editPLN.putString(money_PLN_update, coursePLN.getText().toString());
    editLTL.putString(money_LTL_update, courseLTL.getText().toString());
    editLVL.putString(money_LVL_update, courseLVL.getText().toString());
    
    editUSD.commit();
    editEUR.commit();
    editCHF.commit();
    editGBP.commit();
    editJPY.commit();
    editUA.commit();
    editRUB.commit();
    editMDL.commit();
    editBYR.commit();
    editPLN.commit();
    editLTL.commit();
    editLVL.commit();
    
 }

private void processThread() {
    
	Toast toast2 = Toast.makeText(getApplicationContext(), getString(R.string.updateinprogress), Toast.LENGTH_SHORT);
    LinearLayout ToastView = (LinearLayout) toast2.getView();
    ImageView imageWorld = new ImageView(getApplicationContext());
    imageWorld.setImageResource(R.drawable.dwnld);
    ToastView.addView(imageWorld, 0);
    toast2.show();
    
    
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

private void processThreadforce() {
	progressDialog = ProgressDialog.show(NanoConverter.mContext, getString(R.string.wait), getString(R.string.updateinprogress));

	new Thread() {
		public void run() {
		killLongForce();
		}
	}.start();
	
    if (BANK_ID == "CBR"){
        new Thread() {
            public void run() {
           	 runLongProcessCBR();
           	handlerCloseThreadforce.sendEmptyMessage(0);
            }
        }.start();}
   	if (BANK_ID == "NBU"){
   		new Thread() {
   	         public void run() {
   	        	runLongProcessNBU();
   	        	handlerCloseThreadforce.sendEmptyMessage(0);
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
    handlerCloseforce.sendEmptyMessage(0);
	} catch (Exception ioe) {
	    	 //donothing
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
	  
	    RadioButton fromUSDButton = (RadioButton) findViewById(R.id.fromUSD);
	    RadioButton fromEURButton = (RadioButton) findViewById(R.id.fromEUR);
	    RadioButton fromCHFButton = (RadioButton) findViewById(R.id.fromCHF);
	    RadioButton fromGBPButton = (RadioButton) findViewById(R.id.fromGBP);
	    RadioButton fromJPYButton = (RadioButton) findViewById(R.id.fromJPY);
	    RadioButton fromUAButton  = (RadioButton) findViewById(R.id.fromUA);
	    RadioButton fromRUBButton = (RadioButton) findViewById(R.id.fromRUB);
	    RadioButton fromMDLButton = (RadioButton) findViewById(R.id.fromMDL);
	    RadioButton fromBYRButton = (RadioButton) findViewById(R.id.fromBYR);
		RadioButton fromPLNButton = (RadioButton) findViewById(R.id.fromPLN);
		RadioButton fromLTLButton = (RadioButton) findViewById(R.id.fromLTL);
		RadioButton fromLVLButton = (RadioButton) findViewById(R.id.fromLVL);
	    
	    RadioButton toUSDButton = (RadioButton) findViewById(R.id.toUSD);
	    RadioButton toEURButton = (RadioButton) findViewById(R.id.toEUR);
	    RadioButton toCHFButton = (RadioButton) findViewById(R.id.toCHF);
	    RadioButton toGBPButton = (RadioButton) findViewById(R.id.toGBP);
	    RadioButton toJPYButton = (RadioButton) findViewById(R.id.toJPY);
	    RadioButton toUAButton  = (RadioButton) findViewById(R.id.toUA);
	    RadioButton toRUBButton = (RadioButton) findViewById(R.id.toRUB);
	    RadioButton toMDLButton = (RadioButton) findViewById(R.id.toMDL);
	    RadioButton toBYRButton = (RadioButton) findViewById(R.id.toBYR);
		RadioButton toPLNButton = (RadioButton) findViewById(R.id.toPLN);
		RadioButton toLTLButton = (RadioButton) findViewById(R.id.toLTL);
		RadioButton toLVLButton = (RadioButton) findViewById(R.id.toLVL);
		 
         if (text.getText().length() == 0) {
        	 amountmoney.setText("0.00");
             return;
         }
         if (checkBank != 1) {
        	 	if (fromUSDButton.isChecked()){curentfromcourserate = courseUSD.getText().toString();} else
         	   	if (fromEURButton.isChecked()){curentfromcourserate = courseEUR.getText().toString();} else
         	   	if (fromCHFButton.isChecked()){curentfromcourserate = courseCHF.getText().toString();} else
         	   	if (fromGBPButton.isChecked()){curentfromcourserate = courseGBP.getText().toString();} else
         	   	if (fromJPYButton.isChecked()){curentfromcourserate = courseJPY.getText().toString();} else
         	   	if (fromUAButton.isChecked()){curentfromcourserate = courseUA.getText().toString();}   else
         	   	if (fromRUBButton.isChecked()){curentfromcourserate = courseRUB.getText().toString();} else
         	   	if (fromMDLButton.isChecked()){curentfromcourserate = courseMDL.getText().toString();} else
         	   	if (fromBYRButton.isChecked()){curentfromcourserate = courseBYR.getText().toString();} else
         	   	if (fromPLNButton.isChecked()){curentfromcourserate = coursePLN.getText().toString();} else
         	   	if (fromLTLButton.isChecked()){curentfromcourserate = courseLTL.getText().toString();} else
         	   	if (fromLVLButton.isChecked()){curentfromcourserate = courseLVL.getText().toString();}
         	   	 
         	   	if (toUSDButton.isChecked()){curenttocourserate = courseUSD.getText().toString();} else
         	   	if (toEURButton.isChecked()){curenttocourserate = courseEUR.getText().toString();} else
         	   	if (toCHFButton.isChecked()){curenttocourserate = courseCHF.getText().toString();} else
         	   	if (toGBPButton.isChecked()){curenttocourserate = courseGBP.getText().toString();} else
         	   	if (toJPYButton.isChecked()){curenttocourserate = courseJPY.getText().toString();} else
         	   	if (toUAButton.isChecked()){curenttocourserate = courseUA.getText().toString();}   else
         	   	if (toRUBButton.isChecked()){curenttocourserate = courseRUB.getText().toString();} else
         	   	if (toMDLButton.isChecked()){curenttocourserate = courseMDL.getText().toString();} else
         	   	if (toBYRButton.isChecked()){curenttocourserate = courseBYR.getText().toString();} else
             	if (toPLNButton.isChecked()){curenttocourserate = coursePLN.getText().toString();} else
                if (toLTLButton.isChecked()){curenttocourserate = courseLTL.getText().toString();} else
                if (toLVLButton.isChecked()){curenttocourserate = courseLVL.getText().toString();}
         	   	
        	 amountmoney.setText(String.valueOf( Float.parseFloat(text.getText().toString()) * Float.parseFloat(curentfromcourserate) / Float.parseFloat(curenttocourserate) ));
         } else {
        	 	if (fromUSDButton.isChecked()){curentfromcourserate = courseUSDrate.getText().toString();} else
          	   	if (fromEURButton.isChecked()){curentfromcourserate = courseEURrate.getText().toString();} else
          	   	if (fromCHFButton.isChecked()){curentfromcourserate = courseCHFrate.getText().toString();} else
          	   	if (fromGBPButton.isChecked()){curentfromcourserate = courseGBPrate.getText().toString();} else
          	   	if (fromJPYButton.isChecked()){curentfromcourserate = courseJPYrate.getText().toString();} else
          	   	if (fromUAButton.isChecked()){curentfromcourserate = courseUArate.getText().toString();}   else
          	   	if (fromRUBButton.isChecked()){curentfromcourserate = courseRUBrate.getText().toString();} else
          	   	if (fromMDLButton.isChecked()){curentfromcourserate = courseMDLrate.getText().toString();} else
          	   	if (fromBYRButton.isChecked()){curentfromcourserate = courseBYRrate.getText().toString();} else
             	if (fromPLNButton.isChecked()){curentfromcourserate = coursePLNrate.getText().toString();} else
                if (fromLTLButton.isChecked()){curentfromcourserate = courseLTLrate.getText().toString();} else
                if (fromLVLButton.isChecked()){curentfromcourserate = courseLVLrate.getText().toString();}
          	   	 
          	   	if (toUSDButton.isChecked()){curenttocourserate = courseUSDrate.getText().toString();} else
          	   	if (toEURButton.isChecked()){curenttocourserate = courseEURrate.getText().toString();} else
          	   	if (toCHFButton.isChecked()){curenttocourserate = courseCHFrate.getText().toString();} else
          	   	if (toGBPButton.isChecked()){curenttocourserate = courseGBPrate.getText().toString();} else
          	   	if (toJPYButton.isChecked()){curenttocourserate = courseJPYrate.getText().toString();} else
          	   	if (toUAButton.isChecked()){curenttocourserate = courseUArate.getText().toString();}   else
          	   	if (toRUBButton.isChecked()){curenttocourserate = courseRUBrate.getText().toString();} else
          	   	if (toMDLButton.isChecked()){curenttocourserate = courseMDLrate.getText().toString();} else
          	   	if (toBYRButton.isChecked()){curenttocourserate = courseBYRrate.getText().toString();} else
                if (toPLNButton.isChecked()){curenttocourserate = coursePLNrate.getText().toString();} else
                if (toLTLButton.isChecked()){curenttocourserate = courseLTLrate.getText().toString();} else
                if (toLVLButton.isChecked()){curenttocourserate = courseLVLrate.getText().toString();}
          	   	
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
	     	
	    courseUSDrate.setKeyListener(null);
	    courseEURrate.setKeyListener(null);
	    courseCHFrate.setKeyListener(null);
	    courseGBPrate.setKeyListener(null);
	    courseJPYrate.setKeyListener(null);
	    courseUArate.setKeyListener(null);
	    courseRUBrate.setKeyListener(null);
	    courseMDLrate.setKeyListener(null);
	    courseBYRrate.setKeyListener(null);
	    coursePLNrate.setKeyListener(null);
	    courseLTLrate.setKeyListener(null);
	    courseLVLrate.setKeyListener(null);
	    buttonrefresh.setEnabled(true);
	    
	    UpdateRates();
  }
  
  public void TurnOFFrates() {
		
	    courseUSDrate.setKeyListener(courseUSD.getKeyListener());
	    courseEURrate.setKeyListener(courseUSD.getKeyListener());
	    courseCHFrate.setKeyListener(courseUSD.getKeyListener());
	    courseGBPrate.setKeyListener(courseUSD.getKeyListener());
	    courseJPYrate.setKeyListener(courseUSD.getKeyListener());
	    courseUArate.setKeyListener(courseUSD.getKeyListener());
	    courseRUBrate.setKeyListener(courseUSD.getKeyListener());
	    courseMDLrate.setKeyListener(courseUSD.getKeyListener());
	    courseBYRrate.setKeyListener(courseUSD.getKeyListener());
	    coursePLNrate.setKeyListener(courseUSD.getKeyListener());
	    courseLTLrate.setKeyListener(courseUSD.getKeyListener());
	    courseLVLrate.setKeyListener(courseUSD.getKeyListener());
	    buttonrefresh.setEnabled(false);

	    /*
	     * checkboxUSDPreference
	     * buttonrefresh.setVisibility(View.GONE);
	     * GONE, INVISIBLE, and VISIBLE
	     * 
	     */
  }
  
private void getPrefs() {

             SharedPreferences prefs = PreferenceManager
                             .getDefaultSharedPreferences(getBaseContext());

        	 checkboxUSDPreference = prefs.getBoolean("checkboxUSD", true);;
        	 checkboxEURPreference = prefs.getBoolean("checkboxEUR", true);;
        	 checkboxCHFPreference = prefs.getBoolean("checkboxCHF", true);;
        	 checkboxGBPPreference = prefs.getBoolean("checkboxGBP", true);;
        	 checkboxJPYPreference = prefs.getBoolean("checkboxJPY", true);;
        	 checkboxUAPreference = prefs.getBoolean("checkboxUA", true);;
        	 checkboxRUBPreference = prefs.getBoolean("checkboxRUB", true);;
        	 checkboxMDLPreference = prefs.getBoolean("checkboxMDL", true);;
        	 checkboxBYRPreference = prefs.getBoolean("checkboxBYR", true);;
        	 checkboxPLNPreference = prefs.getBoolean("checkboxPLN", true);;
        	 checkboxLTLPreference = prefs.getBoolean("checkboxLTL", true);;
        	 checkboxLVLPreference = prefs.getBoolean("checkboxLVL", true);;

             ListCurPreference = prefs.getString("listCurByDefault", "0"); //nr1 
            ListBankPreference = prefs.getString("listSourcesDefault", "0");
            listUpdate = prefs.getString("listUpdate", "0");
}
  
  public void UpdateRates() {
	  int checkBank = Integer.parseInt(ListBankPreference);
	  int checkCurd = Integer.parseInt(ListCurPreference);
	  if (checkBank == 1) {} else {

		  String coursebydefaultis = courseUSD.getText().toString();

			 if (checkCurd == 0){coursebydefaultis = courseUSD.getText().toString();}
		else if (checkCurd == 1){coursebydefaultis = courseEUR.getText().toString();}
		else if (checkCurd == 2){coursebydefaultis = courseCHF.getText().toString();}
		else if (checkCurd == 3){coursebydefaultis = courseGBP.getText().toString();}
		else if (checkCurd == 4){coursebydefaultis = courseJPY.getText().toString();}
		else if (checkCurd == 5){coursebydefaultis = courseUA.getText().toString();}
		else if (checkCurd == 6){coursebydefaultis = courseRUB.getText().toString();}
		else if (checkCurd == 7){coursebydefaultis = courseMDL.getText().toString();}
		else if (checkCurd == 8){coursebydefaultis = courseBYR.getText().toString();}
		else if (checkCurd == 9){coursebydefaultis = coursePLN.getText().toString();}
		else if (checkCurd == 10){coursebydefaultis = courseLTL.getText().toString();}
		else if (checkCurd == 11){coursebydefaultis = courseLVL.getText().toString();}

			courseUSDrate.setText(String.valueOf(Float.parseFloat(courseUSD.getText().toString()) / Float.parseFloat(coursebydefaultis)));
			courseEURrate.setText(String.valueOf(Float.parseFloat(courseEUR.getText().toString()) / Float.parseFloat(coursebydefaultis)));
			courseCHFrate.setText(String.valueOf(Float.parseFloat(courseCHF.getText().toString()) / Float.parseFloat(coursebydefaultis)));
			courseGBPrate.setText(String.valueOf(Float.parseFloat(courseGBP.getText().toString()) / Float.parseFloat(coursebydefaultis)));
			courseJPYrate.setText(String.valueOf(Float.parseFloat(courseJPY.getText().toString()) / Float.parseFloat(coursebydefaultis)));
			courseUArate.setText(String.valueOf(Float.parseFloat(courseUA.getText().toString()) / Float.parseFloat(coursebydefaultis)));
			courseRUBrate.setText(String.valueOf(Float.parseFloat(courseRUB.getText().toString()) / Float.parseFloat(coursebydefaultis)));
			courseMDLrate.setText(String.valueOf(Float.parseFloat(courseMDL.getText().toString()) / Float.parseFloat(coursebydefaultis)));
			courseBYRrate.setText(String.valueOf(Float.parseFloat(courseBYR.getText().toString()) / Float.parseFloat(coursebydefaultis)));
			coursePLNrate.setText(String.valueOf(Float.parseFloat(coursePLN.getText().toString()) / Float.parseFloat(coursebydefaultis)));
			courseLTLrate.setText(String.valueOf(Float.parseFloat(courseLTL.getText().toString()) / Float.parseFloat(coursebydefaultis)));
			courseLVLrate.setText(String.valueOf(Float.parseFloat(courseLVL.getText().toString()) / Float.parseFloat(coursebydefaultis)));
	  }
  }
}