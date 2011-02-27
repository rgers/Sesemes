package pl.gers.sesemes;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class Ustawienia extends Activity implements TextWatcher{

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.ustawienia);
	   
	    SharedPreferences prefs = getSharedPreferences("prefs", 0);
        String user = prefs.getString("user", "");
        String passwd = prefs.getString("passwd", "");
        EditText txt_user = (EditText) findViewById(R.id.txt_user);
        txt_user.setText(user);
        EditText txt_passwd = (EditText) findViewById(R.id.txt_pass);
        txt_passwd.setText(passwd);
	    txt_user.addTextChangedListener(this);
	    txt_passwd.addTextChangedListener(this);
	
	    // TODO Auto-generated method stub
	}

	

	public void afterTextChanged(Editable arg0) {
		SharedPreferences prefs = getSharedPreferences("prefs", 0);
		SharedPreferences.Editor edytor = prefs.edit();
		EditText txt_user = (EditText) findViewById(R.id.txt_user);
		EditText txt_passwd = (EditText) findViewById(R.id.txt_pass);
		edytor.putString("user", txt_user.getText().toString());
		edytor.putString("passwd", txt_passwd.getText().toString());
		edytor.commit();
	}

	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}

}
