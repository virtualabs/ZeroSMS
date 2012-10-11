package com.android.zerosms;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import com.android.internal.telephony.IccSmsInterfaceManager;
import com.android.internal.telephony.Phone;
import com.android.internal.telephony.PhoneFactory;
import com.android.internal.telephony.SMSDispatcher;
import com.android.zerosms.R;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.content.Intent;

public class ZeroSMS extends Activity {
	   Button btnSendSMS;
	    EditText txtPhoneNo;
	    EditText txtMessage;
	 
	    /** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) 
	    {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main);        
	 
	        btnSendSMS = (Button) findViewById(R.id.btnSendSMS);
	        txtPhoneNo = (EditText) findViewById(R.id.txtPhoneNo);
	        txtMessage = (EditText) findViewById(R.id.txtMessage);
	 
	        btnSendSMS.setOnClickListener(new View.OnClickListener() 
	        {
	            public void onClick(View v) 
	            {           
	                String phoneNo = txtPhoneNo.getText().toString();
	                String message = txtMessage.getText().toString();                 
	                if (phoneNo.length()>0 && message.length()>0) 
	                {
	                    if(!sendSMS(phoneNo, message))
	                    {
	                    	Toast.makeText(getBaseContext(), 
	    	                        "An error occured while sending SMS.", 
	    	                        Toast.LENGTH_SHORT).show();
	                    }
	                    else
	                    	Toast.makeText(getBaseContext(), 
	    	                        "SMS sent =)", 
	    	                        Toast.LENGTH_SHORT).show();
	                }
	                else
	                    Toast.makeText(getBaseContext(), 
	                        "Please enter both phone number and message.", 
	                        Toast.LENGTH_SHORT).show();

	            }
	        });        
	        
	        
	    }    

	    private String getHexString(byte[] b) throws Exception {
	    	  String result = "";
	    	  for (int i=0; i < b.length; i++) {
	    	    result +=
	    	          Integer.toString( ( b[i] & 0xff ) + 0x100, 16).substring( 1 );
	    	  }
	    	  return result;
	    	}

	    
	    /* Sends class 0 SMS */
	    private boolean sendSMS(String phoneNumber, String message)
	    {
	    	int size;
	    	Field f;	    	
	    	Phone phone = PhoneFactory.getDefaultPhone();
	    	
	    	/* Get IccSmsInterfaceManager */
	    	IccSmsInterfaceManager ismsm = phone.getIccSmsInterfaceManager();

			try {
				f = IccSmsInterfaceManager.class.getDeclaredField("mDispatcher");
				f.setAccessible(true);
		    	SMSDispatcher sms_disp = (SMSDispatcher)f.get(ismsm);
		    	
		    	byte[] b = new byte[0];
		    	SmsMessage.SubmitPdu pdus =
		    			SmsMessage.getSubmitPdu(
		    			null, phoneNumber, message,false
		    			);
		    	
		    	/* change class to Class 0 */
		    	size = (int)pdus.encodedMessage[2];
		    	size = (size/2) + (size%2);
		    	pdus.encodedMessage[size+5] = (byte)0xF0;
		    	
		    	/* send raw pdu */
		    	Method m = SMSDispatcher.class.getDeclaredMethod("sendRawPdu", b.getClass(), b.getClass(), PendingIntent.class, PendingIntent.class);
		    	m.setAccessible(true);
		    	m.invoke(sms_disp, pdus.encodedScAddress, pdus.encodedMessage, null, null);
		    	
		    	return true;
		    	
			} catch (SecurityException e) {
				return false;
			} catch (NoSuchFieldException e) {
				return false;
			} catch (IllegalArgumentException e) {
				return false;
			} catch (IllegalAccessException e) {
				return false;
			} catch (NoSuchMethodException e) {
				return false;
			} catch (InvocationTargetException e) {
				return false;
			}
	    }    
}