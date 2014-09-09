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

public class ToolboxTestActivity extends Activity implements OnTouchListener,
		OnDragListener {

	Activity mContext;
	float mWidth;
	float mHeight;
	private String[] dots = { "red", "green", "blue", "black", "orange" };
	private ArrayList<Integer> dotsList = new ArrayList();
	private int mNumCells;
	private int mNumLines;
	private int mNumColumns;
	private int cell_size;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_toolbox_test);
		initTest();
		createDots();
		createCells();
		// setContentView(R.layout.activity_game_toolbox_test);
	}

	protected void initTest() {
		DisplayMetrics displaymetrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(displaymetrics);
		float density = getResources().getDisplayMetrics().density;
		cell_size = (int) ((float) getResources().getDimension(
				R.dimen.game_toolbox_map_cellsize) / density);
		mWidth = displaymetrics.widthPixels / density;
		mHeight = displaymetrics.heightPixels / density - (90 + cell_size);
		mNumColumns = (int) (mWidth / cell_size);
		mNumLines = (int) (mHeight / cell_size);
		mNumCells = mNumLines * mNumColumns;
		Log.i("Width", "" + mWidth);
		Log.i("Cell_Size", "" + cell_size);
		Log.i("Height", "" + mHeight);
		Log.i("Cells", "" + mNumCells);
		Log.i("mNumColumns", "" + mNumColumns);
		Log.i("mNumLines", "" + mNumLines);
	}

	public void createCells() {

		for (int i = 0; i < mNumCells; i++) {
			dotsList.add(null);
			if (i == 6 || i == 10 || i == 20) {
				Log.i("Activity", "" + i);
				dotsList.set(i, R.drawable.red_dot);
			}

		}

		LinearLayout linearLayout = (LinearLayout) findViewById(R.id.test_layout);
		GridView grid = (GridView) new GridView(ToolboxTestActivity.this);
		// ViewGroup.LayoutParams lp=new
		// ViewGroup.LayoutParams(cellWidth,cellWidth);
		// grid.setLayoutParams(lp);

		ToolboxTestGridElementAdapter adapter = new ToolboxTestGridElementAdapter(
				ToolboxTestActivity.this, dotsList);
		grid.setNumColumns(mNumColumns);
		grid.setAdapter(adapter);
		//grid.setOnDragListener(this);
		linearLayout.addView(grid);

	}

	public void createDots() {
		LinearLayout layout = (LinearLayout) findViewById(R.id.paint_test_dots);
		layout.setOnDragListener(this);

		for (String item : dots) {
			ImageView img_view = (ImageView) new ImageView(this);
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
					cell_size, cell_size);

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
			img_view.setOnTouchListener(this);
			img_view.setLayoutParams(params);
			layout.addView(img_view);
		}
	}

	@Override
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
	}

	@Override
	public boolean onTouch(View v, MotionEvent e) {
		ClipData.Item item = new ClipData.Item((String) v.getTag());
		ClipData clipData = new ClipData((CharSequence) v.getTag(),
				new String[] { ClipDescription.MIMETYPE_TEXT_PLAIN }, item);
		if (e.getAction() == MotionEvent.ACTION_DOWN) {
			v.startDrag(clipData, new View.DragShadowBuilder(v), null, 0);
			v.setVisibility(View.INVISIBLE);
			return true;
		} else {
			return false;
		}
	}
}
