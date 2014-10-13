package de.fau.cs.mad.gamekobold.toolbox;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Handler;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.R.drawable;

public class MapGridElementAdapter extends BaseAdapter {

	private Context mContext;
	private final ArrayList<Drawable> dotsList;
	private LayoutInflater mInflater;
	private String tag;
	ImageView trash;

	private static final int MIN_CLICK_DURATION = 2000;
	private long startClickTime;

	public MapGridElementAdapter(Context context,
			ArrayList<Drawable> dots, String tag, ImageView trash) {
		mContext = context;
		dotsList = dots;
		this.tag = tag;
		this.trash = trash;
		mInflater = (LayoutInflater) this.mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public View getView(final int position, View convertView, ViewGroup parent) {

		View squareContainerView = convertView;

		if (squareContainerView == null) {
			squareContainerView = mInflater.inflate(
					R.layout.activity_game_toolbox_map_cell, null);

			squareContainerView.setOnDragListener(new MyDragListener(
					this.mContext));

			final ImageView pieceView = (ImageView) squareContainerView
					.findViewById(R.id.map_piece);
			if (dotsList.get(position) != null) {
				pieceView.setImageDrawable(dotsList.get(position));
				pieceView.setTag(position);
				pieceView.setContentDescription(tag);

				pieceView.setOnTouchListener(new View.OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						final ClipData data = ClipData.newPlainText("position",
								position + "");
						final DragShadowBuilder pieceDragShadowBuilder = new DragShadowBuilder(
								pieceView);
						if (tag == "grid"){
							v.setVisibility(View.INVISIBLE);
							trash.setVisibility(View.VISIBLE);
						}
						startClickTime = Calendar.getInstance()
								.getTimeInMillis();
						v.startDrag(data, pieceDragShadowBuilder, v, 0);
						/*switch (event.getAction() & MotionEvent.ACTION_MASK) {

						case MotionEvent.ACTION_DOWN:
							startClickTime = Calendar.getInstance()
									.getTimeInMillis();
							break;
						case MotionEvent.ACTION_MOVE:

							break;
						case MotionEvent.ACTION_UP:
							break;
						}*/
						return true;
					}
				});

			}
		}

		return squareContainerView;
	}

	@Override
	public int getCount() {
		return dotsList.size();
	}

	@Override
	public Object getItem(int position) {
		return dotsList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	private void createDrawable(int color) {

	}

	class MyDragListener implements OnDragListener {

		private Context context;

		public MyDragListener(Context pContext) {
			this.context = pContext;
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
				// Log.v("Test", "Entered drop");
				final View view = (View) event.getLocalState();
				if (view != null && (view instanceof ImageView)) {
					ImageView castedView = (ImageView) view;
					final FrameLayout container = (FrameLayout) v;
					if (castedView.getContentDescription().toString() == "item") {
						Drawable copyImg = castedView.getDrawable();
						addImagetoContainer(container, 1, copyImg);
					} else if (castedView.getContentDescription().toString() == "grid") {
						final ViewGroup owner = (ViewGroup) view.getParent();
						long clickDuration = Calendar.getInstance()
								.getTimeInMillis() - startClickTime;
						Log.i("Time", clickDuration + "");
						if (clickDuration >= MIN_CLICK_DURATION) {
							owner.removeView(v);
						}
						else{
						owner.removeView(view);
						view.setVisibility(View.VISIBLE);
						container.addView(view);
						}
					}
				}
				trash.setVisibility(View.INVISIBLE);
				break;
			case DragEvent.ACTION_DRAG_ENDED:
				break;
			default:
				break;
			}
			return true;
		}
	}

	private View addImagetoContainer(View inputView, final int position,
			Drawable drawable) {

		final ImageView pieceView = (ImageView) inputView
				.findViewById(R.id.map_piece);
		pieceView.setImageDrawable(drawable);
		pieceView.setTag(position);
		pieceView.setContentDescription("grid");
		pieceView.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				final ClipData data = ClipData.newPlainText("position",
						position + "");
				final DragShadowBuilder pieceDragShadowBuilder = new DragShadowBuilder(
						pieceView);
				trash.setVisibility(View.VISIBLE);
				startClickTime = Calendar.getInstance()
						.getTimeInMillis();
				v.setVisibility(View.INVISIBLE);
				v.startDrag(data, pieceDragShadowBuilder, v, 0);
				return true;
			}
		});

		/*
		pieceView.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View view) {
				final ViewGroup owner = (ViewGroup) view.getParent();
				owner.removeView(view);
				return true;
			}
		}); */

		return inputView;
	}

}
