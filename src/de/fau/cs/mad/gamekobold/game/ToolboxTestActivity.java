package de.fau.cs.mad.gamekobold.game;

import java.util.ArrayList;

import de.fau.cs.mad.gamekobold.R;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnDragListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.View.DragShadowBuilder;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.AdapterView.OnItemClickListener;

public class ToolboxTestActivity extends Activity  {

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

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_toolbox_test);
		initTest();
		//createDots();
		createCells();
		// setContentView(R.layout.activity_game_toolbox_test);
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

		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.test_layout);
		GridView grid = (GridView) new GridView(ToolboxTestActivity.this);
		// ViewGroup.LayoutParams lp=new
		// ViewGroup.LayoutParams(cellWidth,cellWidth);
		// grid.setLayoutParams(lp);

		ToolboxTestGridElementAdapter adapter = new ToolboxTestGridElementAdapter(
				ToolboxTestActivity.this, dotsList, drag_active);
		grid.setNumColumns(mNumColumns);
		grid.setAdapter(adapter);
		grid.setBackgroundResource(R.drawable.forest);
		//grid.setOnDragListener(this);
		linearLayout.addView(grid);

	}

	/*public void createDots() {
		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.paint_test_dots);
		//linearLayout.setOnDragListener(this);

		for (int item : testDots) {
			FrameLayout frameLayout = (FrameLayout) new FrameLayout(this);
			ImageView img_view = (ImageView) new ImageView(this);
			int dim = (int) getResources().getDimension(R.dimen.game_toolbox_map_cellsize_without_padding);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(dim, dim);
			

			if (item==R.drawable.red_dot) {
				img_view.setTag(item);
				img_view.setImageDrawable(getResources().getDrawable(
						R.drawable.red_dot));
			}
			if (item==R.drawable.blue_dot) {
				img_view.setTag(item);
				img_view.setImageDrawable(getResources().getDrawable(
						R.drawable.blue_dot));
			}
			if (item==R.drawable.black_dot) {
				img_view.setTag(item);
				img_view.setImageDrawable(getResources().getDrawable(
						R.drawable.black_dot));
			}
			if (item==R.drawable.green_dot) {
				img_view.setTag(item);
				img_view.setImageDrawable(getResources().getDrawable(
						R.drawable.green_dot));
			}
			if (item==R.drawable.orange_dot) {
				img_view.setTag(item);
				img_view.setImageDrawable(getResources().getDrawable(
						R.drawable.orange_dot));
			}
			img_view.setOnTouchListener(this);
			img_view.setLayoutParams(params);
			frameLayout.addView(img_view);
			linearLayout.addView(frameLayout);
		}
	}*/
	
	/*@Override
	public boolean onDrag(View v, DragEvent dragEvent) {
		switch (dragEvent.getAction()) {

		case DragEvent.ACTION_DRAG_STARTED:
			System.out.println("ACTION_DRAG_STARTED");
			break;
		case DragEvent.ACTION_DRAG_ENTERED:
			System.out.println("ACTION_DRAG_ENTERED");
			break;
		case DragEvent.ACTION_DRAG_EXITED:
			System.out.println("ACTION_DRAG_EXITED");
			break;
		case DragEvent.ACTION_DROP:
			final View view = (View) dragEvent.getLocalState();

			if (view.getParent() != null) {
				final ViewGroup owner = (ViewGroup) view.getParent();
				owner.removeView(view);
			}

			final FrameLayout container = (FrameLayout) v;
			container.addView(view);
			System.out.println("ACTION_DROP");
			break;
		case DragEvent.ACTION_DRAG_ENDED:
			System.out.println("ACTION_DRAG_ENDED");
			break;
		}
		return true;
	}*/
/*
	@Override
	public boolean onTouch(View v, MotionEvent e) {
		ClipData.Item item = new ClipData.Item((String) v.getTag());
		ClipData clipData = new ClipData((CharSequence) v.getTag(),
				new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN }, item);
		if (e.getAction() == MotionEvent.ACTION_DOWN) {
			v.startDrag(clipData, new View.DragShadowBuilder(v), null, 0);
			//v.setVisibility(View.INVISIBLE);
			return true;
		} else {
			return false;
		}
	}
	*/
}
