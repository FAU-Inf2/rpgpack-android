package de.fau.cs.mad.gamekobold.game;

import java.util.ArrayList;

import de.fau.cs.mad.gamekobold.R;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TableLayout;

public class ToolboxMapActivity extends Activity{

	private ImageButton currPaint;
	private ToolboxMapView mapView;
	private LinearLayout paintLayout;
	
	Activity mContext;
	float mWidth;
	float mHeight;
	private int[] testDots = { R.drawable.red_dot, R.drawable.green_dot, R.drawable.blue_dot, R.drawable.black_dot, R.drawable.orange_dot };
	private ArrayList<Integer> dotsList = new ArrayList();
	private int mNumCells;
	private int mNumLines;
	private int mNumColumns;
	private int cell_size;
	private float density;
	private boolean drag_active;
	private ToolboxMapGridElementAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_toolbox_map);
		mapView = (ToolboxMapView) findViewById(R.id.map);
		initTest();
		createCells();
		paintLayout = (LinearLayout) findViewById(R.id.paint_colors);
		currPaint = (ImageButton) paintLayout.getChildAt(0);
		currPaint.setImageDrawable(getResources().getDrawable(
				R.drawable.paint_pressed));
	}
/*
	@Override
	public boolean onTouch(View v, MotionEvent e) {
		if (e.getAction() == MotionEvent.ACTION_DOWN) {
			DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
			v.startDrag(null, shadowBuilder, v, 0);
			v.setVisibility(View.INVISIBLE);
			return true;
		} else {
			return false;
		}
	}

	@Override
	public boolean onDrag(View v, DragEvent e) {
		if (e.getAction() == DragEvent.ACTION_DROP) {
			View view = (View) e.getLocalState();
			ViewGroup from = (ViewGroup) view.getParent();
			from.removeView(view);
			LinearLayout to = (LinearLayout) v;
			to.addView(view);
			view.setVisibility(View.VISIBLE);
		}
		return true;
	}

*/
	
	public void iconClicked(View view) {
		if (view != currPaint) {
			ImageButton imgView = (ImageButton) view;
			String color = view.getTag().toString();
			mapView.setColor(color);
			int size = paintLayout.getChildCount();
			for (int i = 0; i < size; i++) {
				if (paintLayout.getChildAt(i) instanceof ImageButton) {
					ImageButton tmp = (ImageButton) paintLayout.getChildAt(i);
					tmp.setImageDrawable(getResources().getDrawable(
							R.drawable.paint_unpressed));
				}
			}
			imgView.setImageDrawable(getResources().getDrawable(
					R.drawable.paint_pressed));
			currPaint = (ImageButton) view;
		}
		else if (view == currPaint){
			String color = view.getTag().toString();
			mapView.setColor(color);
			int size = paintLayout.getChildCount();
			for (int i = 0; i < size; i++) {
				if (paintLayout.getChildAt(i) instanceof ImageButton) {
					ImageButton tmp = (ImageButton) paintLayout.getChildAt(i);
					tmp.setImageDrawable(getResources().getDrawable(
							R.drawable.paint_unpressed));
				}
			}
			currPaint = null;
		}
	}

	public void openMenu(View view) {

		PopupMenu popup = new PopupMenu(getBaseContext(), view);

		popup.getMenuInflater().inflate(R.menu.game_toolbox_map,
				popup.getMenu());

		popup.setOnMenuItemClickListener(new OnMenuItemClickListener() {

			@Override
			public boolean onMenuItemClick(MenuItem item) {
				String bg = "";

				switch (item.getItemId()) {
				case R.id.item_forest:
					bg = "forest";
					break;
				case R.id.item_rock:
					bg = "rock";
					break;
				default:
					bg = "forest";
					break;
				}

				mapView.setBackground(bg);

				return true;
			}
		});
		popup.show();
	}

	public void undo(View v) {
		mapView.undoLastStep();
	}

	public void redo(View v) {
		mapView.redoLastUndo();
	}
	
	protected void initTest() {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(displaymetrics);
		density = getResources().getDisplayMetrics().density;
		cell_size = (int) ((float) getResources().getDimension(
				R.dimen.game_toolbox_map_cellsize) / density);
		mWidth = displaymetrics.widthPixels / density;
		mHeight = displaymetrics.heightPixels / density - (90 + cell_size);
		mNumColumns = (int) (mWidth / cell_size);
		mNumLines = (int) (mHeight / cell_size);
		mNumCells = mNumLines * mNumColumns;
		Log.i("Width", "" + mWidth);
		
		Log.i("Height", "" + mHeight);
		Log.i("Cells", "" + mNumCells);
		Log.i("mNumColumns", "" + mNumColumns);
		Log.i("mNumLines", "" + mNumLines);
		drag_active = true;
	}
	
	public void createCells() {

		for (int i = 0; i < mNumCells; i++) {
			dotsList.add(null);
			if (i < testDots.length) {
				dotsList.set(i, testDots[i]);
			}

		}

		mAdapter = new ToolboxMapGridElementAdapter(
				ToolboxMapActivity.this, dotsList, drag_active);
		mapView.setNumColumns(mNumColumns);
		mapView.setAdapter(mAdapter);
		mapView.setBackgroundResource(R.drawable.forest);
	}
}
