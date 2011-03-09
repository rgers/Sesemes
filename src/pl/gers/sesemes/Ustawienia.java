package pl.gers.sesemes;

import android.app.ExpandableListActivity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.ExpandableListAdapter;
import android.widget.TextView;

public class Ustawienia extends ExpandableListActivity implements TextWatcher{

	
	ExpandableListAdapter mAdapter;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.ustawienia);
	    mAdapter = new MyExpandableListAdapter();
	    setListAdapter(mAdapter);
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



public class MyExpandableListAdapter extends BaseExpandableListAdapter {
    // Sample data set.  children[i] contains the children (String[]) for groups[i].
    private String[] groups = { "People Names", "Dog Names", "Cat Names", "Fish Names" };
    private String[][] children = {
            { "Arnold", "Barry", "Chuck", "David" },
            { "Ace", "Bandit", "Cha-Cha", "Deuce" },
            { "Fluffy", "Snuggles" },
            { "Goldy", "Bubbles" }
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
        TextView textView = getGenericView();
        textView.setText(getChild(groupPosition, childPosition).toString());
        return textView;
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

}
