package de.fau.cs.mad.gamekobold.game;

import java.util.ArrayList;

import de.fau.cs.mad.gamekobold.R;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
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

public class ToolboxMapActivity extends Activity implements OnTouchListener,
		OnDragListener {

	private String[] dots = { "red", "green", "blue", "black", "orange" };
	private ImageButton currPaint;
	private ToolboxMapView mapView;
	private LinearLayout paintLayout;
	private final int dot_size = 40;
	private ArrayList<String> dots_array = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_toolbox_map);
		mapView = (ToolboxMapView) findViewById(R.id.map);
		// createGrid();
		paintLayout = (LinearLayout) findViewById(R.id.paint_colors);
		currPaint = (ImageButton) paintLayout.getChildAt(0);
		currPaint.setImageDrawable(getResources().getDrawable(
				R.drawable.paint_pressed));
		createDots();
		findViewById(R.id.paint_colors).setOnDragListener(this);
		findViewById(R.id.paint_dots).setOnDragListener(this);
	}

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

	public void createDots() {
		LinearLayout layout = (LinearLayout) findViewById(R.id.paint_dots);

		for (String item : dots) {
			ImageView img_view = (ImageView) new ImageView(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					dot_size, dot_size);

			if (item.equals("red")) {
				img_view.setTag(item);
				img_view.setImageDrawable(getResources().getDrawable(
						R.drawable.red_dot));
			}
			if (item.equals("blue")) {
				img_view.setTag(item);
				img_view.setImageDrawable(getResources().getDrawable(
						R.drawable.blue_dot));
			}
			if (item.equals("black")) {
				img_view.setTag(item);
				img_view.setImageDrawable(getResources().getDrawable(
						R.drawable.black_dot));
			}
			if (item.equals("green")) {
				img_view.setTag(item);
				img_view.setImageDrawable(getResources().getDrawable(
						R.drawable.green_dot));
			}
			if (item.equals("orange")) {
				img_view.setTag(item);
				img_view.setImageDrawable(getResources().getDrawable(
						R.drawable.orange_dot));
			}
			img_view.setLayoutParams(params);
			img_view.setOnTouchListener(this);
			layout.addView(img_view);
		}
	}

	/*
	 * public void createGrid() { ToolboxMapView mapView = (ToolboxMapView)
	 * findViewById(R.id.map_layout);
	 * 
	 * int height = mapView.getH(); int width = mapView.getW();
	 * 
	 * int num_row = height / (dot_size + (((height / dot_size) +1)*5)) ;
	 * Log.i("Row", num_row + ""); int num_column = width / (dot_size + (((width
	 * / dot_size) +1)*5)); Log.i("Column", num_column + ""); int grid_size =
	 * num_column * num_row; GridView grid = new
	 * GridView(ToolboxMapActivity.this); for (int i = 0; i < grid_size; i++) {
	 * dots_array.add("" + i); } Log.i("Size", dots_array.size()+ "");
	 * 
	 * ToolboxMapElementAdapter adp = new ToolboxMapElementAdapter(
	 * ToolboxMapActivity.this, num_row, num_column, dots_array);
	 * grid.setNumColumns(num_column); grid.setAdapter(adp);
	 * mapView.addView(grid); setContentView(linearLayout);
	 * 
	 * }
	 */

	public void undo(View v) {
		mapView.undoLastStep();
	}

	public void redo(View v) {
		mapView.redoLastUndo();
	}

	public void activateErase(View v) {
		Log.i("Button Pressed", "true");
		mapView.setErase(true);
	}
}
