package de.fau.cs.mad.gamekobold.toolbox;

import java.util.ArrayList;
import java.util.Random;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.R.color;

public class ToolboxRandomListActivity extends Activity {

	public String[] names = { "Albert", "Bertram", "Claudio", "Dennis",
			"Emanuela", "Franzi", "Gretchen", "Hanna", "Ida" };
	public ArrayList<String> char_array = new ArrayList<String>();
	int mSelectedItem = -1;

	ArrayList<String> listItems = new ArrayList<String>();
	ArrayAdapter<String> adapter;
	ListView lv_randomlist;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_game_toolbox_randomlist);

		for (String item : names)
			char_array.add(item);
		setListView();
	}

	public void randomCharList(View v) {
		shuffleArray(names);
	}

	public void shuffleArray(String[] ar) {
		Random rnd = new Random();
		for (int i = ar.length - 1; i > 0; i--) {
			int index = rnd.nextInt(i + 1);
			String a = ar[index];
			ar[index] = ar[i];
			ar[i] = a;
			char_array.removeAll(char_array);
			for (String item : ar)
				char_array.add(item);
			setListView();
		}
	}

	public void setListView() {

		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.randomlist_layout);
		lv_randomlist = new ListView(ToolboxRandomListActivity.this);
		final ToolboxRandomListElementAdapter adp = new ToolboxRandomListElementAdapter(
				ToolboxRandomListActivity.this, char_array, mSelectedItem);
		lv_randomlist.setBackgroundColor(getResources().getColor(
				R.color.background_dark));
		lv_randomlist.setDivider(new ColorDrawable(this.getResources()
				.getColor(R.color.background_green)));
		lv_randomlist.setDividerHeight(1);
		lv_randomlist.setAdapter(adp);
		lv_randomlist.setClickable(true);
		lv_randomlist.setId(0);
		lv_randomlist.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		lv_randomlist.setItemsCanFocus(false);
		lv_randomlist
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> a, View v,
							int position, long id) {
						SparseBooleanArray b_ar = lv_randomlist
								.getCheckedItemPositions();
						for (int i = 0; i<b_ar.size();i++){
							Log.i("puh", "b_ar"+ position + b_ar.get(position));
						}
						if (b_ar.get(position) == false) {
							lv_randomlist.setItemChecked(position, true);
							v.setBackgroundResource(R.color.background_green);
							Log.i("position", "true");
						} else {
							lv_randomlist.setItemChecked(position, false);
							v.setBackgroundResource(R.color.background_dark);
							Log.i("position", "false");
						}

						// Log.i("position", String.valueOf(position));
						// mSelectedItem = position;
						// Log.i("selected", String.valueOf(mSelectedItem));
						adp.notifyDataSetChanged();
					}

				});

		relativeLayout.addView(lv_randomlist);
		setContentView(relativeLayout);

	}

}
