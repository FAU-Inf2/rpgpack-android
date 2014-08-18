package de.fau.cs.mad.gamekobold.game;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Random;

import de.fau.cs.mad.gamekobold.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ToolboxDiceActivity extends Activity{

	ArrayList<String> dice_list = new ArrayList<String>();
	ArrayList<String> rolled_dice = new ArrayList<String>();
	GridView mGridView;
	Boolean isGrid = false;
	private TextView tv_sum;
	ToolboxDiceElementAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_game_toolbox_dice);
		setGridView();
		tv_sum = (TextView) findViewById(R.id.tv_sum);

	}

	@Override
	public void onSaveInstanceState(Bundle icicle) {
		super.onSaveInstanceState(icicle);
		icicle.putStringArrayList("dice_list", dice_list);
		icicle.putStringArrayList("rolled_dice", rolled_dice);
		icicle.putBoolean("isGrid", isGrid);
		icicle.putString("sum", tv_sum.getText().toString());
	}

	@Override
	public void onRestoreInstanceState(Bundle icicle) {
		rolled_dice.removeAll(rolled_dice);
		dice_list.removeAll(dice_list);
		rolled_dice.addAll(icicle.getStringArrayList("rolled_dice"));
		dice_list.addAll(icicle.getStringArrayList("dice_list"));
		isGrid = icicle.getBoolean("isGrid");

		if (isGrid) {
			setGridView();
			tv_sum.setText(icicle.getString("sum"));
		}
	}
	
	public void addDice(View v) {

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

		popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				String dice = "";
				if (dice_list.size() < 9) {

					switch (item.getItemId()) {
					case R.id.item_d4:
						dice = "4";
						break;
					case R.id.item_d6:
						dice = "6";
						break;
					case R.id.item_d8:
						dice = "8";
						break;
					case R.id.item_d10:
						dice = "10";
						break;
					case R.id.item_d12:
						dice = "12";
						break;
					case R.id.item_d20:
						dice = "20";
						break;
					default:
						break;
					}

					dice_list.add(dice);
					rolled_dice.add(dice);
					mAdapter.notifyDataSetChanged();
				}

				return true;
			}
		});
		popup.show();
	}

	public void rollDice(View v) {
		final int size = mGridView.getChildCount();
		int sum = 0;
		for (int i = 0; i < size; i++) {
			ViewGroup gridChild = (ViewGroup) mGridView.getChildAt(i);
			int childSize = gridChild.getChildCount();
			for (int k = 0; k < childSize; k++) {
				if (gridChild.getChildAt(k) instanceof TextView) {
					TextView tmp = (TextView) gridChild.getChildAt(k);
					int maxValue = Integer.parseInt((String) tmp.getHint());
					int dice = diceRoller(maxValue);
					sum = sum + dice;
					rolled_dice.set(i, String.valueOf(dice));
				}
			}
		}
		mAdapter.notifyDataSetChanged();
		TextView tv_sum = (TextView) findViewById(R.id.tv_sum);
		tv_sum.setText("Sum: " + String.valueOf(sum));
	}

	static int diceRoller(int maxValue) {
		Random rnd = new Random();
		int rndint = (rnd.nextInt(maxValue)) + 1;
		return rndint;
	}

	public String getDiceNum(String s) {
		s = s.replaceAll("\\D+", "");
		return s;
	}

	public void clearView(View v) {
		isGrid = false;
		setContentView(R.layout.activity_game_toolbox_dice);
		tv_sum.setText("");
		dice_list.removeAll(dice_list);
		rolled_dice.removeAll(rolled_dice);
	}

	public void rollSingleDice(int position) {
		int sum = 0;
		int maxValue = Integer.valueOf(dice_list.get(position));
		int dice = diceRoller(maxValue);
		rolled_dice.set(position, String.valueOf(dice));

		for (String item : rolled_dice) {
			sum = sum + Integer.valueOf(item);
		}

		TextView tv_sum = (TextView) findViewById(R.id.tv_sum);
		tv_sum.setText("Sum: " + String.valueOf(sum));
		mAdapter.notifyDataSetChanged();
	}

	public void setGridView() {

		isGrid = true;
		RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.random_layout);
		mGridView = new GridView(ToolboxDiceActivity.this);
		mAdapter = new ToolboxDiceElementAdapter(
				ToolboxDiceActivity.this, dice_list, rolled_dice);
		mGridView.setNumColumns(3);
		mGridView.setBackgroundColor(getResources()
				.getColor(R.color.background_dark));
		mGridView.setAdapter(mAdapter);
		mGridView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> av, View v,final int position, long id) {

               rollSingleDice(position);
            }
		});
		relativeLayout.addView(mGridView);
		setContentView(relativeLayout);

	}
}