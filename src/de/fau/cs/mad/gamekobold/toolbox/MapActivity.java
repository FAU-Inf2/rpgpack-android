package de.fau.cs.mad.gamekobold.toolbox;

import java.io.File;
import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.jackson.CharacterSheet;
import de.fau.cs.mad.gamekobold.jackson.ContainerTable;
import de.fau.cs.mad.gamekobold.jackson.JacksonInterface;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
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
import android.view.View.OnLayoutChangeListener;
import android.view.WindowManager;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.PopupMenu.OnMenuItemClickListener;
import android.widget.TableLayout;
import android.widget.Toast;

public class MapActivity extends Activity implements OnDragListener {

	private ImageButton currPaint;
	private MapView mapView;
	private LinearLayout paintLayout;

	Activity mContext;
	float mWidth;
	float mHeight;
	int mWidthPx;
	int mHeightPx;
	private ArrayList<Integer> colors = new ArrayList();
	//private int[] testColor = { R.color.red, R.color.green, R.color.blue,R.color.black, R.color.orange };
	private ArrayList<Drawable> dotsList = new ArrayList();
	private ArrayList<Drawable> justItems = new ArrayList();
	private ArrayList<Drawable> charPics = new ArrayList();
	private int mNumCells;
	private int mNumLines;
	private int mNumColumns;
	private int cell_size;
	private float density;
	private boolean drag_active;
	private MapGridElementAdapter mAdapter;
	private MapGridElementAdapter mAdapterItems;
	private boolean first = true;
	private ImageView trash;

