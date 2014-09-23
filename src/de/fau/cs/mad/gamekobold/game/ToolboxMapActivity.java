package de.fau.cs.mad.gamekobold.game;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;

import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.jackson.CharacterSheet;
import de.fau.cs.mad.gamekobold.jackson.ContainerTable;
import de.fau.cs.mad.gamekobold.jackson.JacksonInterface;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Path;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
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

public class ToolboxMapActivity extends Activity {

	private ImageButton currPaint;
	private ToolboxMapView mapView;
	private LinearLayout paintLayout;

	Activity mContext;
	float mWidth;
	float mHeight;
	private int[] testColor = { R.color.red, R.color.green, R.color.blue,
			R.color.black, R.color.orange };
	private ArrayList<GradientDrawable> dotsList = new ArrayList();
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
		} else if (view == currPaint) {
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
					mapView.setBackground(bg);
					break;
				case R.id.item_rock:
					bg = "rock";
					mapView.setBackground(bg);
					break;
				case R.id.item_add:
					Intent chooseFile;
					Intent intent;
					chooseFile = new Intent(Intent.ACTION_GET_CONTENT);
					chooseFile.setType("file/*");
					intent = Intent.createChooser(chooseFile, "Choose a file");
					startActivityForResult(intent, 1);
				default:
					bg = "forest";
					break;
				}

				return true;
			}
		});
		popup.show();
	}

	private Bitmap getBitmapFromUri(Uri uri) throws IOException {
		
		ParcelFileDescriptor parcelFileDescriptor =
	             getContentResolver().openFileDescriptor(uri, "r");
		FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
		options.inSampleSize = calculateInSampleSize(options, mWidth, mHeight);
		options.inJustDecodeBounds = false;
		image = BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
		Log.i("string image", image.toString());
		parcelFileDescriptor.close();
		return image;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 1: {
			if (resultCode == RESULT_OK) {
				Uri uri = data.getData();
				try {
					mapView.setFileToBackground(getBitmapFromUri(uri));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		}
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
	}

	public void createCells() {
		for (int i = 0; i < mNumCells; i++) {
			dotsList.add(null);

			if (i < testColor.length) {
				Log.i("testfarbe:", "" + testColor[i]);
				dotsList.set(i, createDrawable(testColor[i]));
			}
		}

		mAdapter = new ToolboxMapGridElementAdapter(ToolboxMapActivity.this,
				dotsList);
		mapView.setNumColumns(mNumColumns);
		mapView.setAdapter(mAdapter);
		mapView.setBackgroundResource(R.drawable.forest);
	}

	public GradientDrawable createDrawable(int color) {
		int colorFromRes = getResources().getColor(color);
		GradientDrawable newItem = new GradientDrawable();
		newItem.setShape(GradientDrawable.OVAL);
		newItem.setColor(colorFromRes);
		return newItem;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			float reqWidth, float reqHeight) {
		// Raw height and width of image
		final float height = options.outHeight;
		final float width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final float halfHeight = height / 2;
			final float halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;

	}

}
