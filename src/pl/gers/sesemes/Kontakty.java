package pl.gers.sesemes;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

public class Kontakty extends Activity implements OnClickListener,OnItemClickListener{

	/** Called when the activity is first created. */
	String numer, nazwa;
	ArrayList<String> loglv, lognames, lognumbers;
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.kontakty);
	    numer="";
	    nazwa="";
	    ListView lv1=(ListView)findViewById(R.id.lv_calllog);
	    Uri allCalls = CallLog.Calls.CONTENT_URI; 
	    Cursor c = managedQuery(allCalls, null, null, null, null); 
	    lognumbers = new ArrayList<String>();
	    lognames = new ArrayList<String>();
	    loglv = new ArrayList<String>();
	    while(c.moveToNext() & c.getPosition()<6)
	    {
	    	if(!lognumbers.contains(c.getString(c.getColumnIndex(CallLog.Calls.NUMBER))))
	    	{
	    lognumbers.add(c.getString(c.getColumnIndex(CallLog.Calls.NUMBER)));
	    if(c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME))!=null)
	    {
	    lognames.add(c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME)));
	    loglv.add(c.getString(c.getColumnIndex(CallLog.Calls.CACHED_NAME)));
	    }else{lognames.add("");loglv.add(c.getString(c.getColumnIndex(CallLog.Calls.NUMBER)));}
	    	}
	    }
	    
	    Button btn_getcontact = (Button) findViewById(R.id.btn_contactbook);
	    btn_getcontact.setOnClickListener(this);
		lv1.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, (String[]) loglv.toArray(new String [loglv.size ()])));
		lv1.setOnItemClickListener(this);
	}
	
	public void get_contact()
	{
	Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds. Phone.CONTENT_URI);
	startActivityForResult(i, 1);
	}

	public void onClick(View v) {
		int id = v.getId();
		switch(id)
		{
		case (R.id.btn_contactbook):
		get_contact();
		break;
		}
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
	                        numer = c.getString(c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds. Phone.NUMBER)); 
	                        nazwa = c.getString(c.getColumnIndexOrThrow(ContactsContract.CommonDataKinds. Phone.DISPLAY_NAME));
	                                         }  
	                }
	            Intent res = new Intent();
	    		res.putExtra("nazwa", nazwa);
	    		res.putExtra("numer", numer);
	    		setResult(Activity.RESULT_OK, res);
	    		finish();
	                break;  
	        }  
	    }

	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		nazwa = lognames.get(position);
		numer = lognumbers.get(position);
		 Intent res = new Intent();
 		res.putExtra("nazwa", nazwa);
 		res.putExtra("numer", numer);
 		setResult(Activity.RESULT_OK, res);
 		finish();
		
	}  
}
