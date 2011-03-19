package pl.gers.sesemes;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
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
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Ustawienia extends Activity implements OnClickListener {

	
	ExpandableListAdapter mAdapter;
	Integer noofaccs;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.ustawienia);
	 
		ListView lv1=(ListView)findViewById(R.id.lv_listakont);
		 
		 lv1.setOnItemClickListener(new OnItemClickListener() {
			    public void onItemClick(AdapterView<?> parent, View view,
			        int position, long id) {
			      // When clicked, show a toast with the TextView text
			      Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
			          Toast.LENGTH_SHORT).show();
			      
			      Intent myint = new Intent(getApplicationContext(), Konto.class);
			  	myint.putExtra("nazwa_konta", ((TextView) view).getText());
			  	myint.putExtra("numer_konta", noofaccs);
			  	
			  	startActivity(myint);
			    }
			  });
		 
				
		Button btn1 = (Button)findViewById(R.id.btn_newaccount);
		btn1.setOnClickListener(this);
		Button btn2 = (Button)findViewById(R.id.btn_clear);
		btn2.setOnClickListener(this);
	    
	}

	public void onWindowFocusChanged (boolean hasFocus) {
		if (hasFocus)
		{
		SharedPreferences prefs = getSharedPreferences("prefs", 0);
        noofaccs = prefs.getInt("no_of_accounts", 0);
        ArrayList<String> accounts = new ArrayList<String>();
        String allaccnames = prefs.getString("allaccnames", ";");
        String accnames[] = TextUtils.split(allaccnames, ";");
        if (accnames.length>1)
        {
        for(Integer a=1;a<accnames.length-1;a++)
        {
        accounts.add(accnames[a]);
        }
        }
        
		 
       
       // String test = prefs.getString("test", "");
       // View txt_user1 = (View)mAdapter.getChild(0,0);
       // txt_user1.setText(test);
	   
	    
	   
	
		ListView lv1=(ListView)findViewById(R.id.lv_listakont);
		//if (noofaccs>0)
		//{
			lv1.setAdapter(new ArrayAdapter<String>(this, R.layout.list_item, (String[]) accounts.toArray(new String [accounts.size ()])));
	//}
		}
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
	 switch(v.getId()){
	 
	 case R.id.btn_newaccount:
	Intent myint = new Intent(this, Konto.class);
	myint.putExtra("numer_konta", noofaccs+1);
	
	startActivity(myint);
	break;
	
	 case R.id.btn_clear:
	SharedPreferences prefs = getSharedPreferences("prefs", 0);
	SharedPreferences.Editor edytor = prefs.edit();
	edytor.clear();
	edytor.commit();
	onWindowFocusChanged(true);
	break;
	 }
}

}
