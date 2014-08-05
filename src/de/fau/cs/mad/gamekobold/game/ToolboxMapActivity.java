package de.fau.cs.mad.gamekobold.game;


import de.fau.cs.mad.gamekobold.R;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import de.fau.cs.mad.gamekobold.R;

public class ToolboxMapActivity extends Activity{
	
	private ImageButton currPaint;
	private ToolboxMapView mapView;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		//hier fehlt evtl noch was
			mapView = (ToolboxMapView)findViewById(R.id.map);
			LinearLayout paintLayout = (LinearLayout)findViewById(R.id.paint_colors);
			currPaint = (ImageButton)paintLayout.getChildAt(0);
			currPaint.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
	}
	
	public void paintClicked(View view){
		if(view!=currPaint){
			ImageButton imgView = (ImageButton)view;
			String color = view.getTag().toString();
			mapView.setColor(color);
			
			imgView.setImageDrawable(getResources().getDrawable(R.drawable.paint_pressed));
			currPaint.setImageDrawable(getResources().getDrawable(R.drawable.ic_launcher));
			currPaint=(ImageButton)view;
			}
	}

}
