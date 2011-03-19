package pl.gers.sesemes;

import java.util.Random;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class Konto extends Activity implements OnClickListener{

	
	Integer thisaccno, noofaccs, acchash, sms_free, sms_paid;
	String thisaccname;
	Boolean just_deleted=false;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.konto);
	    SharedPreferences prefs = getSharedPreferences("prefs", 0);
	    Intent acint = getIntent();
	    thisaccno = acint.getIntExtra("numer_konta", 0);
	    noofaccs = prefs.getInt("no_of_accounts", 0);
	    Random rnd = new Random();
	    thisaccname = acint.getStringExtra("nazwa_konta");
	    if (thisaccname==null)
	    {
	    	thisaccname="Konto "+thisaccno.toString();
	    }
	    acchash = prefs.getInt(thisaccname+"_hash", rnd.nextInt());
	    sms_free = prefs.getInt(acchash+"smsy_free", 0);
	    sms_paid = prefs.getInt(acchash+"smsy_paid", 0);
	    TextView txt_fsms = (TextView)findViewById(R.id.txt_fsms);
	    txt_fsms.setText("Smsy bezp³atne: "+sms_free.toString()+"\nSmsy z do³adowania: "+sms_paid.toString());
	    EditText txt_nazwa = (EditText)findViewById(R.id.txt_name);
	    EditText txt_user = (EditText)findViewById(R.id.txt_user);
	    EditText txt_passwd = (EditText)findViewById(R.id.txt_pass);
	    CheckBox chk_def = (CheckBox) findViewById(R.id.chk_default);
	    Button btn_del = (Button) findViewById(R.id.btn_delaccount);
	    btn_del.setOnClickListener(this);
	    txt_nazwa.setText(prefs.getString("account_"+acchash.toString()+"_name",thisaccname));
	    txt_user.setText(prefs.getString("account_"+acchash.toString()+"_user",""));
	    txt_passwd.setText(prefs.getString("account_"+acchash.toString()+"_passwd",""));
	    if(prefs.getInt("account_default",1)==acchash || noofaccs==0)
	    		{
	    	chk_def.setChecked(true);
	    		}
	   
	   
	}
	
	public void onPause()
	{
		if(!just_deleted)
		{
		String allaccnames;
		SharedPreferences prefs = getSharedPreferences("prefs", 0);
		allaccnames = prefs.getString("allaccnames", ";");
		SharedPreferences.Editor edytor = prefs.edit();
		EditText txt_nazwa = (EditText) findViewById(R.id.txt_name);
		EditText txt_user = (EditText) findViewById(R.id.txt_user);
		EditText txt_passwd = (EditText) findViewById(R.id.txt_pass);
		CheckBox chk_def = (CheckBox) findViewById(R.id.chk_default);
		
		if (thisaccno>noofaccs)
	    {
		if (!allaccnames.contains(";"+txt_nazwa.getText().toString()+";"))
		{
	    edytor.putInt("no_of_accounts", thisaccno);
	    noofaccs=thisaccno;
	    edytor.putInt(txt_nazwa.getText().toString()+"_hash", acchash);
	    edytor.putString(acchash.toString(), txt_nazwa.getText().toString());
	    edytor.putString("allaccnames", allaccnames+txt_nazwa.getText().toString()+";");
		}
	    }else{
		
		if(thisaccname!=txt_nazwa.getText().toString())
		{
			if (!allaccnames.contains(";"+txt_nazwa.getText().toString()+";"))
			{
			edytor.remove(thisaccname+"_hash");
			allaccnames.replace(";"+thisaccname+";", ";"+txt_nazwa.getText().toString()+";");
			edytor.putInt(txt_nazwa.getText().toString()+"_hash", acchash);
		    edytor.putString(acchash.toString(), txt_nazwa.getText().toString());
		    edytor.putString("allaccnames", allaccnames);
			}
		}
	    }
		
		edytor.putString("account_"+acchash.toString()+"_name", txt_nazwa.getText().toString());
		edytor.putString("account_"+acchash.toString()+"_user", txt_user.getText().toString());
		edytor.putString("account_"+acchash.toString()+"_passwd", txt_passwd.getText().toString());
		if (chk_def.isChecked())
		{
		edytor.putInt("account_default", acchash);
		}
		
		edytor.commit();
		}
		 super.onPause();
		  finish();
	}
	public void onWindowFocusChanged (boolean hasFocus) {
		if (hasFocus)
		{just_deleted=false;}
	}
	@Override
	public void onClick(View v) {
		just_deleted=true;
		SharedPreferences prefs = getSharedPreferences("prefs", 0);
		String allaccnames = prefs.getString("allaccnames", ";");
		SharedPreferences.Editor edytor = prefs.edit();
		edytor.remove(acchash.toString());
		edytor.remove(thisaccname+"_hash");
		edytor.remove("account_"+acchash.toString()+"_name");
		edytor.remove("account_"+acchash.toString()+"_user");
		edytor.remove("account_"+acchash.toString()+"_passwd");
		allaccnames = allaccnames.replace(";"+thisaccname+";", ";");
		edytor.putString("allaccnames", allaccnames);
		edytor.putInt("no_of_accounts", noofaccs-1);
		edytor.commit();
		finish();
	}

	

}
