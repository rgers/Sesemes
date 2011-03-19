package pl.gers.sesemes;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ProtocolException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.RedirectHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import pl.gers.sesemes.R;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;



public class Sesemes extends Activity implements OnClickListener, TextWatcher{
    /** Called when the activity is first created. */
	String user, passwd;
	Integer smsy_free, smsy_paid;
	Integer acc_hash=0;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        Intent intent = getIntent();
        Uri int_uri = intent.getData();
        String tel=null;
        
        if (int_uri!=null)
        	{
        	
      tel =int_uri.getEncodedSchemeSpecificPart();
      EditText txt_numer = (EditText) findViewById(R.id.txt_numer);
      txt_numer.setText(tel);
        	}
        Button btn_wyslij = (Button) findViewById(R.id.btn_wyslij);
		btn_wyslij.setOnClickListener(this);
		ImageButton btn_contact = (ImageButton) findViewById(R.id.btn_contact);
		btn_contact.setOnClickListener(this);
		EditText txt_wiadomosc = (EditText) findViewById(R.id.txt_wiadomosc);
		txt_wiadomosc.addTextChangedListener(this);
		
		
		    }
    
    public void onResume() {
		
			SharedPreferences prefs = getSharedPreferences("prefs", 0);
	        String allaccnames = prefs.getString("allaccnames", ";");
	        Integer noofaccs = prefs.getInt("no_of_accounts", 0);
	        if(noofaccs>1)
	        {
	        	TextView txt_konta = (TextView) this.findViewById(R.id.textView1);
	            txt_konta.setVisibility(View.VISIBLE);
	        	 Spinner spn_konta = (Spinner) findViewById(R.id.spn_konta);
	        	 spn_konta.setVisibility(View.VISIBLE);
	        	 ArrayList<String> accounts = new ArrayList<String>();
	        	 Integer selected_pos=0;
	             if (noofaccs>0)
	             {
	             
	             String accnames[] = TextUtils.split(allaccnames, ";");
	             int b=prefs.getInt("account_default",1);
	             for(Integer a=1;a<accnames.length-1;a++)
	             {
	             accounts.add(accnames[a]);
	                          
	             if (b==prefs.getInt(accnames[a]+"_hash", 0))
	             {
	            	 selected_pos=a-1;
	             }
	             }
	             }
	             
	             spn_konta.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, (String[]) accounts.toArray(new String [accounts.size ()])));
	             spn_konta.setSelection(selected_pos);
	        }else{
	        	TextView txt_konta = (TextView) this.findViewById(R.id.textView1);
	            txt_konta.setVisibility(View.GONE);
	        Spinner spn_konta = (Spinner) findViewById(R.id.spn_konta);
	        spn_konta.setVisibility(View.GONE);
	        }
	        super.onResume();
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
    case R.id.menu_oautorze:
    	new AlertDialog.Builder(Sesemes.this)

		.setTitle("O Autorze...")

		.setMessage("Autor: Rados≥aw Gers\nE-Mail: sesemes@10g.pl\nWszelkie prawa zastrzeøone.")

		.setNeutralButton("Ok",

		new DialogInterface.OnClickListener() {

		public void onClick(DialogInterface dialog2,

		int which) {
			dialog2.dismiss();

		}

		}).show();
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
			
			SharedPreferences prefs = getSharedPreferences("prefs", 0);
			EditText txt_wiadomosc = (EditText) findViewById(R.id.txt_wiadomosc);
		EditText txt_numer = (EditText) findViewById(R.id.txt_numer);
		if(prefs.getInt("no_of_accounts", 0)>1)
		{
		Spinner spn_konta = (Spinner) findViewById(R.id.spn_konta);
		
		acc_hash = prefs.getInt(spn_konta.getSelectedItem().toString()+"_hash",0);
		if (acc_hash!=0)
		{
        user = prefs.getString("account_"+acc_hash.toString()+"_user", "");
        passwd = prefs.getString("account_"+acc_hash.toString()+"_passwd", "");
		}}else{
			 if (prefs.getInt("no_of_accounts", 0)>0)
		        {
		        String allaccnames = prefs.getString("allaccnames", ";");
		        String accnames[] = TextUtils.split(allaccnames, ";");
		        acc_hash = prefs.getInt(accnames[1]+"_hash",0);		
		        if (acc_hash!=0)
				{
		        user = prefs.getString("account_"+acc_hash.toString()+"_user", "");
		        passwd = prefs.getString("account_"+acc_hash.toString()+"_passwd", "");
				}
		        }
		}
        if (user=="" || passwd=="" || prefs.getInt("no_of_accounts", 0)==0)
        {Toast.makeText(Sesemes.this, "Najpierw musisz dodaÊ konto.", Toast.LENGTH_LONG).show();ustawienia(); break;}
			new send_sms().execute(txt_wiadomosc.getText().toString(), txt_numer.getText().toString(), user, passwd, acc_hash.toString());
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

	private class send_sms extends AsyncTask<String, String, String>
	{
		ProgressDialog dialog;
		protected void onPreExecute()
		{
			dialog = ProgressDialog.show(Sesemes.this, "", "Wysy≥am. ProszÍ czekaÊ...", true);
		}
		
		protected void onPostExecute(String result)
		{
			dialog.dismiss();
			if (result!=null)
			{
			
			new AlertDialog.Builder(Sesemes.this)

			.setTitle("Niepowodzenie!")

			.setMessage(result)

			.setNeutralButton("Ok",

			new DialogInterface.OnClickListener() {

			public void onClick(DialogInterface dialog2,

			int which) {
				dialog2.dismiss();

			}

			}).show();}else{
				SharedPreferences prefs = getSharedPreferences("prefs", 0);
		        
			EditText wiadomosc = (EditText)findViewById(R.id.txt_wiadomosc);
			wiadomosc.setText("");
			Integer smsy_free = prefs.getInt(acc_hash+"smsy_free",0);
			Integer smsy_paid = prefs.getInt(acc_hash+"smsy_paid", 0);
			Toast.makeText(Sesemes.this, "WiadomoúÊ wys≥ana. Pozosta≥o " + smsy_free.toString() + " smsÛw bezp≥atnych oraz "+smsy_paid.toString()+" smsÛw z do≥adowania.", Toast.LENGTH_LONG).show();
			}
		}
		
		protected String doInBackground(String...strings)
		{
		if(strings[0].length()>640)
		{
			return "Maksymalnie moøna wys≥aÊ 4 smsy na raz (640 znakÛw).";
		}
		 HttpResponse resp = null;
		 SchemeRegistry schemeRegistry = new SchemeRegistry();
		 schemeRegistry.register(new Scheme("https", 
		             SSLSocketFactory.getSocketFactory(), 443));
		 HttpParams params = new BasicHttpParams();
		 HttpContext localContext = null;
		 HttpConnectionParams.setConnectionTimeout(params, 20000);
		 HttpConnectionParams.setSoTimeout(params, 20000);
		DefaultHttpClient klient = new DefaultHttpClient(params);
		RedirectHandler rh = new RedirectHandler() {
	        public URI getLocationURI(HttpResponse response,
	                HttpContext context) throws ProtocolException {
	            return null;
	        }

	        public boolean isRedirectRequested(HttpResponse response,
	                HttpContext context) {
	            return false;
	        }
	    };
		klient.setRedirectHandler(rh);
				
		CookieStore cookieJar = new BasicCookieStore();
		
		// Creating a local HTTP context
		localContext = new BasicHttpContext();

		// Bind custom cookie store to the local context
		localContext.setAttribute(ClientContext.COOKIE_STORE, cookieJar);
		
		HttpPost postr = new HttpPost();
			
		try {
			
		List<NameValuePair> pairs = new ArrayList<NameValuePair>(); 
	
		pairs.add(new BasicNameValuePair("_dyncharset", "UTF-8"));
		pairs.add(new BasicNameValuePair("/ptk/map/infoportal/portlet/header/formhandler/ProxyProfileFormhandler.userLogin", strings[2]));
		pairs.add(new BasicNameValuePair("_D:/ptk/map/infoportal/portlet/header/formhandler/ProxyProfileFormhandler.userLogin", " "));
		pairs.add(new BasicNameValuePair("/ptk/map/infoportal/portlet/header/formhandler/ProxyProfileFormhandler.userPassword", strings[3]));
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
		
		
		postr.setEntity(new UrlEncodedFormEntity(pairs));
		
			postr.setHeader("Accept", "application/xml,application/xhtml+xml,text/html;q=0.9,text/plain;q=0.8,image/png,*/*;q=0.5");
			postr.setHeader("Content-Type", "application/x-www-form-urlencoded");
			postr.setHeader("Origin", "http://www.orange.pl");
			postr.setHeader("Referer", "http://www.orange.pl/start.phtml");
			postr.setHeader("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-US) AppleWebKit/534.13 (KHTML, like Gecko) Chrome/9.0.597.98 Safari/534.13");
			postr.setURI(new URI("https://www.orange.pl/start.phtml?_DARGS=/gear/infoportal/header/user-box.jsp"));
			resp = klient.execute(postr, localContext);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return "Nie mogÍ nawiπzaÊ po≥πczenia z serwerem Orange. SprÛbuj ponownie.";
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e)
		{
			//return e.getMessage() + e.getCause().getMessage();
			return "Nie mogÍ nawiπzaÊ po≥πczenia z serwerem Orange. SprÛbuj ponownie.";
		}
		
		if(cookieJar.getCookies().size()<8)
		{
			return "B≥πd logowania. Sprawdü uøytkownika i has≥o.";
		}
		HttpGet getr = new HttpGet();
		try {
			
			getr.setURI(new URI("http://www.orange.pl/portal/map/map/corpo_message_box?mbox_edit=new&stamp=1298579037096&mbox_view=newsms"));
			
		} catch (URISyntaxException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		} 
		StringBuffer sb=null;
		try {
			resp = klient.execute(getr, localContext);
			sb = getHTMLBuffer(resp.getEntity().getContent());
		} catch (ClientProtocolException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			return "Nie mogÍ nawiπzaÊ po≥πczenia z serwerem Orange. SprÛbuj ponownie.";
		}
		
		
		int token_indx = sb.indexOf("MessageFormHandler.token");
		token_indx=token_indx-74;
		String smstoken = sb.substring(token_indx, token_indx+15);
		//Log.v("cookie",smstoken);
		SharedPreferences prefs = getSharedPreferences("prefs", 0);
	    SharedPreferences.Editor edytor = prefs.edit();
	    int indx = sb.indexOf("bezp≥atne :")+38;
	    int indx_end = sb.indexOf("<", indx);
	    //Log.v("Sesemes", sb.substring(indx,indx_end));

	    Integer smsy_free_ = Integer.valueOf(sb.substring(indx,indx_end))-1;
	    edytor.putInt(strings[4]+"smsy_free", smsy_free_.intValue());
	    indx = sb.indexOf("z do≥adowaÒ:", indx_end)+39;
	    if (indx!=38)
	    {
	    indx_end = sb.indexOf("<", indx);
	    Integer smsy_paid_ = Integer.valueOf(sb.substring(indx,indx_end));
	    edytor.putInt(strings[4]+"smsy_paid", smsy_paid_.intValue());
	    }
	    edytor.commit();
		
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
		pairs1.add(new BasicNameValuePair("/amg/ptk/map/messagebox/formhandlers/MessageFormHandler.create", "Wyúlij"));
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
			sb = getHTMLBuffer(resp.getEntity().getContent());
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			return "Nie mogÍ nawiπzaÊ po≥πczenia z serwerem Orange. SprÛbuj ponownie.";
		}
		if(sb.indexOf("Niepoprawny numer telefonu")==-1)
		{
		 final String TELEPHON_NUMBER_FIELD_NAME = "address";
		 final String MESSAGE_BODY_FIELD_NAME = "body";
		 final Uri SENT_MSGS_CONTET_PROVIDER = Uri.parse("content://sms/sent");
		 	ContentValues sentSms = new ContentValues();
		    sentSms.put(TELEPHON_NUMBER_FIELD_NAME, strings[1]);
		    sentSms.put(MESSAGE_BODY_FIELD_NAME, message);
		    ContentResolver contentResolver = getContentResolver();
		    contentResolver.insert(SENT_MSGS_CONTET_PROVIDER, sentSms);
		    
		    
		return null;
		}else{
			return "Niepoprawny numer telefonu";
		}
		}
	}


