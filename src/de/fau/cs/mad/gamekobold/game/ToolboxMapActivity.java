package de.fau.cs.mad.gamekobold.game;

import de.fau.cs.mad.gamekobold.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;

public class ToolboxMapActivity extends Activity {

	private ImageButton currPaint;
	private ToolboxMapView mapView;
	private LinearLayout paintLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_game_toolbox_map);
		mapView = (ToolboxMapView) findViewById(R.id.map);
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
		}
	}

}
