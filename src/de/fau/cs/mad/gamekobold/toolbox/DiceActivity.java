package de.fau.cs.mad.gamekobold.toolbox;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;

import de.fau.cs.mad.gamekobold.R;
import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.GridView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class DiceActivity extends Activity {

	private ArrayList<String> mDice = new ArrayList<String>();
	private ArrayList<String> mRolledDice = new ArrayList<String>();
	private GridView mGridView;
	private TextView mTvSum;
	private DiceElementAdapter mDiceAdapter;
	private int mDisplayWidth;
	private int mColumnWidth;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_toolbox_dice);
		init();

	}

	private void init() {

		mTvSum = (TextView) findViewById(R.id.tv_sum);

		// Get display size and set column width
		Display display = getWindowManager().getDefaultDisplay();
		Point size = new Point();
		display.getSize(size);
		mDisplayWidth = size.x;
		mColumnWidth = mDisplayWidth / 3;

		// Create GridView
		setGridView();

	}

	// Save for Rotation
	@Override
	public void onSaveInstanceState(Bundle icicle) {
		super.onSaveInstanceState(icicle);
		icicle.putStringArrayList("mDice", mDice);
		icicle.putStringArrayList("mRolledDice", mRolledDice);
		icicle.putString("sum", mTvSum.getText().toString());
	}

	// Load after Rotation
	@Override
	public void onRestoreInstanceState(Bundle icicle) {
		mRolledDice.removeAll(mRolledDice);
		mDice.removeAll(mDice);
		mRolledDice.addAll(icicle.getStringArrayList("mRolledDice"));
		mDice.addAll(icicle.getStringArrayList("mDice"));
		setGridView();
		mTvSum.setText(icicle.getString("sum"));
	}

	// Button function to add a new die chosen from menu
	public void addDie(View v) {

		// Create popup menu
		PopupMenu popup = new PopupMenu(getBaseContext(), v);
		popup.getMenuInflater().inflate(R.menu.game_toolbox_dice,
				popup.getMenu());

		try {
			Field[] fields = popup.getClass().getDeclaredFields();
			for (Field field : fields) {
				if ("mPopup".equals(field.getName())) {
					field.setAccessible(true);
					Object menuPopupHelper = field.get(popup);
					Class<?> classPopupHelper = Class.forName(menuPopupHelper
							.getClass().getName());
					Method setForceIcons = classPopupHelper.getMethod(
							"setForceShowIcon", boolean.class);
					setForceIcons.invoke(menuPopupHelper, true);
					break;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// make items clickable and add new item to mRolledDice and mDice
		popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				String die = "";
				if (mDice.size() < 9) {

					switch (item.getItemId()) {
					case R.id.item_d4:
						die = "4";
						break;
					case R.id.item_d6:
						die = "6";
						break;
					case R.id.item_d8:
						die = "8";
						break;
					case R.id.item_d10:
						die = "10";
						break;
					case R.id.item_d12:
						die = "12";
						break;
					case R.id.item_d20:
						die = "20";
						break;
					}

					mDice.add(die);
					mRolledDice.add(die);
					mDiceAdapter.notifyDataSetChanged();
				}

				return true;
			}
		});
		popup.show();
	}

	// Button function to roll all dice in GridView
	public void rollDice(View v) {
		final int size = mGridView.getChildCount();
		for (int i = 0; i < size; i++) {
			ViewGroup gridChild = (ViewGroup) mGridView.getChildAt(i);
			int childSize = gridChild.getChildCount();
			for (int k = 0; k < childSize; k++) {
				if (gridChild.getChildAt(k) instanceof TextView) {
					TextView tmp = (TextView) gridChild.getChildAt(k);
					if ((String) tmp.getHint() != null) {
						int maxValue = Integer.parseInt((String) tmp.getHint());
						int dice = dieRoller(maxValue);
						mRolledDice.set(i, String.valueOf(dice));
					}
				}
			}
		}
		mDiceAdapter.notifyDataSetChanged();
		setSum();
	}

	// roll die with given maxValue
	static int dieRoller(int maxValue) {
		Random rnd = new Random();
		int rndint = (rnd.nextInt(maxValue)) + 1;
		return rndint;
	}

	// remove single die
	public void removeDie(int position) {
		mDice.remove(position);
		mRolledDice.remove(position);
		mDiceAdapter.notifyDataSetChanged();
		setSum();
	}

	// set the sum textview with added values from mRolledDice
	public void setSum() {
		int sum = 0;
		for (String item : mRolledDice)
			sum = sum + Integer.valueOf(item);
		mTvSum.setText("Sum: " + String.valueOf(sum));

	}

	//Build GridView for holding dice
	public void setGridView() {

		//set mGridView attributes
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.random_layout);
		mGridView = new GridView(DiceActivity.this);
		mDiceAdapter = new DiceElementAdapter(DiceActivity.this,
				mDice, mRolledDice);
		mGridView.setNumColumns(3);
		mGridView.setGravity(Gravity.CENTER);
		mGridView.setColumnWidth(mColumnWidth);
		mGridView.setBackgroundColor(getResources().getColor(
				R.color.background_dark));
		mGridView.setAdapter(mDiceAdapter);

		//Make dice clickable and rollable
		mGridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> av, View v,
					final int position, long id) {
				if (mDice.get(position) != null) {
					int maxValue = Integer.valueOf(mDice.get(position));
					int dice = dieRoller(maxValue);
					mRolledDice.set(position, String.valueOf(dice));
					mDiceAdapter.notifyDataSetChanged();
					setSum();
				}
			}
		});

		//Make dice longclickable for remove
		mGridView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> av, View v,
					final int position, long id) {
				removeDie(position);
				return true;
			}

		});
		relativeLayout.addView(mGridView);
		setContentView(relativeLayout);

	}
}