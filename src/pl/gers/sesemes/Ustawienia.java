package pl.gers.sesemes;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ExpandableListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Ustawienia extends Activity implements OnClickListener, TextWatcher{

	
	ExpandableListAdapter mAdapter;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.ustawienia);
	  /*  mAdapter = new MyExpandableListAdapter();
	    setListAdapter(mAdapter); */
	    SharedPreferences prefs = getSharedPreferences("prefs", 0);
        String user = prefs.getString("user", "");
        String passwd = prefs.getString("passwd", "");
       // String test = prefs.getString("test", "");
        EditText txt_user = (EditText) findViewById(R.id.txt_user);
        txt_user.setText(user);
        EditText txt_passwd = (EditText) findViewById(R.id.txt_pass);
        txt_passwd.setText(passwd);
       // View txt_user1 = (View)mAdapter.getChild(0,0);
       // txt_user1.setText(test);
	    txt_user.addTextChangedListener(this);
	    txt_passwd.addTextChangedListener(this);
	    
	   
	
		ListView lv1=(ListView)findViewById(R.id.listView1);
		 String[] COUNTRIES = new String[] {
			    "Konto 1", "Konto 2", "Konto 3" };
		 
		 lv1.setOnItemClickListener(new OnItemClickListener() {
			    public void onItemClick(AdapterView<?> parent, View view,
			        int position, long id) {
			      // When clicked, show a toast with the TextView text
			      Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
			          Toast.LENGTH_SHORT).show();
			    }
			  });
		 ArrayList<String> test = new ArrayList<String>();
		 test.
		lv1.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, COUNTRIES));
	    // TODO Auto-generated method stub
	}

	

	public void afterTextChanged(Editable arg0) {
		SharedPreferences prefs = getSharedPreferences("prefs", 0);
		SharedPreferences.Editor edytor = prefs.edit();
		EditText txt_user1 = (EditText)mAdapter.getChild(0,0);
		EditText txt_user = (EditText) findViewById(R.id.txt_user);
		EditText txt_passwd = (EditText) findViewById(R.id.txt_pass);
		edytor.putString("user", txt_user.getText().toString());
		edytor.putString("passwd", txt_passwd.getText().toString());
		edytor.putString("test", txt_user1.getText().toString());
		edytor.commit();
	}

	public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
			int arg3) {
		// TODO Auto-generated method stub
		
	}

	public void onTextChanged(CharSequence s, int start, int before, int count) {
		// TODO Auto-generated method stub
		
	}



public class MyExpandableListAdapter extends BaseExpandableListAdapter {
    // Sample data set.  children[i] contains the children (String[]) for groups[i].
    private String[] groups = { "Konto 1", "Konto 2", "Konto 3" };
    private String[][] children = {
            { "" },
            { "" },
            { "" }
    };

    public Object getChild(int groupPosition, int childPosition) {
        return children[groupPosition][childPosition];
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public int getChildrenCount(int groupPosition) {
        return children[groupPosition].length;
    }

    public TextView getGenericView() {
        // Layout parameters for the ExpandableListView
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, 64);

        TextView textView = new TextView(Ustawienia.this);
        textView.setLayoutParams(lp);
        // Center the text vertically
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        // Set the text starting position
        textView.setPadding(36, 0, 0, 0);
        return textView;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
            View convertView, ViewGroup parent) {
    	LinearLayout lej = new LinearLayout(Ustawienia.this);
    	LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 64);
    	EditText nazwa = new EditText(Ustawienia.this);
        nazwa.setHint("Nazwa");
        nazwa.setLayoutParams(lp);
        EditText user = new EditText(Ustawienia.this);
        user.setHint("U¿ytkownik");
        user.setLayoutParams(lp);
        EditText pass = new EditText(Ustawienia.this);
        pass.setHint("Has³o");
        pass.setLayoutParams(lp);
        lej.addView(nazwa);
        lej.addView(user);
        lej.addView(pass);
        lej.setOrientation(LinearLayout.VERTICAL);
        return lej;
    }

    public Object getGroup(int groupPosition) {
        return groups[groupPosition];
    }

    public int getGroupCount() {
        return groups.length;
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
            ViewGroup parent) {
        TextView textView = getGenericView();
        textView.setText(getGroup(groupPosition).toString());
        return textView;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean hasStableIds() {
        return true;
    }

}



public void onClick(View v) {
	Intent myint = new Intent(this, Konto.class);
	myint.putExtra("numer_konta", 1);
	
	startActivity(myint);
	
}

}