private StringBuffer getHTMLBuffer(InputStream input)
{
	BufferedReader in=null;
	 try {
		in = new BufferedReader(new InputStreamReader(input));
	} catch (IllegalStateException e) {
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
	return sb;
}
	private String stripNonAscii(String msg) {
		msg=msg.replace("•", "A");
		msg=msg.replace("π", "a");
		msg=msg.replace("∆", "C");
		msg=msg.replace("Ê", "c");
		msg=msg.replace(" ", "E");
		msg=msg.replace("Í", "e");
		msg=msg.replace("£", "L");
		msg=msg.replace("≥", "l");
		msg=msg.replace("—", "N");
		msg=msg.replace("Ò", "n");
		msg=msg.replace("”", "O");
		msg=msg.replace("Û", "o");
		msg=msg.replace("å", "S");
		msg=msg.replace("ú", "s");
		msg=msg.replace("è", "Z");
		msg=msg.replace("ü", "z");
		msg=msg.replace("Ø", "Z");
		msg=msg.replace("ø", "z");
		return msg;
	}
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		// TODO Auto-generated method stub
		
	}
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}
	public void afterTextChanged(Editable s) {
		TextView txt_znaki = (TextView) findViewById(R.id.txt_znaki);
		EditText txt_wiadomosc = (EditText) findViewById(R.id.txt_wiadomosc);
		Integer length = txt_wiadomosc.getText().length();
		txt_znaki.setText(length.toString());
		
	}
}