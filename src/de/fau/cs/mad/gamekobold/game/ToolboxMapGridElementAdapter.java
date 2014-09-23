package de.fau.cs.mad.gamekobold.game;

import java.lang.reflect.Array;
import java.util.ArrayList;

import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.DragShadowBuilder;
import android.view.View.OnDragListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import de.fau.cs.mad.gamekobold.R;
import de.fau.cs.mad.gamekobold.R.drawable;

public class ToolboxMapGridElementAdapter extends BaseAdapter {

	private Context mContext;
	private final ArrayList<GradientDrawable> dotsList;
	private LayoutInflater mInflater;

	public ToolboxMapGridElementAdapter(Context context,
			ArrayList<GradientDrawable> dots) {
		mContext = context;
		dotsList = dots;
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
				pieceView.setOnTouchListener(new View.OnTouchListener() {
					@Override
					public boolean onTouch(View v, MotionEvent event) {
						final ClipData data = ClipData.newPlainText("position",
								position + "");
						final DragShadowBuilder pieceDragShadowBuilder = new DragShadowBuilder(
								pieceView);
						
						v.setVisibility(View.INVISIBLE);
						v.startDrag(data, pieceDragShadowBuilder, v, 0);
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
				if (view != null) {
					final ViewGroup owner = (ViewGroup) view.getParent();
					owner.removeView(view);
					final FrameLayout container = (FrameLayout) v;
					view.setVisibility(View.VISIBLE);
					container.addView(view);
				}				
				break;
			case DragEvent.ACTION_DRAG_ENDED:
			default:
				break;
			}
			return true;
		}
	}

}
