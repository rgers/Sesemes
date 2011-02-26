package pl.gers.sesemes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpHost;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.BasicHttpRequest;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import pl.gers.sesemes.R;
import android.R.string;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.net.http.AndroidHttpClient;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Contacts;
import android.provider.Contacts.People;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.EditText;

public class Sesemes extends Activity implements OnClickListener{
    /** Called when the activity is first created. */
	String user, passwd;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Button btn_wyslij = (Button) findViewById(R.id.btn_wyslij);
		btn_wyslij.setOnClickListener(this);
		Button btn_contact = (Button) findViewById(R.id.btn_contact);
		btn_contact.setOnClickListener(this);
		    }
@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
    	 MenuInflater inflater = getMenuInflater();
    	    inflater.inflate(R.menu.main_menu, menu);
		return true;
    
    }
   
@Override
public boolean onOptionsItemSelected(MenuItem item) {
    // Handle item selection
    switch (item.getItemId()) {
    case R.id.menu_ustawienia:
        ustawienia();
        return true;
    default:
        return super.onOptionsItemSelected(item);
    }
}
    
	private void ustawienia() {
	Intent myint = new Intent(this, Ustawienia.class);
	startActivity(myint);
	
}
	public void onClick(View v) 
	{
		int id = v.getId();
		switch(id)
		{
		case (R.id.btn_wyslij):
			EditText txt_wiadomosc = (EditText) findViewById(R.id.txt_wiadomosc);
		EditText txt_numer = (EditText) findViewById(R.id.txt_numer);
		SharedPreferences prefs = getSharedPreferences("prefs", 0);
        user = prefs.getString("user", "");
        passwd = prefs.getString("passwd", "");
        if (user=="" || passwd=="")
        {ustawienia(); break;}
			new send_sms().execute(txt_wiadomosc.getText().toString(), txt_numer.getText().toString());
		break;
		
		case (R.id.btn_contact):
			get_contact();
		break;
		}
		
	}
	public void get_contact()
	{
	Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds. Phone.CONTENT_URI);
	startActivityForResult(i, 1);
	}
	  @Override  
	    public void onActivityResult(int reqCode, int resultCode, Intent data) {  
	        super.onActivityResult(reqCode, resultCode, data);  
	  
	        switch (reqCode) {  
	            case (1):  
	                if (resultCode == Activity.RESULT_OK) {  
	                    Uri contactData = data.getData();  
	                    Cursor c = managedQuery(contactData, null, null, null, null);  
	                    if (c.moveToFirst()) {  
	                        String name = c.getString(c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds. Phone.NUMBER));  
	                        EditText txt_numer = (EditText) findViewById(R.id.txt_numer);
	                        txt_numer.setText(name);  
	                    }  
	                }  
	                break;  
	        }  
	    }  

	private class send_sms extends AsyncTask<String, Void, Void>
	{
		ProgressDialog dialog;
		protected void onPreExecute()
		{
			dialog = ProgressDialog.show(Sesemes.this, "", "Wysy³am. Proszê czekaæ...", true);
		}
		
		protected void onPostExecute(Void result)
		{
			dialog.dismiss();
			EditText wiadomosc = (EditText)findViewById(R.id.txt_wiadomosc);
			wiadomosc.setText("");
		}
		
		protected Void doInBackground(String...strings)
		{
			
		 HttpResponse resp = null;
		 SchemeRegistry schemeRegistry = new SchemeRegistry();
		 schemeRegistry.register(new Scheme("https", 
		             SSLSocketFactory.getSocketFactory(), 443));
		 HttpParams params = new BasicHttpParams();
		 HttpContext localContext = null;
		
		HttpClient klient = new DefaultHttpClient();
		CookieStore cookieJar = new BasicCookieStore();
		
		// Creating a local HTTP context
		localContext = new BasicHttpContext();

		// Bind custom cookie store to the local context
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieJar);
		
		HttpPost postr = new HttpPost();
		try {
			postr.setURI(new URI("https://www.orange.pl/start.phtml"));
		} catch (URISyntaxException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		try {
			resp = klient.execute(postr, localContext);
		
			int a=0;
			while(a<cookieJar.getCookies().size())
			{
			Log.v("cookie", cookieJar.getCookies().get(a).getName() + "=" + cookieJar.getCookies().get(a).getValue());
			a++;
			}
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		try {
			postr.setURI(new URI("https://www.orange.pl/start.phtml?_DARGS=/gear/infoportal/header/user-box.jsp"));
		} catch (URISyntaxException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		//HttpHost host = new HttpHost("http://www.orange.pl/start.phtml?_DARGS=/gear/infoportal/header/user-box.jsp");
		HttpHost host = new HttpHost("www.google.pl");
		

		params.setParameter("_dyncharset", "UTF-8");
		params.setParameter("/ptk/map/infoportal/portlet/header/formhandler/ProxyProfileFormhandler.userLogin", user);
		params.setParameter("_D:/ptk/map/infoportal/portlet/header/formhandler/ProxyProfileFormhandler.userLogin", " ");
		params.setParameter("/ptk/map/infoportal/portlet/header/formhandler/ProxyProfileFormhandler.userPassword", passwd);
		params.setParameter("_D:/ptk/map/infoportal/portlet/header/formhandler/ProxyProfileFormhandler.userPassword", " ");
		params.setParameter("/ptk/map/infoportal/portlet/header/formhandler/ProxyProfileFormhandler.successUrl", "/start.phtml");
		params.setParameter("_D:/ptk/map/infoportal/portlet/header/formhandler/ProxyProfileFormhandler.successUrl", " ");
		params.setParameter("/ptk/map/infoportal/portlet/header/formhandler/ProxyProfileFormhandler.errorUrl", "/start.phtml");
		params.setParameter("_D:/ptk/map/infoportal/portlet/header/formhandler/ProxyProfileFormhandler.errorUrl", " ");
		params.setParameter("/ptk/map/infoportal/portlet/header/formhandler/ProxyProfileFormhandler.login", "loguj");
		params.setParameter("_D:/ptk/map/infoportal/portlet/header/formhandler/ProxyProfileFormhandler.login", " "); 
		params.setParameter("x", "23");
		params.setParameter("y", "5");
		params.setParameter("_DARGS", "/gear/infoportal/header/user-box.jsp");
		List<NameValuePair> pairs = new ArrayList<NameValuePair>(); 
	
		pairs.add(new BasicNameValuePair("_dyncharset", "UTF-8"));
		pairs.add(new BasicNameValuePair("/ptk/map/infoportal/portlet/header/formhandler/ProxyProfileFormhandler.userLogin", user));
		pairs.add(new BasicNameValuePair("_D:/ptk/map/infoportal/portlet/header/formhandler/ProxyProfileFormhandler.userLogin", " "));
		pairs.add(new BasicNameValuePair("/ptk/map/infoportal/portlet/header/formhandler/ProxyProfileFormhandler.userPassword", passwd));
		pairs.add(new BasicNameValuePair("_D:/ptk/map/infoportal/portlet/header/formhandler/ProxyProfileFormhandler.userPassword", " "));
		pairs.add(new BasicNameValuePair("/ptk/map/infoportal/portlet/header/formhandler/ProxyProfileFormhandler.successUrl", "/start.phtml"));
		pairs.add(new BasicNameValuePair("_D:/ptk/map/infoportal/portlet/header/formhandler/ProxyProfileFormhandler.successUrl", " "));
		pairs.add(new BasicNameValuePair("/ptk/map/infoportal/portlet/header/formhandler/ProxyProfileFormhandler.errorUrl", "/start.phtml"));
		pairs.add(new BasicNameValuePair("_D:/ptk/map/infoportal/portlet/header/formhandler/ProxyProfileFormhandler.errorUrl", " "));
		pairs.add(new BasicNameValuePair("/ptk/map/infoportal/portlet/header/formhandler/ProxyProfileFormhandler.login", "loguj"));
		pairs.add(new BasicNameValuePair("_D:/ptk/map/infoportal/portlet/header/formhandler/ProxyProfileFormhandler.login", " ")); 
		pairs.add(new BasicNameValuePair("x", "23"));
		pairs.add(new BasicNameValuePair("y", "5"));
		pairs.add(new BasicNameValuePair("_DARGS", "/gear/infoportal/header/user-box.jsp"));
		
		try {
		postr.setEntity(new UrlEncodedFormEntity(pairs));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
		e1.printStackTrace();
		} 
		try {
			postr.setParams(params);
			postr.setHeader("Accept", "application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
			postr.setHeader("Content-Type", "application/x-www-form-urlencoded");
			postr.setHeader("Origin", "http://www.orange.pl");
			postr.setHeader("Referer", "http://www.orange.pl/start.phtml");
			postr.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/534.13 (KHTML, like Gecko) Chrome/9.0.597.98 Safari/534.13");
			resp = klient.execute(postr, localContext);
			/*int a=0;
			while(a<cookieJar.getCookies().size())
			{
			Log.v("cookie", cookieJar.getCookies().get(a).getName() + "=" + cookieJar.getCookies().get(a).getValue());
			a++;
			} */
		/*	HttpPost postr2 = new HttpPost();
			try {
				postr2.setURI(new URI("http://www.orange.pl/portal/map/map/message_box?mbox_edit=new&stamp=1298549505941&mbox_view=newsms"));
			} catch (URISyntaxException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		//	resp = klient.execute(postr2);
		*/} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		HttpPost postr2 = new HttpPost();
		HttpGet getr = new HttpGet();
		try {
			
			getr.setURI(new URI("http://www.orange.pl/portal/map/map/corpo_message_box?mbox_edit=new&stamp=1298579037096&mbox_view=newsms"));
			
		} catch (URISyntaxException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} 
		
		try {
			resp = klient.execute(getr, localContext);
			
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		BufferedReader in=null;
		 try {
			in = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         StringBuffer sb = new StringBuffer("");
         String line = "";
         String NL = System.getProperty("line.separator");
         try {
			while ((line = in.readLine()) != null) {
			     sb.append(line + NL);
			 }
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
         try {
			in.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int token_indx = sb.indexOf("MessageFormHandler.token");
		token_indx=token_indx-74;
		String smstoken = sb.substring(token_indx, token_indx+15);
		Log.v("cookie",smstoken);
		
		String message = stripNonAscii(strings[0]);
		List<NameValuePair> pairs1 = new ArrayList<NameValuePair>(); 
		
		pairs1.add(new BasicNameValuePair("_dyncharset", "UTF-8"));
		pairs1.add(new BasicNameValuePair("/amg/ptk/map/messagebox/formhandlers/MessageFormHandler.type", "sms"));
		pairs1.add(new BasicNameValuePair("_D:/amg/ptk/map/messagebox/formhandlers/MessageFormHandler.type", " "));
		pairs1.add(new BasicNameValuePair("enabled", "false"));
		pairs1.add(new BasicNameValuePair("/amg/ptk/map/messagebox/formhandlers/MessageFormHandler.errorURL", "/portal/map/map/message_box?mbox_view=newsms"));
		pairs1.add(new BasicNameValuePair("_D:/amg/ptk/map/messagebox/formhandlers/MessageFormHandler.errorURL", " "));
		pairs1.add(new BasicNameValuePair("/amg/ptk/map/messagebox/formhandlers/MessageFormHandler.successURL", "/portal/map/map/message_box?mbox_view=messageslist"));
		pairs1.add(new BasicNameValuePair("_D:/amg/ptk/map/messagebox/formhandlers/MessageFormHandler.successURL", " "));
		pairs1.add(new BasicNameValuePair("/amg/ptk/map/messagebox/formhandlers/MessageFormHandler.to", strings[1]));
		pairs1.add(new BasicNameValuePair("_D:/amg/ptk/map/messagebox/formhandlers/MessageFormHandler.to", " "));
		pairs1.add(new BasicNameValuePair("_D:/amg/ptk/map/messagebox/formhandlers/MessageFormHandler.body", " "));
		pairs1.add(new BasicNameValuePair("/amg/ptk/map/messagebox/formhandlers/MessageFormHandler.body", message));
		pairs1.add(new BasicNameValuePair("/amg/ptk/map/messagebox/formhandlers/MessageFormHandler.token", smstoken));
		pairs1.add(new BasicNameValuePair("_D:/amg/ptk/map/messagebox/formhandlers/MessageFormHandler.token", " "));
		pairs1.add(new BasicNameValuePair("/amg/ptk/map/messagebox/formhandlers/MessageFormHandler.create.x", "51"));
		pairs1.add(new BasicNameValuePair("/amg/ptk/map/messagebox/formhandlers/MessageFormHandler.create.y", "9"));
		pairs1.add(new BasicNameValuePair("/amg/ptk/map/messagebox/formhandlers/MessageFormHandler.create", "Wyœlij"));
		pairs1.add(new BasicNameValuePair("_D:/amg/ptk/map/messagebox/formhandlers/MessageFormHandler.create", " "));
		pairs1.add(new BasicNameValuePair("_DARGS", "/gear/mapmessagebox/smsform.jsp"));	
		try {
		postr.setEntity(new UrlEncodedFormEntity(pairs1));
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
		e1.printStackTrace();
		} 
		try {
			postr.setURI(new URI("http://www.orange.pl/portal/map/map/message_box?_DARGS=/gear/mapmessagebox/smsform.jsp"));
		} catch (URISyntaxException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		postr.setHeader("Accept", "application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
		postr.setHeader("Content-Type", "application/x-www-form-urlencoded");
		postr.setHeader("Origin", "http://www.orange.pl");
		postr.setHeader("Referer", "http://www.orange.pl/portal/map/map/message_box?mbox_edit=new&stamp=1298580685001&mbox_view=newsms");
		postr.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/534.13 (KHTML, like Gecko) Chrome/9.0.597.98 Safari/534.13");
		try {
			resp = klient.execute(postr, localContext);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	/*	 try {
				in = new BufferedReader(new InputStreamReader(resp.getEntity().getContent()));
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	         sb = new StringBuffer("");
	         line = "";
	         NL = System.getProperty("line.separator");
	         try {
				while ((line = in.readLine()) != null) {
				     sb.append(line + NL);
				 }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	         try {
				in.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		int a=0;
		while(a+50<sb.length())
		{
         String page = sb.substring(a,a+50);
         Log.v("html", page);
         a=a+50;
		} */
		//txt_wiadomosc.setText("");
		//dialog.dismiss();
		 final String TELEPHON_NUMBER_FIELD_NAME = "address";
		 final String MESSAGE_BODY_FIELD_NAME = "body";
		 final Uri SENT_MSGS_CONTET_PROVIDER = Uri.parse("content://sms/sent");
		
		  

		ContentValues sentSms = new ContentValues();
		     sentSms.put(TELEPHON_NUMBER_FIELD_NAME, strings[1]);
		     sentSms.put(MESSAGE_BODY_FIELD_NAME, message);
		     ContentResolver contentResolver = getContentResolver();
		     contentResolver.insert(SENT_MSGS_CONTET_PROVIDER, sentSms);

		return null;
		}
	}



	private String stripNonAscii(String msg) {
		msg=msg.replace("¥", "A");
		msg=msg.replace("¹", "a");
		msg=msg.replace("Æ", "C");
		msg=msg.replace("æ", "c");
		msg=msg.replace("Ê", "E");
		msg=msg.replace("ê", "e");
		msg=msg.replace("£", "L");
		msg=msg.replace("³", "l");
		msg=msg.replace("Ñ", "N");
		msg=msg.replace("ñ", "n");
		msg=msg.replace("Ó", "O");
		msg=msg.replace("ó", "o");
		msg=msg.replace("Œ", "S");
		msg=msg.replace("œ", "s");
		msg=msg.replace("", "Z");
		msg=msg.replace("Ÿ", "z");
		msg=msg.replace("¯", "Z");
		msg=msg.replace("¿", "z");
		return msg;
	}
}