	private CharacterSheet[] characterSheets;
	public static String EXTRA_CHARACTER_ABS_PATH = "EXTRA_CHARACTER_ABS_PATH";
	public static String INFLATE_CHARACTER_NUMBER = "INFLATE_CHARACTER_NUMBER";

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_toolbox_map);
		mapView = (MapView) findViewById(R.id.map);
		mapView.addOnLayoutChangeListener(new OnLayoutChangeListener() {

			@Override
			public void onLayoutChange(View v, int left, int top, int right,
					int bottom, int oldLeft, int oldTop, int oldRight,
					int oldBottom) {
				// its possible that the layout is not complete in which case
				// we will get all zero values for the positions, so ignore the
				// event
				if (left == 0 && top == 0 && right == 0 && bottom == 0) {
					return;
				}

				if (first) {
					initTest();
					createCells();
					first = false;
				}

			}
		});
		Intent intent = getIntent();
		final String[] characterAbsPaths = intent
				.getStringArrayExtra(EXTRA_CHARACTER_ABS_PATH);
		if (savedInstanceState != null) {
			characterSheets = (CharacterSheet[]) savedInstanceState
					.getParcelableArray("characterSheets");
		} else {
			characterSheets = new CharacterSheet[characterAbsPaths.length];
			if (characterAbsPaths != null) {
				Log.d("ToolboxMap", "characterAbsPath != null");
				try {
					int index = 0;
					for (String onePath : characterAbsPaths) {
						characterSheets[index++] = JacksonInterface
								.loadCharacterSheet(new File(onePath), false);
					}

					// characterSheet = JacksonInterface.loadCharacterSheet(new
					// File(
					// characterAbsPath), false);
					Log.d("CharacterPlayActivity", "loaded sheets");
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		}
		mapView.setOnTouchListener(mapView);
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
					chooseFile.setType("images/*");
					intent = Intent.createChooser(chooseFile,
							getString(R.string.choose_image));
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

		ParcelFileDescriptor parcelFileDescriptor = getContentResolver()
				.openFileDescriptor(uri, "r");
		FileDescriptor fileDescriptor = parcelFileDescriptor
				.getFileDescriptor();
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor, null,
				options);
		options.inSampleSize = calculateInSampleSize(options, mWidthPx,
				mHeightPx);
		options.inJustDecodeBounds = false;
		image = BitmapFactory.decodeFileDescriptor(fileDescriptor, null,
				options);
		Log.i("ImageHeight, ImageWidth,actualHeight, actualWidth",
				options.inSampleSize + " " + options.outHeight + " "
						+ options.outWidth + " " + mHeightPx + " " + mWidthPx);
		if (options.inSampleSize < 2
				&& (options.outHeight < mHeightPx || options.outWidth < mWidthPx)) {
			image = null;
		}
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
					if (getBitmapFromUri(uri) != null) {
						mapView.setFileToBackground(getBitmapFromUri(uri));
					} else {
						Toast.makeText(getApplicationContext(),
								"Bild zu klein", Toast.LENGTH_LONG).show();
					}
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
		mapView.setBackgroundResource(R.drawable.forest);
		DisplayMetrics displaymetrics = new DisplayMetrics();
		WindowManager wm = (WindowManager) getApplicationContext()
				.getSystemService(Context.WINDOW_SERVICE);
		wm.getDefaultDisplay().getMetrics(displaymetrics);
		density = getResources().getDisplayMetrics().density;
		cell_size = (int) ((float) getResources().getDimension(
				R.dimen.game_toolbox_map_cellsize) / density);
		mWidthPx = mapView.getW();
		mHeightPx = mapView.getH();
		mWidth = mWidthPx / density;
		mHeight = mHeightPx / density;
		mNumColumns = (int) (mWidth / cell_size);
		mNumLines = (int) (mHeight / cell_size);
		mNumCells = mNumLines * mNumColumns;
		trash = (ImageView) findViewById(R.id.trash);
		trash.setVisibility(View.INVISIBLE);
		trash.setOnDragListener(this);
		for (CharacterSheet character : characterSheets){
			Bitmap bmp = character.getIcon(this);
			if (bmp != null)
				charPics.add(new BitmapDrawable(getResources(), bmp));
			else
				charPics.add(createDrawable(character.getColor()));
		}

		Log.i("Width", "" + mWidth);
		Log.i("Height", "" + mHeight);
		Log.i("Cells", "" + mNumCells);
		Log.i("mNumColumns", "" + mNumColumns);
		Log.i("mNumLines", "" + mNumLines);
	}

	public void createCells() {
		for (int i = 0; i < mNumCells; i++) {
			dotsList.add(null);
		}
		//dotsList.set(colors.size(), createDrawable(R.color.black));
		charPics.add(getResources().getDrawable(R.drawable.dragonmap));//createDrawable(Color.BLACK));

		mAdapterItems = new MapGridElementAdapter(
				MapActivity.this, charPics, "item", trash);
		mAdapter = new MapGridElementAdapter(MapActivity.this,
				dotsList, "grid", trash);
		GridView gridView = (GridView) findViewById(R.id.map_items);
		gridView.setNumColumns(mNumColumns);
		gridView.setAdapter(mAdapterItems);

		mapView.setNumColumns(mNumColumns);
		mapView.setAdapter(mAdapter);
	}

	// Remember to remove the getResources, when loading from charsheet
	public GradientDrawable createDrawable(int color) {
		GradientDrawable newItem = new GradientDrawable();
		newItem.setShape(GradientDrawable.OVAL);
		newItem.setColor(color);
		
		return newItem;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options,
			float reqWidth, float reqHeight) {
		// Raw height and width of image
		final float height = options.outHeight;
		final float width = options.outWidth;
		int inSampleSize = 1;
		/*
		 * if ((height * 2 > reqHeight) && (width * 2 > reqWidth)) {
		 * 
		 * final float halfHeight = height / 2; final float halfWidth = width /
		 * 2;
		 * 
		 * // Calculate the largest inSampleSize value that is a power of 2 and
		 * // keeps both // height and width larger than the requested height
		 * and width.
		 */
		while ((height / (inSampleSize * 2)) > reqHeight
				&& (width / (inSampleSize * 2)) > reqWidth) {
			inSampleSize *= 2;
		}

		return inSampleSize;

	}


	@Override
	public boolean onDrag(View v, DragEvent event) {
		switch (event.getAction()) {
		case DragEvent.ACTION_DRAG_STARTED:
			// Log.v("Test", "Entered start");
			break;
		case DragEvent.ACTION_DRAG_ENTERED:
			// Log.v("Test", "Entered drag");
			break;
		case DragEvent.ACTION_DRAG_EXITED:
			break;
		case DragEvent.ACTION_DROP:
			Log.v("Test", "Entered drop");
			final View view = (View) event.getLocalState();
			if (view != null && (view instanceof ImageView)) {
				ImageView castedView = (ImageView) view;
				if (castedView.getContentDescription().toString() == "grid") {
					final ViewGroup owner = (ViewGroup) view.getParent();
					owner.removeView(v);
				}
			}
			break;
		case DragEvent.ACTION_DRAG_ENDED:
			trash.setVisibility(View.INVISIBLE);
			break;
		default:
			break;
		}
		return true;
	}
	
	public static Intent createIntentForStarting(Context packageContext,
			CharacterSheet[] sheets) {
		Log.i("ToolboxMapActivity",
				"createIntentForStarting: sheets.length == " + sheets.length);
		Intent intent = new Intent(packageContext, MapActivity.class);
		String[] filePaths = new String[sheets.length];
		int index = 0;
		for (CharacterSheet oneSheet : sheets) {
			filePaths[index++] = oneSheet.getFileAbsolutePath();
		}
		intent.putExtra(MapActivity.EXTRA_CHARACTER_ABS_PATH,
				filePaths);
		return intent;
	}
}